package com.ikip.newsdetect.main.controller;

import com.google.common.collect.Lists;
import com.neovisionaries.i18n.CountryCode;
import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.*;
import es.ucm.visavet.gbf.app.repository.FeedLogRepository;
import es.ucm.visavet.gbf.app.repository.LocationRepository;
import es.ucm.visavet.gbf.app.repository.StatisticsRepository;
import es.ucm.visavet.gbf.app.service.*;
import es.ucm.visavet.gbf.app.service.impl.AlertServiceImpl;
import es.ucm.visavet.gbf.app.service.impl.RiskServiceImpl;
import es.ucm.visavet.gbf.app.service.impl.SpecializedClassicSimilarity;
import es.ucm.visavet.gbf.app.statistics.TrendLine;
import es.ucm.visavet.gbf.app.statistics.impl.PolyTrendLine;
import es.ucm.visavet.gbf.topics.validator.CyclicDependencyException;
import es.ucm.visavet.gbf.topics.validator.ParseException;
import es.ucm.visavet.gbf.topics.validator.TokenMgrError;
import es.ucm.visavet.gbf.topics.validator.TopicDoesNotExistsException;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlException;
import org.apache.commons.jexl3.MapContext;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.webjars.WebJarAssetLocator;

import javax.servlet.http.HttpServletRequest;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController extends BaseController {

	private static long ONE_DAY = 86400000;

	private WebJarAssetLocator assetLocator;

	@Autowired
	private AlertServiceImpl service;

	@Autowired
	private RiskServiceImpl serviceRisk;

	@Autowired
	private StatisticsRepository statisticsRepository;
	
	@Autowired
	private FeedLogRepository feedLogRepository;

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private ConfiguracionService configuracionService;

	@Autowired
	private ConfiguracionService configuracion;

	@Autowired
	private NewsIndexService newsIndexService;

	@Autowired
	private FeedService feedService;
	
	@Autowired
	private FeedScraping feedScraping;
	
	@Autowired
	private ConfigurationServiceIO IOFolder;
	
	
	@Autowired
	public MainController(ConfigurationServiceIO IOFolder) {
		this.assetLocator = new WebJarAssetLocator();
		this.menu = LanguageLoad.getinstance().find("web/base/controlpanel");
		this.IOFolder=IOFolder;
	}
	
	@ModelAttribute("queryform")
	public String getQueryFormText() {
		return LanguageLoad.getinstance().find("web/query/queryform");
	}
	
	@ModelAttribute("queryforminfo")
	public String getQueryFormInfoText() {
		return LanguageLoad.getinstance().find("web/query/queryforminfo");
	}
	
	@ModelAttribute("startdate")
	public String getStartDateText() {
		return LanguageLoad.getinstance().find("web/query/startdate");
	}
	
	@ModelAttribute("enddate")
	public String getEndDateText() {
		return LanguageLoad.getinstance().find("web/query/enddate");
	}
	
	@ModelAttribute("levelalertriskm")
	public String getAlertRiskText() {
		return LanguageLoad.getinstance().find("web/query/levelalertriskm");
	}

	@ModelAttribute("selectlevelalertriskm")
	public String getSelectAlertRiskText() {
		return LanguageLoad.getinstance().find("web/query/selectlevelalertriskm");
	}
	
	@ModelAttribute("formulalucenecoord")
	public String getFormulaCoorText() {
		return LanguageLoad.getinstance().find("web/query/formulalucenecoord");
	}
	
	@ModelAttribute("defaultformulalucenecoord")
	public String getFormulaCoorDefText() {
		return LanguageLoad.getinstance().find("web/query/defaultformulalucenecoord");
	}

	@ModelAttribute("formulalucenecoordhelp")
	public String getFormulaCoorHelText() {
		return LanguageLoad.getinstance().find("web/query/formulalucenecoordhelp");
	}
	
	@ModelAttribute("formulalucenequerynorm")
	public String getFormulaNormText() {
		return LanguageLoad.getinstance().find("web/query/formulalucenequerynorm");
	}
	
	@ModelAttribute("defaultformulalucenequerynorm")
	public String getFormulaNormDefText() {
		return LanguageLoad.getinstance().find("web/query/defaultformulalucenequerynorm");
	}
	
	@ModelAttribute("defaultformulalucenequerynormleng")
	public String getFormulaNormDefLengText() {
		return LanguageLoad.getinstance().find("web/query/defaultformulalucenequerynormleng");
	}
	
	@ModelAttribute("defaultformulalucenequerynormover")
	public String getFormulaNormDefOverText() {
		return LanguageLoad.getinstance().find("web/query/defaultformulalucenequerynormover");
	}

	
	
	@ModelAttribute("formulalucenequerynormhelp")
	public String getFormulaNormHelText() {
		return LanguageLoad.getinstance().find("web/query/formulalucenequerynormhelp");
	}
	
	@ModelAttribute("formulalucenequerytf")
	public String getFormulaTfText() {
		return LanguageLoad.getinstance().find("web/query/formulalucenequerytf");
	}
	
	@ModelAttribute("defaultformulalucenequerytf")
	public String getFormulaTfDefText() {
		return LanguageLoad.getinstance().find("web/query/defaultformulalucenequerytf");
	}

	@ModelAttribute("formulalucenequerytfhelp")
	public String getFormulaTfHelText() {
		return LanguageLoad.getinstance().find("web/query/formulalucenequerytfhelp");
	}
	
	@ModelAttribute("formulalucenequeryidf")
	public String getFormulaIdfText() {
		return LanguageLoad.getinstance().find("web/query/formulalucenequeryidf");
	}
	
	@ModelAttribute("defaultformulalucenequeryidf")
	public String getFormulaIdfDefText() {
		return LanguageLoad.getinstance().find("web/query/defaultformulalucenequeryidf");
	}

	@ModelAttribute("formulalucenequeryidfhelp")
	public String getFormulaIdfHelText() {
		return LanguageLoad.getinstance().find("web/query/formulalucenequeryidfhelp");
	}

	@ModelAttribute("formulalucenequerygeneral")
	public String getFormulaGeneralText() {
		return LanguageLoad.getinstance().find("web/query/formulalucenequerygeneral");
	}

	@ModelAttribute("formulalucenequerygeneralhelp")
	public String getFormulaGeneralHelText() {
		return LanguageLoad.getinstance().find("web/query/formulalucenequerygeneralhelp");
	}
	
	@ModelAttribute("formulalucenequerypersist")
	public String getFormulaPersistText() {
		return LanguageLoad.getinstance().find("web/query/formulalucenequerypersist");
	}
	
	@ModelAttribute("formulalucenequeryenviar")
	public String getFormulaSendText() {
		return LanguageLoad.getinstance().find("web/query/formulalucenequeryenviar");
	}
	
	@ModelAttribute("formulalucenequerycancelar")
	public String getFormulaCancelText() {
		return LanguageLoad.getinstance().find("web/query/formulalucenequerycancelar");
	}
	
	@ModelAttribute("querytittle")
	public String getFormulaTittleText() {
		return LanguageLoad.getinstance().find("web/query/querytittle");
	}
	
	@ModelAttribute("querysite")
	public String getFormulaSiteText() {
		return LanguageLoad.getinstance().find("web/query/querysite");
	}
	
	@ModelAttribute("querydateofpubli")
	public String getFormuladateofText() {
		return LanguageLoad.getinstance().find("web/query/querydateofpubli");
	}
	
	@ModelAttribute("queryscore")
	public String getFormulaScoreText() {
		return LanguageLoad.getinstance().find("web/query/queryscore");
	}
	
	@ModelAttribute("querycerrar")
	public String getCerrarText() {
		return LanguageLoad.getinstance().find("web/query/querycerrar");
	}
	
	@ModelAttribute("queryprev")
	public String getPrevText() {
		return LanguageLoad.getinstance().find("web/query/queryprev");
	}
	
	@ModelAttribute("querysig")
	public String getnextText() {
		return LanguageLoad.getinstance().find("web/query/querysig");
	}
	
	@ModelAttribute("queryhoy")
	public String getTodayText() {
		return LanguageLoad.getinstance().find("web/query/queryhoy");
	}

	@ModelAttribute("querymonthcompl")
	public String[] getmonthText() {
		String Loa=LanguageLoad.getinstance().find("web/query/monthscomp");
		String[] recortado=Loa.split(",");
		if (recortado.length!=12)
			recortado=("January,February,March,April,May,June,July,"
					+ "August,September,October,November,Dicember").split(",");
		return recortado;
	}
	
	@ModelAttribute("querymonthrec")
	public String[] getmonthrecText() {
		String Loa=LanguageLoad.getinstance().find("web/query/monthrec");
		String[] recortado=Loa.split(",");
		if (recortado.length!=12)
			recortado=("Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dic").split(",");
		return recortado;
	}
	
	@ModelAttribute("querydaycomp")
	public String[] getdaycompText() {
		String Loa=LanguageLoad.getinstance().find("web/query/daycomp");
		String[] recortado=Loa.split(",");
		if (recortado.length!=7)
			recortado=("Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday").split(",");
		return recortado;
	}
	
	@ModelAttribute("querydayrec")
	public String[] getdaycompRecText() {
		String Loa=LanguageLoad.getinstance().find("web/query/daycomprec");
		String[] recortado=Loa.split(",");
		if (recortado.length!=7)
			recortado=("Sun,Mon,Tue,Wed,Thu,Fri,Sat").split(",");
		return recortado;
	}
	
	@ModelAttribute("querydayone")
	public String[] getdaycompOneText() {
		String Loa=LanguageLoad.getinstance().find("web/query/dayone");
		String[] recortado=Loa.split(",");
		if (recortado.length!=7)
			recortado=("U,M,T,W,R,F,S").split(",");
		return recortado;
	}
	
	@ModelAttribute("resumealerts")
	public String getResumeAlertsText() {
		return LanguageLoad.getinstance().find("web/resume/resumealerts");
	}
	
	@ModelAttribute("resumerecentalerts")
	public String getRecentAlertsText() {
		return LanguageLoad.getinstance().find("web/resume/resumerecentalerts");
	}
	
	@ModelAttribute("resumenamealerts")
	public String getnameAlertsText() {
		return LanguageLoad.getinstance().find("web/resume/resumenamealerts");
	}
	
	@ModelAttribute("resumescore")
	public String getScoreAlertsText() {
		return LanguageLoad.getinstance().find("web/resume/resumescore");
	}
	
	@ModelAttribute("resumenorecentalerts")
	public String getNoRecentAlertsText() {
		return LanguageLoad.getinstance().find("web/resume/resumenorecentalerts");
	}
	
	@ModelAttribute("resumethisweek")
	public String getThisWeekText() {
		return LanguageLoad.getinstance().find("web/resume/resumethisweek");
	}
	
	@ModelAttribute("resumeview")
	public String getViewText() {
		return LanguageLoad.getinstance().find("web/resume/resumeview");
	}
	
	@ModelAttribute("resumeslist")
	public String getListText() {
		return LanguageLoad.getinstance().find("web/resume/resumeslist");
	}
	
	@ModelAttribute("resumesrisk")
	public String getResumeRiskText() {
		return LanguageLoad.getinstance().find("web/resume/resumesrisk");
	}
	
	@ModelAttribute("resumesrecentrisk")
	public String getRecentRiskText() {
		return LanguageLoad.getinstance().find("web/resume/resumesrecentrisk");
	}
	
	@ModelAttribute("resumesnamerisk")
	public String getnameRiskText() {
		return LanguageLoad.getinstance().find("web/resume/resumesnamerisk");
	}
	
	@ModelAttribute("resumesnscorerisk")
	public String getScoreRiskText() {
		return LanguageLoad.getinstance().find("web/resume/resumescorerisk");
	}
	
	@ModelAttribute("resumesnorecentrisk")
	public String getNoRecentRiskText() {
		return LanguageLoad.getinstance().find("web/resume/resumesnorecentrisk");
	}
	
	@ModelAttribute("resumestadistics")
	public String getStadisticsText() {
		return LanguageLoad.getinstance().find("web/resume/resumestadistics");
	}
	
	@ModelAttribute("resumedatetime")
	public String getDateTimeText() {
		return LanguageLoad.getinstance().find("web/resume/resumedatetime");
	}
	
	@ModelAttribute("resumenewss")
	public String getNewsText() {
		return LanguageLoad.getinstance().find("web/resume/resumenewss");
	}
	
	@ModelAttribute("resumealertss")
	public String getAlertsText() {
		return LanguageLoad.getinstance().find("web/resume/resumealertss");
	}
	
	@ModelAttribute("resumeriskss")
	public String getRisksText() {
		return LanguageLoad.getinstance().find("web/resume/resumeriskss");
	}
	
	@ModelAttribute("resumewebsitesanal")
	public String getWebSitesAnalized() {
		return LanguageLoad.getinstance().find("web/resume/resumewebsitesanal");
	}
	
	@ModelAttribute("resumeduration")
	public String getResumeDuration() {
		return LanguageLoad.getinstance().find("web/resume/resumeduration");
	}
	
	@ModelAttribute("resumeprovideralertlevels")
	public String getProviderText() {
		return LanguageLoad.getinstance().find("web/resume/resumeprovideralertlevels");
	}
	
	@ModelAttribute("resumeprovidername")
	public String getProviderNemeText() {
		return LanguageLoad.getinstance().find("web/resume/resumeprovidername");
	}
	
	@ModelAttribute("resumeprovidernodata")
	public String getProviderNodataText() {
		return LanguageLoad.getinstance().find("web/resume/resumeprovidernodata");
	}
	
	@ModelAttribute("resumemap")
	public String getAlertMapText() {
		return LanguageLoad.getinstance().find("web/resume/resumemap");
	}
	
	@ModelAttribute("resumegrapnews")
	public String getNewsMapText() {
		return LanguageLoad.getinstance().find("web/resume/resumegrapnews");
	}
	
	@ModelAttribute("resumegrapalertsre")
	public String getAlertsReMapText() {
		return LanguageLoad.getinstance().find("web/resume/resumegrapalertsre");
	}
	
	@ModelAttribute("resumegrapalertsexp")
	public String getAlertExpMapText() {
		return LanguageLoad.getinstance().find("web/resume/resumegrapalertsexp");
	}
	
	
	
	@SuppressWarnings("deprecation")
	@ModelAttribute("alertsScoreToday")
	public List<Object[]> getAllAlertsToday() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgDay(
				today.toString(), 5);
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
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate() - 7);
		List<Object[]> lista = statisticsRepository.getAlertSocreAvgBetween(
				today.toString(), past.toString(), 7);
		return lista;
	}

	@SuppressWarnings("deprecation")
	@ModelAttribute("risksScoreToday")
	public List<Object[]> getAllRisksToday() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgDay(
				today.toString(), 5);
		return lista;
	}
	
	@SuppressWarnings("deprecation")
	@ModelAttribute("risksScoreLast2Days")
	public List<Object[]> getAllRisksLast2days() {
		java.util.Date now = new Date(System.currentTimeMillis());
		Calendar c = Calendar.getInstance(); 
		c.setTime(now); 
		c.add(Calendar.DATE, -2);
		Date today = new Date(c.getTime().getTime());
		Date today2 = new Date(today.getYear(), today.getMonth(), today.getDate());
		Date now2 = new Date(now.getYear(), now.getMonth(), now.getDate());		
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgBetween(now2.toString(),today2.toString(),5);
		System.out.println("SELECT alert_detect_id,title,fecha,sum(num) num, avg(score_avg) score_avg  FROM risk_score where DATE_FORMAT(fecha, '%Y-%m-%d' ) <= DATE_FORMAT("+now2.toString()+", '%Y-%m-%d' ) and DATE_FORMAT(fecha, '%Y-%m-%d' ) >  DATE_FORMAT("+today2.toString()+", '%Y-%m-%d' ) group by title order by score_avg desc limit  5");
		
		
		return lista;
	}

	@SuppressWarnings("deprecation")
	@ModelAttribute("risksScoreWeek")
	public List<Object[]> getAllRisksWeek() {
		Date now = new Date(System.currentTimeMillis());
		Date today = new Date(now.getYear(), now.getMonth(), now.getDate());
		Date past = new Date(now.getYear(), now.getMonth(), now.getDate() - 7);
		List<Object[]> lista = statisticsRepository.getRiskSocreAvgBetween(
				today.toString(), past.toString(), 7);
		return lista;
	}

	@ModelAttribute("locationsScoreWeek")
	public List<Object[]> getAllLocationsWeek() {
		return statisticsRepository.getStatsOfLocationLastWeek();
	}

