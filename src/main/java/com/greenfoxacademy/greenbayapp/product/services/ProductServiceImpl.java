package com.greenfoxacademy.greenbayapp.product.services;

import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.Product;
import com.greenfoxacademy.greenbayapp.product.models.dtos.UnsoldProductDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.UnsoldProductsResponseDTO;
import com.greenfoxacademy.greenbayapp.product.repositories.ProductRepository;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {
  private ProductRepository productRepository;

  @Override
  public NewProductResponseDTO postNewProduct(NewProductRequestDTO request, UserEntity user) {
    Product product = setUpNewProduct(request, user);
    return prepareNewProductResponseDTO(product, user);
  }

  public NewProductResponseDTO prepareNewProductResponseDTO(Product product, UserEntity user) {
    NewProductResponseDTO response = new NewProductResponseDTO();
    BeanUtils.copyProperties(product,response);
    response.setSellerName(user.getUsername());
    return response;
  }

  public Product setUpNewProduct(NewProductRequestDTO request, UserEntity user) {
    Product product = new Product();
    BeanUtils.copyProperties(request,product);
    product.setSold(false);
    product.setEnlistingTime(LocalDateTime.now());
    product.setSeller(user);
    return productRepository.save(product);
  }

  @Override
  public UnsoldProductsResponseDTO filterUnsoldProducts(Integer pageId) {
    Pageable pageable = PageRequest.of(pageId - 1, 5);
    List<Product> unsoldProducts = productRepository.filterUnsoldPaginated(pageId, pageable);
    List<UnsoldProductDTO> convertedProducts = convertProductsToUnsoldProductsDetailsDTO(unsoldProducts);
    return new UnsoldProductsResponseDTO(convertedProducts);
  }

  public List<UnsoldProductDTO> convertProductsToUnsoldProductsDetailsDTO(List<Product> products) {
    List<UnsoldProductDTO> dtos = new ArrayList<>();

    for (int i = 0; i < products.size(); i++) {
      UnsoldProductDTO dto = new UnsoldProductDTO();
      BeanUtils.copyProperties(products.get(i),dto);
      dtos.add(dto);
    }
    return dtos;
  }


}
