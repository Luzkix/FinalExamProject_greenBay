package com.greenfoxacademy.greenbayapp.product.controllers;

import com.greenfoxacademy.greenbayapp.factories.AuthFactory;
import com.greenfoxacademy.greenbayapp.factories.ProductFactory;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;


import com.greenfoxacademy.greenbayapp.product.services.ProductService;
import org.junit.Before;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public class ProductControllerTest {
  private ProductController productController;
  private ProductService productService;

  private Authentication auth;

  @Before
  public void setUp() {
    auth = AuthFactory.createZdenekAuth();
    productService = Mockito.mock(ProductService.class);
    productController = new ProductController(productService);
  }

  @Test
  public void postNewProduct_returnsCorrectStatusCode() {
    NewProductRequestDTO request = ProductFactory.createDefaultProductRequestDTO();
    UserEntity user = ((CustomUserDetails)auth.getPrincipal()).getUser();
    Mockito.when(productService.postNewProduct(request,user))
        .thenReturn(ProductFactory.createDefaultProductResponseDTO_sellerZdenek());

    ResponseEntity<?> response = productController.postNewProduct(request,auth);

    Assert.assertEquals(201, response.getStatusCode().value());
    Assert.assertEquals("car", ((NewProductResponseDTO) response.getBody()).getName());
    Assert.assertEquals("zdenek", ((NewProductResponseDTO) response.getBody()).getSellerName());
  }

}
