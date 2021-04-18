package com.nordclan.test.eventmanager.model;

import java.time.Instant;
import lombok.Data;

@Data
public class TimeRange {
  Instant start;
  Instant end;
}
