package com.ikip.newsdetect.main.service;

import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.*;
import es.ucm.visavet.gbf.app.service.ConfigurationServiceIO;
import es.ucm.visavet.gbf.app.service.TestNewsIndexService;
import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TestNewsIndexServiceImpl extends AbstractNewsIndexService implements TestNewsIndexService, Runnable {

	private final static Logger LOGGER = Logger
			.getLogger(TestNewsIndexServiceImpl.class);

	
	List<NewsDetect> SalidaR;
	List<NewsDetect> SalidaA;
	List<Location> SalidaO;
	List<AlertAbstract> SalidaAA;
	List<AlertAbstract> SalidaAR;
	
	@Autowired
	public TestNewsIndexServiceImpl(ConfigurationServiceIO IOFolder) {
		super(IOFolder);
	}
	
	public void clearDirectory(){
		this.allDirectory=null;
		this.allIndex=null;
		getDirectory();
		getWriter();
		LOGGER.info(LanguageLoad.getinstance().find("internal/index/info/newsindesactivate"));
		SalidaR=new ArrayList<NewsDetect>();
		SalidaA=new ArrayList<NewsDetect>();
		SalidaO=new ArrayList<Location>();
		SalidaAA=new ArrayList<AlertAbstract>();
		SalidaAR=new ArrayList<AlertAbstract>();
	}
	
	
	@Override
	public void runMe() {
		
		super.run();
		
	}

	@Override
	public void initDirectory() {

//			getDirectory();
//			getWriter();
			
		
		
	}

	@Override
	public void stopDirectory() throws IOException {
		super.stopDirectory();
		
	}


	@Override
	public String helloService() {
		return "hola";
	}



	@Override
	protected Directory getDirectory() {
		if (this.allDirectory == null)
			this.allDirectory = new RAMDirectory();
		return this.allDirectory;
	}

	@Override
	protected IndexWriter getWriter() {
		if (this.allIndex == null) {
			try {
				
	SpecializedClassicSimilarity Symy=new SpecializedClassicSimilarity();
				
				Symy.loadfromfile(IOFolder.getPathgen(),IOFolder.getPathsimy());
				
				IndexWriterConfig config = new IndexWriterConfig(getAnalyzer());
				 config.setSimilarity(Symy);
				
				this.allIndex = new IndexWriter(getDirectory(),
						config);
				
			} catch (IOException e) {
				LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/erroropeningindexdirectory")
						+ e.getMessage());
			}
		} else if (!this.allIndex.isOpen()) {
			try {
				this.allIndex = new IndexWriter(getDirectory(),
						new IndexWriterConfig(getAnalyzer()));
			} catch (IOException e) {
				LOGGER.error(LanguageLoad.getinstance().find("internal/index/error/erroropeningindexdirectory")
						+ e.getMessage());
			}
		}
		
		
		try {
			this.allIndex.commit();
		} catch (IOException e) {
			LOGGER.error("Error al abrir el indice o el directorio -> "
					+ e.getMessage());
		}
		
		return this.allIndex;
	}

	@Override
	protected Feed getFeedByCodeName(Long site) {
		return new Feed();
	}

	@Override
	protected void updateFeed(Feed feed) {
		//NADA
		
	}

	@Override
	protected void updateIndex() throws Exception {
		//NADA
		
	}

	@Override
	protected void resetAlertInter(AlertAbstract alert, IndexSearcher searcher, HashMap<String, List<Location>> Loca) throws UnsupportedEncodingException {
		// NADA
		
	}


	@Override
	protected void riskServiceupdate(Risk alert) {
		SalidaAR.add(alert);
		
	}

	@Override
	protected void alertServiceupdate(Alert alert) {
		 SalidaAA.add(alert);
		
	}

	@Override
	protected void newsDetectRepositorysave(List<NewsDetect> listNewsDetect) {
		// NADA
		long Idini=0l;
		for (NewsDetect newsDetect : listNewsDetect) {
			newsDetect.setId(Idini++);
			if (newsDetect.getAlertDetect() instanceof Alert)
				SalidaA.add(newsDetect);
			else
				if (newsDetect.getAlertDetect() instanceof Risk)
					SalidaR.add(newsDetect);

		}
	}

	@Override
	public void indexNews(List<News> lNews) throws Exception {
		super.indexNews(lNews);
	}

	


	@Override
	protected void locationRepositorysave(Location loc) {
		SalidaO.add(loc);
		
	}


	public List<NewsDetect> getSalidaR() {
		return SalidaR;
	}


	public void setSalidaR(List<NewsDetect> salidaR) {
		SalidaR = salidaR;
	}


	public List<NewsDetect> getSalidaA() {
		return SalidaA;
	}


	public void setSalidaA(List<NewsDetect> salidaA) {
		SalidaA = salidaA;
	}


	public List<Location> getSalidaO() {
		return SalidaO;
	}


	public void setSalidaO(List<Location> salidaO) {
		SalidaO = salidaO;
	}


	@Override
	public List<AlertAbstract> getSalidaAA() {
		return SalidaAA;
	}


	@Override
	public List<AlertAbstract> getSalidaAR() {
		return SalidaAR;
	}


}
