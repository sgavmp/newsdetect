package com.ikip.newsdetect.main.repository;

import com.ikip.newsdetect.model.DetectedNews;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsDetectRepository extends CrudRepository<DetectedNews, Long> {
	
	DetectedNews findFirstByAlertIdAndLink(Long alertId, String link);
	List<DetectedNews> findAllDistinctByFeedIdOrderByDatePubDesc(Long feedId);

}
