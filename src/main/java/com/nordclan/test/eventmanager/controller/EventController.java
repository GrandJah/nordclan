package com.nordclan.test.eventmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nordclan.test.auth.model.Status;
import com.nordclan.test.auth.model.TokenEntity;
import com.nordclan.test.eventmanager.model.Event;
import com.nordclan.test.eventmanager.model.TimeRange;
import com.nordclan.test.eventmanager.service.EventService;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EventController {

  private final EventService eventService;

  public EventController(EventService eventService) {
    this.eventService = eventService;
  }

  @PostMapping("/event")
  public ResponseEntity<List<Event>> event(@RequestBody(required = false) TimeRange range) {
    return ResponseEntity.ok(this.eventService.getEventsFromRange(range));
  }

  @PostMapping("/create")
  public ResponseEntity<Status> create(@RequestBody(required = false) Event event,
    @AuthenticationPrincipal TokenEntity authPrincipal) {
    event.setCreator(authPrincipal.getUser());
    return ResponseEntity.ok(this.eventService.createEvent(event));
  }
}
