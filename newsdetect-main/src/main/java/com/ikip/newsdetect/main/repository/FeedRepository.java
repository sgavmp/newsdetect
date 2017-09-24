package com.ikip.newsdetect.main.repository;

import es.ucm.visavet.gbf.app.domain.Feed;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends PagingAndSortingRepository<Feed, Long> {

	public List<Feed> findAllByOrderByNameAsc();
	public List<Feed> findAllByOrderByUltimaRecuperacionAsc();
}
