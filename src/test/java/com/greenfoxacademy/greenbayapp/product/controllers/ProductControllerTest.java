package com.greenfoxacademy.greenbayapp.product.controllers;

import com.greenfoxacademy.greenbayapp.factories.AuthFactory;
import com.greenfoxacademy.greenbayapp.factories.ProductFactory;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotFoundException;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.ProductDetailsResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.UnsoldProductDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.UnsoldProductsResponseDTO;
import com.greenfoxacademy.greenbayapp.product.services.ProductService;
import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public class ProductControllerTest {
  private ProductController productController;
  private ProductService productService;

  private Authentication auth;

  @Before
  public void setUp() {
    auth = AuthFactory.createAuth_userZdenek();
    productService = Mockito.mock(ProductService.class);
    productController = new ProductController(productService);
  }

  @Test
  public void postNewProduct_returnsCorrectStatusCodeAndProductNameAndSellerName() {
    NewProductRequestDTO request = ProductFactory.createNewProductRequestDTO_defaultDTO();
    UserEntity user = ((CustomUserDetails)auth.getPrincipal()).getUser();
    Mockito.when(productService.postNewProduct(request,user))
        .thenReturn(ProductFactory.createNewProductResponseDTO_defaultDTO_sellerZdenek());

    ResponseEntity<?> response = productController.postNewProduct(request,auth);

    Assert.assertEquals(201, response.getStatusCode().value());
    Assert.assertEquals("car", ((NewProductResponseDTO) response.getBody()).getName());
    Assert.assertEquals("zdenek", ((NewProductResponseDTO) response.getBody()).getSellerName());
  }

  @Test
  public void getSellableProducts_returnsCorrectStatusCodeAndUnsoldProductsResponseDTO() {
    List<UnsoldProductDTO> unsoldProducts = new ArrayList<>();
    unsoldProducts.addAll(Arrays.asList(
        ProductFactory.createUnsoldProductDTO_defaultDTO_sellerZdenek(1L),
        ProductFactory.createUnsoldProductDTO_defaultDTO_sellerZdenek(2L),
        ProductFactory.createUnsoldProductDTO_defaultDTO_sellerZdenek(3L)));
    UnsoldProductsResponseDTO responseDTO = new UnsoldProductsResponseDTO(unsoldProducts);

    Mockito.when(productService.filterUnsoldProducts(1)).thenReturn(responseDTO);

    ResponseEntity<?> response = productController.getSellableProducts(1,auth);

    Assert.assertEquals(200, response.getStatusCode().value());
    Assert.assertEquals(3, ((UnsoldProductsResponseDTO) response.getBody()).getUnsoldProducts().size());
  }

  @Test(expected = InvalidInputException.class)
  public void getSellableProducts_throwsNotFoundException() {
    ResponseEntity<?> response = productController.getSellableProducts(-1,auth);
  }

  @Test
  public void getProductDetails_returnsCorrectStatusCodeAndProductDetailsResponseDTO() {
    UserEntity user = ((CustomUserDetails)auth.getPrincipal()).getUser();
    ProductDetailsResponseDTO dto =
        ProductFactory.createProductDetailsResponseDTO_defaultSold_sellerZdenek_bidderPetr(1L);

    Mockito.when(productService.getProductDetails(1L,user)).thenReturn(dto);

    ResponseEntity<?> response = productController.getProductDetails(1L, auth);

    Assert.assertEquals(200, response.getStatusCode().value());
    Assert.assertEquals(1L,((ProductDetailsResponseDTO) response.getBody()).getId().longValue());
    Assert.assertEquals(2L,((ProductDetailsResponseDTO) response.getBody()).getBuyerId().longValue());
    Assert.assertEquals(3, ((ProductDetailsResponseDTO) response.getBody()).getBids().size());
  }

  @Test(expected = NotFoundException.class)
  public void getProductDetails_throwsNotFoundException() {
    UserEntity user = ((CustomUserDetails)auth.getPrincipal()).getUser();

    Mockito.when(productService.getProductDetails(1L,user)).thenThrow(NotFoundException.class);
    ResponseEntity<?> response = productController.getProductDetails(1L, auth);
  }

  @Test(expected = AuthorizationException.class)
  public void getProductDetails_throwsAuthorizationException() {
    UserEntity user = ((CustomUserDetails)auth.getPrincipal()).getUser();

    Mockito.when(productService.getProductDetails(1L,user)).thenThrow(AuthorizationException.class);
    ResponseEntity<?> response = productController.getProductDetails(1L, auth);
  }
}
