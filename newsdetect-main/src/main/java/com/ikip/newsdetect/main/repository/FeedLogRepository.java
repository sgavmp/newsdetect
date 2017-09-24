package com.ikip.newsdetect.main.repository;

import es.ucm.visavet.gbf.app.domain.FeedLog;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Set;

@Repository
public interface FeedLogRepository extends PagingAndSortingRepository<FeedLog, Long> {
	public Set<FeedLog> readAllByDiaregistro(Date date);
}
