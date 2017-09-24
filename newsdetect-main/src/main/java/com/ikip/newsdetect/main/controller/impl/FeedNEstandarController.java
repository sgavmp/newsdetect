package com.ikip.newsdetect.main.controller.impl;

import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.*;
import es.ucm.visavet.gbf.app.service.ConfigurationServiceIO;
import es.ucm.visavet.gbf.app.service.FeedService;
import es.ucm.visavet.gbf.app.service.TestNewsIndexService;
import es.ucm.visavet.gbf.app.service.impl.AlertServiceImpl;
import es.ucm.visavet.gbf.app.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


@Controller
@RequestMapping("/feedsNE")
public class FeedNEstandarController extends BaseController {

	
//	private static final Logger log = LoggerFactory.getLogger(CargaXLSController.class);

	public static final String ROOT = "folderuploadjar/";

//	private final ResourceLoader resourceLoader;
	
//	@Autowired
//	private TopicRepository topicRepository;
//
	@Autowired
	private TestNewsIndexService testNews;
	
	@Autowired
	private FeedService serviceFeed;

	@Autowired
	private AlertServiceImpl serviceAlert;
	
	@Autowired
	private ConfigurationServiceIO IOFolder;
	
	@Autowired
	public FeedNEstandarController(ResourceLoader resourceLoader, ConfigurationServiceIO IOFolder) {
//		this.resourceLoader = resourceLoader;
		this.menu = LanguageLoad.getinstance().find("web/feedne/tittle");
		new File(IOFolder.getPathgen()+ROOT).mkdirs();
		this.IOFolder=IOFolder;
	}

