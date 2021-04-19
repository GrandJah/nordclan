package com.nordclan.test.auth.model;

import lombok.Data;

@Data
public class UserInfo {
  private Long id;
  private String username;

  static public UserInfo fromUser(User user) {
    UserInfo info = new UserInfo();
    info.id = user.getId();
    info.username = user.getFullname();
    return info;
  }
}
