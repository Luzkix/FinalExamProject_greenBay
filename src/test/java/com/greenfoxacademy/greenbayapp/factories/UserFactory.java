package com.greenfoxacademy.greenbayapp.factories;

import com.greenfoxacademy.greenbayapp.user.models.dtos.LoginRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;

public class UserFactory {
  public static UserEntity createUser(
      Long id, String username, String email, String password, Integer balance) {
    UserEntity user = new UserEntity(username, email, password);
    user.setId(id);
    user.setBalance(balance);
    return user;
  }

  public static UserEntity createDefaultUser(Long id, String username) {
    return createUser(
        id,
        username,
        username + "@seznam.cz",
        "password",
        0);
  }

  public static UserEntity createDefaultZdenekUser() {
    return createDefaultUser(
        1L,
        "zdenek");
  }

  public static UserEntity createDefaultPetrUser() {
    return createDefaultUserWithBalance(
        2L,
        "petr",
        1000);
  }

  public static UserEntity createDefaultUserWithBalance(Long id, String username, int balance) {
    UserEntity user = createDefaultUser(id, username);
    user.setBalance(balance);
    return user;
  }

  public static RegisterRequestDTO createRegisterDTO(String username, String email, String password) {
    return new RegisterRequestDTO(
        username,
        email,
        password);
  }

  public static RegisterRequestDTO createDefaultRegisterDTO() {
    return createRegisterDTO(
        "zdenek",
        "test@seznam.cz",
        "password");
  }

  public static LoginRequestDTO createLoginDTO(String username, String password) {
    return new LoginRequestDTO(
        username,
        password);
  }

  public static LoginRequestDTO createDefaultLoginDTO() {
    return createLoginDTO(
        "zdenek",
        "password");
  }
}
