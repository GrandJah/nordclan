package com.nordclan.test.eventmanager.model;

import com.nordclan.test.auth.model.UserInfo;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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

  private Set<UserInfo> members = new HashSet<>();
}
