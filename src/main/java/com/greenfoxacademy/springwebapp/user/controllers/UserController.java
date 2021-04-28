package com.greenfoxacademy.springwebapp.user.controllers;

import com.greenfoxacademy.springwebapp.user.models.DTO.LoginRequestDTO;
import com.greenfoxacademy.springwebapp.user.models.DTO.RegisterRequestDTO;
import com.greenfoxacademy.springwebapp.user.models.UserEntity;
import com.greenfoxacademy.springwebapp.user.services.UserService;
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
      throws RuntimeException {
  UserEntity newUser = userService.registerNewUser(registerRequestDTO);
  return ResponseEntity.status(HttpStatus.valueOf(201))
      .body(userService.convertUserToRegisterResponseDTO(newUser));
  }

  @PostMapping (LOGIN)
  public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequestDTO loginRequestDTO) throws RuntimeException {
    return ResponseEntity.ok(userService.loginPlayer(loginRequestDTO));
  }

  @PostMapping (LOGIN+"s")
  public ResponseEntity<?> loginsUser(@RequestBody @Valid LoginRequestDTO loginRequestDTO) throws RuntimeException {
    return ResponseEntity.ok(userService.loginPlayer(loginRequestDTO));
  }
}
