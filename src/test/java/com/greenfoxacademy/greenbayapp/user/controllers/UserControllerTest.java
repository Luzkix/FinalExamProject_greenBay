package com.greenfoxacademy.greenbayapp.user.controllers;

import com.greenfoxacademy.greenbayapp.factories.AuthFactory;
import com.greenfoxacademy.greenbayapp.factories.UserFactory;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import com.greenfoxacademy.greenbayapp.user.models.dtos.BalanceResponseDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterResponseDTO;
import com.greenfoxacademy.greenbayapp.user.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;


public class UserControllerTest {
  private UserController userController;
  private UserService userService;


  @Before
  public void setUp() {
    userService = Mockito.mock(UserService.class);
    userController = new UserController(userService);
  }

  @Test
  public void registerUserShouldReturn201StatusCode() {
    RegisterRequestDTO dto = UserFactory.createRegisterRequestDTO_defaultDTO();
    RegisterResponseDTO responseDTO = new RegisterResponseDTO(1L,"zdenek","test@seznam.cz");
    UserEntity newUser = UserFactory.createUser_defaultUserZdenek();

    Mockito.when(userService.registerNewUser(dto)).thenReturn(newUser);
    Mockito.when(userService.convertUserToRegisterResponseDTO(newUser)).thenReturn(responseDTO);

    ResponseEntity<?> response = userController.registerUser(dto);

    Assert.assertEquals(201, response.getStatusCodeValue());
    Assert.assertEquals(1L, ((RegisterResponseDTO)response.getBody()).getId().longValue());
    Assert.assertEquals("zdenek", ((RegisterResponseDTO)response.getBody()).getUsername());
    Assert.assertEquals("test@seznam.cz", ((RegisterResponseDTO)response.getBody()).getEmail());
  }

  @Test(expected = AuthorizationException.class)
  public void registerUserShouldThrowAuthorizationException() {
    RegisterRequestDTO dto = UserFactory.createRegisterRequestDTO_defaultDTO();

    Mockito.when(userService.registerNewUser(dto)).thenThrow(AuthorizationException.class);

    ResponseEntity<?> response = userController.registerUser(dto);
  }

  @Test
  public void depositDollarsShouldReturn200StatusCodeAndCorrectMessage() {
    Authentication auth = AuthFactory.createAuth_userZdenek();
    UserEntity user = ((CustomUserDetails) auth.getPrincipal()).getUser();
    BalanceResponseDTO dto = new BalanceResponseDTO(1L, "zdenek", 200);
    Mockito.when(userService.increaseDollars(user,200)).thenReturn(dto);

    ResponseEntity<?> response = userController.depositDollars(200,auth);

    Assert.assertEquals(200, response.getStatusCodeValue());
    Assert.assertEquals(1L, ((BalanceResponseDTO)response.getBody()).getUserId().longValue());
    Assert.assertEquals("zdenek", ((BalanceResponseDTO)response.getBody()).getUsername());
    Assert.assertEquals(200, ((BalanceResponseDTO)response.getBody()).getBalance().intValue());
  }
}
