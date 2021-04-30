package com.greenfoxacademy.greenbayapp.factories;

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
        LocalDateTime.parse("2021-04-29 21:31:10"),
        null,
        seller,
        null);
  }

  public static Product createDefaultSoldProduct(Long productId, UserEntity seller, UserEntity buyer) {
    Product soldProduct = createDefaultUnsoldProduct(productId, seller);
    soldProduct.setSold(true);
    soldProduct.setSoldPrice(600);
    soldProduct.setSoldTime(LocalDateTime.parse("2022-04-29 21:31:10"));
    soldProduct.setBuyer(buyer);
    return soldProduct;
  }
  public static Product createDefaultUnsoldProductFromZdenek() {
    UserEntity user = UserFactory.createDefaultZdenekUser();
    return createDefaultUnsoldProduct(1l, user);
  }

  public static Product createDefaultSoldProductFromZdenekToPetr() {
    UserEntity zdenek = UserFactory.createDefaultZdenekUser();
    UserEntity petr = UserFactory.createDefaultPetrUser();
    return createDefaultSoldProduct(1l, zdenek, petr);
  }
}
