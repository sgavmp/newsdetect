package com.ikip.newsdetect.find.service;

import com.ikip.newsdetect.find.exception.NewsIndexException;
import com.ikip.newsdetect.find.repository.EventRepository;
import com.ikip.newsdetect.find.repository.FeedRepository;
import com.ikip.newsdetect.find.repository.NewsRepository;
import com.ikip.newsdetect.model.*;
import com.ikip.newsdetect.topic.manager.ITopicsManager;
import com.ikip.newsdetect.topic.queryconstructor.ParseException;
import com.ikip.newsdetect.topic.queryconstructor.QueryConstructor;
import com.ikip.newsdetect.topic.queryconstructor.QueryConstructorSemantics;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

//import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

@Slf4j
@Service
public class NewsIndexServiceImpl implements NewsIndexService{


	protected Directory allDirectory;
	protected IndexWriter allIndex;
	protected boolean newsPending = false, taskScheduled = false;
	private final ITopicsManager topicManager;
	private final String indexPath;
	private final FeedRepository feedRepository;
	private final EventRepository eventRepository;


	@Autowired
	public NewsIndexServiceImpl(ITopicsManager topicManager, @Value("${newsdetect.index.path}") String indexPath, FeedRepository feedRepository, NewsRepository newsRepository, EventRepository eventRepository) {
		this.topicManager = topicManager;
		this.indexPath = indexPath;
		this.feedRepository = feedRepository;
		this.eventRepository = eventRepository;
	}


	@PostConstruct
	public void initDirectory() {
			if (indexPath != null && !indexPath.isEmpty()) {
				getDirectory();
			getWriter();
		} else {
			log.info("");//TODO
		}
	}

	protected Directory getDirectory() {
		if (this.allDirectory == null) {
			try {
				this.allDirectory = FSDirectory.open(new File(indexPath.concat("/all"))
						.toPath());
			} catch (IOException e) {
				log.error("");//TODO
			}
		}
		return this.allDirectory;
	}

