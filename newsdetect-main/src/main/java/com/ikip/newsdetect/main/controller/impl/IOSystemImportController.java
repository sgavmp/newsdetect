package com.ikip.newsdetect.main.controller.impl;

import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.service.ConfigurationServiceIO;
import es.ucm.visavet.gbf.app.service.IOService;
import es.ucm.visavet.gbf.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


@Controller
@RequestMapping("/oisystemimport")
public class IOSystemImportController extends BaseController {

	
	private static final Logger log = LoggerFactory.getLogger(IOSystemImportController.class);
	public static final String ROOT = "folderio/input";

	private final ResourceLoader resourceLoader;
	
	@Autowired
	private IOService ioservice;
	
	@Autowired
	private ConfigurationServiceIO IOFolder;
	
	@Autowired
	public IOSystemImportController(ResourceLoader resourceLoader, ConfigurationServiceIO IOFolder) {
		this.resourceLoader = resourceLoader;
		this.menu = LanguageLoad.getinstance().find("web/iosystem/tittle");
		new File(IOFolder.getPathgen()+ROOT).mkdirs();
		this.IOFolder=IOFolder;
		cleanFolder();
	}

	
	@ModelAttribute("iouploadxls")
	public String getImport() {
		return LanguageLoad.getinstance().find("web/iosystem/iouploadxls");
	}
	
	private void cleanFolder() {
		File f=new File(IOFolder.getPathgen()+ROOT);
		
		if (f.exists()){ 
			File[] ficheros = f.listFiles();
			for (int x=0;x<ficheros.length;x++)
				ficheros[x].delete();
		}
		else
			f.mkdirs();
	}

	@RequestMapping("**")
	public String getAllLocations(Model model) {
		model.addAttribute("message", LanguageLoad.getinstance().find("web/iosystem/tittle"));
		return "oisystemimport";
	}
	
	@RequestMapping(value = "/import", method = RequestMethod.POST)
	public String createLocation(@RequestParam("file") MultipartFile file,
			Model model) {
		
		File f=new File(IOFolder.getPathgen()+ROOT);
		if (!f.exists())
			f.mkdirs();
		
		ArrayList<String> listaSalida=new ArrayList<String>();
		
		if (!file.isEmpty()) {
			try {
				String nanoT=Long.toString(System.nanoTime());
				
				 Path patharchivoSalida = Paths.get(IOFolder.getPathgen()+ROOT,nanoT+"_"+ file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), patharchivoSalida);
				
				
				ioservice.importA(patharchivoSalida.toFile(),listaSalida);
				
				patharchivoSalida.toFile().delete();
				
				listaSalida.add(
						LanguageLoad.getinstance().find("web/iosystem/sucessupload")
						+ " " + file.getOriginalFilename() + "!");
			} catch (IOException|RuntimeException e) {
				listaSalida.add( LanguageLoad.getinstance().find("web/iosystem/failedupload") + " " + file.getOriginalFilename() + " => " + e.getMessage());
			}
		} else {
			listaSalida.add(LanguageLoad.getinstance().find("web/iosystem/failedupload") + " " + file.getOriginalFilename() + " "+ LanguageLoad.getinstance().find("web/iosystem/faileduploadempty"));
		}
		
		
		model.addAttribute("message", LanguageLoad.getinstance().find("web/iosystem/tittle"));
		model.addAttribute("messageS", listaSalida);
		
		return "oisystemimport";
	}

	


}