package com.ikip.newsdetect.main.repository;

import es.ucm.visavet.gbf.app.domain.AlertAbstract;
import es.ucm.visavet.gbf.app.domain.Feed;
import es.ucm.visavet.gbf.app.domain.NewsDetect;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface NewsDetectRepository extends CrudRepository<NewsDetect, Long> {
	
	public NewsDetect findFirstByAlertDetectAndLink(AlertAbstract alertDetect, String link);
	public List<NewsDetect> findAllDistinctBySiteOrderByDatePubDesc(Feed site);

}
