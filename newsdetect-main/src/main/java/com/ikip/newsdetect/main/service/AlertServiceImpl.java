package com.ikip.newsdetect.main.service;

import com.ikip.newsdetect.find.service.NewsIndexService;
import com.ikip.newsdetect.main.repository.AlertRepository;
import com.ikip.newsdetect.main.repository.LocationRepository;
import com.ikip.newsdetect.model.Alert;
import com.ikip.newsdetect.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

public class AlertServiceImpl {
	
	@Autowired
	private AlertRepository repository;
	
	@Autowired
	private NewsIndexService newsIndexService;
	
	@Autowired
	private LocationRepository locationRepository;
	
	public Set<Alert> getAllAlert() {
		return repository.findAllByOrderByTitleAsc();
	}
	
	public Alert getOneById(Long word) {
		return repository.findOne(word);
	}
	
	public Alert update(Alert word) {
		return repository.save(word);
	}
	
	public Alert create(Alert word) throws IOException {
		Alert alert = repository.save(word);
		//newsIndexService.resetAlert(alert);
		return alert;
	}
	
	public void remove(Alert word) {
		repository.delete(word);
	}
	
	public void remove(Long word) {
		repository.delete(word);
	}

	@SuppressWarnings("deprecation")
	public SortedMap<Date,List<Alert>> getAllAlertsOrderByDate() {
		SortedMap<Date,List<Alert>> alertasPorFecha = new TreeMap<Date, List<Alert>>(Collections.reverseOrder());
		for (Alert alert : repository.findAll()) {
			Date pubDate = new Date();//alert.getCreateDate();
			Date day = new Date(pubDate.getYear(),pubDate.getMonth(),pubDate.getDate());
			if (alertasPorFecha.containsKey(day)) {
				alertasPorFecha.get(day).add(alert);
			} else {
				List<Alert> alertas = new ArrayList<Alert>();
				alertas.add(alert);
				alertasPorFecha.put(day,alertas);
			}
		}
		return alertasPorFecha;
	}
	
	@SuppressWarnings("deprecation")
	public SortedMap<Date,List<Alert>> getAllAlertsByFeedOrderByDate(Feed feed) {
		SortedMap<Date,List<Alert>> alertasPorFecha = new TreeMap<Date, List<Alert>>(Collections.reverseOrder());
//		for (Alert alert : repository.readAllDistinctByNewsDetectSite(feed)) {
		for (Alert alert : repository.findAll()) {
			Date pubDate = new Date();//alert.getCreateDate();
			Date day = new Date(pubDate.getYear(),pubDate.getMonth(),pubDate.getDate());
			if (alertasPorFecha.containsKey(day)) {
				alertasPorFecha.get(day).add(alert);
			} else {
				List<Alert> alertas = new ArrayList<Alert>();
				alertas.add(alert);
				alertasPorFecha.put(day,alertas);
			}
		}
		return alertasPorFecha;
	}
	
	public Set<Alert> getAlertDetectActivatedAfter(Date date) {
//		return repository.readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalseAndNewsDetectDatePubGreaterThanEqualOrderByCreateDateDesc(date);
		return repository.findAllByOrderByTitleAsc();
	}
	
	public Set<Alert> getAlertDetectSite(Feed feed) {
//		return repository.readAllDistinctByNewsDetectSite(feed);
		return repository.findAllByOrderByTitleAsc();
	}
	
	public Set<Alert> getAllAlertActive() {
//		return repository.readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalse();
		return repository.findAllByOrderByTitleAsc();
	}
	
	public Set<Alert> getAllAlertWithHistory() {
//		return repository.readAllDistinctByNewsDetectHistoryTrue();
		return repository.findAllByOrderByTitleAsc();
	}
	
	public Set<Alert> getAllAlertWithFalsePositive() {
//		return repository.readAllDistinctByNewsDetectFalPositiveTrue();
		return repository.findAllByOrderByTitleAsc();
	}
	
//	public Alert setNewsLocation(Alert alert,List<Location> location) {
//		for (DetectedNews news : alert.getNewsDetect()) {
//			news.setLocation(location);
//		}
//		return alert;
//	}

	public Alert findAlert(Long id) {
		try {
			Alert Salida=repository.findOne(id);
			return Salida;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
}
