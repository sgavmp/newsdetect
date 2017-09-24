package com.ikip.newsdetect.main.service;

import es.ucm.visavet.gbf.app.domain.*;
import org.apache.lucene.search.similarities.Similarity;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface NewsIndexService {

	public void initDirectory();
	public void stopDirectory() throws IOException;
	public boolean markNewNews(Feed feed);
	public void indexNews(News news) throws Exception;
	public void indexNews(List<News> news) throws Exception;
	public void removeNews(News news);
	public void removeNews(List<News> news);
	public void updateNews(News news);
	public void updateNews(List<News> news);
	public void updateIndex(Feed feed) throws Exception;
	public List<News> search(AlertAbstract alert);
	public void resetAllAlerts() throws IOException;
	public void resetAlert(AlertAbstract alert) throws IOException;
	public List<NewsDetect> search(String query) throws Exception;
	public List<NewsDetect> search(String query, Date Start, Date Ends, Similarity Symy) throws Exception;
	public List<Location> searchLocation(Date Start, Date Ends) throws Exception;
	void resetLocation(Location loc) throws IOException;
	public void removeFeedFromIndex(Feed feed) throws Exception;
	void resetAllLocation() throws IOException;
}
