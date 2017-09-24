package com.ikip.newsdetect.main.repository;

import es.ucm.visavet.gbf.app.domain.Alert;
import es.ucm.visavet.gbf.app.domain.Feed;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface AlertRepository extends PagingAndSortingRepository<Alert, Long> {
	public Set<Alert> readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalseAndNewsDetectDatePubGreaterThanEqualOrderByCreateDateDesc(Date date);
	public Set<Alert> readAllDistinctByNewsDetectSite(Feed Site);
	public Set<Alert> readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalse();
	public Set<Alert> readAllDistinctByNewsDetectHistoryTrue();
	public Set<Alert> readAllDistinctByNewsDetectFalPositiveTrue();
	public Set<Alert> findAllByOrderByTitleAsc();
}
