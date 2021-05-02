package com.greenfoxacademy.greenbayapp.factories;

import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import com.greenfoxacademy.greenbayapp.user.models.dtos.LoginRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterRequestDTO;

public class UserFactory {
  public static UserEntity createUser(
      Long id, String username, String email, String password, Integer balance) {
    UserEntity user = new UserEntity(username, email, password);
    user.setId(id);
    user.setBalance(balance);
    return user;
  }

  public static UserEntity createUser_defaultUser(Long id, String username) {
    return createUser(
        id,
        username,
        username + "@seznam.cz",
        "password",
        0);
  }

  public static UserEntity createUser_defaultUserZdenek() {
    return createUser_defaultUser(
        1L,
        "zdenek");
  }

  public static UserEntity createUser_defaultUserPetr() {
    return createUser_defaultUser_withBalance(
        2L,
        "petr",
        1000);
  }

  public static UserEntity createUser_defaultUser_withBalance(Long id, String username, int balance) {
    UserEntity user = createUser_defaultUser(id, username);
    user.setBalance(balance);
    return user;
  }

  public static RegisterRequestDTO createRegisterRequestDTO(String username, String email, String password) {
    return new RegisterRequestDTO(
        username,
        email,
        password);
  }

  public static RegisterRequestDTO createRegisterRequestDTO_defaultDTO() {
    return createRegisterRequestDTO(
        "zdenek",
        "test@seznam.cz",
        "password");
  }

  public static LoginRequestDTO createLoginRequestDTO(String username, String password) {
    return new LoginRequestDTO(
        username,
        password);
  }

  public static LoginRequestDTO createLoginRequestDTO_defaultDTO() {
    return createLoginRequestDTO(
        "zdenek",
        "password");
  }
}
