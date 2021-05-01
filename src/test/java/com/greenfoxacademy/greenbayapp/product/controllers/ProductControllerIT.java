package com.greenfoxacademy.greenbayapp.product.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.greenbayapp.TestNoSecurityConfig;
import com.greenfoxacademy.greenbayapp.factories.AuthFactory;
import com.greenfoxacademy.greenbayapp.factories.ProductFactory;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
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
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(TestNoSecurityConfig.class)
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class ProductControllerIT {
  @Autowired
  private MockMvc mockMvc;
  private Authentication auth;

  @Before
  public void setUp() throws Exception {
    auth = AuthFactory.createZdenekAuth();
  }

  @Test
  public void postNewProduct_returnsStatus201AndCreatesNewProduct() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String requestJson = mapper.writeValueAsString(ProductFactory.createDefaultProductRequestDTO());
    mockMvc.perform(post(ProductController.PRODUCT_URI)
        .principal(auth)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.name", is("car")))
        .andExpect(jsonPath("$.purchasePrice", is(500)));
  }

  @Test
  public void postNewProduct_returnsStatus400_missingItemName() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    NewProductRequestDTO request = ProductFactory.createDefaultProductRequestDTO();
    request.setName("");
    String requestJson = mapper.writeValueAsString(request);
    mockMvc.perform(post(ProductController.PRODUCT_URI)
        .principal(auth)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Item name can´t be empty!")));
  }

  @Test
  public void postNewProduct_returnsStatus400_startingPriceBellow1() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    NewProductRequestDTO request = ProductFactory.createDefaultProductRequestDTO();
    request.setStartingPrice(0);
    String requestJson = mapper.writeValueAsString(request);
    mockMvc.perform(post(ProductController.PRODUCT_URI)
        .principal(auth)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Starting price must be > 0!")));
  }

  public void postNewProduct_returnsStatus400_missingNameAndStartingPriceBellow1() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    NewProductRequestDTO request = ProductFactory.createDefaultProductRequestDTO();
    request.setName("");
    request.setStartingPrice(0);
    String requestJson = mapper.writeValueAsString(request);
    mockMvc.perform(post(ProductController.PRODUCT_URI)
        .principal(auth)
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.message",containsString("Item name can´t be empty!")))
        .andExpect(jsonPath("$.message",containsString("Starting price must be > 0!")));
  }
}
