package com.nordclan.test.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.nordclan.test.model.TokenEntity;
import com.nordclan.test.model.User;

public interface TokenRepository extends CrudRepository<TokenEntity, Long> {

  TokenEntity findByUuid(String uuid);

  @Transactional
  void deleteByUser(User user);
}
