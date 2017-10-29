package com.ikip.newsdetect.main.controller;

import com.google.common.collect.Lists;
import com.ikip.newsdetect.find.service.NewsIndexService;
import com.ikip.newsdetect.main.repository.LocationRepository;
import com.ikip.newsdetect.main.service.FeedService;
import com.ikip.newsdetect.model.Feed;
import com.ikip.newsdetect.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainController extends BaseController {

	@Autowired
	private LocationRepository locationRepository;

	@Autowired
	private NewsIndexService newsIndexService;

	@Autowired
	private FeedService feedService;

	@ModelAttribute("querymonthcompl")
	public String[] getmonthText() {
		String Loa="";//LanguageLoad.getinstance().find("web/query/monthscomp");
		String[] recortado=Loa.split(",");
		if (recortado.length!=12)
			recortado=("January,February,March,April,May,June,July,"
					+ "August,September,October,November,Dicember").split(",");
		return recortado;
	}
	
	@ModelAttribute("querymonthrec")
	public String[] getmonthrecText() {
		String Loa="";//LanguageLoad.getinstance().find("web/query/monthrec");
		String[] recortado=Loa.split(",");
		if (recortado.length!=12)
			recortado=("Jan,Feb,Mar,Apr,May,Jun,Jul,Aug,Sep,Oct,Nov,Dic").split(",");
		return recortado;
	}
	
	@ModelAttribute("querydaycomp")
	public String[] getdaycompText() {
		String Loa="";//LanguageLoad.getinstance().find("web/query/daycomp");
		String[] recortado=Loa.split(",");
		if (recortado.length!=7)
			recortado=("Sunday,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday").split(",");
		return recortado;
	}
	
	@ModelAttribute("querydayrec")
	public String[] getdaycompRecText() {
		String Loa="";//LanguageLoad.getinstance().find("web/query/daycomprec");
		String[] recortado=Loa.split(",");
		if (recortado.length!=7)
			recortado=("Sun,Mon,Tue,Wed,Thu,Fri,Sat").split(",");
		return recortado;
	}
	
	@ModelAttribute("querydayone")
	public String[] getdaycompOneText() {
		String Loa="";//LanguageLoad.getinstance().find("web/query/dayone");
		String[] recortado=Loa.split(",");
		if (recortado.length!=7)
			recortado=("U,M,T,W,R,F,S").split(",");
		return recortado;
	}

	@ModelAttribute("allCountriesAfects")
	public Map<String, String> getAllCountriesAfects() {
		Map<String, String> listCountries = new HashMap<String, String>();
		
		List<Location> listLocation = (List<Location>) locationRepository.findAllAfect();
		for (Location loc : listLocation) {
//			listCountries.put(CountryCode.getByCode(loc.getCountry().name())
//					.getAlpha3(), "rgba(255,87,34,1)");
		}
		listCountries.put("defaultFill", "rgba(200,200,200,1)");
		
		return listCountries;
	}


	@ResponseBody
	@RequestMapping("/ajax/graph/alerts")
	public List<Object> getGraphDataForAlerts() {
		return Lists.newArrayList();
	}

//	@SuppressWarnings("deprecation")
//	@ResponseBody
//	@RequestMapping("/ajax/scrap/week")
//	public GraphData getGraphDataForWeek() {
////		List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
////				LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
////				LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
////				LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));
//		List<Statistics> stats = new ArrayList<Statistics>();
//		Statistics statistics = null;
//		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
//		GraphData graph = new GraphData();
//		for (int i = 0; i < 7; i++) {
//			Date current = new Date(date + (ONE_DAY * i));
//			statistics = statisticsRepository.findOne(current);
//			if (statistics == null) {
//				statistics = new Statistics(current);
//			}
//			stats.add(statistics);
//		}
//		for (Statistics stat : stats) {
//			graph.alertas.add(stat.getNumNews());
//			graph.noticias.add(stat.getNoticias());
//			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
//					.concat(String.valueOf(stat.getFecha().getDate())));
//		}
//		graph.labels.remove(graph.labels.size() - 1);
//		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));
//		return graph;
//	}


//	@SuppressWarnings("deprecation")
//	@ResponseBody
//	@RequestMapping("/ajax/scrap/week2")
//	public GraphData getGraphDataForWeek2() {
//		List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
//				LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
//				LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
//				LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));
//		List<Statistics> stats = new ArrayList<Statistics>();
//		Statistics statistics = null;
//		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
//		GraphData graph = new GraphData();
//		for (int i = 0; i < 7; i++) {
//			Date current = new Date(date + (ONE_DAY * i));
//			statistics = statisticsRepository.findOne(current);
//			if (statistics == null) {
//				statistics = new Statistics(current);
//			}
//			stats.add(statistics);
//		}
//		for (Statistics stat : stats) {
////			graph.alertas.add(stat.getNumNews());
//			graph.noticias.add(stat.getNoticias());
//			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
//					.concat(String.valueOf(stat.getFecha().getDate())));
//		}
//		graph.labels.remove(graph.labels.size() - 1);
//		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));
//		return graph;
//	}
//
//	@SuppressWarnings("deprecation")
//	@ResponseBody
//	@RequestMapping("/ajax/scrap/week3")
//	public GraphData getGraphDataForWeek3() {
//		List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
//				LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
//				LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
//				LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));
//
//		List<Statistics> statsexp = new ArrayList<Statistics>();
//		Statistics statisticsexp = null;
//
//		//MEto los primeros 15 dias para estadisticas
//		Long dateext = System.currentTimeMillis() - (ONE_DAY * 15);
//		for (int i = 0; i < 7; i++) {
//			Date current = new Date(dateext + (ONE_DAY * i));
//			statisticsexp = statisticsRepository.findOne(current);
//			if (statisticsexp == null) {
//				statisticsexp = new Statistics(current);
//			}
//			statsexp.add(statisticsexp);
//		}
//
//
//		 SimpleRegression simpleRegression = new SimpleRegression(true);
//
//		 for (int i = 0; i < statsexp.size(); i++) {
//				Statistics stat=statsexp.get(i);
//
//				 Double Y = (double)i;
//				 Double X = new Double(stat.getNumNews());
//				 simpleRegression.addData(Y,X);
//
////				 System.out.println(Y+"->"+X);
////					System.out.println(i+"->>"+simpleRegression.predict(i));
//			}
//
//
//		//MEto los primeros 15 dias para estadisticas
//
//		 List<Statistics> stats = new ArrayList<Statistics>();
//		Statistics statistics = null;
//
//
//		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
//		GraphData graph = new GraphData();
//		for (int i = 0; i < 7; i++) {
//			Date current = new Date(date + (ONE_DAY * i));
//			statistics = statisticsRepository.findOne(current);
//			if (statistics == null) {
//				statistics = new Statistics(current);
//			}
//			stats.add(statistics);
//		}
//
//
//		for (int i = 0; i < stats.size(); i++) {
//			Statistics stat=stats.get(i);
//
//			 Double Y = (double)i+7;
//			 Double X = new Double(stat.getNumNews());
//
//			 double statidst = X;
////			 if (i!=0&&i!=1)
////			 {
//				 statidst=simpleRegression.predict(i+7);
//					DecimalFormat df = new DecimalFormat("#.####");
//					df.setRoundingMode(RoundingMode.CEILING);
//					statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
//					if (statidst<0d)
//						statidst=0d;
//
////					System.out.println(Y+"->"+X);
////				System.out.println((i+7)+"->>"+simpleRegression.predict(i+7));
////			 }
//
//
//
//			graph.alertas.add(stat.getNumNews());
//			graph.alertasexp.add(statidst);
//			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
//					.concat(String.valueOf(stat.getFecha().getDate())));
//
//			simpleRegression.addData(Y,X);
//		}
//
//		graph.labels.remove(graph.labels.size() - 1);
//		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));
//
//		 double statidst = simpleRegression.predict(graph.labels.size()+1);
//			DecimalFormat df = new DecimalFormat("#.####");
//			df.setRoundingMode(RoundingMode.CEILING);
//			statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
//			if (statidst<0d)
//				statidst=0d;
//		graph.alertasexp.add(statidst);
//		graph.labels.add(LanguageLoad.getinstance().find("web/base/tomorrow"));
//
//
//		return graph;
//	}
//
//	@SuppressWarnings("deprecation")
//	@ResponseBody
//	@RequestMapping("/ajax/scrap/alert/week1")
//	public GraphData getAlertGraphDataForWeek() {
//		List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
//				LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
//				LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
//				LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));
//		List<Statistics> stats = new ArrayList<Statistics>();
//		Statistics statistics = null;
//		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
//		GraphData graph = new GraphData();
//		for (int i = 0; i < 7; i++) {
//			Date current = new Date(date + (ONE_DAY * i));
//			statistics = statisticsRepository.findOne(current);
//			if (statistics == null) {
//				statistics = new Statistics(current);
//			}
//			stats.add(statistics);
//		}
//		for (Statistics stat : stats) {
//		//	graph.alertas.add(stat.getAlertas());
//			graph.noticias.add(stat.getNoticias());
//			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
//					.concat(String.valueOf(stat.getFecha().getDate())));
//		}
//		graph.labels.remove(graph.labels.size() - 1);
//		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));
//		return graph;
//	}
//
//	@SuppressWarnings("deprecation")
//	@ResponseBody
//	@RequestMapping("/ajax/scrap/alert/week2")
//	public GraphData getAlertGraphDataForWeek2() {
//
//		List<Statistics> statsexp = new ArrayList<Statistics>();
//		Statistics statisticsexp = null;
//
//		//MEto los primeros 15 dias para estadisticas
//		Long dateext = System.currentTimeMillis() - (ONE_DAY * 15);
//		for (int i = 0; i < 7; i++) {
//			Date current = new Date(dateext + (ONE_DAY * i));
//			statisticsexp = statisticsRepository.findOne(current);
//			if (statisticsexp == null) {
//				statisticsexp = new Statistics(current);
//			}
//			statsexp.add(statisticsexp);
//		}
//
//
//		 SimpleRegression simpleRegression = new SimpleRegression(true);
//
//		 for (int i = 0; i < statsexp.size(); i++) {
//				Statistics stat=statsexp.get(i);
//
//				 Double Y = (double)i;
//				 Double X = new Double(stat.getAlertas());
//				 simpleRegression.addData(Y,X);
//			}
//
//
//		//MEto los primeros 15 dias para estadisticas
//
//
//
//		 List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
//					LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
//					LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
//					LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));
//		List<Statistics> stats = new ArrayList<Statistics>();
//		Statistics statistics = null;
//		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
//		GraphData graph = new GraphData();
//		for (int i = 0; i < 7; i++) {
//			Date current = new Date(date + (ONE_DAY * i));
//			statistics = statisticsRepository.findOne(current);
//			if (statistics == null) {
//				statistics = new Statistics(current);
//			}
//			stats.add(statistics);
//		}
//		for (int i = 0; i < stats.size(); i++) {
//			Statistics stat=stats.get(i);
//
//			 Double Y = (double)i+7;
//			 Double X = new Double(stat.getAlertas());
//
//			 double statidst = X;
////			 if (i!=0&&i!=1)
////			 {
//				 statidst=simpleRegression.predict(i+7);
//					DecimalFormat df = new DecimalFormat("#.####");
//					df.setRoundingMode(RoundingMode.CEILING);
//					statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
//					if (statidst<0d)
//						statidst=0d;
////			 }
//			graph.alertas.add(stat.getAlertas());
//			graph.alertasexp.add(statidst);
//			//graph.noticias.add(stat.getNoticias());
//			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
//					.concat(String.valueOf(stat.getFecha().getDate())));
//
//			simpleRegression.addData(Y,X);
//		}
//		graph.labels.remove(graph.labels.size() - 1);
//		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));
//
//		double statidst = simpleRegression.predict(graph.labels.size()+8);
//		DecimalFormat df = new DecimalFormat("#.####");
//		df.setRoundingMode(RoundingMode.CEILING);
//		statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
//		if (statidst<0d)
//			statidst=0d;
//	graph.alertasexp.add(statidst);
//	graph.labels.add(LanguageLoad.getinstance().find("web/base/tomorrow"));
//
//
//		return graph;
//	}
//
//	@ResponseBody
//	@RequestMapping("/ajax/scrap/alert/by/week1")
//	public GraphData getAlertGraphDataByWeek1() {
//		GraphData graph = new GraphData();
//		List<Object[]> stats = statisticsRepository.getStatsByWeek();
//		int i = 0;
//		for (Object[] stat : stats) {
//			//graph.alertas.add(stat[3].toString());
//			graph.noticias.add(stat[0].toString());
//			graph.labels.add(LanguageLoad.getinstance().find("web/base/week")+" " + stat[5]);
//			if (i >= 5)
//				break;
//			i++;
//		}
//
//		Collections.reverse(graph.labels);
//		Collections.reverse(graph.noticias);
//
//		return graph;
//
//
//
//	}
//
//	@ResponseBody
//	@RequestMapping("/ajax/scrap/alert/by/week2")
//	public GraphData getAlertGraphDataByWeek2() {
//
//		List<Integer> listaEst=new ArrayList<Integer>();
//
//		GraphData graph = new GraphData();
//		List<Object[]> stats = statisticsRepository.getStatsByWeek();
//		int i = 0;
//		boolean previoI=false;
////		String sema=null;
//		for (Object[] stat : stats) {
//			if (!previoI)
//			{
//			graph.alertas.add(stat[3].toString());
//			//graph.noticias.add(stat[0].toString());
//			graph.labels.add(LanguageLoad.getinstance().find("web/base/week")+" " + stat[5]);
////			if (sema==null)
////				sema=stat[5].toString();
//			}
//
//			Integer valex=0;
//			try {
//				valex=Integer.parseInt(stat[3].toString());
//			} catch (Exception e) {
//
//			}
//			listaEst.add(valex);
//
//			if (i >= 5)
//				{
//				if (previoI)
//					break;
//				else
//					previoI=true;
//				}
//			i++;
//		}
//
//		if (listaEst.size()<5)
//			listaEst.add(0);
//		Collections.reverse(listaEst);
//
//		List<Double> expected=new ArrayList<Double>();
//		SimpleRegression simpleRegression = new SimpleRegression(true);
//
//		 for (int j = 0; j < listaEst.size(); j++) {
//				Integer stat=listaEst.get(j);
//
//				 Double Y = (double)j;
//				 Double X = new Double(stat);
//
//				double expect=X;
//				if (j!=0&&j!=1)
//				 	{
//					    expect = simpleRegression.predict(j);
//						DecimalFormat df = new DecimalFormat("#.####");
//						df.setRoundingMode(RoundingMode.CEILING);
//						expect=Double.parseDouble(df.format(expect).replace(",", "."));
//						if (X<0d)
//							X=0d;
//
//				 	}
//
//				if (j!=0)
//					 expected.add(expect);
//
//				 simpleRegression.addData(Y,X);
//
//
//
//
//
//			}
//
//		 Collections.reverse(expected);
//
//		 for (Double double1 : expected) {
//			 graph.alertasexp.add(double1);
//		}
//
//
//		 Collections.reverse(graph.alertas);
//		 Collections.reverse(graph.labels);
//		 Collections.reverse(graph.alertasexp);
//
//		 double statidst = simpleRegression.predict(graph.labels.size()+1);
//			DecimalFormat df = new DecimalFormat("#.####");
//			df.setRoundingMode(RoundingMode.CEILING);
//			statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
//			if (statidst<0d)
//				statidst=0d;
//		graph.alertasexp.add(statidst);
//
//
//		String SemanaFuturo=LanguageLoad.getinstance().find("web/base/futureweek");
//		graph.labels.add(SemanaFuturo);
//
//
//
//
//
//
//
//		return graph;
//	}
//
//	@SuppressWarnings("deprecation")
//	@ResponseBody
//	@RequestMapping("/ajax/scrap/alert/trend")
//	public GraphData getAlertGraphDataTrend() {
//		TrendLine t = new PolyTrendLine(2);
//		GraphData graph = new GraphData();
//		List<Statistics> stats = (List<Statistics>) statisticsRepository
//				.findAll();
//		double[] x = new double[stats.size()];
//		double[] y = new double[stats.size()];
//		int i = 0;
//		for (Statistics stat : stats) {
//			try {
//				if (stat.getAlertas() > 200)
//					continue;
//				x[i] = new Double(stat.getFecha().getTime() / 86400000);
//				y[i] = new Double(stat.getAlertas());
//				i++;
//			} catch (Exception e) {
//				//  Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		t.setValues(y, x);
//		for (int z = 365; z >= 0; z--) {
//			Date date = new Date(System.currentTimeMillis() - (z * ONE_DAY));
//			date.setDate(date.getDate() - z);
//			double p = t.predict(new Double(date.getTime() / 86400000));
//			graph.labels.add(date.toLocaleString());
//			graph.alertas.add(p);
//		}
//		return graph;
//	}
//
//	//TODO eee
//	@SuppressWarnings("deprecation")
//	@ResponseBody
//	@RequestMapping("/ajax/scrap/risk/week1")
//	public GraphData getRiskGraphDataForWeek() {
//		List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
//				LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
//				LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
//				LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));
//		List<Statistics> stats = new ArrayList<Statistics>();
//		Statistics statistics = null;
//		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
//		GraphData graph = new GraphData();
//		for (int i = 0; i < 7; i++) {
//			Date current = new Date(date + (ONE_DAY * i));
//			statistics = statisticsRepository.findOne(current);
//			if (statistics == null) {
//				statistics = new Statistics(current);
//			}
//			stats.add(statistics);
//		}
//		for (Statistics stat : stats) {
//			//graph.alertas.add(stat.getRiesgos());
//			graph.noticias.add(stat.getNoticias());
//			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
//					.concat(String.valueOf(stat.getFecha().getDate())));
//		}
//		graph.labels.remove(graph.labels.size() - 1);
//		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));
//		return graph;
//	}
//
//
//	@SuppressWarnings("deprecation")
//	@ResponseBody
//	@RequestMapping("/ajax/scrap/risk/week2")
//	public GraphData getRiskGraphDataForWeek2() {
//
//		List<Statistics> statsexp = new ArrayList<Statistics>();
//		Statistics statisticsexp = null;
//
//		//MEto los primeros 15 dias para estadisticas
//				Long dateext = System.currentTimeMillis() - (ONE_DAY * 15);
//				for (int i = 0; i < 7; i++) {
//					Date current = new Date(dateext + (ONE_DAY * i));
//					statisticsexp = statisticsRepository.findOne(current);
//					if (statisticsexp == null) {
//						statisticsexp = new Statistics(current);
//					}
//					statsexp.add(statisticsexp);
//				}
//
//
//				 SimpleRegression simpleRegression = new SimpleRegression(true);
//
//				 for (int i = 0; i < statsexp.size(); i++) {
//						Statistics stat=statsexp.get(i);
//
//						 Double Y = (double)i;
//						 Double X = new Double(stat.getRiesgos());
//						 simpleRegression.addData(Y,X);
//					}
//
//
//				//MEto los primeros 15 dias para estadisticas
//
//
//				 List<String> dias = Lists.newArrayList(LanguageLoad.getinstance().find("web/base/sunday"),
//							LanguageLoad.getinstance().find("web/base/monday"),LanguageLoad.getinstance().find("web/base/tuesday"),
//							LanguageLoad.getinstance().find("web/base/wednesday"), LanguageLoad.getinstance().find("web/base/thursday"),
//							LanguageLoad.getinstance().find("web/base/friday"), LanguageLoad.getinstance().find("web/base/saturday"));
//		List<Statistics> stats = new ArrayList<Statistics>();
//		Statistics statistics = null;
//		Long date = System.currentTimeMillis() - (ONE_DAY * 6);
//		GraphData graph = new GraphData();
//		for (int i = 0; i < 7; i++) {
//			Date current = new Date(date + (ONE_DAY * i));
//			statistics = statisticsRepository.findOne(current);
//			if (statistics == null) {
//				statistics = new Statistics(current);
//			}
//			stats.add(statistics);
//		}
//		for (int i = 0; i < stats.size(); i++) {
//			Statistics stat=stats.get(i);
//
//			 Double Y = (double)i+7;
//			 Double X = new Double(stat.getRiesgos());
//
//			 double statidst = X;
//
//				 statidst=simpleRegression.predict(i+7);
//					DecimalFormat df = new DecimalFormat("#.####");
//					df.setRoundingMode(RoundingMode.CEILING);
//					statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
//					if (statidst<0d)
//						statidst=0d;
//
//
//			graph.alertas.add(stat.getRiesgos());
//			//graph.noticias.add(stat.getNoticias());
//			graph.alertasexp.add(statidst);
//			graph.labels.add(dias.get(stat.getFecha().getDay()).concat(" ")
//					.concat(String.valueOf(stat.getFecha().getDate())));
//
//			simpleRegression.addData(Y,X);
//		}
//		graph.labels.remove(graph.labels.size() - 1);
//		graph.labels.add(LanguageLoad.getinstance().find("web/base/today"));
//
//		double statidst = simpleRegression.predict(graph.labels.size()+8);
//		DecimalFormat df = new DecimalFormat("#.####");
//		df.setRoundingMode(RoundingMode.CEILING);
//		statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
//		if (statidst<0d)
//			statidst=0d;
//	graph.alertasexp.add(statidst);
//	graph.labels.add(LanguageLoad.getinstance().find("web/base/tomorrow"));
//
//		return graph;
//	}
//
//
//	@ResponseBody
//	@RequestMapping("/ajax/scrap/risk/by/week1")
//	public GraphData getRiskGraphDataByWeek1() {
//		GraphData graph = new GraphData();
//		List<Object[]> stats = statisticsRepository.getStatsByWeek();
//		int i = 0;
//		for (Object[] stat : stats) {
////			graph.alertas.add(stat[2].toString());
//			graph.noticias.add(stat[0].toString());
//			graph.labels.add(LanguageLoad.getinstance().find("web/base/week")+" " + stat[5]);
//			if (i >= 5)
//				break;
//			i++;
//		}
//
//		Collections.reverse(graph.labels);
//		Collections.reverse(graph.noticias);
//
//		return graph;
//	}
//
//
//	//TODO
//	@ResponseBody
//	@RequestMapping("/ajax/scrap/risk/by/week2")
//	public GraphData getRiskGraphDataByWeek2() {
//
//
//List<Integer> listaEst=new ArrayList<Integer>();
//
//		GraphData graph = new GraphData();
//		List<Object[]> stats = statisticsRepository.getStatsByWeek();
//		int i = 0;
//		boolean previoI=false;
////		String sema=null;
//		for (Object[] stat : stats) {
//			if (!previoI)
//			{
//			graph.alertas.add(stat[2].toString());
//			//graph.noticias.add(stat[0].toString());
//			graph.labels.add(LanguageLoad.getinstance().find("web/base/week")+" " + stat[5]);
////			if (sema==null)
////				sema=stat[5].toString();
//			}
//
//			Integer valex=0;
//			try {
//				valex=Integer.parseInt(stat[2].toString());
//			} catch (Exception e) {
//
//			}
//			listaEst.add(valex);
//
//			if (i >= 5)
//				{
//				if (previoI)
//					break;
//				else
//					previoI=true;
//				}
//			i++;
//		}
//
//		if (listaEst.size()<5)
//			listaEst.add(0);
//		Collections.reverse(listaEst);
//
//		List<Double> expected=new ArrayList<Double>();
//		SimpleRegression simpleRegression = new SimpleRegression(true);
//
//		 for (int j = 0; j < listaEst.size(); j++) {
//				Integer stat=listaEst.get(j);
//
//				 Double Y = (double)j;
//				 Double X = new Double(stat);
//
//				double expect=X;
//				if (j!=0&&j!=1)
//				 	{
//					    expect = simpleRegression.predict(j);
//						DecimalFormat df = new DecimalFormat("#.####");
//						df.setRoundingMode(RoundingMode.CEILING);
//						expect=Double.parseDouble(df.format(expect).replace(",", "."));
//						if (X<0d)
//							X=0d;
//
//				 	}
//
//				if (j!=0)
//					 expected.add(expect);
//
//				 simpleRegression.addData(Y,X);
//
//
//
//
//
//			}
//
//		 Collections.reverse(expected);
//
//		 for (Double double1 : expected) {
//			 graph.alertasexp.add(double1);
//		}
//
//
//		 Collections.reverse(graph.alertas);
//		 Collections.reverse(graph.labels);
//		 Collections.reverse(graph.alertasexp);
//
//		 double statidst = simpleRegression.predict(graph.labels.size()+1);
//			DecimalFormat df = new DecimalFormat("#.####");
//			df.setRoundingMode(RoundingMode.CEILING);
//			statidst=Double.parseDouble(df.format(statidst).replace(",", "."));
//			if (statidst<0d)
//				statidst=0d;
//		graph.alertasexp.add(statidst);
//
//
//		String SemanaFuturo=LanguageLoad.getinstance().find("web/base/futureweek");
//		graph.labels.add(SemanaFuturo);
//
//
//
//
//
//
//
//		return graph;
//
//	}
//
//	@ModelAttribute("month")
//	public GraphData getGraphDataForMonth() {
//		GraphData graph = new GraphData();
//		// List<Object[]> stats = statisticsRepository.getStatsByWeek();
//		// while(stats.size()<4) {
//		// Object[] t = {((Integer)stats.get(0)[0])-1,0,0};
//		// stats.add(0, t);
//		// }
//		// for (Object[] stat : stats) {
//		// graph.alertas.add(stat[1]);
//		// graph.noticias.add(stat[2]);
//		// graph.labels.add("Semana " + (Integer)stat[0]);
//		// }
//		return graph;
//	}
//
//	@ModelAttribute("year")
//	public GraphData getGraphDataForYear() {
//		GraphData graph = new GraphData();
//		return graph;
//	}
//
//	@RequestMapping(value = { "", "/" })
//	public String checkAlert() {
//		return "resume";
//	}
//
//
//	@RequestMapping(value = "/query", method = RequestMethod.GET)
//	public String searchQueryTestForm(HttpServletRequest request, String query,
//                                      Model model) {
//		model.addAttribute("query", "");
//
//		SpecializedClassicSimilarity Symy=new SpecializedClassicSimilarity();
//
//		String formuGen = "Sl+(Wa+1)+(Wl+1)+(Ws+1)";
//		if (Symy.getGeneralFor()!=null&&!Symy.getGeneralFor().getSourceText().isEmpty())
//			formuGen=Symy.getGeneralFor().getSourceText();
//
//		String formuLucCoord = "";
//		if (Symy.getCoordFor()!=null&&!Symy.getCoordFor().getSourceText().isEmpty())
//			formuLucCoord=Symy.getCoordFor().getSourceText();
//
//		String formuLucQueryNorm = "";
//		if (Symy.getQueryNormFor()!=null&&!Symy.getQueryNormFor().getSourceText().isEmpty())
//			formuLucQueryNorm=Symy.getQueryNormFor().getSourceText();
//
//		boolean formuLucQueryNormL = Symy.isOriginal();
//
//		boolean formuLucQueryDisco = Symy.getDiscountOverlaps();
//
//		String formuLuctf = "";
//		if (Symy.getTfFor()!=null&&!Symy.getTfFor().getSourceText().isEmpty())
//			formuLuctf=Symy.getTfFor().getSourceText();
//
//		String formuLucidf = "";
//		if (Symy.getIdFor()!=null&&!Symy.getIdFor().getSourceText().isEmpty())
//			formuLucidf=Symy.getIdFor().getSourceText();
//
//
//
//		model.addAttribute("formuGen", formuGen);
//		model.addAttribute("formuLucCoord", formuLucCoord);
//		model.addAttribute("formuLucQueryNorm", formuLucQueryNorm);
//		model.addAttribute("formuLuctf", formuLuctf);
//		model.addAttribute("formuLucidf", formuLucidf);
//		model.addAttribute("AlertT", AlertLevelEnum.YELLOW);
//		model.addAttribute("BasicNor", formuLucQueryNormL);
//		model.addAttribute("DiscNor", formuLucQueryDisco);
//
//		if (formuLucQueryNorm.trim().isEmpty()||formuLucQueryNorm.trim().toLowerCase().equals("QueryNorm".toLowerCase()))
//		model.addAttribute("QueryNorm", true);
//		else
//			model.addAttribute("QueryNorm", false);
//
//
//		if (formuLucCoord.trim().isEmpty()||formuLucCoord.trim().toLowerCase().equals("coord".toLowerCase()))
//			model.addAttribute("Coord", true);
//			else
//				model.addAttribute("Coord", false);
//
//		if (formuLuctf.trim().isEmpty()||formuLuctf.trim().toLowerCase().equals("tf".toLowerCase()))
//			model.addAttribute("tf", true);
//			else
//				model.addAttribute("tf", false);
//
//		if (formuLucidf.trim().isEmpty()||formuLucidf.trim().toLowerCase().equals("idf".toLowerCase()))
//			model.addAttribute("idf", true);
//			else
//				model.addAttribute("idf", false);
//
//
////		this.menu = LanguageLoad.getinstance().find("web/query/welcome");
//
//		return "query";
//	}

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
	@RequestMapping(value = "/load", method = RequestMethod.POST)
	public String loadHistory(@RequestParam("feed") Feed feed,
			@RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) {
		if (feed==null) {
//			redirectAttributes.addFlashAttribute("error",
//					LanguageLoad.getinstance().find("web/base/error/feedempty"));
		} else if (!file.isEmpty()) {
			try {
//				List<News> listNews = feedScraping.scrapingHistoric(feed, file.getInputStream());
//				redirectAttributes.addFlashAttribute("news",listNews);
//				redirectAttributes.addFlashAttribute("info",
//						LanguageLoad.getinstance().find("web/base/info/storednewscorrect"));
			} catch (Exception e) {
//				redirectAttributes.addFlashAttribute(
//						"error",
//						LanguageLoad.getinstance().find("web/base/error/errorprocessfile"));
			}
		} else {
//			redirectAttributes.addFlashAttribute("error",LanguageLoad.getinstance().find("web/base/error/erroremptyfile"));
		}

		return "redirect:/load";
	}
}
