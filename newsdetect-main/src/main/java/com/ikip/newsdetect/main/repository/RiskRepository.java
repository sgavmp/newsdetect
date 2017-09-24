package com.ikip.newsdetect.main.repository;

import es.ucm.visavet.gbf.app.domain.Feed;
import es.ucm.visavet.gbf.app.domain.Risk;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface RiskRepository extends PagingAndSortingRepository<Risk, Long> {
	public Set<Risk> readAllByNewsDetectDatePubGreaterThanEqualOrderByCreateDateDesc(Date date);
	public Set<Risk> readAllDistinctByNewsDetectSite(Feed Site);
	public Set<Risk> readAllDistinctByNewsDetectHistoryFalseAndNewsDetectFalPositiveFalse();
	public Set<Risk> readAllDistinctByNewsDetectHistoryTrue();
	public Set<Risk> readAllDistinctByNewsDetectFalPositiveTrue();
	public Set<Risk> findAllByOrderByTitleAsc();
}
