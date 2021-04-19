package com.nordclan.test.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nordclan.test.auth.model.Status;
import com.nordclan.test.auth.model.TokenEntity;
import com.nordclan.test.auth.model.User;
import com.nordclan.test.auth.model.UserInfo;
import com.nordclan.test.auth.service.AuthService;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/log-in")
  public ResponseEntity<Status> login(@RequestBody User user, HttpServletResponse response) {
    return ResponseEntity.ok(authService.checkUser(response, user));
  }

  @RequestMapping("/log-out")
  public ResponseEntity<Status> logout(@AuthenticationPrincipal TokenEntity tokenEntity) {
    authService.logout(tokenEntity);
    return ResponseEntity.ok(Status.ok());
  }

  @PostMapping("/registration")
  public ResponseEntity<Status> registration(@RequestBody User user) {
    return ResponseEntity.ok(authService.registration(user));
  }

  @PostMapping("/status")
  public ResponseEntity<UserInfo> echo(@AuthenticationPrincipal TokenEntity tokenEntity) {
    return ResponseEntity.ok(UserInfo.fromUser(tokenEntity.getUser()));
  }
}
