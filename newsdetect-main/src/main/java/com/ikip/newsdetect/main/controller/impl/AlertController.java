package com.ikip.newsdetect.main.controller.impl;

import com.ikip.newsdetect.find.service.NewsIndexService;
import com.ikip.newsdetect.main.controller.BaseController;
import com.ikip.newsdetect.main.repository.LocationRepository;
import com.ikip.newsdetect.main.repository.NewsDetectRepository;
import com.ikip.newsdetect.main.service.AlertServiceImpl;
import com.ikip.newsdetect.main.service.TopicManager;
import com.ikip.newsdetect.model.Alert;
import com.ikip.newsdetect.model.DetectedNews;
import com.ikip.newsdetect.topic.validator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class AlertController extends BaseController {
	
	private static String FOLDER = "alerts";
	
	@Autowired
	private AlertServiceImpl service;
	
	@Autowired
	private NewsDetectRepository repository;
	
	@Autowired
	private TopicManager topicManager;
	
	@Autowired
	private NewsIndexService newsIndexService;
	
	@Autowired
	private LocationRepository locationRepository;
	
	@ModelAttribute("folder")
	public String getFolder() {
		return FOLDER;
	}

	@ModelAttribute("allCountriesAfects")
	public Map<String, String> getAllCountriesAfects() {
		Map<String, String> listCountries = new HashMap<String, String>();
		
//		List<Location> listLocation = (List<Location>) locationRepository.findAllAfectAlert();
//		for (Location loc : listLocation) {
//			listCountries.put(
//					CountryCode.getByCode(loc.getCountry().name())
//							.getAlpha3(), "rgba(255,87,34,1)");
//		}
//		listCountries.put("defaultFill", "rgba(200,200,200,1)");
//
		return listCountries;
	}
	@RequestMapping("/list")
	public String getAllLocations(Model model) {
		model.addAttribute("allWords", service.getAllAlert());
		return "/alerts/words";
	}
	
	@RequestMapping(value="/create", method= RequestMethod.POST)
	public String createLocation(Model model, RedirectAttributes redirectAttributes, Alert wordFilter, BindingResult result) throws UnsupportedEncodingException {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
            return "/alerts/formAlert";
        }
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle()+"test", topicManager),
				new InputStreamReader(new ByteArrayInputStream(wordFilter.getQuery().getBytes()),"UTF-8"));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
//			model.addAttribute("error", LanguageLoad.getinstance().find("web/base/error/topicnotexist") +" "+ e.getTopic()
//			+ LanguageLoad.getinstance().find("web/base/error/topicnotexist2"));
			return "/alerts/formAlert";
		} catch (CyclicDependencyException e) {
//			model.addAttribute("error",LanguageLoad.getinstance().find("web/base/error/topicciclico") + " "+ e.toString());
			return "/alerts/formAlert";
		} catch (ParseException | TokenMgrError e) {
//			model.addAttribute("error", LanguageLoad.getinstance().find("web/base/error/validatingquerry") + " "+ e.getMessage());
			return "/alerts/formAlert";
		}
		try {
			service.create(wordFilter);
		} catch (IOException e) {
//			redirectAttributes.addFlashAttribute("error",LanguageLoad.getinstance().find("web/alert/error/detctingalerts"));
			return "redirect:/alerts/get/"+wordFilter.getId();
		}
//		redirectAttributes.addFlashAttribute("info",LanguageLoad.getinstance().find("web/alert/info/correctaddfilter"));
		return "redirect:/alerts/get/"+wordFilter.getId();
	}
	
	@RequestMapping(value = "/create", method= RequestMethod.GET)
	public String getFormCreate(Model model) {
		model.addAttribute("term", new Alert());
		return "/alerts/formAlert";
	}

	
	@RequestMapping(value = "/get/{id}/edit", method= RequestMethod.GET)
	public String getFormUpdateLocation(Model model, @PathVariable("id") Alert word) {
		model.addAttribute("allWords", service.getAllAlert());
		model.addAttribute("term",word);
		return "/alerts/formAlert";
	}
	
	@RequestMapping(value="/get/{id}/edit", method= RequestMethod.POST)
	public String updateLocation(Model model, RedirectAttributes redirectAttributes, Alert wordFilter, @PathVariable("id") Alert before, BindingResult result) throws UnsupportedEncodingException {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
//        	model.addAttribute("error",LanguageLoad.getinstance().find("web/alert/error/formerror"));
            return "/alerts/formAlert";
        }
        model.addAttribute("term", wordFilter);
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle(), topicManager),
				new InputStreamReader(new ByteArrayInputStream(wordFilter.getQuery().getBytes()),"UTF-8"));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
