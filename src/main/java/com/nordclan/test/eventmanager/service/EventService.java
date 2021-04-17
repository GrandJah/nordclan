package com.nordclan.test.eventmanager.service;

import org.springframework.stereotype.Service;

import com.nordclan.test.auth.model.Status;
import com.nordclan.test.eventmanager.model.Event;
import com.nordclan.test.eventmanager.model.TimeRange;
import com.nordclan.test.eventmanager.repository.EventRepository;
import java.util.List;
import javax.transaction.Transactional;


@Service
public class EventService {
  private final EventRepository eventStorage;

  public EventService(EventRepository eventStorage) {
    this.eventStorage = eventStorage;
  }

  public List<Event> getEventsFromRange(TimeRange range) {
    return eventStorage.findAllByStartAfterAndEndBefore(range.getStart(), range.getEnd());
  }

  @Transactional
  public Status createEvent(Event event) {
    System.out.println(event.toString());
    if(this.eventStorage.countAllByEndGreaterThanEqualAndStartLessThanEqual(event.getStart(), event.getEnd()) == 0) {
      this.eventStorage.save(event);
      return Status.ok();
    } else {
      return Status.error("Time is busy");
    }
  }
}
