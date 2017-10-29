package com.ikip.newsdetect.main.repository;

import com.ikip.newsdetect.model.Alert;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Set;

@RepositoryRestResource
public interface AlertRepository extends PagingAndSortingRepository<Alert, Long> {
	Set<Alert> findAllByOrderByTitleAsc();
}
