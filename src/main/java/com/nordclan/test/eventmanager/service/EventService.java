package com.nordclan.test.eventmanager.service;

import org.springframework.stereotype.Service;

import com.nordclan.test.auth.model.Status;
import com.nordclan.test.auth.model.User;
import com.nordclan.test.auth.model.UserInfo;
import com.nordclan.test.auth.repository.UserRepository;
import com.nordclan.test.eventmanager.model.Event;
import com.nordclan.test.eventmanager.model.EventDAO;
import com.nordclan.test.eventmanager.model.TimeRange;
import com.nordclan.test.eventmanager.repository.EventRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.transaction.Transactional;


@Service
public class EventService {
  private static final int HALF_HOURS = 1800;

  private final EventRepository eventStorage;

  private final UserRepository users;

  public EventService(EventRepository eventStorage, UserRepository users) {
    this.eventStorage = eventStorage;
    this.users = users;
  }

  public List<Event> getEventsFromRange(TimeRange range) {
    return eventStorage.findAllByStartAfterAndEndBefore(range.getStart(), range.getEnd())
      .stream()
      .map(EventDAO::toEvent)
      .collect(Collectors.toList());
  }

  private Instant alignInstantHalfHours(Instant instant, boolean direction) {
    if (instant.getEpochSecond() % HALF_HOURS == 0) {
      return instant;
    }
    return Instant.ofEpochSecond(
      ((instant.getEpochSecond() / HALF_HOURS) + (direction ? 1 : 0)) * HALF_HOURS);
  }

  @Transactional
  public Status createEvent(Event event, User user) {
    if (event.getTitle() == null || "".equals(event.getTitle())) {
      return Status.error("Title event empty");
    }
    if (event.getStart() == null || event.getEnd() == null) {
      return Status.error("not start or end time event");
    }

    event.setStart(alignInstantHalfHours(event.getStart(), false));
    event.setEnd(alignInstantHalfHours(event.getEnd(), true));

    if (event.getEnd().isBefore(event.getStart().plus(30, ChronoUnit.MINUTES))) {
      return Status.error("the duration of the event is too short");
    }

    if (event.getEnd().isAfter(event.getStart().plus(24, ChronoUnit.HOURS))) {
      return Status.error("the duration of the event should not exceed 24 hours");
    }

    if (this.eventStorage.countAllByEndGreaterThanEqualAndStartLessThanEqual(event.getStart(),
      event.getEnd()) != 0) {
      return Status.error("Time is busy");
    }

    EventDAO eventDAO = EventDAO.fromEvent(event);
    eventDAO.setCreator(user);
    eventDAO.setMembers(this.users.findByFullnameIn(
      event.getMembers().stream().map(UserInfo::getUsername).collect(Collectors.toSet())));
    this.eventStorage.save(eventDAO);
    return Status.ok();
  }

  public List<UserInfo> getUserInfo() {
    return StreamSupport.stream(this.users.findAll().spliterator(), false)
      .map(UserInfo::fromUser)
      .collect(Collectors.toList());
  }
}
