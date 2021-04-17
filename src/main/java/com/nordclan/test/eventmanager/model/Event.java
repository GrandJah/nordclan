package com.nordclan.test.eventmanager.model;

import com.nordclan.test.auth.model.User;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event {
  @Column(nullable = false, name = "startevent")
  Timestamp start;

  @Column(nullable = false, name = "endevent")
  Timestamp end;

  @ManyToMany(fetch = FetchType.EAGER)
  List<User> members;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(nullable = false, length = 36)
  private String title;

  @Column(nullable = false, length = 500)
  private String description;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false)
  private User creator;
}
