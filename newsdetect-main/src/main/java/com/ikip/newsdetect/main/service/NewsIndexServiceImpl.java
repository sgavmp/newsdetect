package com.ikip.newsdetect.main.service;

import com.google.common.collect.Lists;
import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.*;
import es.ucm.visavet.gbf.app.repository.NewsDetectRepository;
import es.ucm.visavet.gbf.app.repository.NewsRepository;
import es.ucm.visavet.gbf.app.service.ConfiguracionService;
import es.ucm.visavet.gbf.app.service.ConfigurationServiceIO;
import es.ucm.visavet.gbf.app.service.FeedService;
import es.ucm.visavet.gbf.app.service.NewsIndexService;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.MapContext;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.*;

//import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Service
public class NewsIndexServiceImpl extends AbstractNewsIndexService implements NewsIndexService, Runnable{

	protected final static Logger LOGGER = Logger
			.getLogger(NewsIndexServiceImpl.class);



	@Autowired
	private ConfiguracionService configuracion;


	@Autowired
	private FeedService feedService;

	@Autowired
	private TaskScheduler scheduler;

	@Autowired
	private NewsRepository newsRepository;

	@Autowired
	private NewsDetectRepository newsDetectRepository;

//	@Autowired
//	private AutowireCapableBeanFactory beanFactory;

	@Autowired
	public NewsIndexServiceImpl(ConfigurationServiceIO IOFolder) {
		super(IOFolder);
	}

	@PostConstruct
	public void initDirectory() {
		if (!"".equals(configuracion.getConfiguracion().getPathIndexNews())
				&& null != configuracion.getConfiguracion().getPathIndexNews()) {
			getDirectory();
			getWriter();
			LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/newsindesactivate"));
		} else {
			LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/newsindesnoactivate"));
		}
	}

	protected Directory getDirectory() {
		if (this.allDirectory == null) {
			try {
				this.allDirectory = FSDirectory.open(new File(configuracion
						.getConfiguracion().getPathIndexNews().concat("/all"))
						.toPath());
			} catch (IOException e) {
				LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/erroropeningindexdirectory")
						+ e.getMessage());
			}
		}
		return this.allDirectory;
	}

	protected IndexWriter getWriter() {
		if (this.allIndex == null) {
			try {
				
				SpecializedClassicSimilarity Symy=new SpecializedClassicSimilarity();
				
				Symy.loadfromfile(IOFolder.getPathgen(),IOFolder.getPathsimy());
				
				IndexWriterConfig config = new IndexWriterConfig(getAnalyzer());
				 config.setSimilarity(Symy);
				 
				this.allIndex = new IndexWriter(getDirectory(),config);
			} catch (IOException e) {
				LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/erroropeningindexdirectory")
						+ e.getMessage());
			}
		} else if (!this.allIndex.isOpen()) {
			try {
				this.allIndex = new IndexWriter(getDirectory(),
						new IndexWriterConfig(getAnalyzer()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/erroropeningindexdirectory")
						+ e.getMessage());
			}
		}
		return this.allIndex;
	}



	@Override
	public void indexNews(News news) throws Exception {
		super.indexNews(news);
	}

	@Override
	public void indexNews(List<News> lNews) throws Exception {
		super.indexNews(lNews);
	}

	@Override
	public void removeNews(News news) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeNews(List<News> news) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNews(News news) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNews(List<News> news) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<News> search(AlertAbstract alert) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stopDirectory() throws IOException {
		super.stopDirectory();
	}

	@Transactional
	@Override
	public boolean markNewNews(Feed feed) {
		this.newsPending = true;
		if (!taskScheduled) {
			Date startTime = new Date();
			startTime.setTime(startTime.getTime() + 1000 * 300);
			scheduler.schedule(this, startTime);
			taskScheduled = true;
		}
		return true;
	}

	

	@Override
	public void run() {
		super.run();;
	}


	protected void updateIndex() throws Exception {
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/addnewsindex"));
		int i = 0;
		for (News news : newsRepository.findAll()) {
			addDocument(news);
			i++;
			newsRepository.delete(news);
		}
		getWriter().commit();
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/addednewsindex1") + i + LanguageLoad.getinstance().find("internal/index/info/addednewsindex2"));
	}

