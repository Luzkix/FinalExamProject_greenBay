package com.greenfoxacademy.greenbayapp.user.controllers;

import com.greenfoxacademy.greenbayapp.factories.UserFactory;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterResponseDTO;
import com.greenfoxacademy.greenbayapp.user.services.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;


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
}
