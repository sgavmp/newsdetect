package com.ikip.newsdetect.extractor.service;

import com.ikip.newsdetect.extractor.exception.FeedAutoScrapException;
import com.ikip.newsdetect.extractor.exception.FeedManualScrapException;
import com.ikip.newsdetect.extractor.exception.FeedRssScrapException;
import com.ikip.newsdetect.model.Feed;
import com.ikip.newsdetect.model.News;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedScraping {

	List<News> scrapNews(Feed feed, LocalDateTime after, boolean withOutLimit) throws FeedAutoScrapException, FeedRssScrapException, FeedManualScrapException;
	public News scrapOneNews(Feed feed) throws FeedAutoScrapException, FeedRssScrapException, FeedManualScrapException;
	public News getNewsFromSite(String link, Feed feed);
}
