package com.greenfoxacademy.greenbayapp.product.services;

import static org.mockito.ArgumentMatchers.any;


import com.greenfoxacademy.greenbayapp.factories.ProductFactory;
import com.greenfoxacademy.greenbayapp.factories.UserFactory;
import com.greenfoxacademy.greenbayapp.product.models.Product;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.UnsoldProductDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.UnsoldProductsResponseDTO;
import com.greenfoxacademy.greenbayapp.product.repositories.ProductRepository;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    NewProductRequestDTO request = ProductFactory.createNewProductRequestDTO_defaultDTO();
    UserEntity zdenek = UserFactory.createUser_defaultUserZdenek();
    Mockito.when(productRepository.save(any()))
        .thenReturn(ProductFactory.createProduct_defaultUnsoldProduct(1L,zdenek));

    NewProductResponseDTO response = productService.postNewProduct(request,zdenek);

    Assert.assertEquals(1, response.getId().intValue());
    Assert.assertEquals("zdenek", response.getSellerName());
    Assert.assertEquals("car", response.getName());
    Assert.assertEquals(100, response.getStartingPrice().intValue());

  }

  @Test
  public void setUpNewProduct_savesCorrectProduct() {
    NewProductRequestDTO request = ProductFactory.createNewProductRequestDTO_defaultDTO();
    UserEntity zdenek = UserFactory.createUser_defaultUserZdenek();
    Product newProduct = ProductFactory.createProduct_defaultUnsoldProduct(1L,zdenek);

    Product response = productService.setUpNewProduct(request,zdenek);

    /*  captor - it records the object which i am sending to specified method. Here it is Product which i want to save.
    So i can capture Product object to be saved and check whether selected properties matches with my expectations. */
    ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
    Mockito.verify(productRepository).save(captor.capture());

    Assert.assertEquals(newProduct.getName(),captor.getValue().getName());
    Assert.assertEquals(newProduct.getSeller(),captor.getValue().getSeller());
  }

  @Test
  public void convertProductsToUnsoldProductsDetailsDTO_returnsCorrectList() {
    List<Product> products = createListOfProducts();

    List<UnsoldProductDTO> result = productService.convertProductsToUnsoldProductsDetailsDTO(products);

    Assert.assertTrue(products.size() == result.size());
    Assert.assertTrue(products.get(0).getId() == result.get(0).getId());
    Assert.assertTrue(products.get(0).getEnlistingTime() == result.get(0).getEnlistingTime());
    Assert.assertTrue(products.get(2).getId() == result.get(2).getId());
    Assert.assertTrue(products.get(2).getEnlistingTime() == result.get(2).getEnlistingTime());
  }

  private List<Product> createListOfProducts() {
    UserEntity zdenek = UserFactory.createUser_defaultUserZdenek();
    List<Product> products = new ArrayList<>();
    products.add(ProductFactory.createProduct_defaultUnsoldProduct(1L,zdenek));
    products.add(ProductFactory.createProduct_defaultUnsoldProduct(2L,zdenek));
    products.add(ProductFactory.createProduct_defaultUnsoldProduct(3L,zdenek));
    return products;
  }

  @Test
  public void filterUnsoldProducts_returnsCorrectResponseDTO() {
    List<Product> products = createListOfProducts();
    List<UnsoldProductDTO> unsoldProducts = new ArrayList<>();
    unsoldProducts.addAll(Arrays.asList(
        ProductFactory.createUnsoldProductDTO_defaultDTO_sellerZdenek(1L),
        ProductFactory.createUnsoldProductDTO_defaultDTO_sellerZdenek(2L),
        ProductFactory.createUnsoldProductDTO_defaultDTO_sellerZdenek(3L)
    ));

    Mockito.when(productRepository.filterUnsoldPaginated(any(),any())).thenReturn(products);

    UnsoldProductsResponseDTO result = productService.filterUnsoldProducts(1);

    Assert.assertTrue(products.size() == result.getUnsoldProducts().size());
    Assert.assertTrue(products.get(0).getId() == result.getUnsoldProducts().get(0).getId());
    Assert.assertTrue(products.get(0).getEnlistingTime() == result.getUnsoldProducts().get(0).getEnlistingTime());
    Assert.assertTrue(products.get(2).getId() == result.getUnsoldProducts().get(2).getId());
    Assert.assertTrue(products.get(2).getEnlistingTime() == result.getUnsoldProducts().get(2).getEnlistingTime());
  }

}
