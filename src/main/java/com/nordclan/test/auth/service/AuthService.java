package com.nordclan.test.auth.service;

import org.springframework.stereotype.Service;

import com.nordclan.test.auth.model.Status;
import com.nordclan.test.auth.model.TokenEntity;
import com.nordclan.test.auth.model.User;
import com.nordclan.test.auth.repository.UserRepository;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;


@Service
public class AuthService {
  private final UserRepository userStorage;

  private final SecurityService securityService;

  public AuthService(UserRepository userStorage, SecurityService securityService) {
    this.userStorage = userStorage;
    this.securityService = securityService;
  }

  public Status checkUser(HttpServletResponse response, User loginUser) {
    Optional<User> regUser = userStorage.findByLogin(loginUser.getLogin());
    if (!regUser.isPresent() || !regUser.get().checkPass(loginUser.getPassword())) {
      return Status.error("User not found");
    }
    securityService.createToken(regUser.get(), response);
    return Status.ok();
  }

  public void logout(TokenEntity tokenEntity) {
    securityService.deleteToken(tokenEntity);
  }

  public Status registration(User user) {
    Optional<User> findUser = userStorage.findByLogin(user.getLogin());
    if (findUser.isPresent()) {
      return Status.error(String.format("user %s is exist", user.getLogin()));
    }
    if (user.getFullname() == null) {
      user.setFullname("");
    }
    int userLoginLen = user.getLogin().length();
    if (userLoginLen >= 6 && userLoginLen <= 50) {
      userStorage.save(user);
      return Status.message(String.format("user %s registration", user.getLogin()));
    } else {
      return Status.error("login should have between 6 and 50 characters");
    }
  }
}
