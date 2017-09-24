package com.ikip.newsdetect.main.repository;

import es.ucm.visavet.gbf.app.domain.ScrapStatistics;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;

public interface ScrapStatisticsRepository extends CrudRepository<ScrapStatistics, Date> {

}
