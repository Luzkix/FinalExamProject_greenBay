package com.greenfoxacademy.greenbayapp.product.services;

import static org.mockito.ArgumentMatchers.any;

import com.greenfoxacademy.greenbayapp.factories.ProductFactory;
import com.greenfoxacademy.greenbayapp.factories.UserFactory;
import com.greenfoxacademy.greenbayapp.product.models.DTO.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.DTO.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.product.repositories.ProductRepository;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ProductServiceTest {
  ProductServiceImpl productService;
  ProductRepository productRepository;

  @Before
  public void setUp() {
    productRepository = Mockito.mock(ProductRepository.class);
    productService = Mockito.spy(new ProductServiceImpl(productRepository));
  }

  @Test
  public void postNewProduct_returnsCorrectNewProductResponseDTO() {
    NewProductRequestDTO request = ProductFactory.createDefaultProductRequestDTO();
    UserEntity zdenek = UserFactory.createDefaultZdenekUser();
    Mockito.when(productRepository.save(any()))
        .thenReturn(ProductFactory.createDefaultUnsoldProduct(1l,zdenek));

    NewProductResponseDTO response = productService.postNewProduct(request,zdenek);

    Assert.assertEquals(1, response.getId().intValue());
    Assert.assertEquals("zdenek", response.getSellerName());
    Assert.assertEquals("car", response.getName());
    Assert.assertEquals(100, response.getStartingPrice().intValue());
  }
}
