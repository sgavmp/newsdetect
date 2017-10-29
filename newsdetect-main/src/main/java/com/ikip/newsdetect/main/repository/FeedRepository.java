package com.ikip.newsdetect.main.repository;

import com.ikip.newsdetect.model.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource()
public interface FeedRepository extends JpaRepository<Feed, Long> {

	List<Feed> findAllByOrderByNameAsc();
}
