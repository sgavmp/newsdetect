package com.ikip.newsdetect.extractor.repository;

import com.ikip.newsdetect.model.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedCrawlerUrlRepository extends JpaRepository<Feed, Long> {

    @Query(value = "SELECT t.crawler_link FROM feed_crawler_links l WHERE l.feed_id = :feedId ",nativeQuery = true)
    List<String> findAllCrawlerUrlByFeedId(Long feedId);
}
