package com.ikip.newsdetect.find.repository;

import com.ikip.newsdetect.model.Event;
import com.ikip.newsdetect.model.TypeEventEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("eventRepositoryFind")
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findOneByOriginIdAndTypeEventOrderByDateDesc(Long originId, TypeEventEnum typeEvent);
}
