package com.ikip.newsdetect.main.controller;

import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.UserDetailsImpl;
import es.ucm.visavet.gbf.app.util.MD5Util;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
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
	
	@ModelAttribute("yes")
	public String getYesText() {
		return LanguageLoad.getinstance().find("web/base/yes");
	}
	
	@ModelAttribute("no")
	public String getNoText() {
		return LanguageLoad.getinstance().find("web/base/no");
	}
		
	
	@ModelAttribute("loginout")
	public String getloginoutOutText() {
		return LanguageLoad.getinstance().find("web/base/loginout");
	}
	
	
	@ModelAttribute("outmenu")
	public String getOutText() {
		return LanguageLoad.getinstance().find("web/base/outmenu");
	}
	
	@ModelAttribute("anonimmenu")
	public String getAnnonimText() {
		return LanguageLoad.getinstance().find("web/base/anonimmenu");
	}
	
	@ModelAttribute("accesmenu")
	public String getAccesText() {
		return LanguageLoad.getinstance().find("web/base/accesmenu");
	}
	
	@ModelAttribute("controlpanelmenu")
	public String getControlPanelText() {
		return LanguageLoad.getinstance().find("web/base/controlpanelmenu");
	}
	
	@ModelAttribute("alertassanitariasmenu")
	public String getAlertasSanitariasText() {
		return LanguageLoad.getinstance().find("web/base/alertassanitariasmenu");
	}
	
	@ModelAttribute("alertactivemenu")
	public String getAlertActiveText() {
		return LanguageLoad.getinstance().find("web/base/alertactivemenu");
	}
	
	@ModelAttribute("alertlistedmenu")
	public String getAlertListedText() {
		return LanguageLoad.getinstance().find("web/base/alertlistedmenu");
	}
	
	@ModelAttribute("riesgossanitariasmenu")
	public String getRiesgosSanitariasText() {
		return LanguageLoad.getinstance().find("web/base/riesgossanitariasmenu");
	}
	
	@ModelAttribute("riesgosactivemenu")
	public String getRiesgosActiveText() {
		return LanguageLoad.getinstance().find("web/base/riesgosactivemenu");
	}
	
	@ModelAttribute("riesgoslistedmenu")
	public String getRiesgosListedText() {
		return LanguageLoad.getinstance().find("web/base/riesgoslistedmenu");
	}
	
	@ModelAttribute("websitesmenu")
	public String getWebsitesText() {
		return LanguageLoad.getinstance().find("web/base/websitesmenu");
	}
	
	@ModelAttribute("provedoresmenu")
	public String getProveedoresText() {
		return LanguageLoad.getinstance().find("web/base/provedoresmenu");
	}
	
	@ModelAttribute("topicsmenu")
	public String getTopicsText() {
		return LanguageLoad.getinstance().find("web/base/topicsmenu");
	}
	
	@ModelAttribute("querysmenu")
	public String getQuerysText() {
		return LanguageLoad.getinstance().find("web/base/querysmenu");
	}

	@ModelAttribute("testsalertsmenu")
	public String getPruebaText() {
		return LanguageLoad.getinstance().find("web/base/testsalertsmenu");
	}
	
	@ModelAttribute("iosystemsmenu")
	public String getIOText() {
		return LanguageLoad.getinstance().find("web/base/iosystemsmenu");
	}
	
	@ModelAttribute("twohundredtwo")
	public String get202() {
		return LanguageLoad.getinstance().find("web/response/twohundredtwo");
	}
	
	@ModelAttribute("fourhundred1")
	public String get204_1() {
		return LanguageLoad.getinstance().find("web/response/fourhundred1");
	}
	
	@ModelAttribute("fourhundred2")
	public String get204_2() {
		return LanguageLoad.getinstance().find("web/response/fourhundred2");
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
	
	
	@ModelAttribute("alertfinalscore")
	public String getAlertFinalScoreText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertfinalscore");
	}
	
	@ModelAttribute("alertinitialscore")
	public String getAlertInitScoreText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertinitialscore");
	}
	
	@ModelAttribute("alertsite")
	public String getAlertSiteText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertsite");
	}
	
	@ModelAttribute("alertlocation")
	public String getAlertLocationText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertlocation");
	}
	
	@ModelAttribute("alertdeletealertone")
	public String getAlertDeleteOneText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertdeletealertone");
	}
	
	@ModelAttribute("alertmarkactive")
	public String getAlertMarkAsActiveText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertmarkactive");
	}
	
	
	@ModelAttribute("alertmarkaspass")
	public String getAlertMarkAsPassText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertmarkaspass");
	}
	
	
	@ModelAttribute("alerttermfound")
	public String getAlertTermFoundText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alerttermfound");
	}
	
	
	@ModelAttribute("alertsureactive")
	public String getAlertSureActiveText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertsureactive");
	}
	
	@ModelAttribute("alertsurefalsepositive")
	public String getAlertSureFalsePositiveText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertsurefalsepositive");
	}
	
	@ModelAttribute("alertsurepass")
	public String getAlertSurePassText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertsurepass");
	}
	
	@ModelAttribute("alertstascore")
	public String getAlertScoreStaText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertstascore");
	}
	
	@ModelAttribute("alertstaday")
	public String getAlertDayStaText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertstaday");
	}
	
	@ModelAttribute("alertminmax")
	public String getAlertMinMaxStaText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertminmax");
	}
	
	@ModelAttribute("alertstacompara")
	public String getAlertCompareStaText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertstacompara");
	}
	
	@ModelAttribute("alertstamonth")
	public String getAlertMonthStaText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertstamonth");
	}

	@ModelAttribute("dayalertnalerts")
	public String getAlertNElemTexto() {
		return LanguageLoad.getinstance().find("web/alertrisk/daystats");
	}
	
	@ModelAttribute("dayoftheweek")
	public String getAlertdayof() {
		return LanguageLoad.getinstance().find("web/alertrisk/dayoftheweek");
	}
	
	@ModelAttribute("alertrealavg")
	public String getAlertAlertRealAVG() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertrealavg");
	}
	
	@ModelAttribute("alertexpectednextweek")
	public String getAlertAlertExpectedNext() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertexpectednextweek");
	}
	
	@ModelAttribute("alertnewst")
	public String getAlertNewsTittle() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertnewst");
	}
	
	
	
	
	@ModelAttribute("alerttoday")
	public String getAlertTodayText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alerttoday");
	}
	
	@ModelAttribute("alertyesterday")
	public String getAlertYesterdayText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertyesterday");
	}
	
	@ModelAttribute("alertactive")
	public String getAlertActiveOneText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertactive");
	}
	
	@ModelAttribute("alerthistorial")
	public String getAlertHisotrialText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alerthistorial");
	}
	
	@ModelAttribute("alertfalsepositive")
	public String getAlertFalsePositiveText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertfalsepositive");
	}
	
	@ModelAttribute("alertasname")
	public String getAlertnameText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertasname");
	}
	
	@ModelAttribute("alertasscore")
	public String getAlertScoreText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertasscore");
	}
	
	@ModelAttribute("alertsthisweeks")
	public String getAlertRiskthisweekText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertsthisweeks");
	}
	
	@ModelAttribute("alertsthismonth")
	public String getAlertRiskthismonthText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertsthismonth");
	}
	
	@ModelAttribute("alertasmap")
	public String getAlertRiskMapText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertasmap");
	}
	
	@ModelAttribute("alertasstadistics")
	public String getAlertRiskStadisticsText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertasstadistics");
	}
	
	@ModelAttribute("alertreal")
	public String getAlertRealText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertreal");
	}
	
	@ModelAttribute("alertexpected")
	public String getAlertExpectedText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertexpected");
	}
	
	@ModelAttribute("alertintrioducequery")
	public String getAlertQueryText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertintrioducequery");
	}
	
	@ModelAttribute("alertintrioducequerytype")
	public String getAlertQueryLevelTypeText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertintrioducequerytype");
	}
	
	@ModelAttribute("alertborrar")
	public String getAlertDelete() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertborrar");
	}
	
	@ModelAttribute("alertreset")
	public String getAlertReset() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertreset");
	}
	
	@ModelAttribute("alertsend")
	public String getAlertSend() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertsend");
	}
	
	@ModelAttribute("alertcancel")
	public String getAlertCancel() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertcancel");
	}
	
	@ModelAttribute("alertnews")
	public String getalertNewsText() {
		return LanguageLoad.getinstance().find("web/alertrisk/alertnews");
	}
	
	
	
	@ModelAttribute("websitetittle")
	public String getWebsiteTittleText() {
		return LanguageLoad.getinstance().find("web/alertrisk/websitetittle");
	}
	
	@ModelAttribute("websitetype")
	public String getWebsiteTypeText() {
		return LanguageLoad.getinstance().find("web/alertrisk/websitetype");
	}
	
	@ModelAttribute("websitedatecreation")
	public String getWebsitedateCreationText() {
		return LanguageLoad.getinstance().find("web/alertrisk/websitedatecreation");
	}
	
	@ModelAttribute("websitedateedition")
	public String getWebsitedateEditionText() {
		return LanguageLoad.getinstance().find("web/alertrisk/websitedateedition");
	}
	
	@ModelAttribute("websiteedition")
	public String getWebsiteEditionText() {
		return LanguageLoad.getinstance().find("web/alertrisk/websiteedition");
	}
	
	@ModelAttribute("websiteborrar")
	public String getWebsiteDeleteText() {
		return LanguageLoad.getinstance().find("web/alertrisk/websiteborrar");
	}
	
	@ModelAttribute("feedclose")
	public String getFeedCloseText() {
		return LanguageLoad.getinstance().find("web/feed/feedclose");
	}
	
	@ModelAttribute("feedgo")
	public String getFeedGoText() {
		return LanguageLoad.getinstance().find("web/feed/feedgo");
	}
	
	@ModelAttribute("feedcreate")
	public String getFeedCreateText() {
		return LanguageLoad.getinstance().find("web/feed/feedcreate");
	}
	
	@ModelAttribute("feededitf")
	public String getFeededitText() {
		return LanguageLoad.getinstance().find("web/feed/feededitf");
	}
	
	@ModelAttribute("feedswauto")
	public String getFeedswAutoText() {
		return LanguageLoad.getinstance().find("web/feed/feedswauto");
	}
	
	@ModelAttribute("feedswrss")
	public String getFeedSWRssText() {
		return LanguageLoad.getinstance().find("web/feed/feedswrss");
	}
	
	@ModelAttribute("feedswactivated")
	public String getFeedSWActiveText() {
		return LanguageLoad.getinstance().find("web/feed/feedswactivated");
	}

	@ModelAttribute("feedsnamesite")
	public String getFeedNameSiteText() {
		return LanguageLoad.getinstance().find("web/feed/feedsnamesite");
	}
	
	@ModelAttribute("feedurl")
	public String getFeedUrlText() {
		return LanguageLoad.getinstance().find("web/feed/feedurl");
	}
	
	@ModelAttribute("feedfirspage")
	public String getFeedFirstPageSiteText() {
		return LanguageLoad.getinstance().find("web/feed/feedfirspage");
	}
	
	@ModelAttribute("feedplacesof")
	public String getFeedPlaceofText() {
		return LanguageLoad.getinstance().find("web/feed/feedplacesof");
	}
	
	@ModelAttribute("feedtipesof")
	public String getFeedfirstText() {
		return LanguageLoad.getinstance().find("web/feed/feedtipesof");
	}
	
	@ModelAttribute("feedfiability")
	public String getFiabilityText() {
		return LanguageLoad.getinstance().find("web/feed/feedfiability");
	}
	
	@ModelAttribute("feedlang")
	public String getFeedlangText() {
		return LanguageLoad.getinstance().find("web/feed/feedlang");
	}
	
	@ModelAttribute("feeddateformath")
	public String getFeeddateFormText() {
		return LanguageLoad.getinstance().find("web/feed/feeddateformath");
	}
	
	@ModelAttribute("feedclasscss")
	public String getFeedClasCssText() {
		return LanguageLoad.getinstance().find("web/feed/feedclasscss");
	}
	
	@ModelAttribute("feedformsave")
	public String getFeedFormSaveText() {
		return LanguageLoad.getinstance().find("web/feed/feedformsave");
	}

	@ModelAttribute("feedformtestsite")
	public String getFeedFormTestSiteText() {
		return LanguageLoad.getinstance().find("web/feed/feedformtestsite");
	}
	
	@ModelAttribute("feedalertdetectednew")
	public String getFeedAlertDetectedNewText() {
		return LanguageLoad.getinstance().find("web/feed/feedalertdetectednew");
	}
	
	@ModelAttribute("feedprovedafect")
	public String getFeedAfectSourceText() {
		return LanguageLoad.getinstance().find("web/feed/feedprovedafect");
	}
	
	@ModelAttribute("feeddescart")
	public String getFeedDescartText() {
		return LanguageLoad.getinstance().find("web/feed/feeddescart");
	}
	
	@ModelAttribute("feedpress")
	public String getFeedPressText() {
		return LanguageLoad.getinstance().find("web/feed/feedpress");
	}
	
	@ModelAttribute("feedalertdetectedbythesite")
	public String getFeedDetectedbySiteText() {
		return LanguageLoad.getinstance().find("web/feed/feedalertdetectedbythesite");
	}
	
	@ModelAttribute("feedonesite")
	public String getFeedSiteText() {
		return LanguageLoad.getinstance().find("web/feed/feedonesite");
	}
	
	@ModelAttribute("feedonesuredelete")
	public String getFeedSureDeleteText() {
		return LanguageLoad.getinstance().find("web/feed/feedonesuredelete");
	}
	
	@ModelAttribute("feedonesureresolve")
	public String getFeedSureResolveText() {
		return LanguageLoad.getinstance().find("web/feed/feedonesureresolve");
	}
	
	
	
	
	@ModelAttribute("avatar")
	public String getAvatarHash(@AuthenticationPrincipal UserDetailsImpl activeUser) {
		if (activeUser!= null)
			return "http://www.gravatar.com/avatar/" + MD5Util.md5Hex(activeUser.getEmail());
		else
			return "";
	}
	
	@ModelAttribute("menu")
	public String getMenuActive() {
		return menu;
	}

}
