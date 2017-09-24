package com.ikip.newsdetect.main.service;

import es.ucm.visavet.gbf.app.domain.*;
import es.ucm.visavet.gbf.app.scheduler.SchedulerService;

import java.sql.Date;
import java.util.List;

public interface FeedService {
	public Feed createFeed(Feed feed);
	public Feed updateFeed(Feed feed);
	public List<News> scrapFeed(Feed feed, Date after, boolean withOutLimit);
	public List<Feed> getAllFeedAlph();
	public List<Feed> getAllFeedLastUp();
	public Feed getFeedByCodeName(Long codeName);
	public boolean removeFeed(Feed feed);
	public void setSchedulerService(SchedulerService schedulerService);
	public News testFeed(FeedForm feed);
	public List<NewsDetect> findAllDistinctNewsDetectByFeedOrderByDatePub(Feed feed);
	public Feed setSateOfFeed(Feed feed, UpdateStateEnum state);
	public List<String> createFeedAuto(String[] listURL);

}
