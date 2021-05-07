package com.greenfoxacademy.greenbayapp.user.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.greenbayapp.TestNoSecurityConfig;
import com.greenfoxacademy.greenbayapp.factories.AuthFactory;
import com.greenfoxacademy.greenbayapp.factories.UserFactory;
import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import com.greenfoxacademy.greenbayapp.user.models.dtos.LoginRequestDTO;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterRequestDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;


@Import(TestNoSecurityConfig.class)
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class UserControllerIT {

  @Autowired
  private MockMvc mockMvc;

  Authentication auth;

  @Before
  public void setUp() throws Exception {
    auth = AuthFactory.createAuth_userZdenek();
  }

  @Test
  public void registerUserShouldReturnStatus201() throws Exception {
    RegisterRequestDTO dto = UserFactory.createRegisterRequestDTO("zdenek2","test2@seznam.cz","password");
    String requestJson = new ObjectMapper().writeValueAsString(dto);

    mockMvc.perform(post(UserController.REGISTER)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.username",is("zdenek2")));
  }

  @Test
  public void registerUserShouldReturnStatus406() throws Exception {
    RegisterRequestDTO dto = UserFactory.createRegisterRequestDTO_defaultDTO();
    dto.setPassword("12");
    String requestJson = new ObjectMapper().writeValueAsString(dto);

    mockMvc.perform(post(UserController.REGISTER)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isNotAcceptable())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message",is("Password must consist between 8-12 characters!")));
  }

  @Test
  public void registerUser_wrongEmailFormat_ShouldReturnStatus400() throws Exception {
    RegisterRequestDTO dto = UserFactory.createRegisterRequestDTO_defaultDTO();
    dto.setEmail("testgreen.cz");
    String requestJson = new ObjectMapper().writeValueAsString(dto);

    mockMvc.perform(post(UserController.REGISTER)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message",is("Wrong format of email addess!")));
  }

  @Test
  public void registerUser_notSetUsernameNorEmail_ShouldReturnStatus400() throws Exception {
    RegisterRequestDTO dto = UserFactory.createRegisterRequestDTO_defaultDTO();
    dto.setUsername("");
    dto.setEmail("");
    String requestJson = new ObjectMapper().writeValueAsString(dto);

    mockMvc.perform(post(UserController.REGISTER)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message",containsString("Email can´t be empty!")))
        .andExpect(jsonPath("$.message",containsString("Username can´t be empty!")));
  }

  @Test
  public void loginUserShouldReturnStatus200() throws Exception {
    LoginRequestDTO dto = UserFactory.createLoginRequestDTO_defaultDTO();
    String requestJson = new ObjectMapper().writeValueAsString(dto);

    mockMvc.perform(post(UserController.LOGIN)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.status",is("ok")))
        .andExpect(jsonPath("$.token",matchesPattern(".+\\..+\\..+")));
  }

  @Test
  public void loginUser_WrongName_ShouldReturnStatus401() throws Exception {
    LoginRequestDTO dto = UserFactory.createLoginRequestDTO_defaultDTO();
    dto.setUsername("wrongUsername");
    String requestJson = new ObjectMapper().writeValueAsString(dto);

    mockMvc.perform(post(UserController.LOGIN)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().is(401))
        .andExpect(jsonPath("$.status",is("error")))
        .andExpect(jsonPath("$.message",is("Username or password is incorrect!")));;
  }

  @Test
  public void depositDollars_ShouldReturnStatus200andCorrectMessage() throws Exception {
    UserEntity user = ((CustomUserDetails) auth.getPrincipal()).getUser();
    mockMvc.perform(put(UserController.DEPOSIT)
        .param("deposit","100")
        .principal(auth))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.userId", is(user.getId().intValue())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.balance",is(100)));
  }

  @Test
  public void depositDollars_ShouldReturnStatus406andErrorMessage() throws Exception {
    mockMvc.perform(put(UserController.DEPOSIT)
        .param("deposit","-100")
        .principal(auth))
        .andExpect(status().is(406))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Deposited amount can not be lower than 1!")));
  }
}
