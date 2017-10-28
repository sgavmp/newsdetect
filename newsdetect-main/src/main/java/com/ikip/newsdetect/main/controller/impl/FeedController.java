package com.ikip.newsdetect.main.controller.impl;

import com.ikip.newsdetect.main.controller.BaseController;
import com.ikip.newsdetect.main.dto.FeedForm;
import com.ikip.newsdetect.main.scheduler.SchedulerService;
import com.ikip.newsdetect.main.service.AlertServiceImpl;
import com.ikip.newsdetect.main.service.FeedService;
import com.ikip.newsdetect.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/feeds")
public class FeedController extends BaseController {

	@Autowired
	private FeedService serviceFeed;
	@Autowired
	private AlertServiceImpl serviceAlert;
	@Autowired
	private SchedulerService schedulerService;

	@RequestMapping("**")
	public String getAllAlerts() {
		return "/feeds/feeds";
	}
	
	@RequestMapping("/get/{codeName}")
	public String getAllNewsByFeed(Model model, @PathVariable("codeName") Feed feed) {
		model.addAttribute(feed);
		Set<Alert> alertas = serviceAlert.getAlertDetectSite(feed);
		for (Alert alerta : alertas) {
//			Iterator<DetectedNews> iterator = alerta.getNewsDetect().iterator();
//			while(iterator.hasNext()) {
//				try {
//					DetectedNews news = iterator.next();
//					if (news==null||news.getId()==null||news.getSite()==null||!news.getSite().equals(feed)) {
//						iterator.remove();
//					}
//				} catch (NullPointerException e) {
//					e.printStackTrace();
//				}
//
//			}
		}
		model.addAttribute("alertas", alertas);
		return "/feeds/oneFeed";
	}
	
	@ModelAttribute("enumExtraction")
	public ExtractionTypeEnum[] getValuesOfExtraction() {
		return ExtractionTypeEnum.values();
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
		Feed feedP = new Feed();
		feedP = serviceFeed.createFeed(feedP);
		if (feedP.getId() != null) {
			//redirectAttributes.addFlashAttribute("info",
			//		LanguageLoad.getinstance().find("web/feed/info/feedcreatedcorrect"));
		} else {
			//redirectAttributes.addFlashAttribute("error",
			//		LanguageLoad.getinstance().find("web/feed/error/feedcreatederror"));
		}
		return "redirect:/feeds/get/" + feedP.getId() + "/edit";
	}
	
	@RequestMapping(value = "/masive", method = RequestMethod.GET)
	public String createMasiveFeed(Model model) {
		return "/feeds/masiveFeed";
	}
	
	@RequestMapping(value = "/masive", method = RequestMethod.POST)
	public String createMasiveFeed(@RequestParam(name="list") String list, Model model) {
		if (StringUtils.isEmpty(list)) {
			//model.addAttribute("error", LanguageLoad.getinstance().find("web/feed/error/oneurlperline"));
		}
		String[] listURL = list.split("\r\n");
		String regex = "^(?<name>[A-Za-z0-9 ]{2,50})\\t(?<url>(https?|ftp):\\/\\/[^\\s\\/$.?#].[^\\s]*)\\t(?<tipo>@T(massmedia|journal|specificmedia|alert|institucional)+)(?<lugar>(\\t(@L(general|españa|italia|rusia|holanda|alemania|inglaterra|portugal|francia|estadosunidos|india|marruecos)+))+)$";
		Pattern pattern = Pattern.compile(regex);
		for (String linea : listURL) {
			if (!pattern.matcher(linea).find())
			{
//				model.addAttribute("error", LanguageLoad.getinstance().find("web/feed/error/someerrorinsertedline"));
			}
		}
		List<String> urlFail = serviceFeed.createFeedAuto(listURL);
		if (!urlFail.isEmpty()) {
//			model.addAttribute("error", LanguageLoad.getinstance().find("web/feed/error/oneurlperline"));
			model.addAttribute("fail", urlFail);
		}
//		model.addAttribute("info", LanguageLoad.getinstance().find("web/feed/info/websites"));
		return "/feeds/masiveFeed";
	}

	@RequestMapping(value = "/get/{codeName}/edit", method = RequestMethod.GET)
	public String formEditFeed(Model model, @PathVariable("codeName") Feed feedP) {
		model.addAttribute("feed", new FeedForm());
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
//		int version = feedP.getVersion();
//		feedP.changeValues(feed);
//		feedP = serviceFeed.updateFeed(feedP);
//		model.addAttribute(new FeedForm(feedP));
//		if (version < feedP.getVersion()) {
////			redirectAttributes.addFlashAttribute("info",
////					LanguageLoad.getinstance().find("web/feed/info/websites"));
//		} else {
////			redirectAttributes.addFlashAttribute("error",
////					LanguageLoad.getinstance().find("web/feed/error/siteserrorgeneric"));
//		}
		return "/feeds/feedForm";
	}

	@RequestMapping(value = "/get/{codeName}/test", method = RequestMethod.GET)
	public String formTestFeed(Model model, @PathVariable("codeName") Feed feedP) {
		if (feedP.getScrapType().equals(ScrapTypeEnum.RSS)) {
			feedP.setUrlScrap("");
			feedP.setScrapType(ScrapTypeEnum.RSS);
		}
		model.addAttribute("feed", new FeedForm());
		return "/feeds/comprobarForm";
	}

	@RequestMapping(value = "/get/{codeName}/test", method = RequestMethod.POST, params = { "testFeed" })
	public @ResponseBody
    News testTestFeed(Model model,
                      @ModelAttribute(value = "feed") FeedForm feed) {
		feed.setRSS(false);
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
//			redirectAttributes.addFlashAttribute("info",
//					LanguageLoad.getinstance().find("web/feed/info/feedremovedsucces") + " " + feed.getName());
		} else {
//			model.addAttribute("error",
//					LanguageLoad.getinstance().find("web/feed/error/feedremovedfail") + " " + feed.getName());
			return "oneFeed";
		}
		return "redirect:/feeds";
	}
	
	

	@RequestMapping("/update/all")
	public String updateAll(Model model, RedirectAttributes redirectAttributes) {
		schedulerService.startAllTask();
//		redirectAttributes
//				.addFlashAttribute(
//						"info",
//						LanguageLoad.getinstance().find("web/feed/info/startedprocessupdatingfeed"));
		return "redirect:/feeds";
	}
	
}
