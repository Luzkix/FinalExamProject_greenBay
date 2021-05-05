package com.greenfoxacademy.greenbayapp.user.controllers;

import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import com.greenfoxacademy.greenbayapp.user.models.dtos.LoginRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterRequestDTO;
import com.greenfoxacademy.greenbayapp.user.services.UserService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {
  public static final String REGISTER = "/register";
  public static final String LOGIN = "/login";
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
    return ResponseEntity.ok(userService.loginPlayer(loginRequestDTO));
  }
}
