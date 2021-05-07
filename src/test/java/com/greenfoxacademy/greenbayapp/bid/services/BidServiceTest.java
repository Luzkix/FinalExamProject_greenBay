package com.greenfoxacademy.greenbayapp.bid.services;

import com.greenfoxacademy.greenbayapp.bid.models.Bid;
import com.greenfoxacademy.greenbayapp.bid.models.dtos.BidDetailsDTO;
import com.greenfoxacademy.greenbayapp.bid.repositories.BidRepository;
import com.greenfoxacademy.greenbayapp.factories.BidFactory;
import com.greenfoxacademy.greenbayapp.product.services.ProductService;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BidServiceTest {
  private BidServiceImpl bidService;
  private BidRepository bidRepository;
  private ProductService productService;

  @Before
  public void setUp() {
    bidRepository = Mockito.mock(BidRepository.class);
    productService = Mockito.mock(ProductService.class);
    bidService = Mockito.spy(new BidServiceImpl(bidRepository, productService));
  }

  @Test
  public void convertBidIntoBidDetailsDTO_returnsCorrectDTO() {
    Bid bid = BidFactory.createBid_defaultSoldProduct_sellerZdenek_buyerPetr(1L,1L);

    BidDetailsDTO dto = bidService.convertBidIntoBidDetailsDTO(bid);

    Assert.assertEquals(bid.getId(), dto.getId());
    Assert.assertEquals(bid.getBidder().getUsername(), dto.getBidderName());
    Assert.assertEquals(bid.getProduct().getName(), dto.getProductName());
    Assert.assertEquals(bid.getProduct().getId(), dto.getProductId());
  }

  @Test
  public void convertSetOfBidsIntoListOfBidDetailDTOs_returnsCorrectList() {
    Bid bid1 = BidFactory.createBid_defaultSoldProduct_sellerZdenek_buyerPetr(1L,1L);
    Bid bid2 = BidFactory.createBid_defaultSoldProduct_sellerZdenek_buyerPetr(2L,2L);
    Bid bid3 = BidFactory.createBid_defaultSoldProduct_sellerZdenek_buyerPetr(3L,3L);
    Set<Bid> bids = BidFactory.createSetOfBids_3bids(bid1, bid2, bid3);

    List<BidDetailsDTO> result = bidService.convertSetOfBidsIntoListOfBidDetailDTOs(bids);

    Assert.assertEquals(bid1.getId(), result.get(0).getId());
    Assert.assertEquals(bid2.getId(), result.get(1).getId());
    Assert.assertEquals(bid3.getId(), result.get(2).getId());
    Assert.assertEquals(bid1.getProduct().getId(), result.get(0).getProductId());
    Assert.assertEquals(bid2.getProduct().getId(), result.get(1).getProductId());
    Assert.assertEquals(bid3.getProduct().getId(), result.get(2).getProductId());
  }
}
