package com.nordclan.test.auth.repository;

import org.springframework.data.repository.CrudRepository;

import com.nordclan.test.auth.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByLogin(String login);

  List<User> findByLoginIn(List<String> userLogins);
}
