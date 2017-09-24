package com.ikip.newsdetect.main.controller.impl;

import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.*;
import es.ucm.visavet.gbf.app.scheduler.SchedulerService;
import es.ucm.visavet.gbf.app.service.FeedService;
import es.ucm.visavet.gbf.app.service.impl.AlertServiceImpl;
import es.ucm.visavet.gbf.app.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/feeds")
public class FeedController extends BaseController {
		
	public FeedController() {
		this.menu=LanguageLoad.getinstance().find("web/feed/places");
	}
	
	@Autowired
	private FeedService serviceFeed;
	@Autowired
	private AlertServiceImpl serviceAlert;
	@Autowired
	private SchedulerService schedulerService;
	
	
	@ModelAttribute("feeds")
	public List<Feed> getAllFeeds() {
		List<Feed> feeds = serviceFeed.getAllFeedAlph();
		for (Feed feed : feeds) {
			feed.setNextExecution(schedulerService.getNextExecution(feed));
		}
		return feeds;
	}
	
	@ModelAttribute("feedaddnewsite")
	public String getFeedAddSiteText() {
		return LanguageLoad.getinstance().find("web/feed/feedaddnewsite");
	}
	
	@ModelAttribute("feedaddnewsiteauto")
	public String getFeedAddSiteAutoText() {
		return LanguageLoad.getinstance().find("web/feed/feedaddnewsiteauto");
	}
	
	@ModelAttribute("feedaddnewsitenoestand")
	public String getFeedAddSiteNoEstandText() {
		return LanguageLoad.getinstance().find("web/feed/feedaddnewsitenoestand");
	}
	
	@ModelAttribute("feednosites")
	public String getFeedNoSitesText() {
		return LanguageLoad.getinstance().find("web/feed/feednosites");
	}
	
	@ModelAttribute("feedname")
	public String getFeedNameText() {
		return LanguageLoad.getinstance().find("web/feed/feedname");
	}
	
	@ModelAttribute("feedtype")
	public String getFeedTypeText() {
		return LanguageLoad.getinstance().find("web/feed/feedtype");
	}
	
	@ModelAttribute("feedactive")
	public String getFeedActiveText() {
		return LanguageLoad.getinstance().find("web/feed/feedactive");
	}
	
	@ModelAttribute("feedestandar")
	public String getFeedEstandarText() {
		return LanguageLoad.getinstance().find("web/feed/feedestandar");
	}
	
	@ModelAttribute("feedt")
	public String getFeedTText() {
		return LanguageLoad.getinstance().find("web/feed/feedt");
	}
	
	@ModelAttribute("feedl")
	public String getFeedLText() {
		return LanguageLoad.getinstance().find("web/feed/feedl");
	}
	
	@ModelAttribute("feedcreationdate")
	public String getFeedCreationDaterText() {
		return LanguageLoad.getinstance().find("web/feed/feedcreationdate");
	}
	
	@ModelAttribute("feededit")
	public String getFeedEditText() {
		return LanguageLoad.getinstance().find("web/feed/feededit");
	}
	
	@ModelAttribute("feededitiondate")
	public String getFeedEditionDaterText() {
		return LanguageLoad.getinstance().find("web/feed/feededitiondate");
	}
	
	@ModelAttribute("feeddelete")
	public String getFeedDeleteText() {
		return LanguageLoad.getinstance().find("web/feed/feeddelete");
	}
	
	@ModelAttribute("feedsuredelete")
	public String getFeedSureToDeleteText() {
		return LanguageLoad.getinstance().find("web/feed/feedsuredelete");
	}

	@ModelAttribute("feedcodification")
	public String getFeedCodificationText() {
		return LanguageLoad.getinstance().find("web/feed/feedcodification");
	}
	
	@ModelAttribute("feedcodificationh")
	public String getFeedCodificationHText() {
		return LanguageLoad.getinstance().find("web/feed/feedcodificationh");
	}
	
	@ModelAttribute("feedextarctor")
	public String getFeedExtractorText() {
		return LanguageLoad.getinstance().find("web/feed/feedextarctor");
	}
	
	@ModelAttribute("feedextarctorh")
	public String getFeedExtractorHText() {
		return LanguageLoad.getinstance().find("web/feed/feedextarctorh");
	}
	
	@ModelAttribute("feedcontiner")
	public String getFeedContainerText() {
		return LanguageLoad.getinstance().find("web/feed/feedcontiner");
	}
	
	@ModelAttribute("feedselector")
	public String getFeedSelectorText() {
		return LanguageLoad.getinstance().find("web/feed/feedselector");
	}
	
