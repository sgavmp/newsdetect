package com.ikip.newsdetect.main.controller.impl;

import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.Configuracion;
import es.ucm.visavet.gbf.app.domain.Feed;
import es.ucm.visavet.gbf.app.scheduler.SchedulerService;
import es.ucm.visavet.gbf.app.service.ConfiguracionService;
import es.ucm.visavet.gbf.app.service.FeedScraping;
import es.ucm.visavet.gbf.app.service.FeedService;
import es.ucm.visavet.gbf.app.service.NewsIndexService;
import es.ucm.visavet.gbf.app.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/configuracion")
public class ConfiguracionController extends BaseController {
	
	@Autowired
	private ConfiguracionService configuracionService;
	
	@Autowired
	private FeedService feedService;
	
	@Autowired
	private FeedScraping feedScraping;
	
	@Autowired
	private SchedulerService schedulerService;
	
	@Autowired
	private NewsIndexService newsIndexService; 

	
	public ConfiguracionController() {
		this.menu = LanguageLoad.getinstance().find("web/config/tittle");
	}
	
	@ModelAttribute("feeds")
	public List<Feed> getAllFeeds() {
		return feedService.getAllFeedAlph();
	}
	
	@RequestMapping("**")
	public String getAllLocations(Model model) {
		model.addAttribute("conf", configuracionService.getConfiguracion());
		return "202";
	}
	
	@RequestMapping(value="**", method= RequestMethod.POST)
	public String createLocation(Model model, RedirectAttributes redirectAttributes, Configuracion configuracion, BindingResult result) {
        if (result.hasErrors()) {
            return "202";
        }
		try {
			configuracionService.setConfiguracion(configuracion);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		redirectAttributes.addFlashAttribute("info",LanguageLoad.getinstance().find("web/config/configupdate"));
		return "redirect:/configuracion";
	}
	
	@RequestMapping(value="/resetAlerts")
	public String resetAlerts(Model model, RedirectAttributes redirectAttributes, Configuracion configuracion, BindingResult result) {
        try {
			newsIndexService.resetAllAlerts();
		} catch (IOException e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/config/error/resetalerterror"));
		}
		model.addAttribute("info", LanguageLoad.getinstance().find("web/config/info/resetalertscorrect") );
		model.addAttribute("conf", configuracionService.getConfiguracion());
		return "202";
	}
	
	@RequestMapping(value="/resetLocations")
	public String resetLocations(Model model, RedirectAttributes redirectAttributes, Configuracion configuracion, BindingResult result) {
        try {
			newsIndexService.resetAllLocation();
		} catch (IOException e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/config/error/resetlocationerror"));
		}
		model.addAttribute("info", LanguageLoad.getinstance().find("web/config/info/resetlocationscorrect"));
		model.addAttribute("conf", configuracionService.getConfiguracion());
		return "202";
	}
	
	@RequestMapping(value="/shutdown")
	public String shutdown(Model model, RedirectAttributes redirectAttributes) {
        try {
			newsIndexService.stopDirectory();
			schedulerService.stopAllTask();
		} catch (IOException e) {
			
		}
        
		return "202";
	}
}
