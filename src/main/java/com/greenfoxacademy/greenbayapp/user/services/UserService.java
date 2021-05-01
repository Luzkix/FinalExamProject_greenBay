package com.greenfoxacademy.greenbayapp.user.services;

import com.greenfoxacademy.greenbayapp.user.models.dtos.LoginRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterResponseDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.UserTokenDTO;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;

public interface UserService {
  UserEntity registerNewUser(RegisterRequestDTO registerRequestDTO) throws RuntimeException;

  RegisterResponseDTO convertUserToRegisterResponseDTO(UserEntity newUser);

  UserTokenDTO loginPlayer(LoginRequestDTO loginRequestDTO) throws RuntimeException;

  UserEntity findByUsername(String username);
}
