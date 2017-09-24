package com.ikip.newsdetect.main.controller.impl;

import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.*;
import es.ucm.visavet.gbf.app.service.ConfigurationServiceIO;
import es.ucm.visavet.gbf.app.service.TestNewsIndexService;
import es.ucm.visavet.gbf.app.service.impl.XLSLoader;
import es.ucm.visavet.gbf.app.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


@Controller
@RequestMapping("/cargaxls")
public class CargaXLSController extends BaseController {

	
//	private static final Logger log = LoggerFactory.getLogger(CargaXLSController.class);

	public static final String ROOT = "folderupload/";

//	private final ResourceLoader resourceLoader;
	
//	@Autowired
//	private TopicRepository topicRepository;
//
	@Autowired
	private TestNewsIndexService testNews;
	
	@Autowired
	private ConfigurationServiceIO IOFolder;

//	public CargaXLSController() {
//		this.menu = "Sistema de Carga por XLS";
//	}
	
	@Autowired
	public CargaXLSController(ResourceLoader resourceLoader, ConfigurationServiceIO IOFolder) {
//		this.resourceLoader = resourceLoader;
		this.menu = LanguageLoad.getinstance().find("web/test/infointrotop");
		new File(IOFolder.getPathgen()+ROOT).mkdirs();
		this.IOFolder=IOFolder;
	}

	@RequestMapping("**")
	public String getAllLocations(Model model) {
		model.addAttribute("message", LanguageLoad.getinstance().find("web/test/infointro"));
		model.addAttribute("message2", LanguageLoad.getinstance().find("web/test/actralloadfiles"));
		
		List<FileEO> filesT=new ArrayList<FileEO>();
		
		filesT=getFiles();
		
		model.addAttribute("files", filesT);
		return "cargaxls";
	}
	
	
	@ModelAttribute("uploadxls")
	public String getUploadText() {
		return LanguageLoad.getinstance().find("web/test/uploadxls");
	}
	
	@ModelAttribute("xlsdate")
	public String getDateText() {
		return LanguageLoad.getinstance().find("web/test/xlsdate");
	}
	
	@ModelAttribute("xlsgenerate")
	public String getGeneraText() {
		return LanguageLoad.getinstance().find("web/test/xlsgenerate");
	}
	
	@ModelAttribute("xlsdelete")
	public String getBorraText() {
		return LanguageLoad.getinstance().find("web/test/xlsdelete");
	}
	
	@ModelAttribute("xlsname")
	public String getNameText() {
		return LanguageLoad.getinstance().find("web/test/xlsname");
	}

	@ModelAttribute("xlsalert")
	public String getAlertText() {
		return LanguageLoad.getinstance().find("web/test/xlsalert");
	}
	
	@ModelAttribute("xlsalertnodetect")
	public String getAlertDetectedText() {
		return LanguageLoad.getinstance().find("web/test/xlsalertnodetect");
	}
	
	@ModelAttribute("xlsriesgos")
	public String getRiskText() {
		return LanguageLoad.getinstance().find("web/test/xlsriesgos");
	}
	
	@ModelAttribute("xlsriesgosnodetect")
	public String getRiskDetectedText() {
		return LanguageLoad.getinstance().find("web/test/xlsriesgosnodetect");
	}
	
	@ModelAttribute("xlstotalscore")
	public String getRTotalScoreText() {
		return LanguageLoad.getinstance().find("web/test/xlstotalscore");
	}
	
	@ModelAttribute("xlslink")
	public String getLinkText() {
		return LanguageLoad.getinstance().find("web/test/xlslink");
	}
	
	@ModelAttribute("xlsdatepub")
	public String getDatePubText() {
		return LanguageLoad.getinstance().find("web/test/xlsdatepub");
	}
	
	@ModelAttribute("xlstotalsco")
	public String getTotalScoreText() {
		return LanguageLoad.getinstance().find("web/test/xlstotalsco");
	}
	
	@ModelAttribute("xlsquerysco")
	public String getQueryScoreText() {
		return LanguageLoad.getinstance().find("web/test/xlsquerysco");
	}
	
	@ModelAttribute("xlslocation")
	public String getLocationText() {
		return LanguageLoad.getinstance().find("web/test/xlslocation");
	}
	
	@ModelAttribute("xlsback")
	public String getBackText() {
		return LanguageLoad.getinstance().find("web/test/xlsback");
	}
	
