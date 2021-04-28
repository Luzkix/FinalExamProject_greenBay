package com.greenfoxacademy.springwebapp.user.services;

import com.greenfoxacademy.springwebapp.user.models.DTO.LoginRequestDTO;
import com.greenfoxacademy.springwebapp.user.models.DTO.RegisterRequestDTO;
import com.greenfoxacademy.springwebapp.user.models.DTO.RegisterResponseDTO;
import com.greenfoxacademy.springwebapp.user.models.DTO.UserTokenDTO;
import com.greenfoxacademy.springwebapp.user.models.UserEntity;

public interface UserService {
  UserEntity registerNewUser(RegisterRequestDTO registerRequestDTO) throws RuntimeException;

  RegisterResponseDTO convertUserToRegisterResponseDTO(UserEntity newUser);

  UserTokenDTO loginPlayer(LoginRequestDTO loginRequestDTO) throws RuntimeException;
}
