package com.greenfoxacademy.greenbayapp.bid.controllers;

import com.greenfoxacademy.greenbayapp.bid.services.BidService;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotEnoughDollarsException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotFoundException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotSellableException;
import com.greenfoxacademy.greenbayapp.product.models.dtos.ProductDetailsResponseDTO;
import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class BidController {
  public static final String BID_URI = "/bid";

  private BidService bidService;

  @PostMapping(BID_URI + "/{id}")
  public ResponseEntity<?> doBidding(@PathVariable(required = true, name = "productId") Long productId,
                                    @RequestParam(required = true) Integer bidPrice,
                                    Authentication auth)
      throws NotFoundException, AuthorizationException, NotSellableException, NotEnoughDollarsException {
    UserEntity user = ((CustomUserDetails) auth.getPrincipal()).getUser();
    ProductDetailsResponseDTO dto = bidService.doBidding(productId, bidPrice, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }
}