//	@SuppressWarnings("deprecation")
//	@ModelAttribute("risksActivateInLast")
//	public Set<Risk> getAllRiskInLast() {
//		Date now = new Date(System.currentTimeMillis());
//		Date date = new Date(now.getYear(), now.getMonth(), now.getDate()
//				- configuracion.getConfiguracion().getDayRisks());
//		Set<Risk> lista = serviceRisk.getAlertDetectActivatedAfter(date);
//		for (Risk risk : lista) {
//			for (NewsDetect news : risk.getNewsDetect()) {
//				news.setAlertDetect(null);
//			}
//		}
//		return lista;
//	}

	@ModelAttribute("scrapStat")
	public List<Statistics> getScrapStatLastWeek() {
		List<Statistics> lista = statisticsRepository.getStatsLastWeek();
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
		for (Statistics statistics : lista) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(statistics.getFecha());
			calendar.set(Calendar.HOUR,0);
			calendar.set(Calendar.MINUTE,0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);
			Set<FeedLog> LogDate = feedLogRepository.readAllByDiaregistro(calendar.getTime());
			if (LogDate!=null&&!LogDate.isEmpty())
				{
				FeedLog FL=LogDate.iterator().next();
				statistics.setTiempodeuso(dateFormat.format(new Date(FL.getTiempodeuso().getTime())));
				statistics.setTotalrecuperadas(FL.getTotalrecuperadas());
				}
			else
			{
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTime(new Date(0l));
				calendar2.set(Calendar.HOUR,0);
				calendar2.set(Calendar.MINUTE,0);
				calendar2.set(Calendar.SECOND,0);
				calendar2.set(Calendar.MILLISECOND,0);
				statistics.setTiempodeuso(dateFormat.format(calendar2.getTime()));
				statistics.setTotalrecuperadas(0l);
			}
		}
		return statisticsRepository.getStatsLastWeek();
	}

	@ModelAttribute("allCountriesAfects")
	public Map<String, String> getAllCountriesAfects() {
		Map<String, String> listCountries = new HashMap<String, String>();
		
		List<Location> listLocation = (List<Location>) locationRepository.findAllAfect();
		for (Location loc : listLocation) {
			listCountries.put(CountryCode.getByCode(loc.getCountry().name())
					.getAlpha3(), "rgba(255,87,34,1)");
		}
		listCountries.put("defaultFill", "rgba(200,200,200,1)");
		
		return listCountries;
	}

	@ModelAttribute("graph")
	public List<Statistics> getGraphStat() {
		List<Statistics> graph = new ArrayList<Statistics>();
		Statistics statistics = null;
		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
		for (int i = 0; i < 7; i++) {
			Date current = new Date(date + (ONE_DAY * i));
			statistics = statisticsRepository.findOne(current);
			if (statistics == null) {
				statistics = new Statistics(current);
			}
			graph.add(statistics);
		}
		return graph;
	}

	@ResponseBody
	@RequestMapping("/ajax/graph/alerts")
	public List<Object> getGraphDataForAlerts() {
		return Lists.newArrayList();
	}

	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping("/ajax/scrap/week")
	public GraphData getGraphDataForWeek() {
		List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
				LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
				LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
				LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));
		List<Statistics> stats = new ArrayList<Statistics>();
		Statistics statistics = null;
		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
		GraphData graph = new GraphData();
		for (int i = 0; i < 7; i++) {
			Date current = new Date(date + (ONE_DAY * i));
			statistics = statisticsRepository.findOne(current);
			if (statistics == null) {
				statistics = new Statistics(current);
			}
			stats.add(statistics);
		}
		for (Statistics stat : stats) {
			graph.alertas.add(stat.getNumNews());
			graph.noticias.add(stat.getNoticias());
			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
					.concat(String.valueOf(stat.getFecha().getDate())));
		}
		graph.labels.remove(graph.labels.size() - 1);
		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));
		return graph;
	}


	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping("/ajax/scrap/week2")
	public GraphData getGraphDataForWeek2() {
		List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
				LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
				LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
				LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));
		List<Statistics> stats = new ArrayList<Statistics>();
		Statistics statistics = null;
		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
		GraphData graph = new GraphData();
		for (int i = 0; i < 7; i++) {
			Date current = new Date(date + (ONE_DAY * i));
			statistics = statisticsRepository.findOne(current);
			if (statistics == null) {
				statistics = new Statistics(current);
			}
			stats.add(statistics);
		}
		for (Statistics stat : stats) {
//			graph.alertas.add(stat.getNumNews());
			graph.noticias.add(stat.getNoticias());
			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
					.concat(String.valueOf(stat.getFecha().getDate())));
		}
		graph.labels.remove(graph.labels.size() - 1);
		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));
		return graph;
	}

	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping("/ajax/scrap/week3")
	public GraphData getGraphDataForWeek3() {
		List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
				LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
				LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
				LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));

		List<Statistics> statsexp = new ArrayList<Statistics>();
		Statistics statisticsexp = null;

		//MEto los primeros 15 dias para estadisticas
		Long dateext = System.currentTimeMillis() - (ONE_DAY * 15);
		for (int i = 0; i < 7; i++) {
			Date current = new Date(dateext + (ONE_DAY * i));
			statisticsexp = statisticsRepository.findOne(current);
			if (statisticsexp == null) {
				statisticsexp = new Statistics(current);
			}
			statsexp.add(statisticsexp);
		}


		 SimpleRegression simpleRegression = new SimpleRegression(true);

		 for (int i = 0; i < statsexp.size(); i++) {
				Statistics stat=statsexp.get(i);

				 Double Y = (double)i;
				 Double X = new Double(stat.getNumNews());
				 simpleRegression.addData(Y,X);

//				 System.out.println(Y+"->"+X);
//					System.out.println(i+"->>"+simpleRegression.predict(i));
			}


		//MEto los primeros 15 dias para estadisticas

		 List<Statistics> stats = new ArrayList<Statistics>();
		Statistics statistics = null;


		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
		GraphData graph = new GraphData();
		for (int i = 0; i < 7; i++) {
			Date current = new Date(date + (ONE_DAY * i));
			statistics = statisticsRepository.findOne(current);
			if (statistics == null) {
				statistics = new Statistics(current);
			}
			stats.add(statistics);
		}


		for (int i = 0; i < stats.size(); i++) {
			Statistics stat=stats.get(i);

			 Double Y = (double)i+7;
			 Double X = new Double(stat.getNumNews());

			 double statidst = X;
//			 if (i!=0&&i!=1)
//			 {
				 statidst=simpleRegression.predict(i+7);
					DecimalFormat df = new DecimalFormat("#.####");
					df.setRoundingMode(RoundingMode.CEILING);
					statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
					if (statidst<0d)
						statidst=0d;

//					System.out.println(Y+"->"+X);
//				System.out.println((i+7)+"->>"+simpleRegression.predict(i+7));
//			 }



			graph.alertas.add(stat.getNumNews());
			graph.alertasexp.add(statidst);
			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
					.concat(String.valueOf(stat.getFecha().getDate())));

			simpleRegression.addData(Y,X);
		}

		graph.labels.remove(graph.labels.size() - 1);
		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));

		 double statidst = simpleRegression.predict(graph.labels.size()+1);
			DecimalFormat df = new DecimalFormat("#.####");
			df.setRoundingMode(RoundingMode.CEILING);
			statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
			if (statidst<0d)
				statidst=0d;
		graph.alertasexp.add(statidst);
		graph.labels.add(LanguageLoad.getinstance().find("web/base/tomorrow"));


		return graph;
	}

	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping("/ajax/scrap/alert/week1")
	public GraphData getAlertGraphDataForWeek() {
		List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
				LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
				LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
				LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));
		List<Statistics> stats = new ArrayList<Statistics>();
		Statistics statistics = null;
		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
		GraphData graph = new GraphData();
		for (int i = 0; i < 7; i++) {
			Date current = new Date(date + (ONE_DAY * i));
			statistics = statisticsRepository.findOne(current);
			if (statistics == null) {
				statistics = new Statistics(current);
			}
			stats.add(statistics);
		}
		for (Statistics stat : stats) {
		//	graph.alertas.add(stat.getAlertas());
			graph.noticias.add(stat.getNoticias());
			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
					.concat(String.valueOf(stat.getFecha().getDate())));
		}
		graph.labels.remove(graph.labels.size() - 1);
		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));
		return graph;
	}

	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping("/ajax/scrap/alert/week2")
	public GraphData getAlertGraphDataForWeek2() {

		List<Statistics> statsexp = new ArrayList<Statistics>();
		Statistics statisticsexp = null;

		//MEto los primeros 15 dias para estadisticas
		Long dateext = System.currentTimeMillis() - (ONE_DAY * 15);
		for (int i = 0; i < 7; i++) {
			Date current = new Date(dateext + (ONE_DAY * i));
			statisticsexp = statisticsRepository.findOne(current);
			if (statisticsexp == null) {
				statisticsexp = new Statistics(current);
			}
			statsexp.add(statisticsexp);
		}


		 SimpleRegression simpleRegression = new SimpleRegression(true);

		 for (int i = 0; i < statsexp.size(); i++) {
				Statistics stat=statsexp.get(i);

				 Double Y = (double)i;
				 Double X = new Double(stat.getAlertas());
				 simpleRegression.addData(Y,X);
			}


		//MEto los primeros 15 dias para estadisticas



		 List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
					LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
					LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
					LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));
		List<Statistics> stats = new ArrayList<Statistics>();
		Statistics statistics = null;
		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
		GraphData graph = new GraphData();
		for (int i = 0; i < 7; i++) {
			Date current = new Date(date + (ONE_DAY * i));
			statistics = statisticsRepository.findOne(current);
			if (statistics == null) {
				statistics = new Statistics(current);
			}
			stats.add(statistics);
		}
		for (int i = 0; i < stats.size(); i++) {
			Statistics stat=stats.get(i);

			 Double Y = (double)i+7;
			 Double X = new Double(stat.getAlertas());

			 double statidst = X;
//			 if (i!=0&&i!=1)
//			 {
				 statidst=simpleRegression.predict(i+7);
					DecimalFormat df = new DecimalFormat("#.####");
					df.setRoundingMode(RoundingMode.CEILING);
					statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
					if (statidst<0d)
						statidst=0d;
//			 }
			graph.alertas.add(stat.getAlertas());
			graph.alertasexp.add(statidst);
			//graph.noticias.add(stat.getNoticias());
			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
					.concat(String.valueOf(stat.getFecha().getDate())));

			simpleRegression.addData(Y,X);
		}
		graph.labels.remove(graph.labels.size() - 1);
		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));

		double statidst = simpleRegression.predict(graph.labels.size()+8);
		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.CEILING);
		statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
		if (statidst<0d)
			statidst=0d;
	graph.alertasexp.add(statidst);
	graph.labels.add(LanguageLoad.getinstance().find("web/base/tomorrow"));


		return graph;
	}

	@ResponseBody
	@RequestMapping("/ajax/scrap/alert/by/week1")
	public GraphData getAlertGraphDataByWeek1() {
		GraphData graph = new GraphData();
		List<Object[]> stats = statisticsRepository.getStatsByWeek();
		int i = 0;
		for (Object[] stat : stats) {
			//graph.alertas.add(stat[3].toString());
			graph.noticias.add(stat[0].toString());
			graph.labels.add(LanguageLoad.getinstance().find("web/base/week")+" " + stat[5]);
			if (i >= 5)
				break;
			i++;
		}

		Collections.reverse(graph.labels);
		Collections.reverse(graph.noticias);

		return graph;



	}

	@ResponseBody
	@RequestMapping("/ajax/scrap/alert/by/week2")
	public GraphData getAlertGraphDataByWeek2() {

		List<Integer> listaEst=new ArrayList<Integer>();

		GraphData graph = new GraphData();
		List<Object[]> stats = statisticsRepository.getStatsByWeek();
		int i = 0;
		boolean previoI=false;
//		String sema=null;
		for (Object[] stat : stats) {
			if (!previoI)
			{
			graph.alertas.add(stat[3].toString());
			//graph.noticias.add(stat[0].toString());
			graph.labels.add(LanguageLoad.getinstance().find("web/base/week")+" " + stat[5]);
//			if (sema==null)
//				sema=stat[5].toString();
			}

			Integer valex=0;
			try {
				valex=Integer.parseInt(stat[3].toString());
			} catch (Exception e) {

			}
			listaEst.add(valex);

			if (i >= 5)
				{
				if (previoI)
					break;
				else
					previoI=true;
				}
			i++;
		}

		if (listaEst.size()<5)
			listaEst.add(0);
		Collections.reverse(listaEst);

		List<Double> expected=new ArrayList<Double>();
		SimpleRegression simpleRegression = new SimpleRegression(true);

		 for (int j = 0; j < listaEst.size(); j++) {
				Integer stat=listaEst.get(j);

				 Double Y = (double)j;
				 Double X = new Double(stat);

				double expect=X;
				if (j!=0&&j!=1)
				 	{
					    expect = simpleRegression.predict(j);
						DecimalFormat df = new DecimalFormat("#.####");
						df.setRoundingMode(RoundingMode.CEILING);
						expect=Double.parseDouble(df.format(expect).replace(",", "."));
						if (X<0d)
							X=0d;

				 	}

				if (j!=0)
					 expected.add(expect);

				 simpleRegression.addData(Y,X);





			}

		 Collections.reverse(expected);

		 for (Double double1 : expected) {
			 graph.alertasexp.add(double1);
		}


		 Collections.reverse(graph.alertas);
		 Collections.reverse(graph.labels);
		 Collections.reverse(graph.alertasexp);

		 double statidst = simpleRegression.predict(graph.labels.size()+1);
			DecimalFormat df = new DecimalFormat("#.####");
			df.setRoundingMode(RoundingMode.CEILING);
			statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
			if (statidst<0d)
				statidst=0d;
		graph.alertasexp.add(statidst);


		String SemanaFuturo=LanguageLoad.getinstance().find("web/base/futureweek");
		graph.labels.add(SemanaFuturo);







		return graph;
	}

	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping("/ajax/scrap/alert/trend")
	public GraphData getAlertGraphDataTrend() {
		TrendLine t = new PolyTrendLine(2);
		GraphData graph = new GraphData();
		List<Statistics> stats = (List<Statistics>) statisticsRepository
				.findAll();
		double[] x = new double[stats.size()];
		double[] y = new double[stats.size()];
		int i = 0;
		for (Statistics stat : stats) {
			try {
				if (stat.getAlertas() > 200)
					continue;
				x[i] = new Double(stat.getFecha().getTime() / 86400000);
				y[i] = new Double(stat.getAlertas());
				i++;
			} catch (Exception e) {
				//  Auto-generated catch block
				e.printStackTrace();
			}
		}
		t.setValues(y, x);
		for (int z = 365; z >= 0; z--) {
			Date date = new Date(System.currentTimeMillis() - (z * ONE_DAY));
			date.setDate(date.getDate() - z);
			double p = t.predict(new Double(date.getTime() / 86400000));
			graph.labels.add(date.toLocaleString());
			graph.alertas.add(p);
		}
		return graph;
	}

	//TODO eee
	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping("/ajax/scrap/risk/week1")
	public GraphData getRiskGraphDataForWeek() {
		List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
				LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
				LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
				LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));
		List<Statistics> stats = new ArrayList<Statistics>();
		Statistics statistics = null;
		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
		GraphData graph = new GraphData();
		for (int i = 0; i < 7; i++) {
			Date current = new Date(date + (ONE_DAY * i));
			statistics = statisticsRepository.findOne(current);
			if (statistics == null) {
				statistics = new Statistics(current);
			}
			stats.add(statistics);
		}
		for (Statistics stat : stats) {
			//graph.alertas.add(stat.getRiesgos());
			graph.noticias.add(stat.getNoticias());
			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
					.concat(String.valueOf(stat.getFecha().getDate())));
		}
		graph.labels.remove(graph.labels.size() - 1);
		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));
		return graph;
	}


	@SuppressWarnings("deprecation")
	@ResponseBody
	@RequestMapping("/ajax/scrap/risk/week2")
	public GraphData getRiskGraphDataForWeek2() {

		List<Statistics> statsexp = new ArrayList<Statistics>();
		Statistics statisticsexp = null;

		//MEto los primeros 15 dias para estadisticas
				Long dateext = System.currentTimeMillis() - (ONE_DAY * 15);
				for (int i = 0; i < 7; i++) {
					Date current = new Date(dateext + (ONE_DAY * i));
					statisticsexp = statisticsRepository.findOne(current);
					if (statisticsexp == null) {
						statisticsexp = new Statistics(current);
					}
					statsexp.add(statisticsexp);
				}


				 SimpleRegression simpleRegression = new SimpleRegression(true);

				 for (int i = 0; i < statsexp.size(); i++) {
						Statistics stat=statsexp.get(i);

						 Double Y = (double)i;
						 Double X = new Double(stat.getRiesgos());
						 simpleRegression.addData(Y,X);
					}


				//MEto los primeros 15 dias para estadisticas


				 List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
							LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
							LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
							LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));
		List<Statistics> stats = new ArrayList<Statistics>();
		Statistics statistics = null;
		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
		GraphData graph = new GraphData();
		for (int i = 0; i < 7; i++) {
			Date current = new Date(date + (ONE_DAY * i));
			statistics = statisticsRepository.findOne(current);
			if (statistics == null) {
				statistics = new Statistics(current);
			}
			stats.add(statistics);
		}
		for (int i = 0; i < stats.size(); i++) {
			Statistics stat=stats.get(i);
			
			 Double Y = (double)i+7;
			 Double X = new Double(stat.getRiesgos());
			
			 double statidst = X;

				 statidst=simpleRegression.predict(i+7);
					DecimalFormat df = new DecimalFormat("#.####");
					df.setRoundingMode(RoundingMode.CEILING);
					statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
					if (statidst<0d)
						statidst=0d;

			
			graph.alertas.add(stat.getRiesgos());
			//graph.noticias.add(stat.getNoticias());
			graph.alertasexp.add(statidst);
			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
					.concat(String.valueOf(stat.getFecha().getDate())));
			
			simpleRegression.addData(Y,X);
		}
		graph.labels.remove(graph.labels.size() - 1);
		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));
		
		double statidst = simpleRegression.predict(graph.labels.size()+8);
		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.CEILING);
		statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
		if (statidst<0d)
			statidst=0d;
	graph.alertasexp.add(statidst);
	graph.labels.add(LanguageLoad.getinstance().find("web/base/tomorrow"));
		
		return graph;
	}

	
	@ResponseBody
	@RequestMapping("/ajax/scrap/risk/by/week1")
	public GraphData getRiskGraphDataByWeek1() {
		GraphData graph = new GraphData();
		List<Object[]> stats = statisticsRepository.getStatsByWeek();
		int i = 0;
		for (Object[] stat : stats) {
//			graph.alertas.add(stat[2].toString());
			graph.noticias.add(stat[0].toString());
			graph.labels.add(LanguageLoad.getinstance().find("web/base/week")+" " + stat[5]);
			if (i >= 5)
				break;
			i++;
		}
		
		Collections.reverse(graph.labels);
		Collections.reverse(graph.noticias);
		
		return graph;
	}
	
	
	//TODO
	@ResponseBody
	@RequestMapping("/ajax/scrap/risk/by/week2")
	public GraphData getRiskGraphDataByWeek2() {
		

List<Integer> listaEst=new ArrayList<Integer>();
		
		GraphData graph = new GraphData();
		List<Object[]> stats = statisticsRepository.getStatsByWeek();
		int i = 0;
		boolean previoI=false;
//		String sema=null;
		for (Object[] stat : stats) {
			if (!previoI)
			{
			graph.alertas.add(stat[2].toString());
			//graph.noticias.add(stat[0].toString());
			graph.labels.add(LanguageLoad.getinstance().find("web/base/week")+" " + stat[5]);
//			if (sema==null)
//				sema=stat[5].toString();
			}
			
			Integer valex=0;
			try {
				valex=Integer.parseInt(stat[2].toString());
			} catch (Exception e) {
				
			}
			listaEst.add(valex);
			
			if (i >= 5)
				{
				if (previoI)
					break;
				else
					previoI=true;
				}
			i++;
		}
		
		if (listaEst.size()<5)
			listaEst.add(0);
		Collections.reverse(listaEst);
		
		List<Double> expected=new ArrayList<Double>();
		SimpleRegression simpleRegression = new SimpleRegression(true);
		 
		 for (int j = 0; j < listaEst.size(); j++) {
				Integer stat=listaEst.get(j);
				
				 Double Y = (double)j;
				 Double X = new Double(stat);
				 
				double expect=X;
				if (j!=0&&j!=1)
				 	{
					    expect = simpleRegression.predict(j);
						DecimalFormat df = new DecimalFormat("#.####");
						df.setRoundingMode(RoundingMode.CEILING);
						expect=Double.parseDouble(df.format(expect).replace(",", "."));
						if (X<0d)
							X=0d;
						
				 	}
				
				if (j!=0)
					 expected.add(expect);
				
				 simpleRegression.addData(Y,X);
				 
				 
				 
				 
					 
			}
		 
		 Collections.reverse(expected);
		
		 for (Double double1 : expected) {
			 graph.alertasexp.add(double1);
		}
		 
		 
		 Collections.reverse(graph.alertas);
		 Collections.reverse(graph.labels);
		 Collections.reverse(graph.alertasexp);
		 
		 double statidst = simpleRegression.predict(graph.labels.size()+1);
			DecimalFormat df = new DecimalFormat("#.####");
			df.setRoundingMode(RoundingMode.CEILING);
			statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
			if (statidst<0d)
				statidst=0d;
		graph.alertasexp.add(statidst);
		
		
		String SemanaFuturo=LanguageLoad.getinstance().find("web/base/futureweek");
		graph.labels.add(SemanaFuturo);

		
		
		
		 
		 
		
		return graph;
		
	}

	@ModelAttribute("month")
	public GraphData getGraphDataForMonth() {
		GraphData graph = new GraphData();
		// List<Object[]> stats = statisticsRepository.getStatsByWeek();
		// while(stats.size()<4) {
		// Object[] t = {((Integer)stats.get(0)[0])-1,0,0};
		// stats.add(0, t);
		// }
		// for (Object[] stat : stats) {
		// graph.alertas.add(stat[1]);
		// graph.noticias.add(stat[2]);
		// graph.labels.add("Semana " + (Integer)stat[0]);
		// }
		return graph;
	}

	@ModelAttribute("year")
	public GraphData getGraphDataForYear() {
		GraphData graph = new GraphData();
		return graph;
	}

	@RequestMapping(value = { "", "/" })
	public String checkAlert() {
		return "resume";
	}

	@ResponseBody
	@RequestMapping("/webjars/{webjar}/**")
	public ResponseEntity<Object> locateWebjarAsset(
            @PathVariable String webjar, HttpServletRequest request) {
		try {
			String mvcPrefix = "/webjars/" + webjar + "/"; // This prefix must
															// match the mapping
															// path!
			String mvcPath = (String) request
					.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
			String fullPath = assetLocator.getFullPath(webjar,
					mvcPath.substring(mvcPrefix.length()));
			return new ResponseEntity<Object>(new ClassPathResource(fullPath),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public String searchQueryTestForm(HttpServletRequest request, String query,
                                      Model model) {
		model.addAttribute("query", "");
		
		SpecializedClassicSimilarity Symy=new SpecializedClassicSimilarity();
		
		Symy.loadfromfile(IOFolder.getPathgen(),IOFolder.getPathsimy());
		
		String formuGen = "Sl+(Wa+1)+(Wl+1)+(Ws+1)";
		if (Symy.getGeneralFor()!=null&&!Symy.getGeneralFor().getSourceText().isEmpty())
			formuGen=Symy.getGeneralFor().getSourceText();
		
		String formuLucCoord = "";
		if (Symy.getCoordFor()!=null&&!Symy.getCoordFor().getSourceText().isEmpty())
			formuLucCoord=Symy.getCoordFor().getSourceText();
		
		String formuLucQueryNorm = "";
		if (Symy.getQueryNormFor()!=null&&!Symy.getQueryNormFor().getSourceText().isEmpty())
			formuLucQueryNorm=Symy.getQueryNormFor().getSourceText();
		
		boolean formuLucQueryNormL = Symy.isOriginal();
		
		boolean formuLucQueryDisco = Symy.getDiscountOverlaps();
		
		String formuLuctf = "";
		if (Symy.getTfFor()!=null&&!Symy.getTfFor().getSourceText().isEmpty())
			formuLuctf=Symy.getTfFor().getSourceText();
		
		String formuLucidf = "";
		if (Symy.getIdFor()!=null&&!Symy.getIdFor().getSourceText().isEmpty())
			formuLucidf=Symy.getIdFor().getSourceText();
		
		
		
		model.addAttribute("formuGen", formuGen);
		model.addAttribute("formuLucCoord", formuLucCoord);
		model.addAttribute("formuLucQueryNorm", formuLucQueryNorm);
		model.addAttribute("formuLuctf", formuLuctf);
		model.addAttribute("formuLucidf", formuLucidf);
		model.addAttribute("AlertT", AlertLevel.yellow);
		model.addAttribute("BasicNor", formuLucQueryNormL);
		model.addAttribute("DiscNor", formuLucQueryDisco);
		
		if (formuLucQueryNorm.trim().isEmpty()||formuLucQueryNorm.trim().toLowerCase().equals("QueryNorm".toLowerCase()))
		model.addAttribute("QueryNorm", true);
		else
			model.addAttribute("QueryNorm", false);
		
		
		if (formuLucCoord.trim().isEmpty()||formuLucCoord.trim().toLowerCase().equals("coord".toLowerCase()))
			model.addAttribute("Coord", true);
			else
				model.addAttribute("Coord", false);
		
		if (formuLuctf.trim().isEmpty()||formuLuctf.trim().toLowerCase().equals("tf".toLowerCase()))
			model.addAttribute("tf", true);
			else
				model.addAttribute("tf", false);
		
		if (formuLucidf.trim().isEmpty()||formuLucidf.trim().toLowerCase().equals("idf".toLowerCase()))
			model.addAttribute("idf", true);
			else
				model.addAttribute("idf", false);
			
		
//		this.menu = LanguageLoad.getinstance().find("web/query/welcome");
		
		return "query";
	}


	
	@RequestMapping(value = "/query", method = RequestMethod.POST)
	public String searchQueryTest2(HttpServletRequest request, String query, String Des, String Has,
                                   String AlertT, String formuGen, String formuLucCoord, String formuLucQueryNorm,
                                   String formuLuctf, String formuLucidf, boolean persist, boolean BasicNor, boolean DiscNor, boolean QueryNorm,
                                   boolean Coord, boolean tf, boolean idf,
                                   Model model) {
		model.addAttribute("query", query);
		model.addAttribute("Des", Des);
		model.addAttribute("Has", Has);
		model.addAttribute("AlertT", AlertT);
		model.addAttribute("formuGen", formuGen);
		model.addAttribute("BasicNor", BasicNor);
		model.addAttribute("DiscNor", DiscNor);
		model.addAttribute("QueryNorm", QueryNorm);
		
		model.addAttribute("Coord", Coord);
		model.addAttribute("tf", tf);
		model.addAttribute("idf", idf);
		
		
		if (formuLucCoord==null) formuLucCoord="";		
		model.addAttribute("formuLucCoord", formuLucCoord);
		
		if (formuLucQueryNorm==null) formuLucQueryNorm="";
		model.addAttribute("formuLucQueryNorm", formuLucQueryNorm);
		
		if (formuLuctf==null) formuLuctf="";
		model.addAttribute("formuLuctf", formuLuctf);
		
		if (formuLucidf==null) formuLucidf="";
		model.addAttribute("formuLucidf", formuLucidf);

		
		
				
		java.util.Date DateDes=null;
		if (Des!=null&&!Des.isEmpty())
			try {
				SimpleDateFormat SDF=new SimpleDateFormat("dd/MM/yyyy");
				DateDes=SDF.parse(Des);
			} catch (Exception e) {
				
			}
		
		java.util.Date DateHas=new java.util.Date();
		if (Has!=null&&!Has.isEmpty())
			try {
				SimpleDateFormat SDF=new SimpleDateFormat("dd/MM/yyyy");
				DateHas=SDF.parse(Has);
				Calendar c = Calendar.getInstance(); 
				c.setTime(DateHas); 
				c.add(Calendar.DATE, 1);
				DateHas=c.getTime();
			} catch (Exception e) {
				
			}
		
		
	
		int Wa=0;
		try {
			model.addAttribute("AlertT",
			AlertLevel.valueOf(AlertT));
			
			Wa=AlertLevel.valueOf(AlertT).getValue();
		} catch (Exception e) {
			
		}
		
		SpecializedClassicSimilarity Symy=new SpecializedClassicSimilarity();
		
				
		try {
			if (formuLucCoord!=null&&!formuLucCoord.isEmpty())
			Symy.setCoordFor(formuLucCoord);
		} catch (Exception e) {
			model.addAttribute("error",
					LanguageLoad.getinstance().find("web/base/error/errorexecformula")+ " \"coord\" "+ LanguageLoad.getinstance().find("web/base/error/errorexecformula2") + formuLucCoord + " -> "+ e.getMessage());
			return "query";
		}
		
		try {
			if (formuLucQueryNorm!=null&&!formuLucQueryNorm.isEmpty())
			Symy.setQueryNormFor(formuLucQueryNorm);
		} catch (Exception e) {
			model.addAttribute("error",
					LanguageLoad.getinstance().find("web/base/error/errorexecformula")+ " \"QueryNorm\" "+ LanguageLoad.getinstance().find("web/base/error/errorexecformula2")+ formuLucQueryNorm+ " -> "+ e.getMessage());
			return "query";
		}
		
		try {
			if (formuLuctf!=null&&!formuLuctf.isEmpty())
			Symy.setTfFor(formuLuctf);
		} catch (Exception e) {
			model.addAttribute("error",
					LanguageLoad.getinstance().find("web/base/error/errorexecformula")+ " \"tf\" "+ LanguageLoad.getinstance().find("web/base/error/errorexecformula2")+ formuLuctf+ " -> "+ e.getMessage());
			return "query";
		}
		
		try {
			if (formuLucidf!=null&&!formuLucidf.isEmpty())
			Symy.setIdFor(formuLucidf);
		} catch (Exception e) {
			model.addAttribute("error",
					LanguageLoad.getinstance().find("web/base/error/errorexecformula")+ " \"idf\" "+ LanguageLoad.getinstance().find("web/base/error/errorexecformula2")+ formuLucidf+ " -> "+ e.getMessage());
			return "query";
		}
		
		try {
			if (formuGen==null||formuGen.isEmpty())
				formuGen="Sl+(Wa+1)+(Wl+1)+(Ws+1)";
			
			Symy.setGeneralForm(formuGen);
		} catch (Exception e) {
			model.addAttribute("error",
					LanguageLoad.getinstance().find("web/base/error/executingformula")+" "+ formuGen+ " -> "+ e.getMessage());
			return "query";
		}
		
		Symy.setOriginal(BasicNor);
		
		
		if (persist)
			Symy.savetofile(IOFolder.getPathgen(),IOFolder.getPathsimy());
		
		
		try {
			

			
			
			
			List<Location> ResultadoBasicoLoc = newsIndexService.searchLocation(DateDes,DateHas);
			
			HashMap<String, Location> Loca=new HashMap<String, Location>();
			
			for (Location location : ResultadoBasicoLoc) {
				
				if (location.getType()==null)
					location.setType(LocationLevel.Region);
				
				List<String> A=location.getNews();
				for (String string : A) {
					Location padre=Loca.get(string);
					
					if (padre!=null&&padre.getType()==null)
						padre.setType(LocationLevel.Region);
					
					if (padre!=null&&padre.getType().getValue()<location.getType().getValue())
						padre=location;
					
					if (padre==null)
						padre=location;
					
					Loca.put(string, padre);
					
				}
			}
			
			
			
			
			List<NewsDetect> ResultadoBasico = newsIndexService.search(query,DateDes,DateHas,Symy);
			for (NewsDetect newsDetect : ResultadoBasico) {
				
				float Sl = newsDetect.getScoreLucene();
				
//				Integer Wst = newsDetect.getSite().getFeedType().getValue();
				
				Integer Ws = newsDetect.getSite().getType().getValue();
				
				Location Loca_ = Loca.get(newsDetect.getLink());
				
				int Wl=0;
				if (Loca_!=null)
					{
					if (Loca_.getType()==null)
						Wl=2;
					else
						Wl=Loca_.getType().getValue();
					}
				
				
				
			    
			    JexlContext context = new MapContext();
			    
			    context.set("Wa".toLowerCase(),Wa );
			    context.set("Wl".toLowerCase(),Wl );
			    context.set("Ws".toLowerCase(),Ws );
//			    context.set("Wst".toLowerCase(),Wsl );
			    context.set("Sl".toLowerCase(),Sl );
			    
			    Number result = (Number) Symy.getGeneralFor().evaluate(context);
			    
			    float ScoreF=result.floatValue();

			    

				
				newsDetect.setScore(ScoreF);
			}
			model.addAttribute("result", ResultadoBasico);			
			return "query";
		} catch (TopicDoesNotExistsException e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/base/error/topicnotexist") +" "+ e.getTopic()
					+ LanguageLoad.getinstance().find("web/base/error/topicnotexist2"));
			return "query";
		} catch (CyclicDependencyException e) {
			model.addAttribute("error",LanguageLoad.getinstance().find("web/base/error/topicciclico") + " "+ e.toString());
			return "query";
		} catch (ParseException | TokenMgrError e) {
			model.addAttribute("error",
					LanguageLoad.getinstance().find("web/base/error/validatingquerry") + " "+ e.getMessage());
			return "query";
		} catch (JexlException e) {
			model.addAttribute("error",
					LanguageLoad.getinstance().find("web/base/error/executingformula")+" "+ formuGen+ " -> "+ e.getMessage());
			return "query";
		} catch (Exception e) {
			model.addAttribute("error", LanguageLoad.getinstance().find("web/base/error/notdeterminated")+ " "+ e.toString());
			return "query";
		}
	}
	
	

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test(Model model) {
		return "index";
	}
	
	@RequestMapping(value = "/chart", method = RequestMethod.GET)
	public String chart(Model model) {
		return "chart";
	}

	@RequestMapping(value = "/load", method = RequestMethod.GET)
	public String loadHistory(Model model) {
		model.addAttribute("feeds", feedService.getAllFeedAlph());
		return "load";
	}
	
	@ResponseBody
	@RequestMapping("/ajax/days")
	public List<Object> getAllDays() {
		return statisticsRepository.getAllFechas();
	}

	@RequestMapping(value = "/load", method = RequestMethod.POST)
	public String loadHistory(@RequestParam("feed") Feed feed,
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		if (feed==null) {
			redirectAttributes.addFlashAttribute("error",
					LanguageLoad.getinstance().find("web/base/error/feedempty"));
		} else if (!file.isEmpty()) {
			try {
				List<News> listNews = feedScraping.scrapingHistoric(feed, file.getInputStream());
				redirectAttributes.addFlashAttribute("news",listNews);
				redirectAttributes.addFlashAttribute("info",
						LanguageLoad.getinstance().find("web/base/info/storednewscorrect"));
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute(
						"error",
						LanguageLoad.getinstance().find("web/base/error/errorprocessfile"));
			}
		} else {
			redirectAttributes.addFlashAttribute("error",LanguageLoad.getinstance().find("web/base/error/erroremptyfile"));
		}

		return "redirect:/load";
	}
}
