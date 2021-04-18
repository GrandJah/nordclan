package com.nordclan.test.eventmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.nordclan.test.eventmanager.model.Event;
import java.time.Instant;
import java.util.List;

public interface EventRepository extends CrudRepository<Event, Long> {
  List<Event> findAllByStartAfterAndEndBefore(Instant from, Instant to);

  int countAllByEndGreaterThanEqualAndStartLessThanEqual(Instant start, Instant end);
}

