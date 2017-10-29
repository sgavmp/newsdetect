package com.ikip.newsdetect.extractor.service;

import com.ikip.newsdetect.extractor.exception.FeedAutoScrapException;
import com.ikip.newsdetect.extractor.exception.FeedManualScrapException;
import com.ikip.newsdetect.extractor.exception.FeedRssScrapException;
import com.ikip.newsdetect.extractor.repository.EventRepository;
import com.ikip.newsdetect.extractor.repository.FeedCrawlerUrlRepository;
import com.ikip.newsdetect.extractor.util.GBFoodCrawler;
import com.ikip.newsdetect.model.*;
import com.kohlschutter.boilerpipe.BoilerpipeExtractor;
import com.kohlschutter.boilerpipe.BoilerpipeProcessingException;
import com.kohlschutter.boilerpipe.extractors.CommonExtractors;
import com.rometools.rome.feed.synd.SyndContent;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FeedScrapingImpl implements FeedScraping {

	private final FeedCrawlerUrlRepository feedCrawlerUrlRepository;
	private final EventRepository eventRepository;

	@Value("${gbfood.resources.crawler}")
	private String pathCrawler = "/tomcatfolder/app/gbfood/crawler/";

	public FeedScrapingImpl(FeedCrawlerUrlRepository feedCrawlerUrlRepository, EventRepository eventRepository) {
		this.feedCrawlerUrlRepository = feedCrawlerUrlRepository;
		this.eventRepository = eventRepository;
	}

	@Override
	public List<News> scrapNews(Feed feed, LocalDateTime after, boolean withOutLimit) throws FeedAutoScrapException, FeedRssScrapException, FeedManualScrapException {
		List<News> newsList;
		
		if (feed.getScrapType().equals(ScrapTypeEnum.AUTO)) {
			newsList = scrapingAuto(feed);
		} else {
			if (feed.getScrapType().equals(ScrapTypeEnum.RSS)) {
				newsList = scrapingWhitRSS(feed, after, withOutLimit);
			} else {
				newsList = scrapingWithOutRSS(feed, after, withOutLimit);
			}
		}
		log.debug("");//TODO
		return newsList;
	}

	private List<News> scrapingAuto(Feed feed) throws FeedAutoScrapException {
		try {
			List<News> listNews = new ArrayList<News>();
			if (feed.getUrlScrap() != null) {
				String crawlStorageFolder = pathCrawler;
				int numberOfCrawlers = 1;

				CrawlConfig config = new CrawlConfig();
				config.setCrawlStorageFolder(crawlStorageFolder);
				config.setMaxDepthOfCrawling(1);
				config.setPolitenessDelay(10);
				config.setShutdownOnEmptyQueue(true);

				/*
				 * Instantiate the controller for this crawl.
				 */
				PageFetcher pageFetcher = new PageFetcher(config);
				RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
				robotstxtConfig.setEnabled(false);
				RobotstxtServer robotstxtServer = new RobotstxtServer(
						robotstxtConfig, pageFetcher);
				CrawlController controller = new CrawlController(config,
						pageFetcher, robotstxtServer);

				/*
				 * For each crawl, you need to add some seed urls. These are the
				 * first URLs that are fetched and then the crawler starts
				 * following links which are found in these pages
				 */
				controller.addSeed(feed.getUrlScrap());

				/*
				 * Start the crawl. This is a blocking operation, meaning that
				 * your code will reach the line after this only when crawling
				 * is finished.
				 */
				controller.start(GBFoodCrawler.class, numberOfCrawlers);
				List<String> newsLinks = (List<String>) controller
						.getCrawlersLocalData().get(0);
				List<String> feedCrawlerUrlsPast = feedCrawlerUrlRepository.findAllCrawlerUrlByFeedId(feed.getId());
				List<String> feedCrawlerUrlNew = new ArrayList<>();
				News news = null;
				for (String linkNews : newsLinks) {
					if (feedCrawlerUrlsPast.contains(linkNews))
						continue;
					news = getNewsWithOutRSS(feed, linkNews, null);
					listNews.add(news);
					feedCrawlerUrlNew.add(linkNews);
				}
			}
			return listNews;
		} catch (IOException e) {
			log.error("");//TODO
			throw new FeedAutoScrapException("",e);//TODO
		} catch (Exception e) {
			log.error("");//TODO
			throw new FeedAutoScrapException("",e);//TODO
		}
	}


	@Override
	public News scrapOneNews(Feed feed) throws FeedAutoScrapException, FeedRssScrapException, FeedManualScrapException {
		

		if (feed.getScrapType().equals(ScrapTypeEnum.AUTO)) {
			return scrapingOneWithAuto(feed);
		} else {
			if (feed.getScrapType().equals(ScrapTypeEnum.RSS)) {
				return scrapingOneWhitRSS(feed);
			} else {
				return scrapingOneWithOutRSS(feed);
			}
		}
	}

	private News scrapingOneWithAuto(Feed feed) throws FeedAutoScrapException {
		try {
			List<News> listNews = new ArrayList<News>();
			if (feed.getUrlScrap() != null) {
				String crawlStorageFolder = pathCrawler;
				int numberOfCrawlers = 1;

				CrawlConfig config = new CrawlConfig();
				config.setCrawlStorageFolder(crawlStorageFolder);
				config.setMaxPagesToFetch(2);//Solo un enlace
				config.setPolitenessDelay(1);
				config.setMaxDepthOfCrawling(1);
				config.setShutdownOnEmptyQueue(true);

				/*
				 * Instantiate the controller for this crawl.
				 */
				PageFetcher pageFetcher = new PageFetcher(config);
				RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
				robotstxtConfig.setEnabled(false);
				RobotstxtServer robotstxtServer = new RobotstxtServer(
						robotstxtConfig, pageFetcher);
				CrawlController controller = new CrawlController(config,
						pageFetcher, robotstxtServer);

				/*
				 * For each crawl, you need to add some seed urls. These are the
				 * first URLs that are fetched and then the crawler starts
				 * following links which are found in these pages
				 */
				controller.addSeed(feed.getUrlScrap());

				/*
				 * Start the crawl. This is a blocking operation, meaning that
				 * your code will reach the line after this only when crawling
				 * is finished.
				 */
				controller.start(GBFoodCrawler.class, numberOfCrawlers);
				List<String> newsLinks = (List<String>) controller
						.getCrawlersLocalData().get(0);
				News news = null;
				for (String linkNews : newsLinks) {
					news = getNewsWithOutRSS(feed, linkNews, "");
					listNews.add(news);
					break;
				}
			}
			return listNews.get(0);
		} catch (IOException e) {
			log.error("");//TODO
			throw new FeedAutoScrapException("",e);//TODO
		} catch (Exception e) {
			log.error("");//TODO
			throw new FeedAutoScrapException("",e);//TODO
		}
	}

	private List<News> scrapingWhitRSS(Feed feed, LocalDateTime after,
			boolean withOutLimit) throws FeedRssScrapException {
		try {
			List<News> listNews = new ArrayList<News>();
			// open a connection to the rss feed
			if (feed.getUrlScrap() != null) {
				URL url = new URL(feed.getUrlScrap());
				URLConnection conn = url.openConnection();
				SyndFeedInput input = new SyndFeedInput();
				SyndFeed newsList = input.build(new XmlReader(conn
						.getInputStream()));
				boolean isFirst = true;
				String lastNews = null;
				Event lastSearch = eventRepository.findOneByOriginIdAndTypeEventOrderByDateDesc(feed.getId(), TypeEventEnum.SCRAP_FEED_LAST_LINK);
				for (SyndEntry news : newsList.getEntries()) {
					if (!withOutLimit && after == null) {
						// Vamos comprobando el link de la entrada con el enlace
						// de
						// la ultima noticia almacenada del feed
						if (lastSearch != null) {
							// En caso de coincidencia (es decir que ya esta en
							// el
							// sistema) devolvemos la lista (puede estar vacia)
							if (lastSearch.getInfo().equals(
											news.getLink())) {
								break;
							} else if (isFirst) {
								lastNews = news.getLink();
								isFirst = false;
							}
						} else if (isFirst) {
							lastNews = news.getLink();
							isFirst = false;
						}
					}
					News newsData = getNewsWithRSS(feed, news, true);
					if (!withOutLimit && after != null) {
						if (newsData.getPubDate().isBefore(after)) {
							break;
						}
					}
					listNews.add(newsData);
				}
			}
			return listNews;
		} catch (MalformedURLException e) {
			log.error("");//TODO
			throw new FeedRssScrapException("",e);//TODO
		} catch (IOException e) {
			log.error("");//TODO
			throw new FeedRssScrapException("",e);//TODO
		} catch (IllegalArgumentException | FeedException e) {
			log.error("");//TODO
			throw new FeedRssScrapException("",e);//TODO
		}
	}

	private List<News> scrapingWithOutRSS(Feed feed, LocalDateTime after,
			boolean withOutLimit) throws FeedManualScrapException {
		try {
			List<News> listNews = new ArrayList<News>();
			if (feed.getUrlScrap() != null) {
				Document doc = Jsoup.connect(feed.getUrlScrap()).timeout(20000)
						.get();
				Elements newsLinks = doc.select(feed.getUrlScrap());
				boolean isFirst = true;
				String lastNews = null;
				News news = null;
				for (Element link : newsLinks) {
					// Enlace de la noticia
					String linkNews = link.absUrl("href");
					// Titutlo de la noticia
					String title = link.text();
					news = getNewsWithOutRSS(feed, linkNews, title);
					Event lastSearch = eventRepository.findOneByOriginIdAndTypeEventOrderByDateDesc(feed.getId(), TypeEventEnum.SCRAP_FEED_LAST_LINK);
					if (!withOutLimit && after == null) {
						// Vamos comprobando el link de la entrada con el enlace
						// de
						// la ultima noticia almacenada del feed
						if (lastSearch != null) {
							// En caso de coincidencia (es decir que ya esta en
							// el
							// sistema) devolvemos la lista (puede estar vacia)
							if (lastSearch.getInfo().equals(linkNews)) {
								break;
							} else if (isFirst) {
								lastNews = linkNews;
								isFirst = false;
							}
						} else if (isFirst) {
							lastNews = linkNews;
							isFirst = false;
						}
					} else if (!withOutLimit && after != null) {
						if (news.getPubDate().isBefore(after)) {
							break;
						}
					}
					listNews.add(news);
				}
			}
			return listNews;
		} catch (IOException e) {
			log.error("");//TODO
			throw new FeedManualScrapException("",e);//TODO
		} catch (Exception e) {
			log.error("");//TODO
			throw new FeedManualScrapException("",e);//TODO
		}
	}

	private News scrapingOneWhitRSS(Feed feed) throws FeedRssScrapException {
		try {
			List<News> listNews = new ArrayList<News>();
			// open a connection to the rss feed
			if (feed.getUrlScrap() != null) {
				URL url = new URL(feed.getUrlScrap());
				URLConnection conn = url.openConnection();
				SyndFeedInput input = new SyndFeedInput();
				SyndFeed newsList = input.build(new XmlReader(conn
						.getInputStream()));
				for (SyndEntry news : newsList.getEntries()) {
					listNews.add(getNewsWithRSS(feed, news, true));
					break;
				}
			}
			return listNews.get(0);
		} catch (MalformedURLException e) {
			log.error("");//TODO
			throw new FeedRssScrapException("",e);//TODO
		} catch (IOException e) {
			log.error("");//TODO
			throw new FeedRssScrapException("",e);//TODO
		} catch (IllegalArgumentException | FeedException e) {
			log.error("");//TODO
			throw new FeedRssScrapException("",e);//TODO
		}
	}

	private News scrapingOneWithOutRSS(Feed feed) throws FeedManualScrapException {
		try {
			List<News> listNews = new ArrayList<News>();
			if (feed.getUrlScrap() != null) {
				Document doc = Jsoup.connect(feed.getUrlScrap()).timeout(20000)
						.get();
				Elements newsLinks = doc.select(feed.getUrlScrap());
				for (Element link : newsLinks) {
					// Enlace de la noticia
					String linkNews = link.absUrl("href");
					if (linkNews != null) {
						if (!linkNews.isEmpty()) {
							// Titutlo de la noticia
							String title = link.text();
							listNews.add(getNewsWithOutRSS(feed,
									linkNews, title));
							break;
						}
					}
				}
			}
			return listNews.get(0);
		} catch (IOException e) {
			log.error("");//TODO
			throw new FeedManualScrapException("",e);//TODO
		}
	}

	public News getNewsWithRSS(Feed feed, SyndEntry news, boolean accessLink) {
		String link = news.getLink();
		try {
			URL url = new URL(news.getLink());
		} catch (MalformedURLException ex) {
			URL url = null;
			try {
				url = new URL(feed.getUrlScrap());
				link = url.getProtocol().concat(url.getAuthority().concat(news.getLink()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		String url = link;
		News.NewsBuilder temp = News.builder();//TODO from Feed
		temp.title(news.getTitle());
		temp.url(url);
		temp.pubDate(LocalDateTime.from(news.getPublishedDate().toInstant()));
		String content = "";
		for (SyndContent part : news.getContents()) {
			content += part.getValue();
		}
		if (!content.equals("")) {
			temp.content(content);
		}
		Document newsPage = null;
		Integer count = 0;
		if (accessLink) {
		while (true) {
			try {
				if (feed.getCharSet().equals(CharsetEnum.UTF8)) {// Codificacion
																	// por
																	// defecto
					newsPage = Jsoup.connect(url).userAgent("Mozilla")
							.timeout(10000).get();
				} else {
					newsPage = Jsoup.parse(new URL(url).openStream(), feed
							.getCharSet().getValue(), url);
				}
				break;
			} catch (IOException e) {// En caso de error intentamos tres veces
				count++;
				log.error("");//TODO
				if (count == 3)
					return null;
			}
		}
		}
		if (feed.getExtractionType() == null
				|| ExtractionTypeEnum.CSS_EXTRACTOR
						.equals(feed.getExtractionType())) {
			if (feed.getSelectorContent() != null) {
				if (!feed.getSelectorContent().isEmpty()) {
					temp.content(feed.getSelectorContentMeta() ? newsPage
							.select(feed.getSelectorContent()).attr("content")
							: newsPage.select(feed.getSelectorContent()).text());
				}
			}
		} else if (ExtractionTypeEnum.RSS_DESCRIPTION
				.equals(feed.getExtractionType()) || !accessLink) {
			if (news.getDescription()!=null)
				temp.content(news.getDescription().getValue());
		} else if (ExtractionTypeEnum.ALL_CONTENT
				.equals(feed.getExtractionType())) {
			temp.content(newsPage.text());
		} else {
			BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
			switch (feed.getExtractionType()) {
			case ARTICLE_EXTRACTOR:
				extractor = CommonExtractors.ARTICLE_EXTRACTOR;
				break;
			case DEFAULT_EXTRACTOR:
				extractor = CommonExtractors.DEFAULT_EXTRACTOR;
				break;
			case CANOLA_EXTRACTOR:
				extractor = CommonExtractors.CANOLA_EXTRACTOR;
				break;
			case LARGEST_CONTENT_EXTRACTOR:
				extractor = CommonExtractors.LARGEST_CONTENT_EXTRACTOR;
				break;
			default:
				extractor = CommonExtractors.ARTICLE_EXTRACTOR;
				break;
			}
			try {
				temp.content(extractor.getText(newsPage.outerHtml()));
			} catch (BoilerpipeProcessingException e) {
				log.error("");//TODO
			}
		}
		if (feed.getSelectorPubDate() != null) {
			if (!feed.getSelectorPubDate().isEmpty()) {
				temp.pubDate(LocalDateTime.parse(feed.getSelectorPubDateMeta() ? newsPage
						.select(feed.getSelectorPubDate()).attr("content")
						: newsPage.select(feed.getSelectorPubDate()).text(),DateTimeFormatter.ofPattern(feed.getDateFormat())));
			}
		}
		if (feed.getSelectorTitle() != null) {
			if (!feed.getSelectorTitle().isEmpty()) {
				temp.title(feed.getSelectorTitleMeta() ? newsPage.select(
						feed.getSelectorTitle()).attr("content") : newsPage
						.select(feed.getSelectorTitle()).text());
			} 
		} 
		return temp.build();

	}

	public News getNewsWithOutRSS(Feed feed, String linkNews, String title) {
		Document newsPage = null;
		Integer count = 0;
		while (true) {
			try {
				if (feed.getCharSet().equals(CharsetEnum.UTF8)) {// Codificacion
																	// por
																	// defecto
					newsPage = Jsoup.connect(linkNews).userAgent("Mozilla")
							.timeout(10000).get();
				} else {
					newsPage = Jsoup.parse(new URL(linkNews).openStream(), feed
							.getCharSet().getValue(), linkNews);
				}
				break;
			} catch (IOException e) {// En caso de error intentamos tres veces
				count++;
				log.warn("");//TODO
				if (count == 3)
					return null;
			}
		}
		News.NewsBuilder temp = News.builder();//TODO from Feed
		temp.url(linkNews);
		temp.title(title);
		if (feed.getExtractionType() == null
				|| ExtractionTypeEnum.CSS_EXTRACTOR
						.equals(feed.getExtractionType())) {
			if (feed.getSelectorContent() != null) {
				if (!feed.getSelectorContent().isEmpty()) {
					temp.content(feed.getSelectorContentMeta() ? newsPage
							.select(feed.getSelectorContent()).attr("content")
							: newsPage.select(feed.getSelectorContent()).text());
				}
			}
		}  else if (ExtractionTypeEnum.ALL_CONTENT
				.equals(feed.getExtractionType()) || ExtractionTypeEnum.RSS_DESCRIPTION
				.equals(feed.getExtractionType())) {
			temp.content(newsPage.text());
		} else {
			BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
			switch (feed.getExtractionType()) {
			case ARTICLE_EXTRACTOR:
				extractor = CommonExtractors.ARTICLE_EXTRACTOR;
				break;
			case DEFAULT_EXTRACTOR:
				extractor = CommonExtractors.DEFAULT_EXTRACTOR;
				break;
			case CANOLA_EXTRACTOR:
				extractor = CommonExtractors.CANOLA_EXTRACTOR;
				break;
			case LARGEST_CONTENT_EXTRACTOR:
				extractor = CommonExtractors.LARGEST_CONTENT_EXTRACTOR;
				break;
			default:
				extractor = CommonExtractors.ARTICLE_EXTRACTOR;
				break;
			}
			try {
				temp.content(extractor.getText(newsPage.outerHtml()));
			} catch (BoilerpipeProcessingException e) {
				log.error("");//TODO
			}
		}
		if (feed.getSelectorPubDate() != null) {
			if (!feed.getSelectorPubDate().isEmpty()) {
				temp.pubDate(LocalDateTime.parse(feed.getSelectorPubDateMeta() ? newsPage
						.select(feed.getSelectorPubDate()).attr("content")
						: newsPage.select(feed.getSelectorPubDate()).text(),DateTimeFormatter.ofPattern(feed.getDateFormat())));
			}
		}
		if (feed.getSelectorTitle() != null) {
			if (!feed.getSelectorTitle().isEmpty()) {
				temp.title(feed.getSelectorTitleMeta() ? newsPage.select(
						feed.getSelectorTitle()).attr("content") : newsPage
						.select(feed.getSelectorTitle()).text());
			} else {
				temp.title(newsPage.title());
			}
		} else {
			temp.title(newsPage.title());
		}
		return temp.build();
	}

	@Override
	public News getNewsFromSite(String link, Feed feed) {
		return getNewsWithOutRSS(feed, link, link);
	}


}
