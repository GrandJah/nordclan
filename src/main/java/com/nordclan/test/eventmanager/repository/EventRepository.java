package com.nordclan.test.eventmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.nordclan.test.eventmanager.model.Event;
import java.sql.Timestamp;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {
  List<Event> findAllByStartAfterAndEndBefore(Timestamp from, Timestamp to);

  int countAllByEndGreaterThanEqualAndStartLessThanEqual(Timestamp start, Timestamp end);
}

