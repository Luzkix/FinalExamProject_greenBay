package com.greenfoxacademy.greenbayapp.bid.services;

import com.greenfoxacademy.greenbayapp.bid.models.Bid;
import com.greenfoxacademy.greenbayapp.bid.models.dtos.BidDetailsDTO;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.LowBidException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotEnoughDollarsException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotFoundException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotSellableException;
import com.greenfoxacademy.greenbayapp.product.models.dtos.ProductDetailsResponseDTO;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import java.util.List;
import java.util.Set;

public interface BidService {
  BidDetailsDTO convertBidIntoBidDetailsDTO(Bid bid);

  List<BidDetailsDTO> convertSetOfBidsIntoListOfBidDetailDTOs(Set<Bid> bids);

  ProductDetailsResponseDTO doBidding(Long productId, Integer bidPrice, UserEntity bidder)
      throws NotFoundException, AuthorizationException, NotSellableException, NotEnoughDollarsException,
      LowBidException, InvalidInputException;
}
