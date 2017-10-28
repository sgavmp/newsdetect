package com.ikip.newsdetect.main.service;

import com.google.common.collect.Lists;
import com.ikip.newsdetect.extractor.service.FeedScraping;
import com.ikip.newsdetect.find.service.NewsIndexService;
import com.ikip.newsdetect.main.dto.FeedForm;
import com.ikip.newsdetect.main.repository.FeedRepository;
import com.ikip.newsdetect.main.repository.NewsDetectRepository;
import com.ikip.newsdetect.main.scheduler.SchedulerService;
import com.ikip.newsdetect.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FeedServiceImpl implements FeedService {
	
	@Autowired
	private FeedRepository repositoryFeed;
	@Autowired
	private FeedScraping scrapingFeed;
	@Autowired
	private NewsDetectRepository newsDetectRepository;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private NewsIndexService newsIndexService;

	@Override
	public List<News> scrapFeed(Feed feed, Date after, boolean withOutLimit) {
//		return scrapingFeed.scrapNews(feed, after, withOutLimit);
		return null;
	}
	
	public News testFeed(FeedForm feed) {
//		News news = scrapingFeed.scrapOneNews(feed);
		return null;
	}

	@Override
	public List<Feed> getAllFeedAlph() {
		return repositoryFeed.findAllByOrderByNameAsc();
	//	return repositoryFeed.findAllByOrderByUltimaRecuperacionAsc();
	}

	@Override
	public List<Feed> getAllFeedLastUp() {
		return repositoryFeed.findAll();
	}
	
	
	@Override
	public Feed getFeedByCodeName(Long codeName) {
		return repositoryFeed.findOne(codeName);
	}

	@Override
	public Feed createFeed(Feed feed) {
		feed = repositoryFeed.save(feed);
		schedulerService.addFeedTask(feed);
		return feed;
	}

	@Override
	public boolean removeFeed(Feed feed) {
		schedulerService.removeFeedTask(feed);
		try {
			newsIndexService.removeFeedFromIndex(feed);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		List<DetectedNews> lista = newsDetectRepository.findAllDistinctByFeedIdOrderByDatePubDesc(feed.getId());
		for (DetectedNews news : lista)
			newsDetectRepository.delete(news.getId());
		this.repositoryFeed.delete(feed);
		return !this.repositoryFeed.exists(feed.getId());
	}

	@Override
	public Feed updateFeed(Feed feed) {
		Feed feedU = repositoryFeed.save(feed);
		schedulerService.updateFeedTask(feed);
			try {
				newsIndexService.updateIndex(feed);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return feedU;
	}

	public SchedulerService getSchedulerService() {
		return schedulerService;
	}

	public void setSchedulerService(SchedulerService schedulerService) {
		this.schedulerService = schedulerService;
	}
	
	public List<DetectedNews> findAllDistinctNewsDetectByFeedOrderByDatePub(Feed feed) {
		return newsDetectRepository.findAllDistinctByFeedIdOrderByDatePubDesc(feed.getId());
	}
	
	public Feed setSateOfFeed(Feed feed, UpdateStateEnum state) {
		feed = repositoryFeed.findOne(feed.getId());
		feed.setState(state);
		Feed feedU = repositoryFeed.save(feed);
		return feedU;
	}

	@SuppressWarnings("unused")
	@Override
	public List<String> createFeedAuto(String[] listUrl) {
		List<String> fail = Lists.newArrayList();
		String regex = "^(?<name>[A-Za-z0-9 ]{2,50})\\t(?<url>(https?|ftp):\\/\\/[^\\s\\/$.?#].[^\\s]*)\\t(?<tipo>@T(massmedia|journal|specificmedia|alert|institucional)+)(?<lugar>(\\t(@L(general|españa|italia|rusia|holanda|alemania|inglaterra|portugal|francia|estadosunidos|india|marruecos)+))+)$";
		Pattern pattern = Pattern.compile(regex);
		for (String linea : listUrl) {
			Matcher matcher = pattern.matcher(linea);
			matcher.find();
			String name = matcher.group("name");
			String url = matcher.group("url");
			String tipo = matcher.group("tipo");
			String lugares = matcher.group("lugar");
//			FeedTypeEnum typeE = FeedTypeEnum.valueOf(tipo.substring(2));
//			List<FeedPlaceEnum> lugaresE = Lists.newArrayList();
//			for (String lugaStep : lugares.split("\t")) {
//				if (!lugaStep.trim().isEmpty())
//					lugaresE.add(FeedPlaceEnum.valueOf(lugaStep.trim().substring(2)));
//			}
			URL link = null;
			try {
				link = new URL(url);
			} catch (MalformedURLException e) {
				fail.add(url);
				continue;
			}
			Feed feed = new Feed();
			feed.setUrlScrap(url);
			feed.setName(name);
			feed.setScrapType(ScrapTypeEnum.AUTO);
			feed.setFeedType(tipo);
//			feed.setFeedPlace(lugaresE);
			this.createFeed(feed);
		}
		return fail;
	}

}
