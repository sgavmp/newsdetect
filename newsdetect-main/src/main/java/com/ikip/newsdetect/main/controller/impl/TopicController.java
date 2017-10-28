package com.ikip.newsdetect.main.controller.impl;

import com.ikip.newsdetect.main.controller.BaseController;
import com.ikip.newsdetect.main.repository.TopicRepository;
import com.ikip.newsdetect.main.service.TopicManager;
import com.ikip.newsdetect.model.Topic;
import com.ikip.newsdetect.topic.validator.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/topic")
public class TopicController extends BaseController {

	@Autowired
	private TopicRepository topicRepository;

	@Autowired
	private TopicManager topicManager;
	
	@RequestMapping("**")
	public String getAllLocations(Model model) {
		model.addAttribute("allTopic", topicRepository.findAll());
		model.addAttribute("term", new Topic());
		return "topic";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String createLocation(Model model,
                                 RedirectAttributes redirectAttributes, Topic wordFilter,
                                 BindingResult result) throws UnsupportedEncodingException {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
			//model.addAttribute("error", LanguageLoad.getinstance().find("web/topics/error/formerror") );
			return "topic";
		}
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle(), topicManager),
				new InputStreamReader(new ByteArrayInputStream(wordFilter.getQuery().getBytes()),"UTF-8"));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			//model.addAttribute("error", LanguageLoad.getinstance().find("web/topics/error/topicnotexixt1")+" " + e.getTopic() + " " + LanguageLoad.getinstance().find("web/topics/error/topicnotexixt2"));
			return "topic";
		} catch (CyclicDependencyException e) {
			//model.addAttribute("error", LanguageLoad.getinstance().find("web/topics/error/cyclicerror")+" "+e.toString());
			return "topic";
		} catch (ParseException | TokenMgrError e) {
			//model.addAttribute("error", LanguageLoad.getinstance().find("web/topics/error/topicgenericerror"));
			return "topic";
		}
		//TODO almacenar referencias
		//wordFilter.setReferences(validator.getReferences());
		//topicRepository.save(wordFilter);
		//redirectAttributes.addFlashAttribute("info",LanguageLoad.getinstance().find("web/topics/info/topicaddcorrect"));
		return "redirect:/topic";
	}

	@RequestMapping(value = "/get/{id}/edit", method = RequestMethod.GET)
	public String getFormUpdateLocation(Model model,
			@PathVariable("id") Topic word) {
		model.addAttribute("allTopic", topicRepository.findAll());
		model.addAttribute("term", word);
		return "topic";
	}

	@RequestMapping(value = "/get/{id}/edit", method = RequestMethod.POST)
	public String updateLocation(Model model,
                                 RedirectAttributes redirectAttributes, Topic wordFilter,
                                 @PathVariable("id") Topic before, BindingResult result) throws UnsupportedEncodingException {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
			//model.addAttribute("error", LanguageLoad.getinstance().find("web/topics/error/formerror"));
			return "topic";
		}
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle(), topicManager),
				new InputStreamReader(new ByteArrayInputStream(wordFilter.getQuery().getBytes()),"UTF-8"));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			//model.addAttribute("error", LanguageLoad.getinstance().find("web/topics/error/topicnotexixt1")+" " + e.getTopic() + " " + LanguageLoad.getinstance().find("web/topics/error/topicnotexixt2"));
			return "topic";
		} catch (CyclicDependencyException e) {
			//model.addAttribute("error", LanguageLoad.getinstance().find("web/topics/error/cyclicerror")+" "+e.toString());
			return "topic";
		} catch (ParseException | TokenMgrError e) {
			//model.addAttribute("error", LanguageLoad.getinstance().find("web/topics/error/topicgenericerror"));
			return "topic";
		}
		if (before.getTitle().equals(wordFilter.getTitle())) {
		    before.setQuery(wordFilter.getQuery());
		    topicRepository.save(before);
		    //redirectAttributes.addFlashAttribute("info",
		    //		LanguageLoad.getinstance().find("web/topics/info/topicupdatecorrect"));
		    return "redirect:/topic";
		} else {
		    //model.addAttribute("error", LanguageLoad.getinstance().find("web/topics/error/topicnamecannotbeedit"));
			return "topic";
		}
	}

	@RequestMapping(value = "/get/{id}/remove", method = RequestMethod.GET)
	public String updateNewsByFeed(Model model, @PathVariable("id") Topic word) {
		topicRepository.delete(word);
		return "redirect:/topic";
	}

}