package com.nordclan.test.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.nordclan.test.auth.model.User;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByLogin(String login);

  Set<User> findByFullnameIn(Set<String> userLogins);
}
