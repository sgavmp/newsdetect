package com.ikip.newsdetect.main.repository;

import com.ikip.newsdetect.model.DetectedNews;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface NewsDetectRepository extends CrudRepository<DetectedNews, Long> {
	
	DetectedNews findFirstByAlertIdAndLink(Long alertId, String link);
	List<DetectedNews> findAllDistinctByFeedIdOrderByDatePubDesc(Long feedId);

}
