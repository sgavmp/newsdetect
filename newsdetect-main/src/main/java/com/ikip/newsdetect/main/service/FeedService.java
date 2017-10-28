package com.ikip.newsdetect.main.service;

import com.ikip.newsdetect.main.dto.FeedForm;
import com.ikip.newsdetect.main.scheduler.SchedulerService;
import com.ikip.newsdetect.model.DetectedNews;
import com.ikip.newsdetect.model.Feed;
import com.ikip.newsdetect.model.News;
import com.ikip.newsdetect.model.UpdateStateEnum;

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
	public List<DetectedNews> findAllDistinctNewsDetectByFeedOrderByDatePub(Feed feed);
	public Feed setSateOfFeed(Feed feed, UpdateStateEnum state);
	public List<String> createFeedAuto(String[] listURL);

}
