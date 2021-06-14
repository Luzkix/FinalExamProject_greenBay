package com.greenfoxacademy.greenbayapp.factories;

import com.greenfoxacademy.greenbayapp.bid.models.dtos.BidDetailsDTO;
import com.greenfoxacademy.greenbayapp.product.models.Product;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.ProductDetailsResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.UnsoldProductDTO;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.BeanUtils;

public class ProductFactory {
  public static Product createProduct(Long id, String name, String description, String photoUrl,
                                      Integer startingPrice, Integer purchasePrice, Boolean sold,
                                      Integer soldPrice, LocalDateTime enlistingTime,
                                      LocalDateTime soldTime, UserEntity seller, UserEntity buyer) {
    Product product = new Product();
    product.setId(id);
    product.setName(name);
    product.setDescription(description);
    product.setPhotoUrl(photoUrl);
    product.setStartingPrice(startingPrice);
    product.setPurchasePrice(purchasePrice);
    product.setSold(sold);
    product.setSoldPrice(soldPrice);
    product.setEnlistingTime(enlistingTime);
    product.setSoldTime(soldTime);
    product.setSeller(seller);
    product.setBuyer(buyer);
    return product;
  }

  public static Product createProduct_defaultUnsoldProduct(Long productId, UserEntity seller) {
    return createProduct(
        productId,
        "car",
        "blue car",
        "http://localhost:8080",
        100,
        500,
        false,
        null,
        LocalDateTime.parse("2021-04-30T23:16:02.462664400"),
        null,
        seller,
        null);
  }

  public static Product createProduct_defaultSoldProduct(Long productId, UserEntity seller, UserEntity buyer) {
    Product soldProduct = createProduct_defaultUnsoldProduct(productId, seller);
    soldProduct.setSold(true);
    soldProduct.setSoldPrice(600);
    soldProduct.setSoldTime(LocalDateTime.parse("2022-04-30T23:16:02.462664400"));
    soldProduct.setBuyer(buyer);
    return soldProduct;
  }

  public static Product createProduct_defaultUnsoldProductFromZdenek() {
    UserEntity user = UserFactory.createUser_defaultUserZdenek();
    return createProduct_defaultUnsoldProduct(1L, user);
  }

  public static Product createProduct_defaultSoldProductFromZdenekToPetr() {
    UserEntity zdenek = UserFactory.createUser_defaultUserZdenek();
    UserEntity petr = UserFactory.createUser_defaultUserPetr();
    return createProduct_defaultSoldProduct(1L, zdenek, petr);
  }

  public static NewProductRequestDTO createNewProductRequestDTO(String name, String description, String photoUrl,
                                                                Integer startingPrice, Integer purchasePrice) {
    NewProductRequestDTO requestDTO = new NewProductRequestDTO();
    requestDTO.setName(name);
    requestDTO.setDescription(description);
    requestDTO.setPhotoUrl(photoUrl);
    requestDTO.setStartingPrice(startingPrice);
    requestDTO.setPurchasePrice(purchasePrice);
    return requestDTO;
  }

  public static NewProductRequestDTO createNewProductRequestDTO_defaultDTO() {
    return createNewProductRequestDTO(
        "car",
        "blue car",
        "http://localhost:8080",
        100,
        500);
  }

  public static NewProductResponseDTO createNewProductResponseDTO(
      Long id, String name, String description, String photoUrl, Integer startingPrice,
       Integer purchasePrice, LocalDateTime enlistingTime, String sellerName) {
    NewProductResponseDTO responseDTO = new NewProductResponseDTO();
    responseDTO.setId(id);
    responseDTO.setName(name);
    responseDTO.setDescription(description);
    responseDTO.setPhotoUrl(photoUrl);
    responseDTO.setStartingPrice(startingPrice);
    responseDTO.setPurchasePrice(purchasePrice);
    responseDTO.setEnlistingTime(enlistingTime);
    responseDTO.setSellerName(sellerName);
    return responseDTO;
  }

  public static NewProductResponseDTO createNewProductResponseDTO_defaultDTO_sellerZdenek() {
    UserEntity zdenek = UserFactory.createUser_defaultUserZdenek();
    return createNewProductResponseDTO(
        1L,
        "car",
        "blue car",
        "http://localhost:8080",
        100,
        500,
        LocalDateTime.parse("2021-04-30T23:16:02.462664400"),
        zdenek.getUsername());
  }

  public static UnsoldProductDTO createUnsoldProductDTO_defaultDTO_sellerZdenek(Long productId) {
    UserEntity zdenek = UserFactory.createUser_defaultUserZdenek();
    Product product = ProductFactory.createProduct_defaultUnsoldProduct(productId,zdenek);
    UnsoldProductDTO unsoldProductDTO = new UnsoldProductDTO();
    BeanUtils.copyProperties(product, unsoldProductDTO);
    return unsoldProductDTO;
  }

  public static ProductDetailsResponseDTO createProductDetailsResponseDTO_defaultSold_sellerZdenek_bidderPetr(
      Long productId) {
    UserEntity zdenek = UserFactory.createUser_defaultUserZdenek();
    UserEntity petr = UserFactory.createUser_defaultUserPetr();
    Product soldProduct = ProductFactory.createProduct_defaultSoldProduct(productId,zdenek,petr);
    List<BidDetailsDTO> bids = BidFactory.createListOfBidDetailsDTO_3predefinedBids_bidderPetr();

    ProductDetailsResponseDTO dto = new ProductDetailsResponseDTO();
    BeanUtils.copyProperties(soldProduct, dto);
    dto.setBids(bids);
    dto.setSellerId(1L);
    dto.setSellerName(zdenek.getUsername());
    dto.setBuyerId(2L);
    dto.setBuyerName(petr.getUsername());

    return dto;
  }
}