	@RequestMapping("**")
	public String getAllLocations(Model model) {
		this.menu = LanguageLoad.getinstance().find("web/feedne/tittleext");
		model.addAttribute("message", LanguageLoad.getinstance().find("web/feedne/loadconfigurator"));
		model.addAttribute("message2", LanguageLoad.getinstance().find("web/feedne/actualloadconfigurator"));
		
		List<FileEO> filesT=new ArrayList<FileEO>();
		
		filesT=getFiles();
		
		model.addAttribute("files", filesT);
		return "/feeds/inyectedfeed";
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

	@ModelAttribute("nefeedsubmit")
	public String getNeFeedSubmitText() {
		return LanguageLoad.getinstance().find("web/feed/nefeedsubmit");
	}
	
	@ModelAttribute("nefeedsuredelete")
	public String getNeFeedsuredeleteText() {
		return LanguageLoad.getinstance().find("web/feed/nefeedsuredelete");
	}
	
	@ModelAttribute("nefeedname")
	public String getNeFeedNameText() {
		return LanguageLoad.getinstance().find("web/feed/nefeedname");
	}
	
	@ModelAttribute("nefeedcreationdate")
	public String getNeFeedCreationDateText() {
		return LanguageLoad.getinstance().find("web/feed/nefeedcreationdate");
	}
	
	@ModelAttribute("nefeedappli")
	public String getNeFeedAppliText() {
		return LanguageLoad.getinstance().find("web/feed/nefeedappli");
	}
	
	@ModelAttribute("nefeeddelete")
	public String getNeFeedDeleteText() {
		return LanguageLoad.getinstance().find("web/feed/nefeeddelete");
	}
	
	
	@RequestMapping(value = "/up", method = RequestMethod.POST)
	public String createLocation(@RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes, Model model) {
		
		
		
		
		if (!file.isEmpty()) {
			try {
				//CAmbiar el nombre del archivo a uno magico con el timestamp
				String nanoT=Long.toString(System.nanoTime());
				
				Files.copy(file.getInputStream(), Paths.get(IOFolder.getPathgen()+ROOT,nanoT+"_"+ file.getOriginalFilename()));
				redirectAttributes.addFlashAttribute("message",
						LanguageLoad.getinstance().find("web/feedne/info/updatesucces") + " " + file.getOriginalFilename() + "!");
				
				File fileI=new File(IOFolder.getPathgen()+ROOT+nanoT+"_"+ file.getOriginalFilename());
				URLClassLoader loader = URLClassLoader.newInstance(
					    new URL[] { fileI.toURI().toURL() },
					    getClass().getClassLoader()	);
					try {
						
						
						
							Class<?> clazz = Class.forName("ExternalCollectionImp", true, loader);
							app.model.ExternalCollection instance=(app.model.ExternalCollection) clazz.newInstance ();
							
							//Capturo el nombre para ver que esta bien
							instance.getName();
							loader.close();
	
				
				} catch (Exception e) {
					loader.close();
					try {
						fileI.delete();
					} catch (Exception e2) {
						//Si puede puede si no na
					}					
					model.addAttribute("message", LanguageLoad.getinstance().find("web/feedne/error/uploadfiled") + " " + file.getOriginalFilename() + " " + LanguageLoad.getinstance().find("web/feedne/error/uploadfiled2"));
					return "403";
				}
				
				
				
			} catch (IOException|RuntimeException e) {
				model.addAttribute("message", LanguageLoad.getinstance().find("web/feedne/error/uploadfiled") + file.getOriginalFilename() + " => " + e.getMessage());
				return "403";
			}
		} else {
			model.addAttribute("message", LanguageLoad.getinstance().find("web/feedne/error/uploadfiled") + file.getOriginalFilename() + " " + LanguageLoad.getinstance().find("web/feedne/error/uploadfiled2empty"));
			return "403";
		}

		
		return "redirect:/feedsNE";
	}
	
	@RequestMapping(value = "file/{id}", method = RequestMethod.GET)
	public String getFormUpdateLocation(Model model,
			@PathVariable("id") String FileNamed) {
		System.out.println(LanguageLoad.getinstance().find("web/feedne/procesandofuente")+"... "+FileNamed);
		FileNamed=findmeFile(FileNamed);
		
		try {
			File fileI=new File(IOFolder.getPathgen()+ROOT+FileNamed);
			URLClassLoader loader = URLClassLoader.newInstance(
				    new URL[] { fileI.toURI().toURL() },
				    getClass().getClassLoader()
				);
				Class<?> clazz = Class.forName("ExternalCollectionImp", true, loader);
				app.model.ExternalCollection instance=(app.model.ExternalCollection) clazz.newInstance ();
				Set<String> values=instance.getAtributes();
				loader.close();
	
		this.menu = LanguageLoad.getinstance().find("web/feedne/addsourcene");
		
		FeedForm forFormBase = new FeedForm();
		forFormBase.setPairAttributesInput(new ArrayList<PairAtributte>());
		for (String string : values) {
			forFormBase.getPairAttributesTypes().add(string);
			forFormBase.getPairAttributesInput().add(new PairAtributte(string, "", null));
		}
		forFormBase.setNoEstandar(true);
		forFormBase.setPluginPath(FileNamed);
		model.addAttribute("feed", forFormBase);
		model.addAttribute("nuevo", true);
		//model.addAttribute("pluginmodelfile",FileNamed);
		//model.addAttribute("values",values);
		
		//return "/feeds/feedForm";
		
		return "/feeds/inyectedfeedForm";
		
	} catch (Exception e) {
		e.printStackTrace();
		model.addAttribute("message",LanguageLoad.getinstance().find("web/feedne/error/erroraddsourcene"));
		return "403";
	}
	}
	
	@RequestMapping(value = "filedel/{id}", method = RequestMethod.GET)
	public String getFormUpdateLocationDel(Model model,
			@PathVariable("id") String FileNamed) {
		System.out.println("Borrando... "+FileNamed);
		FileNamed=findmeFile(FileNamed);
		try {
			File XLSFile=new File(IOFolder.getPathgen()+ROOT+FileNamed);
			XLSFile.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.menu = LanguageLoad.getinstance().find("web/feedne/tittleext");
		model.addAttribute("message", LanguageLoad.getinstance().find("web/feedne/loadconfigurator"));
		model.addAttribute("message2", LanguageLoad.getinstance().find("web/feedne/actualloadconfigurator"));
		model.addAttribute("message3", LanguageLoad.getinstance().find("web/feedne/resultreport"));
		
		
		
	List<FileEO> filesT=new ArrayList<FileEO>();
	filesT=getFiles();
		
		
		model.addAttribute("files", filesT);
		
		return "redirect:/feedsNE";
	}
	
	

	private List<FileEO> getFiles() {
		List<FileEO> Salida=new ArrayList<FileEO>();
File f=new File(IOFolder.getPathgen()+ROOT);
		f.mkdirs();
		if (f.exists()){ 
			File[] ficheros = f.listFiles();
			for (int x=0;x<ficheros.length;x++){
				
				String hrefe=ficheros[x].getName();
				String ShowName=ficheros[x].getName();
				String[] ShowNameL=ficheros[x].getName().split("_");
				
				if (ShowNameL.length>1)
					ShowName=ShowNameL[1];
				
				
				try {
	    			File fileI = ficheros[x];
	    			fileI.mkdirs();
	    			URLClassLoader loader = URLClassLoader.newInstance(
	    				    new URL[] { fileI.toURI().toURL() },
	    				    getClass().getClassLoader()
	    				);
	    				Class<?> clazz = Class.forName("ExternalCollectionImp", true, loader);
	    				app.model.ExternalCollection instance=(app.model.ExternalCollection) clazz.newInstance ();
	    				
	    				
	    				ShowName=instance.getName();
	    				loader.close();
	    				
	    		} catch (Exception e) {
	    			
	    			System.out.println(LanguageLoad.getinstance().find("web/feedne/error/errorjar1") + ShowName + LanguageLoad.getinstance().find("web/feedne/error/errorjar2") +ficheros[x].getPath() );
	    			ShowName=ShowName+"Error";
	    			e.printStackTrace();
	    		}
				
				
			

			
			Date fecha=new Date();
			 try {
				BasicFileAttributes view
				   = Files.getFileAttributeView(ficheros[x].toPath(), BasicFileAttributeView.class)
				          .readAttributes();
				FileTime fechaAt = view.creationTime();
				fecha.setTime(fechaAt.toMillis());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			 
			 
			
			  
			Salida.add(new FileEO(hrefe,ShowName,fecha));
			}
		}
		return Salida;
	}

	private String findmeFile(String fileNamed) {
		File f=new File(IOFolder.getPathgen()+ROOT);
		
		if (f.exists()){ 
			File[] ficheros = f.listFiles();
			for (int x=0;x<ficheros.length;x++){
			String hrefe=ficheros[x].getName();
			if (hrefe.startsWith(fileNamed))
				return hrefe;
			}
		}
		
		return "";
	}
	
	@RequestMapping(value = "/file/{id}", method = RequestMethod.POST)
	public String formNewFeed(Model model
			,RedirectAttributes redirectAttributes
			,@Valid FeedForm feed
			,BindingResult result
			) {
		if (result.hasErrors()) {
			return "redirect:/feedsNE";
		}
		Feed feedP = new Feed(feed);
		feedP.setNoEstandar(true);
		feedP = serviceFeed.createFeed(feedP);
		if (feedP.getId() != null) {
			redirectAttributes.addFlashAttribute("info",
					LanguageLoad.getinstance().find("web/feedne/info/sitesucesfullicreated"));
		} else {
			redirectAttributes.addFlashAttribute("error",
					LanguageLoad.getinstance().find("web/feedne/error/siteerrorcreated"));
		}
//		return "redirect:/feeds/get/" + feedP.getId() + "/edit";
		return "redirect:/feeds";
	}

	@RequestMapping(value = "/get/{codeName}/edit", method = RequestMethod.GET)
	public String formEditFeed(Model model, @PathVariable("codeName") Feed feedP) {
		model.addAttribute("feed", new FeedForm(feedP));
		
		
		
		
		return "/feeds/inyectedfeedForm";
	}
	
	
	@RequestMapping("/get/{codeName}/show")
	public String getAllNewsByFeed(Model model, @PathVariable("codeName") Feed feed) {
		model.addAttribute(feed);
		Set<Alert> alertas = serviceAlert.getAlertDetectSite(feed);
		for (Alert alerta : alertas) {
			Iterator<NewsDetect> iterator = alerta.getNewsDetect().iterator();
			while(iterator.hasNext()) {
				NewsDetect news = iterator.next();
				if (!news.getSite().equals(feed)) {
					iterator.remove();
				}
			}
		}
		model.addAttribute("alertas", alertas);
		return "/feeds/oneFeed";
	}
	
	
	@RequestMapping(value = "/get/{codeName}/edit", method = RequestMethod.POST)
	public String formEditFeed(Model model
			,RedirectAttributes redirectAttributes
			,@PathVariable("codeName") Feed feedP
			,@Valid FeedForm feed
			,BindingResult result
			) {
		if (result.hasErrors()) {
			return "redirect:/feedsNE";
		}
		int version = feedP.getVersion();
		feedP.changeValues(feed);
		feedP.setNoEstandar(true);
		feedP = serviceFeed.updateFeed(feedP);
		if (feedP.getId() != null) {
			redirectAttributes.addFlashAttribute("info",
					LanguageLoad.getinstance().find("web/feedne/info/sitesucesfullicreated"));
		} else {
			redirectAttributes.addFlashAttribute("error",
					LanguageLoad.getinstance().find("web/feedne/error/siteerrorcreated"));
		}
//		return "redirect:/feeds/get/" + feedP.getId() + "/edit";
		return "redirect:/feeds";
	}
	
	
	@RequestMapping(value = "/get/{codeName}/test", method = RequestMethod.POST, params = { "testFeed" })
	public @ResponseBody
    News testTestFeed(Model model,
                      @ModelAttribute(value = "feed") FeedForm feed) {
		feed.setIsRSS(false);
		News news = serviceFeed.testFeed(feed);
		return news;
	}

	@RequestMapping(value = { "/get/{codeName}/edit", "/file/{id}" }, method = RequestMethod.POST, params = { "testFeed" })
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
					LanguageLoad.getinstance().find("web/feedne/info/sitesucesfullideleted")+"->"+ feed.getName());
		} else {
			model.addAttribute("error",
					LanguageLoad.getinstance().find("web/feedne/error/siteerrorfullideleted")+"->" + feed.getName());
			return "oneFeed";
		}
		return "redirect:/feeds";
	}
	
	
}