package com.greenfoxacademy.greenbayapp.product.services;

import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.NotFoundException;
import com.greenfoxacademy.greenbayapp.product.models.Product;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.ProductDetailsResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.UnsoldProductsResponseDTO;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;

public interface ProductService {
  NewProductResponseDTO postNewProduct(NewProductRequestDTO request, UserEntity user);

  UnsoldProductsResponseDTO filterUnsoldProducts(Integer pageId);

  ProductDetailsResponseDTO getProductDetails(Long id, UserEntity user) throws
      NotFoundException, AuthorizationException;

  Product getProductById(Long productId);
}
