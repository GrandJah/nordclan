package com.nordclan.test.eventmanager.model;

import com.nordclan.test.auth.model.User;
import com.nordclan.test.auth.model.UserInfo;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity(name = "event")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventDAO {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  @EqualsAndHashCode.Include
  private Long id;

  @Column(nullable = false, name = "startevent")
  private Instant start;

  @Column(nullable = false, name = "endevent")
  private Instant end;

  @Column(nullable = false, length = 36)
  private String title;

  @Column(nullable = false, length = 500)
  private String description;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(nullable = false)
  private User creator;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "event_users", joinColumns = @JoinColumn(name = "event_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "members_id", referencedColumnName = "id"))
  private Set<User> members = new HashSet<>();

  static public EventDAO fromEvent(Event event) {
    EventDAO dao = new EventDAO();
    dao.id = event.getId();
    dao.start = event.getStart();
    dao.end = event.getEnd();
    dao.title = event.getTitle();
    dao.description = event.getDescription();
    return dao;
  }

  static public Event toEvent(EventDAO event) {
    Event dto = new Event();
    dto.setId(event.getId());
    dto.setStart(event.getStart());
    dto.setEnd(event.getEnd());
    dto.setTitle(event.getTitle());
    dto.setDescription(event.getDescription());
    dto.setCreator(event.getCreator().getFullname());
    dto.getMembers()
      .addAll(event.members.stream().map(UserInfo::fromUser).collect(Collectors.toList()));
    return dto;
  }
}
