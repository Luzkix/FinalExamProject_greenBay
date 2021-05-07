package com.greenfoxacademy.greenbayapp.bid.services;

import com.greenfoxacademy.greenbayapp.bid.models.Bid;
import com.greenfoxacademy.greenbayapp.bid.models.dtos.BidDetailsDTO;
import com.greenfoxacademy.greenbayapp.bid.repositories.BidRepository;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotEnoughDollarsException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotFoundException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotSellableException;
import com.greenfoxacademy.greenbayapp.product.models.Product;
import com.greenfoxacademy.greenbayapp.product.models.dtos.ProductDetailsResponseDTO;
import com.greenfoxacademy.greenbayapp.product.services.ProductService;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
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
  private ProductService productService;

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

  @Override
  public ProductDetailsResponseDTO doBidding(Long productId, Integer bidPrice, UserEntity user)
      throws NotFoundException, AuthorizationException, NotSellableException, NotEnoughDollarsException {
    Product product = productService.getProductById(productId);
    if (product == null) throw new NotFoundException();
    if (product.getSold()) throw new NotSellableException();
    if (!bidIsAuthorized(product,user)) throw new AuthorizationException("User can not do bidding on own items!");
    if (bidPrice > user.getBalance()) throw new NotEnoughDollarsException();

    return null;
  }

  public boolean bidIsAuthorized(Product product, UserEntity user) {
    return product.getSeller().getId() != user.getId();
  }
}
