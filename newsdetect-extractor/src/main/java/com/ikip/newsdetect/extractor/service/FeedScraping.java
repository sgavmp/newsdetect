package com.ikip.newsdetect.extractor.service;

import com.ikip.newsdetect.model.Feed;
import com.ikip.newsdetect.model.News;

import java.io.InputStream;
import java.sql.Date;
import java.util.List;

public interface FeedScraping {
	public List<News> scrapNews(Feed feed, Date after, boolean withOutLimit);
	public News scrapOneNews(FeedDto feed);
	public News getNewsFromSite(String link, Feed feed);
	public List<News> scrapingHistoric(Feed feed, InputStream xml);
}
