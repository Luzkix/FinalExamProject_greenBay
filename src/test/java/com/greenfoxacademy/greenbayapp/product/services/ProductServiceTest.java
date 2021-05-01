package com.greenfoxacademy.greenbayapp.product.services;

import static org.mockito.ArgumentMatchers.any;

import com.greenfoxacademy.greenbayapp.factories.ProductFactory;
import com.greenfoxacademy.greenbayapp.factories.UserFactory;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.Product;
import com.greenfoxacademy.greenbayapp.product.repositories.ProductRepository;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
        .thenReturn(ProductFactory.createDefaultUnsoldProduct(1L,zdenek));

    NewProductResponseDTO response = productService.postNewProduct(request,zdenek);

    Assert.assertEquals(1, response.getId().intValue());
    Assert.assertEquals("zdenek", response.getSellerName());
    Assert.assertEquals("car", response.getName());
    Assert.assertEquals(100, response.getStartingPrice().intValue());

  }

  @Test
  public void setUpNewProduct_savesCorrectProduct() {
    NewProductRequestDTO request = ProductFactory.createDefaultProductRequestDTO();
    UserEntity zdenek = UserFactory.createDefaultZdenekUser();
    Product newProduct = ProductFactory.createDefaultUnsoldProduct(1L,zdenek);

    Product response = productService.setUpNewProduct(request,zdenek);

    /*  captor - it records the object which i am sending to specified method. Here it is Product which i want to save.
    So i can capture Product object to be saved and check whether selected properties matches with my expectations. */
    ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
    Mockito.verify(productRepository).save(captor.capture());

    Assert.assertEquals(newProduct.getName(),captor.getValue().getName());
    Assert.assertEquals(newProduct.getSeller(),captor.getValue().getSeller());
  }
}
