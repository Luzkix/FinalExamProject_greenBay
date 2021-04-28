package com.greenfoxacademy.springwebapp.factories;

import com.greenfoxacademy.springwebapp.user.models.DTO.LoginRequestDTO;
import com.greenfoxacademy.springwebapp.user.models.DTO.RegisterRequestDTO;
import com.greenfoxacademy.springwebapp.user.models.UserEntity;

public class UserFactory {
  public static UserEntity createUser(String username, String email, String password){
    return new UserEntity(
        username,
        email,
        password);
  }

  public static UserEntity createDefaultUser(Long dollars){
    UserEntity defaultUser = createUser(
        "zdenek",
        "test@seznam.cz",
        "password");
    defaultUser.setDollars(dollars);
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
