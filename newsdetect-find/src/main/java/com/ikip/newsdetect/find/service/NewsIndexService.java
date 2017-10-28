package com.ikip.newsdetect.find.service;

import com.ikip.newsdetect.model.*;
import org.apache.lucene.search.similarities.Similarity;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface NewsIndexService {


	void removeNews(News news);

	void removeNews(List<News> news);

	void updateNews(News news);

	void updateNews(List<News> news);

	List<News> search(Alert alert);

	@Transactional
	void resetAllAlerts(List<Alert> alerts) throws Exception;

	void resetAllLocation(List<Location> locationList) throws IOException;

	void resetLocation(Location loc) throws IOException;

	void updateIndex(Feed feed) throws Exception;

	List<DetectedNews> search(String query) throws Exception;

	List<DetectedNews> search(String query, Date Start, Date Ends, Similarity Symy) throws Exception;

	void removeFeedFromIndex(Feed feed) throws Exception;

	void indexNews(News news) throws Exception;

	void indexNews(List<News> lNews) throws Exception;

	void stopDirectory() throws IOException;

	List<DetectedNews> run(List<News> listNews) throws Exception;
}
