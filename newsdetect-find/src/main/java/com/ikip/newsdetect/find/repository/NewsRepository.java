package com.ikip.newsdetect.find.repository;

import com.ikip.newsdetect.model.DetectedNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("newsRepositoryFind")
public interface NewsRepository extends JpaRepository<DetectedNews, Long> {

    List<DetectedNews> findAllByAlertId(Long alertId);
}
