package com.greenfoxacademy.greenbayapp.user.services;

import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import com.greenfoxacademy.greenbayapp.user.models.dtos.BalanceResponseDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.LoginRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterResponseDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.UserTokenDTO;

public interface UserService {
  UserEntity registerNewUser(RegisterRequestDTO registerRequestDTO) throws AuthorizationException;

  RegisterResponseDTO convertUserToRegisterResponseDTO(UserEntity newUser);

  UserTokenDTO loginPlayer(LoginRequestDTO loginRequestDTO) throws AuthorizationException;

  UserEntity findByUsername(String username);

  BalanceResponseDTO increaseDollars(UserEntity user, Integer increasedDollars);

  BalanceResponseDTO decreaseDollars(UserEntity user, Integer decreasedDollars);
}
