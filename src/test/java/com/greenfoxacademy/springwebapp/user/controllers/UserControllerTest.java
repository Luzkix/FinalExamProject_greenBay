package com.greenfoxacademy.springwebapp.user.controllers;

import com.greenfoxacademy.springwebapp.factories.UserFactory;
import com.greenfoxacademy.springwebapp.user.models.DTO.RegisterRequestDTO;
import com.greenfoxacademy.springwebapp.user.models.DTO.RegisterResponseDTO;
import com.greenfoxacademy.springwebapp.user.models.UserEntity;
import com.greenfoxacademy.springwebapp.user.services.UserService;
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
  public void registerUserShouldReturn201StatusCode(){
    RegisterRequestDTO dto = UserFactory.createDefaultRegisterDTO();
    RegisterResponseDTO responseDTO = new RegisterResponseDTO(1l,"zdenek","test@seznam.cz");
    UserEntity newUser = UserFactory.createDefaultUser(0l);

    Mockito.when(userService.registerNewUser(dto)).thenReturn(newUser);
    Mockito.when(userService.convertUserToRegisterResponseDTO(newUser)).thenReturn(responseDTO);

    ResponseEntity<?> response = userController.registerUser(dto);

    Assert.assertEquals(201, response.getStatusCodeValue());
    Assert.assertEquals(1l, ((RegisterResponseDTO)response.getBody()).getId().longValue());
    Assert.assertEquals("zdenek", ((RegisterResponseDTO)response.getBody()).getUsername());
    Assert.assertEquals("test@seznam.cz", ((RegisterResponseDTO)response.getBody()).getEmail());
  }

  @Test(expected = RuntimeException.class)
  public void registerUserShouldThrowRuntimeException(){
    RegisterRequestDTO dto = UserFactory.createDefaultRegisterDTO();

    Mockito.when(userService.registerNewUser(dto)).thenThrow(RuntimeException.class);

    ResponseEntity<?> response = userController.registerUser(dto);
  }
}
