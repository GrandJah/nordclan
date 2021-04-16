package com.nordclan.test.repository;

import org.springframework.data.repository.CrudRepository;

import com.nordclan.test.model.User;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
  Optional<User> findByLogin(String login);
}