package com.greenfoxacademy.greenbayapp.user.controllers;

import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import com.greenfoxacademy.greenbayapp.user.models.dtos.LoginRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterRequestDTO;
import com.greenfoxacademy.greenbayapp.user.services.UserService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
  public static final String REGISTER = "/register";
  public static final String LOGIN = "/login";
  public static final String DEPOSIT = "/deposit";

  private final UserService userService;

  @PostMapping (REGISTER)
  public ResponseEntity<?> registerUser(@RequestBody @Valid RegisterRequestDTO registerRequestDTO)
      throws AuthorizationException {
    UserEntity newUser = userService.registerNewUser(registerRequestDTO);
    return ResponseEntity.status(HttpStatus.valueOf(201))
      .body(userService.convertUserToRegisterResponseDTO(newUser));
  }

  @PostMapping (LOGIN)
  public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequestDTO loginRequestDTO)
      throws AuthorizationException {
    return ResponseEntity.ok(userService.loginUser(loginRequestDTO));
  }

  //implementation is, that every user with valid token can increase his own balance...i know, strange :-)
  @PutMapping(DEPOSIT)
  public ResponseEntity<?> depositDollars(@RequestParam (required = true) Integer deposit,
                                          Authentication auth) throws InvalidInputException {
    if (deposit <= 0) throw new InvalidInputException("Deposited amount can not be lower than 1!");

    UserEntity user = ((CustomUserDetails) auth.getPrincipal()).getUser();

    return ResponseEntity.ok().body(userService.increaseDollars(user, deposit));
  }
}
