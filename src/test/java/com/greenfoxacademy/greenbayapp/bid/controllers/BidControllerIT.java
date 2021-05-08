package com.greenfoxacademy.greenbayapp.bid.controllers;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.greenfoxacademy.greenbayapp.TestNoSecurityConfig;
import com.greenfoxacademy.greenbayapp.factories.AuthFactory;
import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

//since tests affected data.sql and thus each other, this annotation solves it (but it is slow)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(TestNoSecurityConfig.class)
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class BidControllerIT {
  @Autowired
  private MockMvc mockMvc;
  private Authentication auth;

  @Before
  public void SetUp() throws Exception {
    auth = AuthFactory.createAuth_userZdenek();
  }

  @Test
  public void doBidding_returnsNewBid_productNotSold_status201() throws Exception {
    UserEntity zdenek = ((CustomUserDetails) auth.getPrincipal()).getUser();
    zdenek.setBalance(1000);
    mockMvc.perform(post(BidController.BID_URI + "/5")
        .param("bidPrice","50")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.id", is(5)))
        .andExpect(jsonPath("$.sold", is(false)))
        .andExpect(jsonPath("$.bids[0].bidPrice", is(50)));
    Assert.assertEquals(950, zdenek.getBalance().intValue());
  }

  @Test
  public void doBidding_returnsNewBid_productSold_status201() throws Exception {
    UserEntity zdenek = ((CustomUserDetails) auth.getPrincipal()).getUser();
    zdenek.setBalance(1000);
    mockMvc.perform(post(BidController.BID_URI + "/5")
        .param("bidPrice","200")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(201))
        .andExpect(jsonPath("$.id", is(5)))
        .andExpect(jsonPath("$.sold", is(true)))
        .andExpect(jsonPath("$.buyerName", is(zdenek.getUsername())))
        .andExpect(jsonPath("$.bids[0].bidPrice", is(200)));
    Assert.assertEquals(800, zdenek.getBalance().intValue());
  }

  @Test
  public void doBidding_nonExistingProduct_throwsExceptionAndStatus404() throws Exception {
    UserEntity zdenek = ((CustomUserDetails) auth.getPrincipal()).getUser();
    zdenek.setBalance(1000);
    mockMvc.perform(post(BidController.BID_URI + "/100")
        .param("bidPrice","200")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("The item was not found!")));
    Assert.assertEquals(1000, zdenek.getBalance().intValue());
  }

  @Test
  public void doBidding_notSellableProduct_throwsExceptionAndStatus406() throws Exception {
    UserEntity zdenek = ((CustomUserDetails) auth.getPrincipal()).getUser();
    zdenek.setBalance(1000);
    mockMvc.perform(post(BidController.BID_URI + "/7")
        .param("bidPrice","200")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(406))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("The item was already sold!")));
    Assert.assertEquals(1000, zdenek.getBalance().intValue());
  }

  @Test
  public void doBiddingOnOwnProduct_throwsExceptionAndStatus401() throws Exception {
    UserEntity zdenek = ((CustomUserDetails) auth.getPrincipal()).getUser();
    zdenek.setBalance(1000);
    mockMvc.perform(post(BidController.BID_URI + "/1")
        .param("bidPrice","200")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(401))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("User can not do bidding on own items!")));
    Assert.assertEquals(1000, zdenek.getBalance().intValue());
  }

  @Test
  public void doBidding_lowBalance_throwsExceptionAndStatus406() throws Exception {
    UserEntity zdenek = ((CustomUserDetails) auth.getPrincipal()).getUser();
    zdenek.setBalance(100);
    mockMvc.perform(post(BidController.BID_URI + "/5")
        .param("bidPrice","200")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(406))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Not enought greenBay dollars!")));
    Assert.assertEquals(100, zdenek.getBalance().intValue());
  }

  @Test
  public void doBidding_negativeBidPrice_throwsExceptionAndStatus406() throws Exception {
    UserEntity zdenek = ((CustomUserDetails) auth.getPrincipal()).getUser();
    zdenek.setBalance(1000);
    mockMvc.perform(post(BidController.BID_URI + "/5")
        .param("bidPrice","-200")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(406))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Bid price must be > 0!")));
    Assert.assertEquals(1000, zdenek.getBalance().intValue());
  }

  @Test
  public void doBidding_BidPriceLowerThanHighestBidPrice_throwsExceptionAndStatus406() throws Exception {
    UserEntity zdenek = ((CustomUserDetails) auth.getPrincipal()).getUser();
    zdenek.setBalance(1000);
    mockMvc.perform(post(BidController.BID_URI + "/6")
        .param("bidPrice","100")
        .principal(auth))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().is(406))
        .andExpect(jsonPath("$.status", is("error")))
        .andExpect(jsonPath("$.message", is("Bid price is too low!")));
    Assert.assertEquals(1000, zdenek.getBalance().intValue());
  }
}
