package com.greenfoxacademy.greenbayapp.bid.services;

import com.greenfoxacademy.greenbayapp.bid.models.Bid;
import com.greenfoxacademy.greenbayapp.bid.models.dtos.BidDetailsDTO;
import java.util.List;
import java.util.Set;

public interface BidService {
  BidDetailsDTO convertBidIntoBidDetailsDTO(Bid bid);

  List<BidDetailsDTO> convertSetOfBidsIntoListOfBidDetailDTOs(Set<Bid> bids);
}