	protected IndexWriter getWriter() {
		if (this.allIndex == null) {
			try {
				
				IndexWriterConfig config = new IndexWriterConfig(getAnalyzer());

				this.allIndex = new IndexWriter(getDirectory(),config);
			} catch (IOException e) {
				log.error("");//TODO
			}
		} else if (!this.allIndex.isOpen()) {
			try {
				this.allIndex = new IndexWriter(getDirectory(),
						new IndexWriterConfig(getAnalyzer()));
			} catch (IOException e) {
				log.error("");//TODO
			}
		}
		return this.allIndex;
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
	public List<News> search(Alert alert) {
		// TODO Auto-generated method stub
		return null;
	}

	protected void updateIndex(List<News> listNews) throws Exception {
		int i = 0;
		for (News news : listNews) {
			addDocument(news);
			i++;
		}
		getWriter().commit();
	}

	@Override
	@Transactional
	public void resetAllAlerts(List<Alert> alerts) throws Exception {
		boolean closed = getWriter() == null ? false : getWriter().isOpen();
		if (!closed) {
			getDirectory();
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			log.error("");//TODO
		}
		// newsDetectRepository.deleteAll();
		IndexSearcher searcher = new IndexSearcher(reader);
		
		
		Date Start = new Date();
		Start.setTime(0l);
		//List<Location> ResultadoBasicoLoc = searchLocation(Start,new Date());
		
		HashMap<String, List<Location>> Loca=new HashMap<String, List<Location>>();
		/* TODO revisar
		for (Location location : ResultadoBasicoLoc) {
			
			if (location.getType()==null) {
				location.setType(LocationLevelEnum.REGION);
			}
			
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
		*/
		for (Alert alert : alerts) {
			resetAlertInter(alert, searcher,Loca);
		}
		try {
			reader.close();
		} catch (IOException e) {
			log.error("");//TODO
		}
		if (!closed) {
			getDirectory().close();
			this.allDirectory = null;
		}

	}

	protected List<DetectedNews> resetAlertInter(Alert alert, IndexSearcher searcher, HashMap<String, List<Location>> Loca) throws Exception {
		Long to = new Date().getTime();
		Query q = createQuery(alert, 0L, to);
		try {
			
			List<DetectedNews> listDetectedNews = search(q, searcher, alert);

			return listDetectedNews;
		} catch (Exception e) {
			log.error("");//TODO
			throw e;
		}
	}

	private void resetLocationInter(Location loc, IndexSearcher searcher) throws UnsupportedEncodingException {
		Long to = new Date().getTime();
		Query q = createQuery(loc, 0L, to);
		try {
			List<String> listNewsDetect = searchLocation(q, searcher, loc);
			//TODO
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void resetAllLocation(List<Location> locationList) throws IOException {
		boolean closed = getWriter() == null ? false : getWriter().isOpen();
		if (!closed) {
			getDirectory();
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			log.error("");//TODO
		}
		// newsDetectRepository.deleteAll();
		IndexSearcher searcher = new IndexSearcher(reader);
		// Proceso de detección de localizaciones
		Iterable<Location> locations = locationList;
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

	}

	@Override
	public void resetLocation(Location loc) throws IOException {
		boolean closed = getWriter() == null ? false : getWriter().isOpen();
		if (!closed) {
			getDirectory();
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			log.error("");//TODO
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
	}

	@Override
	public void updateIndex(Feed feed) throws Exception {
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			log.error("");//TODO
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
			doc.add(new StringField(News.fieldSiteType, feed.getFeedType(), Field.Store.YES));
			//TODO revisar
			//for (FeedPlaceEnum type : feed.getFeedPlace()) {
			//	doc.add(new StringField(News.fieldSiteLoc, type.getValue()
			//			.toString(), Field.Store.YES));
			//}
			String url = doc.get("id").toString();
			try {
				getWriter().updateDocument(new Term("id", url), doc);
			} catch (Exception e) {
				log.error("");//TODO
			}
		}
		reader.close();
		getWriter().commit();
	}

	@Override
	public List<DetectedNews> search(String query) throws Exception {
		Alert alert = new Alert();
		alert.setQuery(query);
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			log.error("");//TODO
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		Long to = new Date().getTime();
		Query q = createQuery(alert, 0L, to);
		List<DetectedNews> listDetectedNews = new ArrayList<>();
		try {
			listDetectedNews = search(q, searcher, alert);

		} catch (Exception e) {
			log.error("");//TODO
		}
		try {
			reader.close();
		} catch (IOException e) {
			log.error("");//TODO
		}
		if (listDetectedNews.size() > 21)
			return listDetectedNews.subList(0, 20);
		else
			return listDetectedNews;
	}
	
	@Override
	public List<DetectedNews> search(String query, Date Start, Date Ends, Similarity Symy) throws Exception {
		Alert alert = new Alert();
		alert.setQuery(query);
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			log.error("");//TODO
		}
		IndexSearcher searcher = new IndexSearcher(reader);
		
		if (Symy!=null)
			searcher.setSimilarity(Symy);
		
		Long StartL=0l;
		if (Start!=null)
			StartL=Start.getTime();
		
		
		Query q = createQuery(alert, StartL, Ends.getTime());
		List<DetectedNews> listDetectedNews = new ArrayList<>();
		try {
			listDetectedNews = search(q, searcher, alert);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
		}
		log.error("");//TODO
		return listDetectedNews;
	}

	@Override
	public void removeFeedFromIndex(Feed feed) throws Exception {
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			log.error("");//TODO
		}
		Query q = new TermQuery(new Term(News.fieldSite, feed.getId()
				.toString()));
		try {
			getWriter().deleteDocuments(q);
		} catch (Exception e) {
			log.error("");//TODO
		}
		reader.close();
		getWriter().commit();

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
						.dateToString(Date.from(news.getPubDate().toInstant(ZoneOffset.UTC)), DateTools.Resolution.MINUTE),
						Field.Store.YES));
				d.add(new StringField(News.fieldSite,
						news.getFeedId().toString(), Field.Store.YES));
				d.add(new StringField(News.fieldDateCreate, DateTools
						.dateToString(new Date(), DateTools.Resolution.MINUTE),
						Field.Store.YES));
				Feed feed = feedRepository.findOne(news.getFeedId());
				if (feed.getFeedType() == null) {
					feed.setFeedType(feed.getFeedType());
				}
				d.add(new StringField(News.fieldSiteType, feed.getFeedType(), Field.Store.YES));
				List<String> places = feedRepository.getPlacesByFeedId(news.getFeedId());
				for (String place : places) {
					d.add(new StringField(News.fieldSiteLoc, place, Field.Store.YES));
				}
				getWriter().updateDocument(
						new Term(News.fieldUrl, news.getUrl()), d);
			} catch (NullPointerException ex) {
				log.error("");//TODO
				throw new NewsIndexException("", ex);
			}
		}
	}


	protected List<DetectedNews> search(Query q, IndexSearcher searcher,
										Alert alert) throws Exception {
		TopDocs docs = searcher.search(q, Integer.MAX_VALUE);
		ScoreDoc[] docsA = docs.scoreDocs;
		List<DetectedNews> listDetectedNews = new ArrayList<>();
		for (int i = 0; i < docs.totalHits; ++i) {
			int docId = docsA[i].doc;
			Document d = searcher.doc(docId);
			DetectedNews newsDocument = new DetectedNews();
			newsDocument.setAlertId(alert.getId());
			newsDocument.setDatePub(LocalDateTime.from(DateTools.stringToDate(d.get("datePub")).toInstant()));
			newsDocument.setLink(d.get("id"));
			newsDocument.setScore(docsA[i].score);
			newsDocument.setScoreLucene(docsA[i].score);
			newsDocument.setFeedId(new Long(d.get("feed")));
			if (alert == null || newsDocument.getFeedId() == null) {
				log.error("");//TODO
			}
			newsDocument.setTitle(d.get("title"));
			listDetectedNews.add(newsDocument);
		}
		return listDetectedNews;
	}


	protected Query createQuery(Alert alert, Long from, Long to) throws UnsupportedEncodingException {
		InputStreamReader alertDefinition = new InputStreamReader(new ByteArrayInputStream(alert.getQuery()
				.getBytes()), "UTF-8");
		QueryConstructor queryConstructorBody = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldBody,
						News.fieldBodyNoCase, News.fieldSiteLoc,
						News.fieldSiteType), alertDefinition);
		Query q = null;
		try {
			q = queryConstructorBody.topic();
		} catch (ParseException e) {
			log.error("");//TODO
		}
		alertDefinition = new InputStreamReader(new ByteArrayInputStream(alert.getQuery().getBytes()), "UTF-8");
		QueryConstructor queryConstructorTitle = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldTitle,
						News.fieldTitleNoCase, News.fieldSiteLoc,
						News.fieldSiteType), alertDefinition);
		Query q1 = null;
		try {
			q1 = queryConstructorTitle.topic();
		} catch (ParseException e) {
			log.error("");//TODO
		}
		Query q2 = TermRangeQuery.newStringRange(News.fieldDateCreate,
				DateTools.timeToString(from, DateTools.Resolution.MINUTE),
				DateTools.timeToString(to, DateTools.Resolution.MINUTE), false, true);
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
				.getQuery().getBytes()), "UTF-8");
		QueryConstructor queryConstructorBody = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldBody,
						News.fieldBodyNoCase, News.fieldSiteLoc,
						News.fieldSiteType), alertDefinition);
		Query q = null;
		try {
			q = queryConstructorBody.topic();
		} catch (ParseException e) {
			log.error("");//TODO
		}
		alertDefinition = new InputStreamReader(new ByteArrayInputStream(location.getQuery()
				.getBytes()), "UTF-8");
		QueryConstructor queryConstructorTitle = new QueryConstructor(
				new QueryConstructorSemantics(topicManager, News.fieldTitle,
						News.fieldTitleNoCase, News.fieldSiteLoc,
						News.fieldSiteType), alertDefinition);
		Query q1 = null;
		try {
			q1 = queryConstructorTitle.topic();
		} catch (ParseException e) {
			log.error("");//TODO
		}
		Query q2 = TermRangeQuery.newStringRange("dateCreate",
				DateTools.timeToString(from, DateTools.Resolution.MINUTE),
				DateTools.timeToString(to, DateTools.Resolution.MINUTE), false, true);
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
		List<String> listNewsDetect = new ArrayList<>();
		for (int i = 0; i < docs.totalHits; ++i) {
			int docId = docsA[i].doc;
			Document d = searcher.doc(docId);
			DetectedNews newsDocument = new DetectedNews();
			newsDocument.setTitle(d.get("title"));
			listNewsDetect.add(d.get("id"));
		}
		return listNewsDetect;
	}

	public void resetAlert(Alert alert) throws Exception {
		boolean closed = getWriter() == null ? false : getWriter().isOpen();
		if (!closed) {
			getDirectory();
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			log.error("");//TODO
		}
		IndexSearcher searcher = new IndexSearcher(reader);

		Date start = new Date();
		start.setTime(0l);
//		List<Location> ResultadoBasicoLoc = searchLocation(Start, new Date());

		HashMap<String, List<Location>> Loca = new HashMap<String, List<Location>>();


		resetAlertInter(alert, searcher, Loca);
		try {
			reader.close();
		} catch (IOException e) {
			log.error("");//TODO
		}
		if (!closed) {
			getDirectory().close();
			this.allDirectory = null;
		}
	}

	@Override
	public void indexNews(News news) throws Exception {
		addDocument(news);
		getWriter().commit();
	}

	@Override
	public void indexNews(List<News> lNews) throws Exception {
		for (News news : lNews) {
			addDocument(news);
		}
		getWriter().commit();
	}

	@Override
	public void stopDirectory() throws IOException {
		getWriter().commit();
		getWriter().close();
		getDirectory().close();
		this.allDirectory = null;
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

	@Override
	public List<DetectedNews> run(List<News> listNews) throws Exception {
		taskScheduled = false;
		if (this.newsPending) {
			try {
				updateIndex(listNews);
			} catch (Exception e) {
				log.error("");//TODO
			}
			this.newsPending = false;
		}
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			log.error("");//TODO
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
		Set<Alert> tasks = new HashSet<>();
		//tasks.addAll(alertService.getAllAlert()); TODO


		Date Start = new Date();
		Start.setTime(0l);
		List<Location> ResultadoBasicoLoc = null;
		try {
			//ResultadoBasicoLoc = searchLocation(Start, new Date());
		} catch (Exception e) {
			e.printStackTrace();
		}



		for (Alert alert : tasks) {
			Event lastSearch = eventRepository.findOneByOriginIdAndTypeEventOrderByDateDesc(alert.getId(), TypeEventEnum.FIND_ALERT_END);
			Long from = lastSearch.getDate().toInstant(ZoneOffset.of(ZoneOffset.systemDefault().getId())).toEpochMilli();
			Long to = LocalDateTime.now().toEpochSecond(ZoneOffset.of(ZoneOffset.systemDefault().getId()));
			Query q;
			try {
				q = createQuery(alert, from, to);
			} catch (UnsupportedEncodingException e1) {
				log.error("");//TODO
				continue;
			}
			try {

				List<DetectedNews> listDetectedNews = search(q, searcher, alert);


				return listDetectedNews;
			} catch (Exception e) {
				log.error("");//TODO
				throw e;
			} finally {
				try {
					reader.close();
				} catch (IOException e) {
					log.error("");//TODO

				}
			}
		}
		return null;
	}

	public List<Location> searchLocation(List<Location> locations, Date from, Date to) {
		ArrayList<Location> Salida = new ArrayList<Location>();
		IndexReader reader = null;
		try {
			reader = DirectoryReader.open(getDirectory());
		} catch (IOException e1) {
			log.error("");//TODO
		}
		IndexSearcher searcher = new IndexSearcher(reader);

		Long StartL = 0l;
		if (from != null)
			StartL = from.getTime();

		for (Location loc : locations) {

			Query q;
			try {
				q = createQuery(loc, StartL, to.getTime());
			} catch (UnsupportedEncodingException e1) {
				log.error("");//TODO
				continue;
			}
			try {
				List<String> listNewsDetect = searchLocation(q, searcher, loc);
				//loc.getNews().addAll(listNewsDetect); TODO
				Salida.add(loc);
			} catch (Exception e) {
				log.error("");//TODO
			}
		}

		try {
			reader.close();
		} catch (IOException e) {
			log.error("");//TODO
		}
		return Salida;
	}

}
