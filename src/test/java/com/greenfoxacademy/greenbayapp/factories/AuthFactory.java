package com.greenfoxacademy.greenbayapp.factories;

import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class AuthFactory {
  public static Authentication createAuth_zeroBalanceUser(Long id, String userName) {
    UserEntity user = UserFactory.createDefaultUser(id, userName);
    CustomUserDetails userDetails = setCustomUserDetails(user);
    return new UsernamePasswordAuthenticationToken(userDetails, null, null);
  }

  public static Authentication createAuth_positiveBalanceUser(Long id, String userName, int balance) {
    UserEntity user = UserFactory.createDefaultUserWithBalance(id, userName, balance);
    CustomUserDetails userDetails = setCustomUserDetails(user);
    return new UsernamePasswordAuthenticationToken(userDetails, null, null);
  }

  public static Authentication createZdenekAuth() {
    UserEntity user = UserFactory.createDefaultZdenekUser();
    CustomUserDetails userDetails = setCustomUserDetails(user);
    return new UsernamePasswordAuthenticationToken(userDetails, null, null);
  }

  public static Authentication createPetrAuth() {
    UserEntity user = UserFactory.createDefaultPetrUser();
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
