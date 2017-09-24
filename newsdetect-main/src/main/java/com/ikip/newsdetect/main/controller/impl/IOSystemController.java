package com.ikip.newsdetect.main.controller.impl;

import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.service.ConfigurationServiceIO;
import es.ucm.visavet.gbf.app.service.IOService;
import es.ucm.visavet.gbf.app.web.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;


@Controller
@RequestMapping("/oisystem")
public class IOSystemController extends BaseController {

	
	private static final Logger log = LoggerFactory.getLogger(IOSystemController.class);
	public static final String ROOT = "folderio/output";

	private final ResourceLoader resourceLoader;
	
	private ArrayList<File> borrables=new ArrayList<File>();
	
	@Autowired
	private IOService ioservice;
	
	@Autowired
	private ConfigurationServiceIO IOFolder;
	
	@Autowired
	public IOSystemController(ResourceLoader resourceLoader, ConfigurationServiceIO IOFolder) {
		this.resourceLoader = resourceLoader;
		this.menu =LanguageLoad.getinstance().find("web/iosystem/tittle"); 
		new File(IOFolder.getPathgen()+ROOT).mkdirs();
		this.IOFolder=IOFolder;
		cleanFolder();
		
	}

	
	
	@ModelAttribute("ioexport")
	public String getExport() {
		return LanguageLoad.getinstance().find("web/iosystem/ioexport");
	}
	
	@ModelAttribute("ioimport")
	public String getImport() {
		return LanguageLoad.getinstance().find("web/iosystem/ioimport");
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
		return "oisystem";
	}
	
	@RequestMapping(value = "export", method = RequestMethod.GET)
	@ResponseBody
	public FileSystemResource getFormUpdateLocationDel(HttpServletResponse response) {
		try {
			for (File borrar : borrables) {
				borrar.delete();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		try {
			String Path = ioservice.exportA();
			
////			return new FileSystemResource(new File(Path)); 
//			
//			InputStream is = new FileInputStream(Path);
//		      org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
//		      response.flushBuffer();
//		      response.setContentType("application/octet-stream");
//		      response.setHeader("Content-Disposition", "attachment; filename=sample.xls");
		      
		      File file = new File(Path);
		      response.setContentLength((int)file.getName().length());
		      response.setContentType("application/force-download");
		      response.setHeader("Content-Disposition","attachment; filename=\"" + file.getName() + "\"");//fileName);
		      FileSystemResource Salida=new FileSystemResource(file);
//		      file.delete();
		      borrables.add(file);
		      file.deleteOnExit();
		      return Salida;
		      
		      
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null; 
		
	}

	


}