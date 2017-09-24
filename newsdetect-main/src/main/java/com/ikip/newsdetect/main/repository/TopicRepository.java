package com.ikip.newsdetect.main.repository;

import es.ucm.visavet.gbf.app.domain.Topic;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends CrudRepository<Topic, String> {

}
