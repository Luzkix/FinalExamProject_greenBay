package com.greenfoxacademy.greenbayapp.factories;

import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class AuthFactory {
  public static Authentication createAuth_userWithBalance(Long id, String userName, int balance) {
    UserEntity user = UserFactory.createUser_defaultUser_withBalance(id, userName, balance);
    CustomUserDetails userDetails = setCustomUserDetails(user);
    return new UsernamePasswordAuthenticationToken(userDetails, null, null);
  }

  public static Authentication createAuth_userZdenek() {
    UserEntity user = UserFactory.createUser_defaultUserZdenek();
    CustomUserDetails userDetails = setCustomUserDetails(user);
    return new UsernamePasswordAuthenticationToken(userDetails, null, null);
  }

  public static Authentication createAuth_userPetr() {
    UserEntity user = UserFactory.createUser_defaultUserPetr();
    CustomUserDetails userDetails = setCustomUserDetails(user);
    return new UsernamePasswordAuthenticationToken(userDetails, null, null);
  }

  private static CustomUserDetails setCustomUserDetails(UserEntity user) {
    CustomUserDetails userDetails = new CustomUserDetails();
    userDetails.setUser(user);
    userDetails.setUsername(user.getUsername());
    userDetails.setPassword(user.getPassword());
    return userDetails;
  }
}
