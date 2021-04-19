package com.nordclan.test.auth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(unique = true, nullable = false)
  @EqualsAndHashCode.Include
  @ToString.Include
  private Long id;

  @Column(unique = true, nullable = false, length = 50)
  private String login;

  @Column(nullable = false, length = 50)
  private String password;

  @ToString.Include
  @Column(nullable = false, length = 50)
  private String fullname;

  public boolean checkPass(String password) {
    return this.password.equals(password);
  }
}
