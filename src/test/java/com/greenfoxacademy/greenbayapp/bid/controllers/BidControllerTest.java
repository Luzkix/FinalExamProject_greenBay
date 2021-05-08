package com.greenfoxacademy.greenbayapp.bid.controllers;

import com.greenfoxacademy.greenbayapp.bid.services.BidService;
import com.greenfoxacademy.greenbayapp.factories.AuthFactory;
import com.greenfoxacademy.greenbayapp.factories.ProductFactory;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.LowBidException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotEnoughDollarsException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotFoundException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotSellableException;
import com.greenfoxacademy.greenbayapp.product.models.dtos.ProductDetailsResponseDTO;
import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public class BidControllerTest {
  BidService bidService;
  BidController bidController;

  Authentication auth;

  @Before
  public void setUp() {
    bidService = Mockito.mock(BidService.class);
    bidController = new BidController(bidService);

    auth = AuthFactory.createAuth_userZdenek();
  }

  @Test
  public void doBidding_soldProduct_returnsStatus201andCorrectDTO() {
    auth = AuthFactory.createAuth_userPetr();
    UserEntity petr = ((CustomUserDetails) auth.getPrincipal()).getUser();
    ProductDetailsResponseDTO dto = ProductFactory
        .createProductDetailsResponseDTO_defaultSold_sellerZdenek_bidderPetr(1L);
    Mockito.when(bidService.doBidding(1L,100,petr)).thenReturn(dto);

    ResponseEntity<?> response = bidController.doBidding(1L,100, auth);

    Assert.assertEquals(201,response.getStatusCode().value());
  }

  @Test(expected = NotFoundException.class)
  public void doBidding_returnsNotFoundException() {
    auth = AuthFactory.createAuth_userPetr();
    UserEntity petr = ((CustomUserDetails) auth.getPrincipal()).getUser();
    ProductDetailsResponseDTO dto = ProductFactory
        .createProductDetailsResponseDTO_defaultSold_sellerZdenek_bidderPetr(1L);
    Mockito.when(bidService.doBidding(1L,100,petr)).thenThrow(NotFoundException.class);

    ResponseEntity<?> response = bidController.doBidding(1L,100, auth);
  }

  @Test(expected = NotSellableException.class)
  public void doBidding_returnsNotSellableException() {
    auth = AuthFactory.createAuth_userPetr();
    UserEntity petr = ((CustomUserDetails) auth.getPrincipal()).getUser();
    ProductDetailsResponseDTO dto = ProductFactory
        .createProductDetailsResponseDTO_defaultSold_sellerZdenek_bidderPetr(1L);
    Mockito.when(bidService.doBidding(1L,100,petr)).thenThrow(NotSellableException.class);

    ResponseEntity<?> response = bidController.doBidding(1L,100, auth);
  }

  @Test(expected = AuthorizationException.class)
  public void doBidding_returnsAuthorizationException() {
    auth = AuthFactory.createAuth_userPetr();
    UserEntity petr = ((CustomUserDetails) auth.getPrincipal()).getUser();
    ProductDetailsResponseDTO dto = ProductFactory
        .createProductDetailsResponseDTO_defaultSold_sellerZdenek_bidderPetr(1L);
    Mockito.when(bidService.doBidding(1L,100,petr)).thenThrow(AuthorizationException.class);

    ResponseEntity<?> response = bidController.doBidding(1L,100, auth);
  }

  @Test(expected = NotEnoughDollarsException.class)
  public void doBidding_returnsNotEnoughDollarsException() {
    auth = AuthFactory.createAuth_userPetr();
    UserEntity petr = ((CustomUserDetails) auth.getPrincipal()).getUser();
    ProductDetailsResponseDTO dto = ProductFactory
        .createProductDetailsResponseDTO_defaultSold_sellerZdenek_bidderPetr(1L);
    Mockito.when(bidService.doBidding(1L,100,petr)).thenThrow(NotEnoughDollarsException.class);

    ResponseEntity<?> response = bidController.doBidding(1L,100, auth);
  }

  @Test(expected = InvalidInputException.class)
  public void doBidding_returnsInvalidInputException() {
    auth = AuthFactory.createAuth_userPetr();
    UserEntity petr = ((CustomUserDetails) auth.getPrincipal()).getUser();
    ProductDetailsResponseDTO dto = ProductFactory
        .createProductDetailsResponseDTO_defaultSold_sellerZdenek_bidderPetr(1L);
    Mockito.when(bidService.doBidding(1L,100,petr)).thenThrow(InvalidInputException.class);

    ResponseEntity<?> response = bidController.doBidding(1L,100, auth);
  }

  @Test(expected = LowBidException.class)
  public void doBidding_returnsLowBidException() {
    auth = AuthFactory.createAuth_userPetr();
    UserEntity petr = ((CustomUserDetails) auth.getPrincipal()).getUser();
    ProductDetailsResponseDTO dto = ProductFactory
        .createProductDetailsResponseDTO_defaultSold_sellerZdenek_bidderPetr(1L);
    Mockito.when(bidService.doBidding(1L,100,petr)).thenThrow(LowBidException.class);

    ResponseEntity<?> response = bidController.doBidding(1L,100, auth);
  }
}
