package com.ikip.newsdetect.main.controller.impl;

import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.Location;
import es.ucm.visavet.gbf.app.domain.topic.TopicManager;
import es.ucm.visavet.gbf.app.service.impl.PlaceAlertServiceImpl;
import es.ucm.visavet.gbf.app.web.controller.BaseController;
import es.ucm.visavet.gbf.topics.validator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/locations")
public class LocationsController extends BaseController {
	
	@Autowired
	private PlaceAlertServiceImpl serviceLocation;
	
	@Autowired
	private TopicManager topicManager;
	
	public LocationsController() {
		this.menu = LanguageLoad.getinstance().find("web/locations/tittle");
	}
	
	
	@ModelAttribute("locationcreate")
	public String getLocationCreateText() {
		return LanguageLoad.getinstance().find("web/locations/locationcreate");
	}
	
	@ModelAttribute("locationedit")
	public String getLocationEditText() {
		return LanguageLoad.getinstance().find("web/locations/locationedit");
	}
	
	@ModelAttribute("locationplacename")
	public String getLocationnameText() {
		return LanguageLoad.getinstance().find("web/locations/locationplacename");
	}
	
	@ModelAttribute("locationcountry")
	public String getLocationCountryText() {
		return LanguageLoad.getinstance().find("web/locations/locationcountry");
	}
	
	@ModelAttribute("locationterritoriallevel")
	public String getLocationTerritLevelText() {
		return LanguageLoad.getinstance().find("web/locations/locationterritoriallevel");
	}
	
	@ModelAttribute("locationcountryselect")
	public String getLocationCountrySelectText() {
		return LanguageLoad.getinstance().find("web/locations/locationcountryselect");
	}
	
	@ModelAttribute("locationterritoriallevelselect")
	public String getLocationTerritLevelSelectText() {
		return LanguageLoad.getinstance().find("web/locations/locationterritoriallevelselect");
	}
	
	@ModelAttribute("locationquery")
	public String getLocationQueryText() {
		return LanguageLoad.getinstance().find("web/locations/locationquery");
	}
	
	@ModelAttribute("locationsend")
	public String getLocationSendText() {
		return LanguageLoad.getinstance().find("web/locations/locationsend");
	}
	
	@ModelAttribute("locationcancel")
	public String getLocationCancelText() {
		return LanguageLoad.getinstance().find("web/locations/locationcancel");
	}
	
	@ModelAttribute("locationname")
	public String getLocationNameText() {
		return LanguageLoad.getinstance().find("web/locations/locationname");
	}
	
	@ModelAttribute("locationcreationdate")
	public String getLocationCreationdateText() {
		return LanguageLoad.getinstance().find("web/locations/locationcreationdate");
	}
	
	@ModelAttribute("locationeditiondate")
	public String getLocationEditionText() {
		return LanguageLoad.getinstance().find("web/locations/locationeditiondate");
	}
	
	@ModelAttribute("locationeditt")
	public String getLocationEdit2Text() {
		return LanguageLoad.getinstance().find("web/locations/locationeditt");
	}
	
	@ModelAttribute("locationdelete")
	public String getLocationDeleteText() {
		return LanguageLoad.getinstance().find("web/locations/locationdelete");
	}
	
	@ModelAttribute("locationdeletetext")
	public String getLocationDeleteTestText() {
		return LanguageLoad.getinstance().find("web/locations/locationdeletetext");
	}
	
	
	
	@RequestMapping("**")
	public String getAllLocations(Model model) {
		model.addAttribute("location", new Location());
		model.addAttribute("allLocations", serviceLocation.getAllLocations());
		return "locations";
	}
	
	@RequestMapping(value="/create", method= RequestMethod.POST)
	public String createLocation(Model model, RedirectAttributes redirectAttributes, @Valid Location location, BindingResult result) {
        if (result.hasErrors()) {
            return "locations";
        }
		try {
			serviceLocation.createLocation(location);
		}  catch (IOException e) {
			model.addAttribute("error",LanguageLoad.getinstance().find("web/locations/error/errordetectinglocations"));
			return "locations";
		}
		redirectAttributes.addFlashAttribute("info",LanguageLoad.getinstance().find("web/locations/info/detectinglocationscorrect"));
		return "redirect:/locations";
	}
	
	@RequestMapping(value = "/get/{id}/edit", method= RequestMethod.GET)
	public String getFormUpdateLocation(Model model, @PathVariable("id") Location location) {
		model.addAttribute("location",location);
		model.addAttribute("allLocations", serviceLocation.getAllLocations());
		return "locations";
	}
	
	@RequestMapping(value="/get/{id}/edit", method= RequestMethod.POST)
	public String updateLocation(Model model, RedirectAttributes redirectAttributes, @Valid Location location, @PathVariable("id") Location before, BindingResult result) throws UnsupportedEncodingException {
        if (result.hasErrors() & location.getId().equals(before.getId())) {
        	redirectAttributes.addFlashAttribute("error",LanguageLoad.getinstance().find("web/locations/error/formerror"));
            return "locations";
        }
        TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(location.getName(), topicManager),
				new InputStreamReader(new ByteArrayInputStream(location.getQuery().getBytes()),"UTF-8"));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/locations/error/topicnotexixt1") + " " + e.getTopic() + " " +LanguageLoad.getinstance().find("web/locations/error/topicnotexixt2"));
			return "locations";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/locations/error/ciclicerrortopic") + " " + e.toString());
			return "locations";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/locations/error/errorgentopic"));
			return "locations";
		}
		try {
			serviceLocation.updateLocation(before.bind(location));
		} catch (IOException e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/locations/error/errorprocesinglocations"));
			return "locations";
		}
		redirectAttributes.addFlashAttribute("info",LanguageLoad.getinstance().find("web/locations/info/updatelocationscorrect"));
		return "redirect:/locations";
	}
	
	@RequestMapping(value = "/get/{id}/remove", method= RequestMethod.GET)
	public String updateNewsByFeed(Model model, @PathVariable("id") Location location) {
		serviceLocation.removeLocation(location);
		return "redirect:/locations";
	}
	
}