	@ModelAttribute("feedtitulo")
	public String getFeedTituloText() {
		return LanguageLoad.getinstance().find("web/feed/feedtitulo");
	}
	
	@ModelAttribute("feedtituloh")
	public String getFeedTituloHText() {
		return LanguageLoad.getinstance().find("web/feed/feedtituloh");
	}
	
	@ModelAttribute("feedyes")
	public String getFeedYesText() {
		return LanguageLoad.getinstance().find("web/feed/feedyes");
	}
	
	@ModelAttribute("feedcontent2")
	public String getFeedContent2Text() {
		return LanguageLoad.getinstance().find("web/feed/feedcontent2");
	}
	
	@ModelAttribute("feedformpubdate")
	public String getFeedFormPubDateText() {
		return LanguageLoad.getinstance().find("web/feed/feedformpubdate");
	}
	
	@ModelAttribute("feedformpubdateh")
	public String getFeedFormPubDateHText() {
		return LanguageLoad.getinstance().find("web/feed/feedformpubdateh");
	}
	
	@ModelAttribute("masfeedlis")
	public String getFeedLisText() {
		return LanguageLoad.getinstance().find("web/feed/masfeedlis");
	}
	
	@ModelAttribute("masfeedform")
	public String getFeedFormmateText() {
		return LanguageLoad.getinstance().find("web/feed/masfeedform");
	}
	
	@ModelAttribute("feedcancel")
	public String getFeedCancelText() {
		return LanguageLoad.getinstance().find("web/feed/feedcancel");
	}
	
	@ModelAttribute("feedreviseformat")
	public String getFeedReviseFormatText() {
		return LanguageLoad.getinstance().find("web/feed/feedreviseformat");
	}
	
	
	@RequestMapping("**")
	public String getAllAlerts() {
		return "/feeds/feeds";
	}
	
