package com.ikip.newsdetect.main.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.*;
import es.ucm.visavet.gbf.app.domain.topic.TopicManager;
import es.ucm.visavet.gbf.app.repository.LocationRepository;
import es.ucm.visavet.gbf.app.service.ConfigurationServiceIO;
import es.ucm.visavet.gbf.topics.queryconstructor.QueryConstructor;
import es.ucm.visavet.gbf.topics.queryconstructor.QueryConstructorSemantics;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.*;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

public abstract class AbstractNewsIndexService {

	protected final static Logger LOGGER = Logger
			.getLogger(AbstractNewsIndexService.class);

	protected Directory allDirectory;
	protected IndexWriter allIndex;
	protected boolean newsPending = false, taskScheduled = false;

	@Autowired
	protected TopicManager topicManager;
	
	@Autowired
	protected AlertServiceImpl alertService;

	@Autowired
	protected RiskServiceImpl riskService;
	
	@Autowired
	protected LocationRepository locationRepository;
	
	@Autowired
	protected ConfigurationServiceIO IOFolder;
	

	@PostConstruct
	public abstract void initDirectory();

	protected abstract Directory getDirectory(); 

	protected abstract IndexWriter getWriter();
	
	protected abstract Feed getFeedByCodeName(Long site);
	
	protected abstract void updateFeed(Feed feed);
	
	protected abstract void updateIndex() throws Exception;
	
	protected abstract void resetAlertInter(AlertAbstract alert, IndexSearcher searcher,HashMap<String, List<Location>> Loca) throws UnsupportedEncodingException;


	protected abstract void riskServiceupdate(Risk alert);

	protected abstract void alertServiceupdate(Alert alert);

	protected abstract void newsDetectRepositorysave(List<NewsDetect> listNewsDetect);
	
	protected abstract void locationRepositorysave(Location loc);
	
	
	@Autowired
	public AbstractNewsIndexService(ConfigurationServiceIO IOFolder) {
		this.IOFolder=IOFolder;
	}
	
	protected void addDocument(News news) throws Exception {
		if (news != null) {
			try {
				Document d = new Document();
				d.add(new StringField(News.fieldUrl, news.getUrl(),
						Field.Store.YES));
				d.add(new TextField(News.fieldTitle, news.getTitle(),
						Field.Store.YES));
				d.add(new TextField(News.fieldTitleNoCase, news.getTitle(),
						Field.Store.NO));
				d.add(new TextField(News.fieldBody, news.getContent(),
						Field.Store.YES));
				d.add(new TextField(News.fieldBodyNoCase, news.getContent(),
						Field.Store.NO));
				d.add(new StringField(News.fieldDatePub, DateTools
						.dateToString(news.getPubDate(), Resolution.MINUTE),
						Field.Store.YES));
				d.add(new StringField(News.fieldSite,
						news.getSite().toString(), Field.Store.YES));
				d.add(new StringField(News.fieldDateCreate, DateTools
						.dateToString(new Date(), Resolution.MINUTE),
						Field.Store.YES));
				Feed feed = getFeedByCodeName(news.getSite());
				if (feed.getFeedType() == null) {
					feed.setFeedType(FeedTypeEnum.massmedia);
				}
				if (feed.getFeedPlace() != null) {
					if (feed.getFeedPlace().isEmpty()) {
						feed.getFeedPlace().add(FeedPlaceEnum.general);
					}
				} else {
					feed.setFeedPlace(Lists.newArrayList(FeedPlaceEnum.general));
				}
				d.add(new StringField(News.fieldSiteType, feed.getFeedType()
						.getValue().toString(), Field.Store.YES));
				for (FeedPlaceEnum type : feed.getFeedPlace())
					d.add(new StringField(News.fieldSiteLoc, type.getValue()
							.toString(), Field.Store.YES));
				getWriter().updateDocument(
						new Term(News.fieldUrl, news.getUrl()), d);
				updateFeed(feed);
			} catch (NullPointerException ex) {
				ex.printStackTrace();
				LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/storenews"));
			}
		}
	}

	

	

