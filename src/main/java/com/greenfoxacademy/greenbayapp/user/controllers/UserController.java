package com.greenfoxacademy.greenbayapp.user.controllers;

import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.user.models.DTO.LoginRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.DTO.RegisterRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import com.greenfoxacademy.greenbayapp.user.services.UserService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
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

  //just for testing purposes of spring security - Authentication
  @GetMapping("/test")
  public String showPassword(Authentication auth) throws RuntimeException {
    return ((CustomUserDetails) auth.getPrincipal()).getPassword();
  }
}
