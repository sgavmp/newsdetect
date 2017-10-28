package com.ikip.newsdetect.main.scheduler;

import com.ikip.newsdetect.find.service.NewsIndexService;
import com.ikip.newsdetect.model.Alert;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class AlertResetTaskContainer implements Runnable {

	private final static Logger LOGGER = Logger.getLogger(AlertResetTaskContainer.class);
	
	private Alert alert;
	@Autowired
	private NewsIndexService newsIndexService;
	
	
	public AlertResetTaskContainer(Alert alert) {
		this.alert = alert;
	}

	@Override
	public void run() {
//		LOGGER.info(LanguageLoad.getinstance().find("internal/scheduler/info/alertresetinittask") + alert.getTitle());
//		try {
//			newsIndexService.resetAlert(alert);
//		} catch (IOException e) {
////			LOGGER.error(LanguageLoad.getinstance().find("internal/scheduler/error/alertreseterrorinittask") + alert.getTitle());
//		}
//		LOGGER.info(LanguageLoad.getinstance().find("internal/scheduler/info/alertresetendtask") + alert.getTitle());
		
	}

}
