package com.ikip.newsdetect.main.repository;

import com.ikip.newsdetect.model.News;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("newsRepository")
public interface NewsRepository extends CrudRepository<News, Long> {
	

}
