package com.nordclan.test.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.nordclan.test.auth.model.TokenEntity;
import com.nordclan.test.auth.model.User;

public interface TokenRepository extends CrudRepository<TokenEntity, Long> {

  TokenEntity findByUuid(String uuid);

  @Transactional
  void deleteByUser(User user);
}