	@RequestMapping(value = "/up", method = RequestMethod.POST)
	public String createLocation(@RequestParam("file") MultipartFile file,
			   RedirectAttributes redirectAttributes) {
		
		
		if (!file.isEmpty()) {
			try {
				//CAmbiar el nombre del archivo a uno magico con el timestamp
				String nanoT=Long.toString(System.nanoTime());
				
				Files.copy(file.getInputStream(), Paths.get(IOFolder.getPathgen()+ROOT,nanoT+"_"+ file.getOriginalFilename()));
				redirectAttributes.addFlashAttribute("message",
						"You successfully uploaded " + file.getOriginalFilename() + "!");
			} catch (IOException|RuntimeException e) {
				redirectAttributes.addFlashAttribute("message", LanguageLoad.getinstance().find("web/test/error/filedtoupload")+" " + file.getOriginalFilename() + " => " + e.getMessage());
			}
		} else {
			redirectAttributes.addFlashAttribute("message", LanguageLoad.getinstance().find("web/test/error/filedtoupload")+" " + file.getOriginalFilename() + " "+LanguageLoad.getinstance().find("web/test/error/filedtoupload2"));
		}

		return "redirect:/cargaxls";
	}
	
	@RequestMapping(value = "file/{id}", method = RequestMethod.GET)
	public String getFormUpdateLocation(Model model,
			@PathVariable("id") String FileNamed) {
		System.out.println(LanguageLoad.getinstance().find("web/test/info/procesing")+"... "+FileNamed);
		FileNamed=findmeFile(FileNamed);
		List<News> lista=new ArrayList<News>();
		try {
			File XLSFile=new File(IOFolder.getPathgen()+ROOT+FileNamed);
			
			try {
				lista=XLSLoader.readExcelFile(XLSFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			testNews.clearDirectory();
			testNews.indexNews(lista);
			testNews.runMe();

		
			
			List<NewsDetect> AlertsL = testNews.getSalidaA();
			List<NewsDetect> RisksL = testNews.getSalidaR();
			List<Location> LocationL = testNews.getSalidaO();
			
			List<AlertAbstract> AlertsALBas = testNews.getSalidaAA();
			List<AlertAbstract> RiskRLBas = testNews.getSalidaAR();
			
			List<AlertAbstractScored> AlertsAL = new ArrayList<AlertAbstractScored>();
			List<AlertAbstractScored> RiskRL = new ArrayList<AlertAbstractScored>();
			
			List<AlertAbstractScored> AlertsALFinal = new ArrayList<AlertAbstractScored>();
			List<AlertAbstractScored> RiskRLFinal = new ArrayList<AlertAbstractScored>();
			
			
			for (AlertAbstract alertAbstract : AlertsALBas) {

					AlertAbstractScored Anew=new AlertAbstractScored(alertAbstract);
					AlertsAL.add(Anew);

			}
			
			for (AlertAbstract alertAbstract : RiskRLBas) {

					AlertAbstractScored Anew=new AlertAbstractScored(alertAbstract);
					RiskRL.add(Anew);

			}
			
			
			HashMap<String, List<Location>> Cuadre_Noticia_Localizaciones=new HashMap<>();

			
			for (Location localizacion : LocationL) {
				for ( String newTittle : localizacion.getNews()) {
					List<Location> listaA=Cuadre_Noticia_Localizaciones.get(newTittle);
					if (listaA==null)
						listaA=new ArrayList<Location>();
					
					listaA.add(localizacion);
					Cuadre_Noticia_Localizaciones.put(newTittle, listaA);
				}
				
			}
			
			
			
			
			HashMap<Long,List<NewsDetect>> Alertset=new HashMap<Long,List<NewsDetect>>();
			HashMap<Long,List<NewsDetect>> Riskset=new HashMap<Long,List<NewsDetect>>();
			
			for (NewsDetect alertAbstract : AlertsL) {
				List<NewsDetect> aletList=Alertset.get(alertAbstract.getAlertDetect().getId());
				if (aletList==null)
					aletList=new ArrayList<NewsDetect>();
				
				aletList.add(alertAbstract);
				Alertset.put(alertAbstract.getAlertDetect().getId(), aletList);
			}
			
			for (NewsDetect alertAbstract : RisksL) {
				List<NewsDetect> riskList=Riskset.get(alertAbstract.getAlertDetect().getId());
				if (riskList==null)
					riskList=new ArrayList<NewsDetect>();
				
				riskList.add(alertAbstract);
				Riskset.put(alertAbstract.getAlertDetect().getId(), riskList);
			}
			
			
			
			java.util.Comparator<NewsDetect> comparatorFN= new java.util.Comparator<NewsDetect>() {
				
				public int compare(NewsDetect o1, NewsDetect o2) {
					
					return new Float(o2.getScore()).compareTo(new Float(o1.getScore()));
					
			};
			};
			
			
			for (AlertAbstractScored alertAbstract : AlertsAL) {
			//	System.out.println("Alert: " +alertAbstract.getTitle());
				List<NewsDetect> aletList=Alertset.get(alertAbstract.getId());
				float totalAlert = 0f;
				if (aletList!=null)
					{
					for (NewsDetect newsDetect : aletList) 
					{
						Integer Multy = newsDetect.getAlertDetect().getType().getValue()+1;
						List<Location> local=Cuadre_Noticia_Localizaciones.get(newsDetect.getLink());			
						
						if (local!=null&&!local.isEmpty())
							Multy=Multy*3;
						
						float calculo = Multy*newsDetect.getScore();
						newsDetect.setScore(calculo);
						newsDetect.setLocation(local);
						totalAlert=totalAlert+calculo;
						
					}
					Collections.sort(aletList, comparatorFN);
					alertAbstract.setListnewsDetect(aletList);	
					if (totalAlert!=0f)
						totalAlert=totalAlert/aletList.size();
					alertAbstract.setScore(totalAlert);
					AlertsALFinal.add(alertAbstract);
					
					}
				
			}

			
			for (AlertAbstractScored alertAbstract : RiskRL) {
				//System.out.println("Risk: " +alertAbstract.getTitle());
				List<NewsDetect> aletList=Riskset.get(alertAbstract.getId());
				float totalAlert = 0f;
				
				if (aletList!=null)
					{
					for (NewsDetect newsDetect : aletList)
					{
						Integer Multy = newsDetect.getAlertDetect().getType().getValue()+1;
						List<Location> local=Cuadre_Noticia_Localizaciones.get(newsDetect.getLink());	
						
						if (local!=null&&!local.isEmpty())
							Multy=Multy*3;
						float calculo = Multy*newsDetect.getScore();
						newsDetect.setScore(calculo);
						newsDetect.setLocation(local);
						totalAlert=totalAlert+calculo;
						
					}
					Collections.sort(aletList, comparatorFN);
					alertAbstract.setListnewsDetect(aletList);
					if (totalAlert!=0f)
						totalAlert=totalAlert/aletList.size();
					alertAbstract.setScore(totalAlert);
					RiskRLFinal.add(alertAbstract);
					
					}
			}
			
			
			
			
//			model.addAttribute("alerts", AlertsL);
//			model.addAttribute("risks", RisksL);
			
			
			
			
			
			java.util.Comparator<AlertAbstractScored> comparatorF= new java.util.Comparator<AlertAbstractScored>() {
				
				public int compare(AlertAbstractScored o1, AlertAbstractScored o2) {
					
					return new Float(o2.getScore()).compareTo(new Float(o1.getScore()));
					
			};
			};
			
			Collections.sort(AlertsALFinal, comparatorF);
			Collections.sort(RiskRLFinal, comparatorF);
			
			model.addAttribute("alertsT", AlertsALFinal);
			model.addAttribute("risksT", RiskRLFinal);
		} catch (Exception e) {
			System.out.println(LanguageLoad.getinstance().find("web/test/error/errorintest"));
			e.printStackTrace();
		}
		
		model.addAttribute("message3",LanguageLoad.getinstance().find("web/test/inforesult"));
		
		
		return "cargaxlsresult";
	}
	
	@RequestMapping(value = "filedel/{id}", method = RequestMethod.GET)
	public String getFormUpdateLocationDel(Model model,
			@PathVariable("id") String FileNamed) {
		System.out.println(LanguageLoad.getinstance().find("web/test/info/deleting")+"... "+FileNamed);
		FileNamed=findmeFile(FileNamed);
		try {
			File XLSFile=new File(IOFolder.getPathgen()+ROOT+FileNamed);
			XLSFile.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("message", LanguageLoad.getinstance().find("web/test/infointro"));
		model.addAttribute("message2", LanguageLoad.getinstance().find("web/test/actralloadfiles"));

		
		
		
	List<FileEO> filesT=new ArrayList<FileEO>();
	filesT=getFiles();
		
		
		model.addAttribute("files", filesT);
		
		return "cargaxls";
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
			
			 
			 
			if (ShowNameL.length>1)
				ShowName=ShowNameL[1];
			  
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
	


}