//			model.addAttribute("error", LanguageLoad.getinstance().find("web/base/error/topicnotexist") +" "+ e.getTopic()
//			+ LanguageLoad.getinstance().find("web/base/error/topicnotexist2"));
			return "/alerts/formAlert";
		} catch (CyclicDependencyException e) {
//			model.addAttribute("error",LanguageLoad.getinstance().find("web/base/error/topicciclico") + " "+  e.toString());
			return "/alerts/formAlert";
		} catch (ParseException | TokenMgrError e) {
//			model.addAttribute("error",LanguageLoad.getinstance().find("web/base/error/validatingquerry") + " "+e.toString() );
			return "/alerts/formAlert";
		}
//		wordFilter = service.update((Alert)wordFilter.bind(before));
//		redirectAttributes.addFlashAttribute("info",LanguageLoad.getinstance().find("web/alert/info/correctupdatefilter"));
		return "redirect:/alerts/get/"+wordFilter.getId();
	}
	
	@RequestMapping(value = "/get/{id}/reset", method= RequestMethod.GET)
	public String resetNewsByFeed(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Alert word) {
//		try {
////			newsIndexService.resetAlert(word);
//		} catch (IOException e) {
////			redirectAttributes.addFlashAttribute("error",LanguageLoad.getinstance().find("web/alert/error/detctingalerts"));
//			return "redirect:/alerts/get/"+word.getId();
//		}
//		redirectAttributes.addFlashAttribute("info",LanguageLoad.getinstance().find("web/alert/info/correctresetalerts"));
		return "redirect:/alerts/get/"+word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/remove", method= RequestMethod.GET)
	public String updateNewsByFeed(Model model, @PathVariable("id") Alert word) {
		service.remove(word);
		return "redirect:/alerts/list";
	}
	
	@RequestMapping("/get/{idAlert}")
	public String getAlert(Model model, @PathVariable("idAlert") Alert alert) {
//		alert = service.setNewsLocation(alert);
		Alert alertActive = new Alert();
		alertActive.setId(alert.getId());
		alertActive.setTitle(alert.getTitle());
//		alertActive.setNewsDetect(new HashSet<DetectedNews>());
		Alert alertHistory = new Alert();
		alertHistory.setId(alert.getId());
		alertHistory.setTitle(alert.getTitle());
//		alertHistory.setNewsDetect(new HashSet<DetectedNews>());
		Alert alertFalse = new Alert();
		alertFalse.setId(alert.getId());
		alertFalse.setTitle(alert.getTitle());
//		alertFalse.setNewsDetect(new HashSet<DetectedNews>());
//		for (DetectedNews news : alert.getNewsDetect()) {
//			if (news.getFalPositive()) {
//				alertFalse.getNewsDetect().add(news);
//			} else if (news.getHistory()) {
//				alertHistory.getNewsDetect().add(news);
//			} else {
//				alertActive.getNewsDetect().add(news);
//			}
//		}
		model.addAttribute("alert", alert);
		model.addAttribute("alertActive", alertActive);
		model.addAttribute("alertHistory", alertHistory);
		model.addAttribute("alertFalse", alertFalse);
		return "/alerts/oneAlert";
	}

	@RequestMapping("/detect")
	public String getAllAlerts(Model model) {
		return "/alerts/alerts";
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/history", method= RequestMethod.GET)
	public String setNewsDetectHistory(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Alert word, @PathVariable("idNews") DetectedNews news) {
		repository.save(news);
//		redirectAttributes.addFlashAttribute("info", LanguageLoad.getinstance().find("web/alert/info/passedalert") );
		return "redirect:/alerts/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/false", method= RequestMethod.GET)
	public String setNewsDetectFalse(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Alert word, @PathVariable("idNews") DetectedNews news) {
		repository.save(news);
//		redirectAttributes.addFlashAttribute("info", LanguageLoad.getinstance().find("web/alert/info/falsealert"));
		return "redirect:/alerts/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/active", method= RequestMethod.GET)
	public String setNewsDetectActive(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Alert word, @PathVariable("idNews") DetectedNews news) {
		repository.save(news);
//		redirectAttributes.addFlashAttribute("info", LanguageLoad.getinstance().find("web/alert/info/actuivealert"));
		return "redirect:/alerts/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/remove", method= RequestMethod.GET)
	public String setNewsDetectRemove(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Alert word, @PathVariable("idNews") DetectedNews news) {
		boolean exist = true;
		do {
//			exist = word.getNewsDetect().remove(news);
		} while (exist);
		service.update(word);
		repository.delete(news.getId());
//		redirectAttributes.addFlashAttribute("info",  LanguageLoad.getinstance().find("web/alert/info/deletealert"));
		return "redirect:/alerts/get/" + word.getId();
	}
	
}
