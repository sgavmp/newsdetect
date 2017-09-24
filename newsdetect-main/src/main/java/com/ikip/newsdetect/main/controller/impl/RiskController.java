package com.ikip.newsdetect.main.controller.impl;

import com.google.common.collect.Maps;
import com.neovisionaries.i18n.CountryCode;
import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.Alert;
import es.ucm.visavet.gbf.app.domain.Location;
import es.ucm.visavet.gbf.app.domain.NewsDetect;
import es.ucm.visavet.gbf.app.domain.Risk;
import es.ucm.visavet.gbf.app.domain.topic.TopicManager;
import es.ucm.visavet.gbf.app.repository.LocationRepository;
import es.ucm.visavet.gbf.app.repository.NewsDetectRepository;
import es.ucm.visavet.gbf.app.repository.StatisticsRepository;
import es.ucm.visavet.gbf.app.service.NewsIndexService;
import es.ucm.visavet.gbf.app.service.impl.RiskServiceImpl;
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
@RequestMapping("/risks")
public class RiskController extends BaseController {
		
	
	private static String FOLDER = "risks";
	
	@Autowired
	private RiskServiceImpl service;
	
	@Autowired
	private NewsDetectRepository repository;
	
	@Autowired
	private NewsIndexService newsIndexService;
	
	@Autowired
	private TopicManager topicManager;
	
	@Autowired
	private StatisticsRepository statisticsRepository;
	
	@Autowired
	private LocationRepository locationRepository;
	
	public RiskController() {
		this.menu = LanguageLoad.getinstance().find("web/risk/otherrisk");
	}
	
	@ModelAttribute("alertname")
	public String getRiskNameText() {
		return LanguageLoad.getinstance().find("web/risk/alertname");
	}
	
	@ModelAttribute("alertnameoriginal")
	public String getRiskDefNameText() {
		return LanguageLoad.getinstance().find("web/risk/alertnameoriginal");
	}

	
	@ModelAttribute("alertcreate")
	public String getRisksCreateText() {
		return LanguageLoad.getinstance().find("web/risk/alertcreate");
	}
	
	@ModelAttribute("alertquerylevel")
	public String getAlertQueryLevelText() {
		return LanguageLoad.getinstance().find("web/risk/alertquerylevel");
	}
	
	@ModelAttribute("alertsuredelete")
	public String getAlertsureDeleteText() {
		return LanguageLoad.getinstance().find("web/risk/alertsuredelete");
	}
	
	@ModelAttribute("alertsureresolvealert")
	public String getAlertSureToResolText() {
		return LanguageLoad.getinstance().find("web/risk/alertsureresolvealert");
	}
	
	@ModelAttribute("alertsurereset")
	public String getAlertSureResetText() {
		return LanguageLoad.getinstance().find("web/risk/alertsurereset");
	}
	
	@ModelAttribute("alertnalerts")
	public String getAlertNElemText() {
		return LanguageLoad.getinstance().find("web/risk/alertnalerts");
	}
	
	
	@ModelAttribute("alertedit")
	public String getRisksEditText() {
		return LanguageLoad.getinstance().find("web/risk/alertedit");
	}
	
	@ModelAttribute("risksnorecente")
	public String getRisksNoRecientesText() {
		return LanguageLoad.getinstance().find("web/risk/risksnorecente");
	}
	
	@ModelAttribute("chartittle")
	public String getRisksstaTitText() {
		return LanguageLoad.getinstance().find("web/risk/chartittle");
	}
	
	@ModelAttribute("chartdetected")
	public String getRisksstasubTitText() {
		return LanguageLoad.getinstance().find("web/risk/chartdetected");
	}
	
	@ModelAttribute("alertaddnewalert")
	public String getWebsiteAddText() {
		return LanguageLoad.getinstance().find("web/risk/alertaddnewalert");
	}
	
	@ModelAttribute("websitesuredeleteweb")
	public String getWebsiteSureDeleteText() {
		return LanguageLoad.getinstance().find("web/risk/websitesuredeleteweb");
	}
	
	@ModelAttribute("allCountriesAfects")
	public Map<String, String> getAllCountriesAfects() {
		Map<String, String> listCountries = new HashMap<String, String>();
		
		List<Location> listLocation = (List<Location>) locationRepository.findAllAfectRisk();
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
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgDay(today.toString(),5);
		return lista;
	}
	
