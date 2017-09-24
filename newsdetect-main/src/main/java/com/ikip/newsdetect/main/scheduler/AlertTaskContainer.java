package com.ikip.newsdetect.main.scheduler;

import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.Feed;
import es.ucm.visavet.gbf.app.domain.FeedLog;
import es.ucm.visavet.gbf.app.domain.News;
import es.ucm.visavet.gbf.app.domain.UpdateStateEnum;
import es.ucm.visavet.gbf.app.repository.FeedLogRepository;
import es.ucm.visavet.gbf.app.repository.NewsRepository;
import es.ucm.visavet.gbf.app.service.FeedService;
import es.ucm.visavet.gbf.app.service.NewsIndexService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class AlertTaskContainer implements Runnable {

	private final static Logger LOGGER = Logger
			.getLogger(AlertTaskContainer.class);

	private Feed feed;
	@Autowired
	private FeedService service;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private NewsIndexService newsIndexService;
	@Autowired
	private NewsRepository newsRepository;
	@Autowired
	private FeedLogRepository feedlogrepository;

	public AlertTaskContainer(Feed feed) {
		this.feed = feed;
	}

	@Override
	public void run() {
		//TODO
		long time_start, time_end;
		time_start = System.currentTimeMillis();
		LOGGER.info(LanguageLoad.getinstance().find("internal/scheduler/info/alertinittask") + feed.getName());
		Feed feedComp = null;
		// Obtenemos el sitio de la base de datos por si se hubiese modificado o
		// borrado
		feedComp = service.getFeedByCodeName(feed.getId());
		if (feedComp != null) {
			feed = service.setSateOfFeed(feedComp, UpdateStateEnum.GET_NEWS);
			List<News> listNews = service.scrapFeed(feed, null, false);
			feed = service.getFeedByCodeName(feed.getId());
			
			//TODO

			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR,0);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);
			Set<FeedLog> hoys = feedlogrepository.readAllByDiaregistro(calendar.getTime());
			
			FeedLog updateday=null;
			if (!hoys.isEmpty())
				updateday=hoys.iterator().next();
			else
				updateday=new FeedLog();
			
			
			
			if (listNews != null) {
				if (!listNews.isEmpty()) {
					LOGGER.info(LanguageLoad.getinstance().find("internal/scheduler/info/alertrecover1") + listNews.size()
							+ LanguageLoad.getinstance().find("internal/scheduler/info/alertrecover2") + feed.getName());
					// newsCheckService.checkNews(listNews, feed);
					for (News news : listNews) {
						try {
							if (news != null) {
								// Si no ha extraido contenido ni titulo
								if (!"".equals(news.getContent())
										&& news.getContent() != null
										|| !"".equals(news.getTitle())
										&& news.getTitle() != null)
									newsRepository.save(news);
							} else
								LOGGER.warn(LanguageLoad.getinstance().find("internal/scheduler/warn/alertnullnews")
										+ feed.getName());
						} catch (Exception ex) {
							LOGGER.error(LanguageLoad.getinstance().find("internal/scheduler/error/alertsavenews1")
									+ feed.getName()
									+ LanguageLoad.getinstance().find("internal/scheduler/error/alertsavenews2")
									+ news.getUrl());
						}
					}
					newsIndexService.markNewNews(feed);
				} else {
					LOGGER.info(LanguageLoad.getinstance().find("internal/scheduler/info/alertrecovervacio")
							+ feed.getName());
				}
			}
			
			//TODO
			time_end = System.currentTimeMillis();
			long finalt = ( time_end - time_start );
			
			updateday.setTotalrecuperadas(updateday.getTotalrecuperadas()+1);
			updateday.setTiempodeuso(new Timestamp(updateday.getTiempodeuso().getTime()+finalt));
			updateday.setTotalrecuperadas(updateday.getTotalrecuperadas()+1);
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date(updateday.getDiaregistro().getTime());
			System.out.println("Hoy: " +dateFormat.format(date));

			System.out.println("Computo: " +updateday.getTiempodeuso().getTime());
			
			feedlogrepository.save(updateday);
			LOGGER.info(LanguageLoad.getinstance().find("internal/scheduler/info/alertrecoverfin") 
					+ feed.getName());
			feed = service.setSateOfFeed(feed, UpdateStateEnum.WAIT);
		} else {
			schedulerService.removeFeedTask(feed);
			LOGGER.info(LanguageLoad.getinstance().find("internal/scheduler/info/alertrecoverdelete1")  + feed.getName()
					+ LanguageLoad.getinstance().find("internal/scheduler/info/alertrecoverdelete2"));
		}

	}

	public void setSchedulerService(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}

}
