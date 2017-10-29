package com.ikip.newsdetect.main.scheduler;

import com.google.common.collect.Maps;
import com.ikip.newsdetect.main.service.FeedService;
import com.ikip.newsdetect.model.Feed;
import com.ikip.newsdetect.model.UpdateStateEnum;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SchedulerService {
	
	private final static Logger LOGGER = Logger.getLogger(SchedulerService.class);

	@Autowired
	private TaskScheduler scheduler;
	@Autowired
	private AutowireCapableBeanFactory beanFactory;
	private static final int MIN_MILIS = 60000;
	@Autowired
	private FeedService serviceFeed;
	private Map<Long, ScheduledFuture<?>> tasks = Maps.newLinkedHashMap();
	

	@PostConstruct
	public void init() {
//		if (configuracion.getConfiguracion().isRunService()) {
			List<Feed> listFeeds = serviceFeed.getAllFeedLastUp();
			Date startTime = new Date();
			startTime.setTime(startTime.getTime() + 1000 * 10);// Las tareas se
																// comienzan a
																// ejecutar
																// despues
																// de dos
																// minutos
																// tras
																// planificar
			for (Feed feed : listFeeds) {
				if (!feed.getState().equals(UpdateStateEnum.DEACTIVATED)) {
					AlertTaskContainer task = new AlertTaskContainer(feed);
					beanFactory.autowireBean(task);
					ScheduledFuture<?> futureTask = scheduler
							.scheduleWithFixedDelay(task, startTime, MIN_MILIS
									* feed.getMinRefresh());
					startTime.setTime(startTime.getTime() + 1000 * 10);// Vamos
																		// espacioandolas
																		// cada
																		// 2
																		// minutos
					tasks.put(feed.getId(), futureTask);
				}
			}
//			LOGGER.info(LanguageLoad.getinstance().find("internal/scheduler/info/scheduler1") + listFeeds.size() + LanguageLoad.getinstance().find("internal/scheduler/info/scheduler2"));
//		} else {
////			LOGGER.info(LanguageLoad.getinstance().find("internal/scheduler/info/schedulerdesactivado") );
//		}
	}

	public void removeFeedTask(Feed feed) {
		if (tasks.containsKey(feed.getId())) {
			ScheduledFuture<?> futureTask = tasks.get(feed.getId());
			futureTask.cancel(true);
			tasks.remove(feed.getId());
		}
	}

	public void updateFeedTask(Feed feed) {
		Date startTime = new Date();
		startTime.setTime(startTime.getTime() + 1000 * 120);
		if (tasks.containsKey(feed.getId())) {
			ScheduledFuture<?> futureTask = tasks.remove(feed.getId());
			futureTask.cancel(true);
		}
//		if (feed.isActived() && configuracion.getConfiguracion().isRunService()) {
			ScheduledFuture<?> futureTask = null;
			AlertTaskContainer task = new AlertTaskContainer((Feed) feed);
			beanFactory.autowireBean(task);
			futureTask = scheduler.scheduleWithFixedDelay(task, startTime,
					MIN_MILIS * feed.getMinRefresh());
			tasks.put(feed.getId(), futureTask);
//		}
	}

	public void addFeedTask(Feed feed) {
		Date startTime = new Date();
		startTime.setTime(startTime.getTime() + 1000 * 120);
//		if (feed.isActived() && configuracion.getConfiguracion().isRunService()) {
			ScheduledFuture<?> futureTask = null;
			AlertTaskContainer task = new AlertTaskContainer((Feed) feed);
			beanFactory.autowireBean(task);
			futureTask = scheduler.scheduleWithFixedDelay(task, startTime,
					MIN_MILIS * feed.getMinRefresh());
			tasks.put(feed.getId(), futureTask);
//		}
	}

	public void startTask(Feed feed) {
		if (!feed.getState().equals(UpdateStateEnum.DEACTIVATED)) {
			Date startTime = new Date();
			startTime.setTime(startTime.getTime() + 1000 * 5); // Iniciar en 5
			AlertTaskContainer task = new AlertTaskContainer((Feed) feed);
			beanFactory.autowireBean(task);
			scheduler.schedule(task, startTime);

		}
	}

	public void startAllTask() {
		for (Feed feed : serviceFeed.getAllFeedLastUp()) {
			startTask(feed);
		}

	}
	
	public void stopAllTask() {
		for (Feed feed : serviceFeed.getAllFeedLastUp()) {
			removeFeedTask(feed);
		}
//		LOGGER.info(LanguageLoad.getinstance().find("internal/scheduler/info/schedulerdestop"));
	}
	
	public String getNextExecution(Feed feed) {
		if (tasks.containsKey(feed.getId())) {
			Long milis = tasks.get(feed.getId()).getDelay(TimeUnit.MILLISECONDS);
			Date now = new Date();
			now.setTime(now.getTime()+milis);
			return DateFormat.getInstance().format(now);
		}
		return "";
	}

}
