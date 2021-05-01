package com.greenfoxacademy.greenbayapp.factories;

import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.Product;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import java.time.LocalDateTime;

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

  public static Product createDefaultUnsoldProduct(Long productId, UserEntity seller) {
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

  public static Product createDefaultSoldProduct(Long productId, UserEntity seller, UserEntity buyer) {
    Product soldProduct = createDefaultUnsoldProduct(productId, seller);
    soldProduct.setSold(true);
    soldProduct.setSoldPrice(600);
    soldProduct.setSoldTime(LocalDateTime.parse("2022-04-30T23:16:02.462664400"));
    soldProduct.setBuyer(buyer);
    return soldProduct;
  }

  public static Product createDefaultUnsoldProductFromZdenek() {
    UserEntity user = UserFactory.createDefaultZdenekUser();
    return createDefaultUnsoldProduct(1L, user);
  }

  public static Product createDefaultSoldProductFromZdenekToPetr() {
    UserEntity zdenek = UserFactory.createDefaultZdenekUser();
    UserEntity petr = UserFactory.createDefaultPetrUser();
    return createDefaultSoldProduct(1L, zdenek, petr);
  }

  public static NewProductRequestDTO createProductRequestDTO(String name, String description, String photoUrl,
                                                              Integer startingPrice, Integer purchasePrice) {
    NewProductRequestDTO requestDTO = new NewProductRequestDTO();
    requestDTO.setName(name);
    requestDTO.setDescription(description);
    requestDTO.setPhotoUrl(photoUrl);
    requestDTO.setStartingPrice(startingPrice);
    requestDTO.setPurchasePrice(purchasePrice);
    return requestDTO;
  }

  public static NewProductRequestDTO createDefaultProductRequestDTO() {
    return createProductRequestDTO(
        "car",
        "blue car",
        "http://localhost:8080",
        100,
        500);
  }

  public static NewProductResponseDTO createProductResponseDTO(
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

  public static NewProductResponseDTO createDefaultProductResponseDTO_sellerZdenek() {
    UserEntity zdenek = UserFactory.createDefaultZdenekUser();
    return createProductResponseDTO(
        1L,
        "car",
        "blue car",
        "http://localhost:8080",
        100,
        500,
        LocalDateTime.parse("2021-04-30T23:16:02.462664400"),
        zdenek.getUsername());
  }



}
