package com.ikip.newsdetect.main.service;

import es.ucm.visavet.gbf.app.domain.AlertAbstract;
import es.ucm.visavet.gbf.app.domain.Location;
import es.ucm.visavet.gbf.app.domain.News;
import es.ucm.visavet.gbf.app.domain.NewsDetect;

import java.io.IOException;
import java.util.List;

public interface TestNewsIndexService {

	public void initDirectory();
	public void stopDirectory() throws IOException;
	public void indexNews(List<News> news) throws Exception;
	public List<NewsDetect> getSalidaA();
	public List<NewsDetect> getSalidaR();
	public List<Location> getSalidaO();
	public List<AlertAbstract> getSalidaAA();
	public List<AlertAbstract> getSalidaAR();
	public String helloService();
	void runMe();
	public void clearDirectory();
}
