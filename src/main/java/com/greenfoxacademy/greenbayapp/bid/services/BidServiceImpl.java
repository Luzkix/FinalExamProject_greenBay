package com.greenfoxacademy.greenbayapp.bid.services;

import com.greenfoxacademy.greenbayapp.bid.models.Bid;
import com.greenfoxacademy.greenbayapp.bid.models.dtos.BidDetailsDTO;
import com.greenfoxacademy.greenbayapp.bid.repositories.BidRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BidServiceImpl implements BidService {
  private BidRepository bidRepository;

  @Override
  public BidDetailsDTO convertBidIntoBidDetailsDTO(Bid bid) {
    BidDetailsDTO dto = new BidDetailsDTO();

    BeanUtils.copyProperties(bid,dto);
    dto.setProductId(bid.getProduct().getId());
    dto.setProductName(bid.getProduct().getName());
    dto.setBidderId(bid.getBidder().getId());
    dto.setBidderName(bid.getBidder().getUsername());

    return dto;
  }

  @Override
  public List<BidDetailsDTO> convertSetOfBidsIntoListOfBidDetailDTOs(Set<Bid> bids) {
    return bids.stream()
        .map(a -> convertBidIntoBidDetailsDTO(a))
        .sorted(Comparator.comparing(bid -> bid.getId()))
        .collect(Collectors.toList());
  }
}
