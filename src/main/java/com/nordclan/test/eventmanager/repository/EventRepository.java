package com.nordclan.test.eventmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.nordclan.test.eventmanager.model.EventDAO;
import java.time.Instant;
import java.util.List;

public interface EventRepository extends CrudRepository<EventDAO, Long> {
  List<EventDAO> findAllByStartAfterAndEndBefore(Instant from, Instant to);

  int countAllByEndGreaterThanEqualAndStartLessThanEqual(Instant start, Instant end);
}

