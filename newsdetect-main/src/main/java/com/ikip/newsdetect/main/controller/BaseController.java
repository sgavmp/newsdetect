package com.ikip.newsdetect.main.controller;

import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Date;

@SuppressWarnings("deprecation")
public abstract class BaseController {
	
	//Indica al template el menu que tiene que activar
	protected String menu="";
	
	
	@ModelAttribute("hoy")
	public Date getHoy() {
		return new Date();
	}

	@ModelAttribute("semana")
	public Date getSemana() {
		Date ayer = new Date(new Date().getTime() - 7 * 24 * 3600 * 1000l  );
		return ayer;
	}
	
	@ModelAttribute("mes")
	public Date getMes() {
		Date ayer = new Date(new Date().getTime() - 31 * 24 * 3600 * 1000l  );
		return ayer;
	}
	
	@ModelAttribute("ayer")
	public Date getAyer() {
		Date ayer = new Date(new Date().getTime() - 24 * 3600 * 1000l );
		return ayer;
	}
	
	@ModelAttribute("menu")
	public String getMenuActive() {
		return menu;
	}

}
