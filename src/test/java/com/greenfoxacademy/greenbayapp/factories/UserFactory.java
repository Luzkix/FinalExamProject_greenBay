package com.greenfoxacademy.greenbayapp.factories;

import com.greenfoxacademy.greenbayapp.user.models.DTO.LoginRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.DTO.RegisterRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;

public class UserFactory {
  public static UserEntity createUser(String username, String email, String password){
    return new UserEntity(
        username,
        email,
        password);
  }

  public static UserEntity createDefaultUser(Integer dollars){
    UserEntity defaultUser = createUser(
        "zdenek",
        "test@seznam.cz",
        "password");
    defaultUser.setBalance(dollars);
    return defaultUser;
  }

  public static RegisterRequestDTO createRegisterDTO(String username, String email, String password){
    return new RegisterRequestDTO(
        username,
        email,
        password);
  }

  public static RegisterRequestDTO createDefaultRegisterDTO(){
    return createRegisterDTO(
        "zdenek",
        "test@seznam.cz",
        "password");
  }

  public static LoginRequestDTO createLoginDTO(String username, String password){
    return new LoginRequestDTO(
        username,
        password);
  }

  public static LoginRequestDTO createDefaultLoginDTO(){
    return createLoginDTO(
        "zdenek",
        "password");
  }
}
