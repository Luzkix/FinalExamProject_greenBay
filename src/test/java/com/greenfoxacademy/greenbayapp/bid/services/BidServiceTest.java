package com.greenfoxacademy.greenbayapp.bid.services;

import static org.mockito.ArgumentMatchers.any;


import com.greenfoxacademy.greenbayapp.bid.models.Bid;
import com.greenfoxacademy.greenbayapp.bid.models.dtos.BidDetailsDTO;
import com.greenfoxacademy.greenbayapp.bid.repositories.BidRepository;
import com.greenfoxacademy.greenbayapp.factories.BidFactory;
import com.greenfoxacademy.greenbayapp.factories.ProductFactory;
import com.greenfoxacademy.greenbayapp.factories.UserFactory;
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
  private UserService userService;

  @Before
  public void setUp() {
    bidRepository = Mockito.mock(BidRepository.class);
    productService = Mockito.mock(ProductService.class);
    userService = Mockito.mock(UserService.class);
    bidService = Mockito.spy(new BidServiceImpl(bidRepository, productService, userService));
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

  @Test
  public void doBidding_returnsCorrectDTO() {
    Bid bid = BidFactory.createBid_defaultUnsoldProduct_sellerZdenek_bidderPetr(1L,1L,10);
    UserEntity bidder = bid.getBidder();
    Product product = bid.getProduct();
    ProductDetailsResponseDTO dto = ProductFactory
        .createProductDetailsResponseDTO_defaultSold_sellerZdenek_bidderPetr(product.getId());

    Mockito.when(productService.findProductById(product.getId())).thenReturn(product);
    Mockito.doNothing().when(bidService).checkBaseExceptions(product,bid.getBidPrice(),bidder);
    Mockito.doReturn(bid).when(bidService).createBid(product,bid.getBidPrice(),bidder);
    Mockito.doReturn(product).when(bidService).setProductToSoldProduct(product,bid);
    Mockito.when(productService.convertProductIntoProductDetailsResponseDTO(product)).thenReturn(dto);

    ProductDetailsResponseDTO responseDTO = bidService.doBidding(product.getId(), bid.getBidPrice(), bidder);

    Assert.assertEquals(dto.getId(), responseDTO.getId());
    Assert.assertEquals(dto.getSellerName(), responseDTO.getSellerName());
    Assert.assertEquals(dto.getBids(), responseDTO.getBids());
  }

  @Test(expected = NotFoundException.class)
  public void checkBaseExceptions_throwsNotFoundException() {
    Product product = null;
    UserEntity bidder = UserFactory.createUser_defaultUser_withBalance(2L,"petr",100);
    Integer bidPrice = 100;

    bidService.checkBaseExceptions(product,bidPrice,bidder);
  }

  @Test(expected = NotSellableException.class)
  public void checkBaseExceptions_throwsNotSellableException() {
    Product product = ProductFactory.createProduct_defaultUnsoldProductFromZdenek(); //purchace price = 500
    product.setSold(true);
    UserEntity bidder = UserFactory.createUser_defaultUser_withBalance(2L,"petr",100);
    Integer bidPrice = 100;

    bidService.checkBaseExceptions(product,bidPrice,bidder);
  }

  @Test(expected = AuthorizationException.class)
  public void checkBaseExceptions_throwsAuthorizationException() {
    Product product = ProductFactory.createProduct_defaultUnsoldProductFromZdenek(); //purchace price = 500
    UserEntity bidder = UserFactory.createUser_defaultUser_withBalance(2L,"petr",100);
    product.setSeller(bidder);
    Integer bidPrice = 100;

    bidService.checkBaseExceptions(product,bidPrice,bidder);
  }

  @Test(expected = NotEnoughDollarsException.class)
  public void checkBaseExceptions_throwsNotEnoughDollarsException() {
    Product product = ProductFactory.createProduct_defaultUnsoldProductFromZdenek(); //purchace price = 500
    UserEntity bidder = UserFactory.createUser_defaultUser_withBalance(2L,"petr",100);
    Integer bidPrice = 200;

    bidService.checkBaseExceptions(product,bidPrice,bidder);
  }

  @Test(expected = InvalidInputException.class)
  public void checkBaseExceptions_throwsInvalidInputException() {
    Product product = ProductFactory.createProduct_defaultUnsoldProductFromZdenek(); //purchace price = 500
    UserEntity bidder = UserFactory.createUser_defaultUser_withBalance(2L,"petr",100);
    Integer bidPrice = 0;

    bidService.checkBaseExceptions(product,bidPrice,bidder);
  }

  @Test(expected = LowBidException.class)
  public void checkBaseExceptions_throwsLowBidException() {
    Bid bid = BidFactory.createBid_defaultUnsoldProduct_sellerZdenek_bidderPetr(1L,1L,200);
    Product product = bid.getProduct();
    UserEntity bidder = UserFactory.createUser_defaultUser_withBalance(2L,"petr",100);
    Integer bidPrice = 100;

    bidService.checkBaseExceptions(product,bidPrice,bidder);
  }

  @Test
  public void createBid_productHasNoPreviousBids_returnsNewBid() {
    Product product = ProductFactory.createProduct_defaultUnsoldProductFromZdenek(); //purchace price = 500
    UserEntity bidder = UserFactory.createUser_defaultUser_withBalance(2L,"petr",100);
    Integer bidPrice = 100;

    Mockito.when(userService.decreaseDollars(bidder, bidPrice)).thenReturn(null);

    Bid bid = bidService.createBid(product,bidPrice,bidder);

    Assert.assertEquals(bidPrice, bid.getBidPrice());
    Assert.assertEquals(product, bid.getProduct());
    Assert.assertEquals(bidder, bid.getBidder());
    Assert.assertEquals(bid,product.getHighestBid());
  }

  @Test
  public void createBid_productHasPreviousOwnBids_returnsNewBid() {
    Bid previousBid = BidFactory.createBid_defaultUnsoldProduct_sellerZdenek_bidderPetr(1L,1L,100);
    Product product = previousBid.getProduct();
    UserEntity bidder = previousBid.getBidder();
    Integer bidPrice = 200;

    Mockito.doReturn(bidder).when(bidService).returnDollarsToPreviousBidder(previousBid);
    Mockito.when(userService.decreaseDollars(bidder,bidPrice - previousBid.getBidPrice()))
        .thenReturn(null);

    Bid bid = bidService.createBid(product,bidPrice,bidder);

    Assert.assertEquals(bidPrice, bid.getBidPrice());
    Assert.assertEquals(product, bid.getProduct());
    Assert.assertEquals(bidder, bid.getBidder());
    Assert.assertEquals(bid,product.getHighestBid());
  }

  @Test
  public void createBid_productHasPreviousNotOwnBids_returnsNewBid() {
    Bid previousBid = BidFactory.createBid_defaultUnsoldProduct_sellerZdenek_bidderPetr(1L,1L,100);
    Product product = ProductFactory.createProduct_defaultUnsoldProductFromZdenek();
    previousBid.setProduct(product);
    product.setHighestBid(previousBid);
    UserEntity bidder = UserFactory.createUser_defaultUser(3L, "marek");
    Integer bidPrice = 200;

    Mockito.doReturn(bidder).when(bidService).returnDollarsToPreviousBidder(previousBid);
    Mockito.when(userService.decreaseDollars(bidder, bidPrice)).thenReturn(null);

    Bid bid = bidService.createBid(product,bidPrice,bidder);

    Assert.assertEquals(bidPrice, bid.getBidPrice());
    Assert.assertEquals(product, bid.getProduct());
    Assert.assertEquals(bidder, bid.getBidder());
    Assert.assertEquals(bid,product.getHighestBid());
  }

  @Test
  public void setNewBid_returnsNewBid() {
    Product product = ProductFactory.createProduct_defaultUnsoldProductFromZdenek();
    UserEntity bidder = UserFactory.createUser_defaultUserPetr();
    Integer bidPrice = 200;

    Bid newBid = bidService.setNewBid(bidPrice,product,bidder);

    Assert.assertEquals(bidPrice, newBid.getBidPrice());
    Assert.assertEquals(product, newBid.getProduct());
    Assert.assertEquals(bidder, newBid.getBidder());
  }

  @Test
  public void returnDollarsToPreviousBidder_returnsBidder() {
    Bid bid = BidFactory.createBid_defaultUnsoldProduct_sellerZdenek_bidderPetr(1L,1L,100);
    UserEntity bidder = bid.getBidder();

    Mockito.when(userService.increaseDollars(bid.getBidder(), bid.getBidPrice()))
        .thenReturn(null);

    UserEntity finalBidder = bidService.returnDollarsToPreviousBidder(bid);

    Assert.assertEquals(bidder, finalBidder);
  }

  @Test
  public void setProductToSoldProduct_returnsSoldProduct() {
    Product product = ProductFactory.createProduct_defaultUnsoldProductFromZdenek();
    Bid bid = BidFactory.createBid_defaultUnsoldProduct_sellerZdenek_bidderPetr(1L,1L,600);

    Mockito.when(productService.saveProduct(any())).thenReturn(null);

    Product soldProduct = bidService.setProductToSoldProduct(product,bid);

    Assert.assertEquals(true, soldProduct.getSold());
    Assert.assertEquals(bid.getBidPrice(), soldProduct.getSoldPrice());
    Assert.assertEquals(bid.getBidTime(), soldProduct.getSoldTime());
    Assert.assertEquals(bid.getBidder(), soldProduct.getBuyer());
  }
}
