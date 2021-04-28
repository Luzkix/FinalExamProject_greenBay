package com.greenfoxacademy.greenbayapp.user.services;

import com.greenfoxacademy.greenbayapp.user.models.DTO.LoginRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.DTO.RegisterRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.DTO.RegisterResponseDTO;
import com.greenfoxacademy.greenbayapp.user.models.DTO.UserTokenDTO;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;

public interface UserService {
  UserEntity registerNewUser(RegisterRequestDTO registerRequestDTO) throws RuntimeException;

  RegisterResponseDTO convertUserToRegisterResponseDTO(UserEntity newUser);

  UserTokenDTO loginPlayer(LoginRequestDTO loginRequestDTO) throws RuntimeException;
}
