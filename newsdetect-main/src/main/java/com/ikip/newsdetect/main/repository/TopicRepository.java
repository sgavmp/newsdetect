package com.ikip.newsdetect.main.repository;

import com.ikip.newsdetect.model.Topic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends PagingAndSortingRepository<Topic, String> {

    @Query(value = "SELECT t.* FROM topics t JOIN LEFT topic_references r ON (t.title = r.topic_references_id) WHERE r.topic_id = :title ", nativeQuery = true)
    List<Topic> findAllByReference(String title);
}
