package com.ikip.newsdetect.main.repository;

import es.ucm.visavet.gbf.app.domain.News;
import org.springframework.data.repository.CrudRepository;

public interface NewsRepository extends CrudRepository<News, Long> {
	

}
