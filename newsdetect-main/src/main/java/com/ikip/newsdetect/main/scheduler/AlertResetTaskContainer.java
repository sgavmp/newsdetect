package com.ikip.newsdetect.main.scheduler;

import es.ucm.visavet.gbf.app.LanguageLoad;
import es.ucm.visavet.gbf.app.domain.AlertAbstract;
import es.ucm.visavet.gbf.app.service.NewsIndexService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class AlertResetTaskContainer implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(AlertResetTaskContainer.class);
	
	private AlertAbstract alert;
	@Autowired
	private NewsIndexService newsIndexService;
	
	
	public AlertResetTaskContainer(AlertAbstract alert) {
		this.alert = alert;
	}

	@Override
	public void run() {
		LOGGER.info(LanguageLoad.getinstance().find("internal/scheduler/info/alertresetinittask") + alert.getTitle());
		try {
			newsIndexService.resetAlert(alert);
		} catch (IOException e) {
			LOGGER.error(LanguageLoad.getinstance().find("internal/scheduler/error/alertreseterrorinittask") + alert.getTitle());
		}
		LOGGER.info(LanguageLoad.getinstance().find("internal/scheduler/info/alertresetendtask") + alert.getTitle());
		
	}

}
