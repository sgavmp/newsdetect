package com.ikip.newsdetect.main.repository;

import com.ikip.newsdetect.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
	Set<Alert> findAllByOrderByTitleAsc();
}
