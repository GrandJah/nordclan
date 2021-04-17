package com.nordclan.test.eventmanager.model;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class TimeRange {
  Timestamp start;
  Timestamp end;
}
