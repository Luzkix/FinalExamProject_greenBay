package com.greenfoxacademy.springwebapp.user.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.TestNoSecurityConfig;
import com.greenfoxacademy.springwebapp.factories.UserFactory;
import com.greenfoxacademy.springwebapp.user.models.DTO.LoginRequestDTO;
import com.greenfoxacademy.springwebapp.user.models.DTO.RegisterRequestDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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

  @Test
  public void registerUserShouldReturnStatus201() throws Exception {
    RegisterRequestDTO dto = UserFactory.createRegisterDTO("zdenek2","test2@seznam.cz","password");
    String requestJson = new ObjectMapper().writeValueAsString(dto);

    mockMvc.perform(post(UserController.REGISTER)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.username",is("zdenek2")));
  }

  @Test
  public void registerUserShouldReturnStatus406() throws Exception {
    RegisterRequestDTO dto = UserFactory.createDefaultRegisterDTO();
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
    RegisterRequestDTO dto = UserFactory.createDefaultRegisterDTO();
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
    RegisterRequestDTO dto = UserFactory.createDefaultRegisterDTO();
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
    LoginRequestDTO dto = UserFactory.createDefaultLoginDTO();
    String requestJson = new ObjectMapper().writeValueAsString(dto);

    mockMvc.perform(post(UserController.LOGIN)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.status",is("ok")))
        .andExpect(jsonPath("$.token",startsWith("ey")));;
  }

  @Test
  public void loginUser_WrongName_ShouldReturnStatus401() throws Exception {
    LoginRequestDTO dto = UserFactory.createDefaultLoginDTO();
    dto.setUsername("wrongUsername");
    String requestJson = new ObjectMapper().writeValueAsString(dto);

    mockMvc.perform(post(UserController.LOGIN)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andExpect(status().is(401))
        .andExpect(jsonPath("$.status",is("error")))
        .andExpect(jsonPath("$.message",is("Username or password is incorrect!")));;
  }
}