	@SuppressWarnings("deprecation")
	@ModelAttribute("alertsScoreLast2Days")
	public List<Object[]> getAllRisksLast2days() {
		java.util.Date now = new Date(System.currentTimeMillis());
		Calendar c = Calendar.getInstance(); 
		c.setTime(now); 
		c.add(Calendar.DATE, -2);
		Date today = new Date(c.getTime().getTime());
		Date today2 = new Date(today.getYear(), today.getMonth(), today.getDate());
		Date now2 = new Date(now.getYear(), now.getMonth(), now.getDate());		
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgBetween(now2.toString(),today2.toString(),5);
		return lista;
	}
	
	
	@SuppressWarnings("deprecation")
	@ModelAttribute("alertsScoreWeek")
	public List<Object[]> getAllAlertsWeek() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate()-7);
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgBetween(today.toString(),past.toString(),7);
		return lista;
	}
	
	@SuppressWarnings("deprecation")
	@ModelAttribute("alertsScoreMonth")
	public List<Object[]> getAllAlertsMonth() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate()-31);
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgBetween(today.toString(),past.toString(),7);
		return lista;
	}
	
	@ModelAttribute("folder")
	public String getFolder() {
		return FOLDER;
	}
	
	
	@ModelAttribute("riskrecente")
	public String getRecentRiskText() {
		return LanguageLoad.getinstance().find("web/risk/riskrecente");
	}
	
	@RequestMapping("/get/{idAlert}")
	public String getAlert(Model model, @PathVariable("idAlert") Risk alert) {
//		alert = service.setNewsLocation(alert);
		Risk alertActive = new Risk();
		alertActive.setId(alert.getId());
		alertActive.setTitle(alert.getTitle());
		alertActive.setNewsDetect(new HashSet<NewsDetect>());
		Risk alertHistory = new Risk();
		alertHistory.setId(alert.getId());
		alertHistory.setTitle(alert.getTitle());
		alertHistory.setNewsDetect(new HashSet<NewsDetect>());
		Risk alertFalse = new Risk();
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
	public String getMainRisks(Model model) {
		return "/alerts/alerts";
	}
	
	@RequestMapping("/list")
	public String getAllLocations(Model model) {
		model.addAttribute("allWords", service.getAllAlert());
		model.addAttribute("term", new Alert());
		return "/alerts/words";
	}
	
	@RequestMapping(value="/create", method= RequestMethod.POST)
	public String createLocation(Model model, RedirectAttributes redirectAttributes, Risk wordFilter, BindingResult result) throws UnsupportedEncodingException {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/risk/error/formerror"));
            return "/alerts/formAlert";
        }
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle()+"test", topicManager),
				new InputStreamReader(new ByteArrayInputStream(wordFilter.getWords().getBytes()),"UTF-8"));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/risk/error/topicnotexist1")+ " " + e.getTopic() + " " +LanguageLoad.getinstance().find("web/risk/error/topicnotexist2"));
			return "/alerts/formAlert";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/risk/error/cyclicerror") + " "+ e.toString());
			return "/alerts/formAlert";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/risk/error/errorvalidatingg"));
			return "/alerts/formAlert";
		}
		try {
			wordFilter = service.create(wordFilter);
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error",LanguageLoad.getinstance().find("web/risk/error/errordetectingrisks"));
			return "redirect:/alerts/get/"+wordFilter.getId();
		}
		redirectAttributes.addFlashAttribute("info",LanguageLoad.getinstance().find("web/risk/info/riksaddcorrecttofilter"));
		return "redirect:/risks/get/"+wordFilter.getId();
	}
	
	@RequestMapping(value = "/create", method= RequestMethod.GET)
	public String getFormCreate(Model model) {
		model.addAttribute("term", new Risk());
		return "/alerts/formAlert";
	}
	
	@RequestMapping(value = "/get/{id}/edit", method= RequestMethod.GET)
	public String getFormUpdateLocation(Model model, @PathVariable("id") Risk word) {
		model.addAttribute("term",word);
		return "/alerts/formAlert";
	}
	
	@RequestMapping(value="/get/{id}/edit", method= RequestMethod.POST)
	public String updateLocation(Model model, RedirectAttributes redirectAttributes, Risk wordFilter, @PathVariable("id") Risk before, BindingResult result) throws UnsupportedEncodingException {
		model.addAttribute("term", wordFilter);
		if (result.hasErrors()) {
        	model.addAttribute("error",LanguageLoad.getinstance().find("web/risk/error/formerror"));
            return "/alerts/formAlert";
        }
		TopicValidator validator = new TopicValidator(
				new TopicValidatorSemantics(wordFilter.getTitle(), topicManager),
				new InputStreamReader(new ByteArrayInputStream(wordFilter.getWords().getBytes()),"UTF-8"));
		try {
			validator.topic();
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/risk/error/topicnotexist1")+ " " + e.getTopic() + " " +LanguageLoad.getinstance().find("web/risk/error/topicnotexist2"));
			return "/alerts/formAlert";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/risk/error/cyclicerror") + " "+ e.toString());
			return "/alerts/formAlert";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/risk/error/errorvalidatingg"));
			return "/alerts/formAlert";
		}
		service.update((Risk)wordFilter.bind(before));
		redirectAttributes.addFlashAttribute("info",LanguageLoad.getinstance().find("web/risk/info/riksupdatecorrecttofilter"));
		return "redirect:/risks/get/"+wordFilter.getId();
	}
	
	@RequestMapping(value = "/get/{id}/reset", method= RequestMethod.GET)
	public String resetNewsByFeed(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Risk word) {
		try {
			newsIndexService.resetAlert(word);
		} catch (IOException e) {
			redirectAttributes.addFlashAttribute("error",LanguageLoad.getinstance().find("web/risk/error/errordetctingrisks"));
			return "redirect:/get/"+word.getId();
		}
		redirectAttributes.addFlashAttribute("info",LanguageLoad.getinstance().find("web/risk/info/resetcorrect"));
		return "redirect:/risks/get/"+word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/remove", method= RequestMethod.GET)
	public String updateNewsByFeed(Model model, @PathVariable("id") Risk word) {
		service.remove(word);
		return "redirect:/risks/list";
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/history", method= RequestMethod.GET)
	public String setNewsDetectHistory(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Risk word, @PathVariable("idNews") NewsDetect news) {
		news.setHistory(true);
		news.setFalPositive(false);
		repository.save(news);
		redirectAttributes.addFlashAttribute("info", LanguageLoad.getinstance().find("web/risk/info/wasmarkedaspass"));
		return "redirect:/risks/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/false", method= RequestMethod.GET)
	public String setNewsDetectFalse(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Risk word, @PathVariable("idNews") NewsDetect news) {
		news.setHistory(false);
		news.setFalPositive(true);
		repository.save(news);
		redirectAttributes.addFlashAttribute("info", LanguageLoad.getinstance().find("web/risk/info/wasmarkedasfalsepositive") );
		return "redirect:/risks/get/" + word.getId();
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/active", method= RequestMethod.GET)
	public String setNewsDetectActive(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Risk word, @PathVariable("idNews") NewsDetect news) {
		news.setHistory(false);
		news.setFalPositive(false);
		repository.save(news);
		redirectAttributes.addFlashAttribute("info", LanguageLoad.getinstance().find("web/risk/info/thenewwasmarkedasactive"));
		return "redirect:/risks/get/" + word.getId();
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/stats", method= RequestMethod.GET)
	public Map<String,Map<Object,Object[]>> getAlertsStat() {
		Map<String,Map<Object,Object[]>> alerts = Maps.newLinkedHashMap();
		for (Risk alert : service.getAllAlert()) {
			alerts.put(alert.getTitle(), new HashMap<Object, Object[]>());	
		}
		for (Object[] ob : statisticsRepository.getRisktStats()) {
			alerts.get(ob[1]).put(ob[2].toString(),ob);
		}
		return alerts;
	}
	
	@ResponseBody
	@RequestMapping(value = "/ajax/stats/{alertId}", method= RequestMethod.GET)
	public Map<String,Map<Object,Object[]>> getAlertsStat(@PathVariable("alertId") Risk alert) {
		Map<String,Map<Object,Object[]>> alerts = Maps.newLinkedHashMap();
		alerts.put(alert.getTitle(), new HashMap<Object, Object[]>());	
List<Object[]> alertStats = statisticsRepository.getRisktStats(alert.getId().toString());
		
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
	@RequestMapping(value = "/ajax/stats2/{alertId}", method= RequestMethod.GET)
	public Map<String,Map<Object,Object[]>> getAlertsStat2(@PathVariable("alertId") Risk alert) {
		
		
		Map<String,Map<Object,Object[]>> alerts = Maps.newLinkedHashMap();
		alerts.put(alert.getTitle(), new HashMap<Object, Object[]>());	
		
		List<Object[]> alertStats = statisticsRepository.getRisktStats(alert.getId().toString());
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
			double result=0;
			if (esta!=null)
				result=esta.predict(esta.getN()+1);
			
			List<Object> aux=new ArrayList<Object>();
			aux.add(days[i]);
			aux.add(dia_alertfin.get(i+1));
			aux.add(result);
			
			alerts.get(alert.getTitle()).put(days[i],aux.toArray());
		}
		
		//Domingo
		
		SimpleRegression esta = dia_alert_esta.get(1);
		double result=0;
		if (esta!=null)
			result=esta.predict(esta.getN()+1);
		
		List<Object> aux=new ArrayList<Object>();
		aux.add(days[0]);
		aux.add(dia_alert.get(1));
		aux.add(result);
		
		alerts.get(alert.getTitle()).put(days[0],aux.toArray());
		
		
		return alerts;
		
		
	
		
		
	}
	
	@RequestMapping(value = "/get/{id}/news/{idNews}/remove", method= RequestMethod.GET)
	public String setNewsDetectRemove(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Risk word, @PathVariable("idNews") NewsDetect news) {
		boolean exist = true;
		do {
			exist = word.getNewsDetect().remove(news);
		} while (exist);
		service.update(word);
		repository.delete(news.getId());
		redirectAttributes.addFlashAttribute("info", LanguageLoad.getinstance().find("web/risk/info/thenewwasdeleted"));
		return "redirect:/risks/get/" + word.getId();
	}
	
}
