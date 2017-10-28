package com.ikip.newsdetect.main.repository;

import com.ikip.newsdetect.model.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("feedRepository")
public interface FeedRepository extends JpaRepository<Feed, Long> {

	List<Feed> findAllByOrderByNameAsc();
}