//	protected void processLocation(IndexSearcher searcher) {
//		Iterable<Location> locations = locationRepository.findAll();
//		for (Location loc : locations) {
//			Long from = loc.getUltimaRecuperacion() != null ? loc
//					.getUltimaRecuperacion().getTime() : 0;
//			Long to = new Date().getTime();
//			Query q;
//			try {
//				q = createQuery(loc, from, to);
//			} catch (UnsupportedEncodingException e1) {
//				LOGGER.error("Error al construir la query de loc:" + loc.getName());
//				continue;
//			}
//			try {
//				List<String> listNewsDetect = searchLocation(q, searcher, loc);
//				loc = locationRepository.findOne(loc.getId());
//				loc.setUltimaRecuperacion(new Timestamp(to));
//				loc.getNews().addAll(listNewsDetect);
//				locationRepository.save(loc);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		
//	}
	
	@Override
	@Transactional
	public void resetAllAlerts() throws IOException {
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/startalertfindindex"));
		boolean closed = getWriter() == null ? false : getWriter().isOpen();
		if (!closed) {
			getDirectory();
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/erroropeningindexfind"));
		}
		// newsDetectRepository.deleteAll();
		IndexSearcher searcher = new IndexSearcher(reader);
		Set<Alert> alertas = alertService.getAllAlert();
		Set<Risk> riesgos = riskService.getAllAlert();
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/foundalertrisk1")+ alertas.size() + LanguageLoad.getinstance().find("internal/index/info/foundalert2"));
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/foundalertrisk1")+ riesgos.size() + LanguageLoad.getinstance().find("internal/index/info/foundrisk2"));
		
		
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
				
				if (!padre.contains(location))
					padre.add(location);
				
				
				Loca.put(string, padre);
				
			}
		}	
		
		for (Alert alert : alertas) {
			resetAlertInter(alert, searcher,Loca);
		}
		for (Risk alert : riesgos) {
			resetAlertInter(alert, searcher,Loca);
		}
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
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/endalertfindindex"));

	}

	protected void resetAlertInter(AlertAbstract alert, IndexSearcher searcher, HashMap<String, List<Location>> Loca) throws UnsupportedEncodingException {
		Long to = new Date().getTime();
		Query q = createQuery(alert, 0L, to);
		try {
			
			SpecializedClassicSimilarity Symy=new SpecializedClassicSimilarity();
			
			Symy.loadfromfile(IOFolder.getPathgen(),IOFolder.getPathsimy());
				searcher.setSimilarity(Symy);
			
			List<NewsDetect> listNewsDetect = search(q, searcher, alert);
			
			
			
			
		
		
		for (NewsDetect newsDetect : listNewsDetect) {
			
			float Sl = newsDetect.getScoreLucene();
			
			Integer Ws=1;

				if (newsDetect.getSite()!=null)
					Ws = newsDetect.getSite().getType().getValue();

			
			
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
			
			
			LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/alertdetecteddelete"));
			List<NewsDetect> newsToRemove = Lists.newArrayList(alert
					.getNewsDetect());
			newsDetectRepository.save(listNewsDetect);
			alert.getNewsDetect().clear();
			alert.getNewsDetect().addAll(listNewsDetect);
			if (alert instanceof Alert) {
				alert = alertService.getOneById(alert.getId());
				alert.setUltimaRecuperacion(new Timestamp(to));
				alertService.update((Alert) alert);
			} else {
				alert = riskService.getOneById(alert.getId());
				alert.setUltimaRecuperacion(new Timestamp(to));
				riskService.update((Risk) alert);
			}
			for (NewsDetect news : newsToRemove) {
				newsDetectRepository.delete(news.getId());
			}
			if (listNewsDetect.size() > 0)
				LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/alertdetectedresult1") + listNewsDetect.size()
						+ LanguageLoad.getinstance().find("internal/index/info/alertdetectedresult2") + alert.getTitle());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void resetAlert(AlertAbstract alert) throws IOException {
		super.resetAlert(alert);
	}

	private void resetLocationInter(Location loc, IndexSearcher searcher) throws UnsupportedEncodingException {
		Long to = new Date().getTime();
		Query q = createQuery(loc, 0L, to);
		try {
			List<String> listNewsDetect = searchLocation(q, searcher, loc);
			loc = locationRepository.findOne(loc.getId());
			loc.setUltimaRecuperacion(new Timestamp(to));
			loc.setNews(listNewsDetect);
			locationRepository.save(loc);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void resetAllLocation() throws IOException {
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/startlocationfind"));
		boolean closed = getWriter() == null ? false : getWriter().isOpen();
		if (!closed) {
			getDirectory();
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/erroropeningindexfind"));
		}
		// newsDetectRepository.deleteAll();
		IndexSearcher searcher = new IndexSearcher(reader);
		// Proceso de detección de localizaciones
		Iterable<Location> locations = locationRepository.findAll();
		for (Location loc : locations) {
			resetLocationInter(loc, searcher);
		}
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
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/endlocationfind"));

	}

	@Override
	public void resetLocation(Location loc) throws IOException {
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/startalertfindindex"));
		boolean closed = getWriter() == null ? false : getWriter().isOpen();
		if (!closed) {
			getDirectory();
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/erroropeningindexfind"));
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		resetLocationInter(loc, searcher);
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
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/endalertfindindex"));
	}

	@Override
	public void updateIndex(Feed feed) throws Exception {
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/erroropeningindexfind"));
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		Query q = new TermQuery(new Term(News.fieldSite, feed.getId()
				.toString()));
		TopDocs docs = searcher.search(q, Integer.MAX_VALUE);
		ScoreDoc[] docsA = docs.scoreDocs;
		for (int i = 0; i < docs.totalHits; ++i) {
			int docId = docsA[i].doc;
			Document doc = searcher.doc(docId);
			doc.removeFields(News.fieldSiteType);
			doc.removeFields(News.fieldSiteLoc);
			doc.add(new StringField(News.fieldSiteType, feed.getFeedType()
					.getValue().toString(), Field.Store.YES));
			for (FeedPlaceEnum type : feed.getFeedPlace())
				doc.add(new StringField(News.fieldSiteLoc, type.getValue()
						.toString(), Field.Store.YES));
			String url = doc.get("id").toString();
			try {
				getWriter().updateDocument(new Term("id", url), doc);
			} catch (Exception e) {
				LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/errorupdatingdocumentbyid") + docId
						+ LanguageLoad.getinstance().find("internal/index/error/errorupdatingdocumentbyid2") + url);
			}
		}
		reader.close();
		getWriter().commit();
	}

	@Override
	public List<NewsDetect> search(String query) throws Exception {
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/startalertfindindextest"));
		Alert alert = new Alert();
		alert.setWords(query);
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/erroropeningindexfind"));
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		Long to = new Date().getTime();
		Query q = createQuery(alert, 0L, to);
		List<NewsDetect> listNewsDetect = Lists.newArrayList();
		try {
			
			SpecializedClassicSimilarity Symy=new SpecializedClassicSimilarity();
			
			Symy.loadfromfile(IOFolder.getPathgen(),IOFolder.getPathsimy());
				searcher.setSimilarity(Symy);
			
			listNewsDetect = search(q, searcher, alert);
			
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
			
			LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/alertdetectedtest") + listNewsDetect.size()
					+ LanguageLoad.getinstance().find("internal/index/info/alertdetectedtest2"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/endalertfindindextest"));
		if (listNewsDetect.size() > 21)
			return listNewsDetect.subList(0, 20);
		else
			return listNewsDetect;
	}
	
	@Override
	public List<NewsDetect> search(String query, Date Start, Date Ends,Similarity Symy) throws Exception {
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/startalertfindindextest"));
		Alert alert = new Alert();
		alert.setWords(query);
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/erroropeningindexfind"));
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		
		if (Symy!=null)
			searcher.setSimilarity(Symy);
		
		Long StartL=0l;
		if (Start!=null)
			StartL=Start.getTime();
		
		
		Query q = createQuery(alert, StartL, Ends.getTime());
		List<NewsDetect> listNewsDetect = Lists.newArrayList();
		try {
			listNewsDetect = search(q, searcher, alert);
			LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/alertdetectedtest") + listNewsDetect.size()
					+ LanguageLoad.getinstance().find("internal/index/info/alertdetectedtest2"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/endalertfindindextest"));
		if (listNewsDetect.size() > 21)
			return listNewsDetect.subList(0, 20);
		else
			return listNewsDetect;
	}

	@Override
	public void removeFeedFromIndex(Feed feed) throws Exception {
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/erroropeningindexfind"));
		}
		Query q = new TermQuery(new Term(News.fieldSite, feed.getId()
				.toString()));
		try {
			getWriter().deleteDocuments(q);
		} catch (Exception e) {
			LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/errordeletingnewswebsite") + feed.getId());
		}
		reader.close();
		getWriter().commit();

	}

	@Override
	protected Feed getFeedByCodeName(Long site) {
		return feedService.getFeedByCodeName(site);
	}

	@Override
	protected void updateFeed(Feed feed) {
		feedService.updateFeed(feed);
		
	}

	@Override
	protected void riskServiceupdate(Risk alert) {
		riskService.update(alert);
		
	}

	@Override
	protected void alertServiceupdate(Alert alert) {
		alertService.update(alert);		
	}

	@Override
	protected void newsDetectRepositorysave(List<NewsDetect> listNewsDetect) {
		newsDetectRepository.save(listNewsDetect);
		
	}

	@Override
	protected void locationRepositorysave(Location loc) {
		locationRepository.save(loc);
		
	}




}
