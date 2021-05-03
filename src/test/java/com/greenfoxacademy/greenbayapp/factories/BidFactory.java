package com.greenfoxacademy.greenbayapp.factories;

import com.greenfoxacademy.greenbayapp.bid.models.Bid;
import com.greenfoxacademy.greenbayapp.bid.models.dtos.BidDetailsDTO;
import com.greenfoxacademy.greenbayapp.product.models.Product;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BidFactory {

  public static Bid createBid(Long id, Integer bidPrice, LocalDateTime bidTime, Product product, UserEntity bidder) {
    Bid bid = new Bid();
    bid.setId(id);
    bid.setBidPrice(bidPrice);
    bid.setBidTime(bidTime);
    bid.setProduct(product);
    bid.setBidder(bidder);
    return bid;
  }

  public static Bid createBid_defaultSoldProduct_sellerZdenek_buyerPetr(Long bidId, Long productId) {
    UserEntity zdenek = UserFactory.createUser_defaultUserZdenek();
    UserEntity petr = UserFactory.createUser_defaultUserPetr();
    Product product = ProductFactory.createProduct_defaultSoldProduct(productId,zdenek,petr);
    return createBid(
        bidId,
        600,
        LocalDateTime.parse("2022-05-30T23:16:02.462664400"),
        product,
        petr
    );
  }

  public static Set<Bid> createSetOfBids_3bids(Bid bid1, Bid bid2, Bid bid3) {
    Set<Bid> bids = new HashSet<>();
    bids.add(bid1);
    bids.add(bid2);
    bids.add(bid3);
    return bids;
  }

  public static Set<Bid> createSetOfBids_3predefinedBids_sellerZdenek_buyerPetr() {
    Bid bid1 = BidFactory.createBid_defaultSoldProduct_sellerZdenek_buyerPetr(1L,1L);
    Bid bid2 = BidFactory.createBid_defaultSoldProduct_sellerZdenek_buyerPetr(2L,1L);
    Bid bid3 = BidFactory.createBid_defaultSoldProduct_sellerZdenek_buyerPetr(3L,1L);
    Set<Bid> bids = BidFactory.createSetOfBids_3bids(bid1, bid2, bid3);
    return bids;
  }

  public static BidDetailsDTO createBidDetailsDTO(Long id, Integer bidPrice, LocalDateTime bidTime, Long productId,
                                                  String productName, Long bidderId, String bidderName) {
    BidDetailsDTO bid = new BidDetailsDTO();
    bid.setId(id);
    bid.setBidPrice(bidPrice);
    bid.setBidTime(bidTime);
    bid.setProductId(productId);
    bid.setProductName(productName);
    bid.setBidderId(bidderId);
    bid.setBidderName(bidderName);
    return bid;
  }

  public static BidDetailsDTO createBidDetailsDTO_defaultBid(Long bidId, Long bidderId) {
    return createBidDetailsDTO(
        bidId,
        500,
        LocalDateTime.parse("2022-05-30T23:16:02.462664400"),
        1L,
        "car",
        bidderId,
        "petr"
    );
  }

  public static List<BidDetailsDTO> createListOfBidDetailsDTO_3predefinedBids_bidderPetr() {
    List<BidDetailsDTO> bids = new ArrayList<>();
    bids.add(createBidDetailsDTO_defaultBid(1L,2L));
    bids.add(createBidDetailsDTO_defaultBid(2L,2L));
    bids.add(createBidDetailsDTO_defaultBid(3L,2L));
    return bids;
  }
}
