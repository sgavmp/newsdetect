package com.ikip.newsdetect.main.controller.impl;

import es.ucm.visavet.gbf.app.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
	
	
	
	@ModelAttribute("folder")
	public String getFolder() {
		return "hola";
	}

	
	
}
