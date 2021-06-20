package com.greenfoxacademy.greenbayapp.bid.services;

import com.greenfoxacademy.greenbayapp.bid.models.Bid;
import com.greenfoxacademy.greenbayapp.bid.models.dtos.BidDetailsDTO;
import com.greenfoxacademy.greenbayapp.bid.repositories.BidRepository;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.LowBidException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotEnoughDollarsException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotFoundException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotSellableException;
import com.greenfoxacademy.greenbayapp.product.models.Product;
import com.greenfoxacademy.greenbayapp.product.models.dtos.ProductDetailsResponseDTO;
import com.greenfoxacademy.greenbayapp.product.services.ProductService;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import com.greenfoxacademy.greenbayapp.user.services.UserService;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BidServiceImpl implements BidService {
  private BidRepository bidRepository;
  private ProductService productService;
  private UserService userService;

  public BidServiceImpl(BidRepository bidRepository, ProductService productService, UserService userService) {
    this.bidRepository = bidRepository;
    this.productService = productService;
    this.userService = userService;
  }

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
  public ProductDetailsResponseDTO doBidding(Long productId, Integer bidPrice, UserEntity bidder)
      throws NotFoundException, AuthorizationException, NotSellableException, NotEnoughDollarsException,
      LowBidException, InvalidInputException {
    Product product = productService.findProductById(productId);

    checkBaseExceptions(product, bidPrice, bidder);
    Bid bid = createBid(product, bidPrice, bidder);
    if (bid.getBidPrice() >= product.getPurchasePrice()) setProductToSoldProduct(product, bid);

    return productService.convertProductIntoProductDetailsResponseDTO(product);
  }

  public void checkBaseExceptions(Product product, Integer bidPrice, UserEntity bidder)
      throws NotFoundException, AuthorizationException, NotSellableException, NotEnoughDollarsException,
      LowBidException, InvalidInputException {
    if (product == null) throw new NotFoundException();
    if (product.getSold()) throw new NotSellableException();
    if (product.getSeller().getId().equals(bidder.getId())) throw new AuthorizationException(
        "User can not do bidding on own items!");
    if (bidPrice > bidder.getBalance()) throw new NotEnoughDollarsException();
    if (bidPrice <= 0) throw new InvalidInputException("Bid price must be > 0!");
    if (product.getHighestBid() != null && bidPrice <= product.getHighestBid().getBidPrice())
      throw new LowBidException();
  }

  public Bid createBid(Product product, Integer bidPrice, UserEntity bidder) {
    Bid previousBid = product.getHighestBid();
    if (previousBid != null) returnDollarsToPreviousBidder(previousBid);

    //if same bidder is overbidding own bids, previous method wont work. Thus he is charged lower price here.
    if (previousBid != null && bidder.getId().equals(previousBid.getBidder().getId())) {
      userService.decreaseDollars(bidder,bidPrice - previousBid.getBidPrice());
    } else userService.decreaseDollars(bidder, bidPrice);

    Bid newBid = setNewBid(bidPrice, product, bidder);
    bidRepository.save(newBid);

    product.setHighestBid(newBid);
    productService.saveProduct(product);

    return newBid;
  }

  public Bid setNewBid(Integer bidPrice, Product product, UserEntity bidder) {
    Bid newBid = new Bid();
    newBid.setBidPrice(bidPrice);
    newBid.setBidTime(LocalDateTime.now());
    newBid.setProduct(product);
    newBid.setBidder(bidder);
    return newBid;
  }

  public UserEntity returnDollarsToPreviousBidder(Bid bid) {
    userService.increaseDollars(bid.getBidder(), bid.getBidPrice());
    return bid.getBidder();
  }

  public Product setProductToSoldProduct(Product product, Bid bid) {
    product.setSold(true);
    product.setSoldPrice(bid.getBidPrice());
    product.setSoldTime(bid.getBidTime());
    product.setBuyer(bid.getBidder());
    productService.saveProduct(product);
    return product;
  }
}
