package com.greenfoxacademy.greenbayapp.product.controllers;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    auth = AuthFactory.createAuth_userZdenek();
  }

  @Test
  public void postNewProduct_returnsStatus201AndCreatesNewProduct() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String requestJson = mapper.writeValueAsString(ProductFactory.createNewProductRequestDTO_defaultDTO());
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
    NewProductRequestDTO request = ProductFactory.createNewProductRequestDTO_defaultDTO();
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
    NewProductRequestDTO request = ProductFactory.createNewProductRequestDTO_defaultDTO();
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
    NewProductRequestDTO request = ProductFactory.createNewProductRequestDTO_defaultDTO();
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

  @Test
  public void getSellableProducts_noParamsSet_returnsDefaultXproductsFromPage1AndStatus200() throws Exception {
    mockMvc.perform(get(ProductController.ACTIVE_DEALS_URI)
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.unsoldProducts[0].id", is(1)))
        .andExpect(jsonPath("$.unsoldProducts[0].name", is("car1")))
        .andExpect(jsonPath("$.unsoldProducts[4].id", is(5)))
        .andExpect(jsonPath("$.unsoldProducts[4].name", is("bike1")))
        .andExpect(jsonPath("$.unsoldProducts[5]").doesNotExist());
  }

  @Test
  public void getSellableProducts_pageIsSetTo1_returnsDefaultXproductsFromPage1AndStatus200() throws Exception {
    mockMvc.perform(get(ProductController.ACTIVE_DEALS_URI)
        .param("page","1")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.unsoldProducts[0].id", is(1)))
        .andExpect(jsonPath("$.unsoldProducts[0].name", is("car1")))
        .andExpect(jsonPath("$.unsoldProducts[4].id", is(5)))
        .andExpect(jsonPath("$.unsoldProducts[4].name", is("bike1")))
        .andExpect(jsonPath("$.unsoldProducts[5]").doesNotExist());
  }

  @Test
  public void getSellableProducts_pageIsSetTo2_returnsDefaultXproductsFromPage2AndStatus200() throws Exception {
    mockMvc.perform(get(ProductController.ACTIVE_DEALS_URI)
        .param("page","2")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.unsoldProducts[0].id", is(6)))
        .andExpect(jsonPath("$.unsoldProducts[0].name", is("bike2")))
        .andExpect(jsonPath("$.unsoldProducts[1]").doesNotExist());
  }

  @Test
  public void getSellableProducts_pageIsSetToNegativeNumber_returnsErrorMsgAndStatus406() throws Exception {
    mockMvc.perform(get(ProductController.ACTIVE_DEALS_URI)
        .param("page","-2")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(406))
        .andExpect(jsonPath("$.unsoldProducts").doesNotExist())
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Invalid page number! Page must be > 0!")));
  }

  @Test
  public void getProductDetails_paramsSetCorrectly_unsold_returnsCorrectProductDetailsAndStatus200() throws Exception {
    mockMvc.perform(get(ProductController.PRODUCT_URI+"/1")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.enlistingTime", is("2021-05-01T17:39:55")))
        .andExpect(jsonPath("$.sold", is(false)))
        .andExpect(jsonPath("$.sellerId", is(1)));
  }

  @Test
  public void getProductDetails_paramsSetCorrectly_sold_returnsCorrectProductDetailsAndStatus200() throws Exception {
    auth = AuthFactory.createAuth_userPetr();
    mockMvc.perform(get(ProductController.PRODUCT_URI+"/7")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.id", is(7)))
        .andExpect(jsonPath("$.enlistingTime", is("2021-05-01T23:39:55")))
        .andExpect(jsonPath("$.sold", is(true)))
        .andExpect(jsonPath("$.buyerId", is(1)));
  }

  @Test
  public void getProductDetails_itemNotFromUser_returnsStatus401_unauthorizedMessage() throws Exception {
    mockMvc.perform(get(ProductController.PRODUCT_URI+"/7")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(401))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Not authorized to view details of selected item!")));
  }

  @Test
  public void getProductDetails_negativeId_returnsStatus404_notFoundMessage() throws Exception {
    mockMvc.perform(get(ProductController.PRODUCT_URI+"/-1")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("The item was not found!")));
  }

}
