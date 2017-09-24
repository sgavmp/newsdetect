package com.ikip.newsdetect.main.controller.impl;

import com.google.common.collect.Maps;
import com.neovisionaries.i18n.CountryCode;
import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.Alert;
import es.ucm.visavet.gbf.app.domain.Location;
import es.ucm.visavet.gbf.app.domain.NewsDetect;
import es.ucm.visavet.gbf.app.domain.topic.TopicManager;
import es.ucm.visavet.gbf.app.repository.LocationRepository;
import es.ucm.visavet.gbf.app.repository.NewsDetectRepository;
import es.ucm.visavet.gbf.app.repository.StatisticsRepository;
import es.ucm.visavet.gbf.app.service.NewsIndexService;
import es.ucm.visavet.gbf.app.service.impl.AlertServiceImpl;
import es.ucm.visavet.gbf.app.web.controller.BaseController;
import es.ucm.visavet.gbf.topics.validator.*;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Controller
@RequestMapping("/alerts")
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
	private StatisticsRepository statisticsRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	public AlertController() {
		this.menu = LanguageLoad.getinstance().find("web/alert/sanitarualerts");
	}
	
	@ModelAttribute("folder")
	public String getFolder() {
		return FOLDER;
	}
	
	
	@ModelAttribute("alertname")
	public String getAlertNameText() {
		return LanguageLoad.getinstance().find("web/alert/alertname");
	}
	
	@ModelAttribute("alertnameoriginal")
	public String getAlerDefNameText() {
		return LanguageLoad.getinstance().find("web/alert/alertnameoriginal");
	}

	@ModelAttribute("alertsureresolvealert")
	public String getAlertSureToResolText() {
		return LanguageLoad.getinstance().find("web/alert/alertsureresolvealert");
	}
	
	@ModelAttribute("alertnalerts")
	public String getAlertNElemText() {
		return LanguageLoad.getinstance().find("web/alert/alertnalerts");
	}
	
	
	@ModelAttribute("alertsurereset")
	public String getAlertSureResetText() {
		return LanguageLoad.getinstance().find("web/alert/alertsurereset");
	}
	
	@ModelAttribute("alertquerylevel")
	public String getAlertQueryLevelText() {
		return LanguageLoad.getinstance().find("web/alert/alertquerylevel");
	}
	
	@ModelAttribute("alertcreate")
	public String getAlertCreateText() {
		return LanguageLoad.getinstance().find("web/alert/alertcreate");
	}
	
	@ModelAttribute("alertsuredelete")
	public String getAlertsureDeleteText() {
		return LanguageLoad.getinstance().find("web/alert/alertsuredelete");
	}
	
	@ModelAttribute("alertedit")
	public String getAlertEditText() {
		return LanguageLoad.getinstance().find("web/alert/alertedit");
	}

	@ModelAttribute("alertasrecente")
	public String getRecentAlertText() {
		return LanguageLoad.getinstance().find("web/alert/alertasrecente");
	}
	

	
	
	@ModelAttribute("alertasnorecente")
	public String getAlertNoRecientesText() {
		return LanguageLoad.getinstance().find("web/alert/alertasnorecente");
	}
	
	@ModelAttribute("chartittle")
	public String getalertstaTitText() {
		return LanguageLoad.getinstance().find("web/alert/chartittle");
	}
	
	@ModelAttribute("chartdetected")
	public String getalertstasubTitText() {
		return LanguageLoad.getinstance().find("web/alert/chartdetected");
	}
	

	@ModelAttribute("alertaddnewalert")
	public String getWebsiteAddText() {
		return LanguageLoad.getinstance().find("web/alert/alertaddnewalert");
	}
	
	@ModelAttribute("websitesuredeleteweb")
	public String getWebsiteSureDeleteText() {
		return LanguageLoad.getinstance().find("web/alert/websitesuredeleteweb");
	}
	
	@ModelAttribute("allCountriesAfects")
	public Map<String, String> getAllCountriesAfects() {
		Map<String, String> listCountries = new HashMap<String, String>();
		
		List<Location> listLocation = (List<Location>) locationRepository.findAllAfectAlert();
		for (Location loc : listLocation) {
			listCountries.put(
					CountryCode.getByCode(loc.getCountry().name())
							.getAlpha3(), "rgba(255,87,34,1)");
		}
		listCountries.put("defaultFill", "rgba(200,200,200,1)");
		
		return listCountries;
	}
	
	@SuppressWarnings("deprecation")
	@ModelAttribute("alertsScoreToday")
	public List<Object[]> getAllAlertsToday() {
		java.util.Date now = new Date(System.currentTimeMillis());
		Date now2 = new Date(now.getYear(), now.getMonth(), now.getDate());		
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgDay(now2.toString(), 5);
		return lista;
	}
	
	@SuppressWarnings("deprecation")
	@ModelAttribute("alertsScoreLast2Days")
	public List<Object[]> getAllAlertsLast2days() {
		java.util.Date now = new Date(System.currentTimeMillis());
		Calendar c = Calendar.getInstance(); 
		c.setTime(now); 
		c.add(Calendar.DATE, -2);
		Date today = new Date(c.getTime().getTime());
		Date today2 = new Date(today.getYear(), today.getMonth(), today.getDate());
		Date now2 = new Date(now.getYear(), now.getMonth(), now.getDate());		
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgBetween(now2.toString(),today2.toString(),5);
		return lista;
	}
	
	@SuppressWarnings("deprecation")
	@ModelAttribute("alertsScoreWeek")
	public List<Object[]> getAllAlertsWeek() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate()-7);
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgBetween(today.toString(),past.toString(),7);
		return lista;
	}
	
	@SuppressWarnings("deprecation")
	@ModelAttribute("alertsScoreMonth")
	public List<Object[]> getAllAlertsMonth() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate()-31);
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgBetween(today.toString(),past.toString(),7);
		return lista;
	}
	
	@SuppressWarnings("deprecation")
	@ModelAttribute("alertsTrendsMonth")
	public List<Object[]> getAllAlertsTrends() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate()-31);
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgBetween(today.toString(),past.toString(),7);
		return lista;
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
				new InputStreamReader(new ByteArrayInputStream(wordFilter.getWords().getBytes()),"UTF-8"));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/base/error/topicnotexist") +" "+ e.getTopic()
			+ LanguageLoad.getinstance().find("web/base/error/topicnotexist2"));
			return "/alerts/formAlert";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error",LanguageLoad.getinstance().find("web/base/error/topicciclico") + " "+ e.toString());
			return "/alerts/formAlert";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/base/error/validatingquerry") + " "+ e.getMessage());
			return "/alerts/formAlert";
		}
		try {
			service.create(wordFilter);
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error",LanguageLoad.getinstance().find("web/alert/error/detctingalerts"));
			return "redirect:/alerts/get/"+wordFilter.getId();
		}
		redirectAttributes.addFlashAttribute("info",LanguageLoad.getinstance().find("web/alert/info/correctaddfilter"));
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
        	model.addAttribute("error",LanguageLoad.getinstance().find("web/alert/error/formerror"));
            return "/alerts/formAlert";
        }
        model.addAttribute("term", wordFilter);
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle(), topicManager),
				new InputStreamReader(new ByteArrayInputStream(wordFilter.getWords().getBytes()),"UTF-8"));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/base/error/topicnotexist") +" "+ e.getTopic()
			+ LanguageLoad.getinstance().find("web/base/error/topicnotexist2"));
			return "/alerts/formAlert";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error",LanguageLoad.getinstance().find("web/base/error/topicciclico") + " "+  e.toString());
			return "/alerts/formAlert";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error",LanguageLoad.getinstance().find("web/base/error/validatingquerry") + " "+e.toString() );
			return "/alerts/formAlert";
		}
		wordFilter = service.update((Alert)wordFilter.bind(before));
		redirectAttributes.addFlashAttribute("info",LanguageLoad.getinstance().find("web/alert/info/correctupdatefilter"));
		return "redirect:/alerts/get/"+wordFilter.getId();
	}
	
	@RequestMapping(value = "/get/{id}/reset", method= RequestMethod.GET)
	public String resetNewsByFeed(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Alert word) {
		try {
			newsIndexService.resetAlert(word);
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error",LanguageLoad.getinstance().find("web/alert/error/detctingalerts"));
			return "redirect:/alerts/get/"+word.getId();
		}
		redirectAttributes.addFlashAttribute("info",LanguageLoad.getinstance().find("web/alert/info/correctresetalerts"));
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
		alertActive.setNewsDetect(new HashSet<NewsDetect>());
		Alert alertHistory = new Alert();
		alertHistory.setId(alert.getId());
		alertHistory.setTitle(alert.getTitle());
		alertHistory.setNewsDetect(new HashSet<NewsDetect>());
		Alert alertFalse = new Alert();
		alertFalse.setId(alert.getId());
		alertFalse.setTitle(alert.getTitle());
		alertFalse.setNewsDetect(new HashSet<NewsDetect>());
		for (NewsDetect news : alert.getNewsDetect()) {
			if (news.getFalPositive()) {
				alertFalse.getNewsDetect().add(news);
			} else if (news.getHistory()) {
				alertHistory.getNewsDetect().add(news);
			} else {
				alertActive.getNewsDetect().add(news);
			}
		}
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
	public String setNewsDetectHistory(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Alert word, @PathVariable("idNews") NewsDetect news) {
		news.setHistory(true);
		news.setFalPositive(false);
		repository.save(news);
		redirectAttributes.addFlashAttribute("info", LanguageLoad.getinstance().find("web/alert/info/passedalert") );
		return "redirect:/alerts/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/false", method= RequestMethod.GET)
	public String setNewsDetectFalse(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Alert word, @PathVariable("idNews") NewsDetect news) {
		news.setHistory(false);
		news.setFalPositive(true);
		repository.save(news);
		redirectAttributes.addFlashAttribute("info", LanguageLoad.getinstance().find("web/alert/info/falsealert"));
		return "redirect:/alerts/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/active", method= RequestMethod.GET)
	public String setNewsDetectActive(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Alert word, @PathVariable("idNews") NewsDetect news) {
		news.setHistory(false);
		news.setFalPositive(false);
		repository.save(news);
		redirectAttributes.addFlashAttribute("info", LanguageLoad.getinstance().find("web/alert/info/actuivealert"));
		return "redirect:/alerts/get/" + word.getId();
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/stats", method= RequestMethod.GET)
	public Map<String,Map<Object,Object[]>> getAlertsStat() {
		Map<String,Map<Object,Object[]>> alerts = Maps.newLinkedHashMap();
		for (Alert alert : service.getAllAlert()) {
			alerts.put(alert.getTitle(), new HashMap<Object, Object[]>());	
		}
		for (Object[] ob : statisticsRepository.getAlertStats()) {
			alerts.get(ob[1]).put(ob[2].toString(),ob);
		}
		return alerts;
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/stats/{id}", method= RequestMethod.GET)
	public Map<String,Map<Object,Object[]>> getAlertsStat(@PathVariable("id") Alert alert) {
		Map<String,Map<Object,Object[]>> alerts = Maps.newLinkedHashMap();
		alerts.put(alert.getTitle(), new HashMap<Object, Object[]>());	
		List<Object[]> alertStats = statisticsRepository.getAlertStats(alert.getId().toString());
		
		SimpleRegression simpleRegression = new SimpleRegression(true);
		SimpleRegression simpleRegression2 = new SimpleRegression(true);
		
		
		ArrayList<Object> obn=null;
		int last1=0;
		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.CEILING);
		
		
		for (int i = 0; i < alertStats.size(); i++) {
			Object[] ob = alertStats.get(i);
			
			
			
			
			Double Y = (double)i;
			Double X = new Double(ob[4].toString());
			Double X2 = new Double(ob[3].toString());
			
			
			
			
			double statidst = X;
			double statidst2 = X2;
			if (i!=0&&i!=1)
				{
				statidst=simpleRegression.predict(i);
				statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
				if (statidst<0d)
					statidst=0d;

				statidst2=simpleRegression2.predict(i);
				statidst2=Double.parseDouble(df.format(statidst2).replace(",", "."));
				if (statidst2<0d)
					statidst2=0d;
				}
			
			
			obn = new ArrayList<Object>();
			for (Object object : ob) {
				obn.add(object);
			}
			
			obn.add(statidst);
			obn.add(statidst2);
			
			
			simpleRegression.addData(Y,X);
			simpleRegression2.addData(Y,X2);
			
			alerts.get(ob[1]).put(ob[2].toString(),obn.toArray());
			
			last1=i;
			
		}
		
		
		java.util.Date Hoy=new java.util.Date();
		Calendar cal = Calendar.getInstance();
        cal.setTime(Hoy);
        cal.add(Calendar.MONTH, 1); 
        java.util.Date Mes_1=cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String myDate = format.format(Mes_1);
        
        double statidst = 0;
		double statidst2 = 0;
		if (last1!=0&&last1!=1)
			{
			statidst=simpleRegression.predict(last1);
			statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
			if (statidst<0d)
				statidst=0d;

			statidst2=simpleRegression2.predict(last1);
			statidst2=Double.parseDouble(df.format(statidst2).replace(",", "."));
			if (statidst2<0d)
				statidst2=0d;
			}
		
		ArrayList<Object> obnx = new ArrayList<Object>();
		for (Object object : obn) {
			obnx.add(object);
		}
		
		obnx.set(2, myDate);
		obnx.set(7, statidst);
		obnx.set(8, statidst2);
        
		alerts.get(alert.getTitle()).put(myDate,obnx.toArray());
		
		return alerts;
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/stats2/{id}", method= RequestMethod.GET)
	public Map<String,Map<Object,Object[]>> getAlertsStat2(@PathVariable("id") Alert alert) {
		
		
		Map<String,Map<Object,Object[]>> alerts = Maps.newLinkedHashMap();
		alerts.put(alert.getTitle(), new HashMap<Object, Object[]>());	
		
		List<Object[]> alertStats = statisticsRepository.getAlertStats(alert.getId().toString());
		HashMap<Integer, HashMap<Date, Integer>> dia_alert=new HashMap<Integer, HashMap<Date, Integer>>();
		HashMap<Integer, SimpleRegression> dia_alert_esta=new HashMap<Integer, SimpleRegression>();

		for (int i = 1; i <= 7; i++) 
			dia_alert_esta.put(i, new SimpleRegression(true));
		
		for (Object[] objects : alertStats) {
			try {
				Calendar c = Calendar.getInstance();
				Date fechas=(Date)objects[2];
				c.setTime((Date)objects[2]);
				Integer Listtoday=Integer.parseInt(objects[3].toString());
				
				int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
				
				HashMap<Date, Integer> ac=dia_alert.get(dayOfWeek);
				
				if (ac==null)
					ac=new HashMap<Date, Integer>();
				
				ac.put(fechas, Listtoday);
	
				dia_alert.put(dayOfWeek, ac);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		HashMap<Integer, Double> dia_alertfin = new HashMap<Integer, Double>();
		
		for (Entry<Integer, HashMap<Date, Integer>> objects : dia_alert.entrySet()) {
			Integer diaS = objects.getKey();
			HashMap<Date, Integer> hashobj = objects.getValue();
			Set<Date> fechas=hashobj.keySet();
			ArrayList<Date> fechasO = new ArrayList<Date>();
			fechasO.addAll(fechas);
			Collections.sort(fechasO);
			
			double sum=0;
			
			SimpleRegression sta = dia_alert_esta.get(diaS);
			
			for (int i = 0; i < fechasO.size(); i++) {
				Integer valor = hashobj.get(fechasO.get(i));
				sum=sum+valor;
				
				
				Double X = (double)i;
				 Double Y = new Double(valor);
				
				 sta.addData(X,Y);
				 
			}
			
			dia_alert_esta.put(diaS, sta);
			
			if (sum!=0)
				sum=sum/fechasO.size();
			
			dia_alertfin.put(diaS, sum);
		}
		
		
	    final String[] days = {LanguageLoad.getinstance().find("web/base/sunday"), LanguageLoad.getinstance().find("web/base/monday"), LanguageLoad.getinstance().find("web/base/tuesday"), LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"), LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday")};

	    //menos domingo que es el 1
		for (int i = 1; i < 7; i++) {
			SimpleRegression esta = dia_alert_esta.get(i+1);
			Double result=0d;
			if (esta!=null)
				result=esta.predict(esta.getN()+1);
			
			if (result<0||result.isNaN())
				result=0d;
			
			Double valore = dia_alertfin.get(i+1);
			
			if (valore==null||valore<0||valore.isNaN())
				valore=0d;
			
			List<Object> aux=new ArrayList<Object>();
			aux.add(days[i]);
			aux.add(valore);
			aux.add(result);
			
			alerts.get(alert.getTitle()).put(days[i],aux.toArray());
		}
		
		//Domingo
		
		SimpleRegression esta = dia_alert_esta.get(1);
		Double result=0d;
		
		if (esta!=null)
			result=esta.predict(esta.getN()+1);
		
		if (result<0||result.isNaN())
			result=0d;
		
		Double valore = dia_alertfin.get(1);
		
		if (valore==null||valore<0||valore.isNaN())
			valore=0d;
		
		List<Object> aux=new ArrayList<Object>();
		aux.add(days[0]);
		aux.add(valore);
		aux.add(result);
		
		alerts.get(alert.getTitle()).put(days[0],aux.toArray());
		
		
		return alerts;
		
		
	
		
		
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/remove", method= RequestMethod.GET)
	public String setNewsDetectRemove(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Alert word, @PathVariable("idNews") NewsDetect news) {
		boolean exist = true;
		do {
			exist = word.getNewsDetect().remove(news);
		} while (exist);
		service.update(word);
		repository.delete(news.getId());
		redirectAttributes.addFlashAttribute("info",  LanguageLoad.getinstance().find("web/alert/info/deletealert"));
		return "redirect:/alerts/get/" + word.getId();
	}
	
}
