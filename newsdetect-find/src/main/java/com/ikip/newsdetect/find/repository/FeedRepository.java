package com.ikip.newsdetect.find.repository;

import com.ikip.newsdetect.model.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("feedRepositoryFind")
public interface FeedRepository extends JpaRepository<Feed, Long> {

    @Query(value = " SELECT * FROM feed_places p WHERE p.feed_id = :feedId ", nativeQuery = true)
    List<String> getPlacesByFeedId(Long feedId);
}