	@RequestMapping("/get/{codeName}")
	public String getAllNewsByFeed(Model model, @PathVariable("codeName") Feed feed) {
		model.addAttribute(feed);
		Set<Alert> alertas = serviceAlert.getAlertDetectSite(feed);
		for (Alert alerta : alertas) {
			Iterator<NewsDetect> iterator = alerta.getNewsDetect().iterator();
			while(iterator.hasNext()) {
				try {
					NewsDetect news = iterator.next();
					if (news==null||news.getId()==null||news.getSite()==null||!news.getSite().equals(feed)) {
						iterator.remove();
					}
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
				
			}
		}
		model.addAttribute("alertas", alertas);
		return "/feeds/oneFeed";
	}
	
	@ModelAttribute("enumFiabilidad")
	public WebLevel[] getValuesOfFiabilidad() {
		return WebLevel.values();
	}

	@ModelAttribute("enumLanguaje")
	public Language[] getValuesOfLanguaje() {
		return Language.values();
	}
	
	@ModelAttribute("enumType")
	public FeedTypeEnum[] getValuesOfType() {
		return FeedTypeEnum.values();
	}
	
	@ModelAttribute("enumPlace")
	public FeedPlaceEnum[] getValuesOfPlace() {
		return FeedPlaceEnum.values();
	}
	
	@ModelAttribute("enumExtraction")
	public ExtractionType[] getValuesOfExtraction() {
		return ExtractionType.values();
	}

	@RequestMapping("/get/{codeName}/update/ajax")
	public @ResponseBody
    String updateNewsByFeedAjax(Model model,
                                @PathVariable("codeName") Feed feed) {
		schedulerService.startTask(feed);
		return "ok";
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String formNewFeed(Model model) {
		model.addAttribute("feed", new FeedForm());
		model.addAttribute("nuevo", true);
		return "/feeds/feedForm";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String formNewFeed(Model model,
                              RedirectAttributes redirectAttributes, @Valid FeedForm feed,
                              BindingResult result) {
		if (result.hasErrors()) {
			return "/feeds/feedForm";
		}
		Feed feedP = new Feed(feed);
		feedP = serviceFeed.createFeed(feedP);
		if (feedP.getId() != null) {
			redirectAttributes.addFlashAttribute("info",
					LanguageLoad.getinstance().find("web/feed/info/feedcreatedcorrect"));
		} else {
			redirectAttributes.addFlashAttribute("error",
					LanguageLoad.getinstance().find("web/feed/error/feedcreatederror"));
		}
		return "redirect:/feeds/get/" + feedP.getId() + "/edit";
	}
	
	@RequestMapping(value = "/masive", method = RequestMethod.GET)
	public String createMasiveFeed(Model model) {
		return "/feeds/masiveFeed";
	}
	
	@RequestMapping(value = "/masive", method = RequestMethod.POST)
	public String createMasiveFeed(@RequestParam(name="list") String list, Model model) {
		if (StringUtils.isEmpty(list))
			model.addAttribute("error", LanguageLoad.getinstance().find("web/feed/error/oneurlperline"));
		String[] listURL = list.split("\r\n");
		String regex = "^(?<name>[A-Za-z0-9 ]{2,50})\\t(?<url>(https?|ftp):\\/\\/[^\\s\\/$.?#].[^\\s]*)\\t(?<tipo>@T(massmedia|journal|specificmedia|alert|institucional)+)(?<lugar>(\\t(@L(general|españa|italia|rusia|holanda|alemania|inglaterra|portugal|francia|estadosunidos|india|marruecos)+))+)$";
		Pattern pattern = Pattern.compile(regex);
		for (String linea : listURL) {
			if (!pattern.matcher(linea).find())
			{
				model.addAttribute("error", LanguageLoad.getinstance().find("web/feed/error/someerrorinsertedline"));
			}
		}
		List<String> urlFail = serviceFeed.createFeedAuto(listURL);
		if (!urlFail.isEmpty()) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/feed/error/oneurlperline"));
			model.addAttribute("fail", urlFail);
		}
		model.addAttribute("info", LanguageLoad.getinstance().find("web/feed/info/websites"));
		return "/feeds/masiveFeed";
	}

	@RequestMapping(value = "/get/{codeName}/edit", method = RequestMethod.GET)
	public String formEditFeed(Model model, @PathVariable("codeName") Feed feedP) {
		model.addAttribute("feed", new FeedForm(feedP));
		return "/feeds/feedForm";
	}

	@RequestMapping(value = "/get/{codeName}/edit", method = RequestMethod.POST)
	public String saveFormEditFeed(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable("codeName") Feed feedP,
			@ModelAttribute(value = "feed") FeedForm feed,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "/feeds/newsFeed";
		}
		int version = feedP.getVersion();
		feedP.changeValues(feed);
		feedP = serviceFeed.updateFeed(feedP);
		model.addAttribute(new FeedForm(feedP));
		if (version < feedP.getVersion()) {
			redirectAttributes.addFlashAttribute("info",
					LanguageLoad.getinstance().find("web/feed/info/websites"));
		} else {
			redirectAttributes.addFlashAttribute("error",
					LanguageLoad.getinstance().find("web/feed/error/siteserrorgeneric"));
		}
		return "/feeds/feedForm";
	}

	@RequestMapping(value = "/get/{codeName}/test", method = RequestMethod.GET)
	public String formTestFeed(Model model, @PathVariable("codeName") Feed feedP) {
		if (feedP.isRSS()) {
			feedP.setUrlNews("");
			feedP.setRSS(false);
		}
		model.addAttribute("feed", new FeedForm(feedP));
		return "/feeds/comprobarForm";
	}

	@RequestMapping(value = "/get/{codeName}/test", method = RequestMethod.POST, params = { "testFeed" })
	public @ResponseBody
    News testTestFeed(Model model,
                      @ModelAttribute(value = "feed") FeedForm feed) {
		feed.setIsRSS(false);
		News news = serviceFeed.testFeed(feed);
		return news;
	}

	@RequestMapping(value = { "/get/{codeName}/edit", "/create" }, method = RequestMethod.POST, params = { "testFeed" })
	public String testFeed(Model model,
			@ModelAttribute(value = "feed") FeedForm feed) {
		News news = serviceFeed.testFeed(feed);
		model.addAttribute(news);
		return "/feeds/comprobarForm";
	}

	@RequestMapping("/get/{codeName}/remove")
	public String removeFeed(Model model,
			RedirectAttributes redirectAttributes,
			@PathVariable("codeName") Feed feed) {
		if (this.serviceFeed.removeFeed(feed)) {
			redirectAttributes.addFlashAttribute("info",
					LanguageLoad.getinstance().find("web/feed/info/feedremovedsucces") + " " + feed.getName());
		} else {
			model.addAttribute("error",
					LanguageLoad.getinstance().find("web/feed/error/feedremovedfail") + " " + feed.getName());
			return "oneFeed";
		}
		return "redirect:/feeds";
	}
	
	

	@RequestMapping("/update/all")
	public String updateAll(Model model, RedirectAttributes redirectAttributes) {
		schedulerService.startAllTask();
		redirectAttributes
				.addFlashAttribute(
						"info",
						LanguageLoad.getinstance().find("web/feed/info/startedprocessupdatingfeed"));
		return "redirect:/feeds";
	}
	
}