	protected List<NewsDetect> search(Query q, IndexSearcher searcher,
			AlertAbstract alert) throws Exception {
		TopDocs docs = searcher.search(q, Integer.MAX_VALUE);
		ScoreDoc[] docsA = docs.scoreDocs;
		List<NewsDetect> listNewsDetect = Lists.newArrayList();
		for (int i = 0; i < docs.totalHits; ++i) {
			int docId = docsA[i].doc;
			Document d = searcher.doc(docId);
			NewsDetect newsDocument = new NewsDetect();
			newsDocument.setAlertDetect(alert);
			newsDocument.setDatePub(DateTools.stringToDate(d.get("datePub")));
			newsDocument.setLink(d.get("id"));
			newsDocument.setScore(docsA[i].score);
			newsDocument.setScoreLucene(docsA[i].score);
			newsDocument.setSite(getFeedByCodeName(new Long(d
					.get("feed"))));
			if (alert==null || newsDocument.getSite()==null)
				LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/alertnewsitenull"));
			newsDocument.setTitle(d.get("title"));
			listNewsDetect.add(newsDocument);
		}
		return listNewsDetect;
	}

	

	protected Query createQuery(AlertAbstract alert, Long from, Long to) throws UnsupportedEncodingException {
		InputStreamReader alertDefinition = new InputStreamReader(new ByteArrayInputStream(alert.getWords()
				.getBytes()),"UTF-8");
		QueryConstructor queryConstructorBody = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldBody,
						News.fieldBodyNoCase, News.fieldSiteLoc,
						News.fieldSiteType), alertDefinition);
		Query q = null;
		try {
			q = queryConstructorBody.topic();
		} catch (es.ucm.visavet.gbf.topics.queryconstructor.ParseException e) {
			System.err.println(alert.getTitle());
			e.printStackTrace();
		}
		alertDefinition = new InputStreamReader(new ByteArrayInputStream(alert.getWords().getBytes()),"UTF-8");
		QueryConstructor queryConstructorTitle = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldTitle,
						News.fieldTitleNoCase, News.fieldSiteLoc,
						News.fieldSiteType), alertDefinition);
		Query q1 = null;
		try {
			q1 = queryConstructorTitle.topic();
		} catch (es.ucm.visavet.gbf.topics.queryconstructor.ParseException e) {
			System.err.println(alert.getTitle());
			e.printStackTrace();
		}
		Query q2 = TermRangeQuery.newStringRange(News.fieldDateCreate,
				DateTools.timeToString(from, Resolution.MINUTE),
				DateTools.timeToString(to, Resolution.MINUTE), false, true);
		BooleanClause op1 = new BooleanClause(q, BooleanClause.Occur.SHOULD);
		BooleanClause op2 = new BooleanClause(q1, BooleanClause.Occur.SHOULD);
		BooleanQuery.Builder b = new BooleanQuery.Builder();
		b.add(op1);
		b.add(op2);
		Query qS = b.build();
		BooleanClause opS = new BooleanClause(qS, BooleanClause.Occur.MUST);
		BooleanClause opC = new BooleanClause(q2, BooleanClause.Occur.MUST);
		b = new BooleanQuery.Builder();
		b.add(opS);
		b.add(opC);
		Query query = b.build();
		return query;
	}

	protected Query createQuery(Location location, Long from, Long to) throws UnsupportedEncodingException {
		InputStreamReader alertDefinition = new InputStreamReader(new ByteArrayInputStream(location
				.getQuery().getBytes()),"UTF-8");
		QueryConstructor queryConstructorBody = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldBody,
						News.fieldBodyNoCase, News.fieldSiteLoc,
						News.fieldSiteType), alertDefinition);
		Query q = null;
		try {
			q = queryConstructorBody.topic();
		} catch (es.ucm.visavet.gbf.topics.queryconstructor.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		alertDefinition = new InputStreamReader(new ByteArrayInputStream(location.getQuery()
				.getBytes()),"UTF-8");
		QueryConstructor queryConstructorTitle = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldTitle,
						News.fieldTitleNoCase, News.fieldSiteLoc,
						News.fieldSiteType), alertDefinition);
		Query q1 = null;
		try {
			q1 = queryConstructorTitle.topic();
		} catch (es.ucm.visavet.gbf.topics.queryconstructor.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Query q2 = TermRangeQuery.newStringRange("dateCreate",
				DateTools.timeToString(from, Resolution.MINUTE),
				DateTools.timeToString(to, Resolution.MINUTE), false, true);
		BooleanClause op1 = new BooleanClause(q, BooleanClause.Occur.SHOULD);
		BooleanClause op2 = new BooleanClause(q1, BooleanClause.Occur.SHOULD);
		BooleanQuery.Builder b = new BooleanQuery.Builder();
		b.add(op1);
		b.add(op2);
		Query qS = b.build();
		BooleanClause opS = new BooleanClause(qS, BooleanClause.Occur.MUST);
		BooleanClause opC = new BooleanClause(q2, BooleanClause.Occur.MUST);
		b = new BooleanQuery.Builder();
		b.add(opS);
		b.add(opC);
		Query query = b.build();
		return query;
	}


	protected List<String> searchLocation(Query q, IndexSearcher searcher,
			Location loc) throws Exception {
		TopDocs docs = searcher.search(q, Integer.MAX_VALUE);
		ScoreDoc[] docsA = docs.scoreDocs;
		List<String> listNewsDetect = Lists.newArrayList();
		for (int i = 0; i < docs.totalHits; ++i) {
			int docId = docsA[i].doc;
			Document d = searcher.doc(docId);
			NewsDetect newsDocument = new NewsDetect();
			newsDocument.setTitle(d.get("title"));
			listNewsDetect.add(d.get("id"));
		}
		return listNewsDetect;
	}


	



	protected void resetAlert(AlertAbstract alert) throws IOException {
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/alertnewsreset"));
		boolean closed = getWriter() == null ? false : getWriter().isOpen();
		if (!closed) {
			getDirectory();
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			e1.printStackTrace();
			LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/erroropenindex"));
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		
		Date Start = new Date();
		Start.setTime(0l);
		List<Location> ResultadoBasicoLoc = searchLocation(Start,new Date());
		
HashMap<String, List<Location>> Loca=new HashMap<String, List<Location>>();
		
		for (Location location : ResultadoBasicoLoc) {
			
			if (location.getType()==null)
				location.setType(LocationLevel.Region);
			
			List<String> A=location.getNews();
			for (String string : A) {
				List<Location> padre=Loca.get(string);
				
				if (padre==null)
					padre=new ArrayList<Location>();
				
				for (Location pareuni : padre) {
					if (pareuni.getType()==null)
						pareuni.setType(LocationLevel.Region);
				}
				
				if (!padre.contains(location))
					padre.add(location);
				
				
				Loca.put(string, padre);
				
			}
		}	
		
		
		resetAlertInter(alert, searcher,Loca);
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!closed) {
			getDirectory().close();
			this.allDirectory = null;
		}
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/alertfindend"));
	}

	


	protected void indexNews(News news) throws Exception {
		addDocument(news);
		getWriter().commit();
	}


	protected void indexNews(List<News> lNews) throws Exception {
		for (News news : lNews) {
			addDocument(news);
		}
		getWriter().commit();
	}
	
	protected void stopDirectory() throws IOException {
		getWriter().commit();
		getWriter().close();
		getDirectory().close();
		this.allDirectory = null;
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/alertcloseindex"));
	}
	
	protected static Analyzer getAnalyzer() {
		Map<String, Analyzer> aMap = new HashMap<String, Analyzer>();
		Analyzer sa = new StandardAnalyzer(CharArraySet.EMPTY_SET);
		aMap.put(News.fieldTitleNoCase, sa);
		aMap.put(News.fieldTitle, new WhitespaceAnalyzer());
		aMap.put(News.fieldBodyNoCase, sa);
		aMap.put(News.fieldBody, new WhitespaceAnalyzer());
		aMap.put(News.fieldUrl, new WhitespaceAnalyzer());
		aMap.put(News.fieldSite, new WhitespaceAnalyzer());
		return new PerFieldAnalyzerWrapper(sa, aMap);
	}
	
	public void run() {
		taskScheduled = false;
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/alertinitfind"));
		if (this.newsPending) {
			try {
				updateIndex();
			} catch (Exception e) {
				LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/alertupdateindex"));
			}
			this.newsPending = false;
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			e1.printStackTrace();
			LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/alertopenindex"));
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		
		
//		// Proceso de detección de localizaciones
//		Iterable<Location> locations = locationRepository.findAll();
//		for (Location loc : locations) {
//			Long from = loc.getUltimaRecuperacion() != null ? loc
//					.getUltimaRecuperacion().getTime() : 0;
//			Long to = new Date().getTime();
//			Query q;
//			try {
//				q = createQuery(loc, from, to);
//			} catch (UnsupportedEncodingException e1) {
//				LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/alertcreatingquery") + loc.getName());
//				continue;
//			}
//			try {
//				List<String> listNewsDetect = searchLocation(q, searcher, loc);
//				loc = locationRepository.findOne(loc.getId());
//				loc.setUltimaRecuperacion(new Timestamp(to));
////				loc.getNews().addAll(listNewsDetect);
//				locationRepositorysave(loc);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		
		// Proceso de detección de alertas
		Set<AlertAbstract> tasks = Sets.newHashSet();
		tasks.addAll(alertService.getAllAlert());
		tasks.addAll(riskService.getAllAlert());
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/alerthaynanalizar1") + tasks.size() + LanguageLoad.getinstance().find("internal/index/info/alerthaynanalizar2"));
		
		
		
		Date Start = new Date();
		Start.setTime(0l);
		List<Location> ResultadoBasicoLoc = searchLocation(Start,new Date());
		
		HashMap<String, List<Location>> Loca=new HashMap<String, List<Location>>();
		
		for (Location location : ResultadoBasicoLoc) {
			
			if (location.getType()==null)
				location.setType(LocationLevel.Region);
			
			List<String> A=location.getNews();
			for (String string : A) {
				List<Location> padre=Loca.get(string);
				
				if (padre==null)
					padre=new ArrayList<Location>();
				
				for (Location pareuni : padre) {
					if (pareuni.getType()==null)
						pareuni.setType(LocationLevel.Region);
				}
				
				if (!padre.contains(location))
					padre.add(location);
				
				
				Loca.put(string, padre);
				
			}
		}	
		
		
		
		
		for (AlertAbstract alert : tasks) {
			Long from = alert.getUltimaRecuperacion() != null ? alert
					.getUltimaRecuperacion().getTime() : 0;
			Long to = new Date().getTime();
			Query q;
			try {
				q = createQuery(alert, from, to);
			} catch (UnsupportedEncodingException e1) {
				LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/alertcreatequeryrun") + alert.getTitle());
				continue;
			}
			try {
				
				SpecializedClassicSimilarity Symy=new SpecializedClassicSimilarity();
				
				Symy.loadfromfile(IOFolder.getPathgen(),IOFolder.getPathsimy());
					searcher.setSimilarity(Symy);
					
					List<NewsDetect> listNewsDetect = search(q, searcher, alert);
				
						
				
				
				for (NewsDetect newsDetect : listNewsDetect) {
					
					float Sl = newsDetect.getScoreLucene();
					
					Integer Ws = newsDetect.getSite().getType().getValue();
					
					List<Location> Loca_ = Loca.get(newsDetect.getLink());
					
					newsDetect.setLocation(Loca_);
					
					int Wl=0;
					if (Loca_!=null)
						{
						for (Location location : Loca_) {
							if (location.getType()==null&&Wl<2)
								Wl=2;
							else
								if (Wl<location.getType().getValue())
									Wl=location.getType().getValue();
						}
						}
					
					JexlContext context = new MapContext();
				    
					try{
				    context.set("Wa".toLowerCase(),newsDetect.getAlertDetect().getType().getValue());
					}catch (Exception E)
					{
						context.set("Wa".toLowerCase(),0);
					}
					
				    context.set("Wl".toLowerCase(),Wl );
				    context.set("Ws".toLowerCase(),Ws );
				    context.set("Sl".toLowerCase(),Sl );
				    
				    Number result = (Number) Symy.getGeneralFor().evaluate(context);
				    
				    float ScoreF=result.floatValue();
				    
				    newsDetect.setScore(ScoreF);
				}
				
				if (!listNewsDetect.isEmpty()) {
					LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/alertdetected1") + listNewsDetect.size()
							+ LanguageLoad.getinstance().find("internal/index/info/alertdetected2") + alert.getTitle());
					newsDetectRepositorysave(listNewsDetect);
				}
				alert.setUltimaRecuperacion(new Timestamp(to));
				if (alert instanceof Alert) {
					alert = alertService.getOneById(alert.getId());
					alert.setUltimaRecuperacion(new Timestamp(to));
					alertServiceupdate((Alert) alert);
				} else {
					alert = riskService.getOneById(alert.getId());
					alert.setUltimaRecuperacion(new Timestamp(to));
					riskServiceupdate((Risk) alert);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/alertendfind"));
	}

	public List<Location> searchLocation(Date from, Date to) {
		ArrayList<Location> Salida=new ArrayList<Location>();
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			e1.printStackTrace();
			LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/alertopenindex"));
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		// Proceso de detección de localizaciones
		Iterable<Location> locations = locationRepository.findAll();
		
		Long StartL=0l;
		if (from!=null)
			StartL=from.getTime();
		
		for (Location loc : locations) {

			Query q;
			try {
				q = createQuery(loc, StartL, to.getTime());
			} catch (UnsupportedEncodingException e1) {
				LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/alertcreatingquery") + loc.getName());
				continue;
			}
			try {
				List<String> listNewsDetect = searchLocation(q, searcher, loc);
				loc = locationRepository.findOne(loc.getId());
				loc.setUltimaRecuperacion(new Timestamp(to.getTime()));
				loc.getNews().addAll(listNewsDetect);
				Salida.add(loc);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return Salida;
	}



	

	

}
