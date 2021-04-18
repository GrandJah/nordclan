package com.nordclan.test.eventmanager.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Transient;
import lombok.Data;

@Data
public class Event {
  @Transient
  private Long id;

  private Instant start;

  private Instant end;

  private String title;

  private String description;

  private String creator;

  private List<String> members = new ArrayList<>();
}
