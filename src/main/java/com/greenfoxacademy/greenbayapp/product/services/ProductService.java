package com.greenfoxacademy.greenbayapp.product.services;

import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.UnsoldProductsResponseDTO;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;

public interface ProductService {
  NewProductResponseDTO postNewProduct(NewProductRequestDTO request, UserEntity user);

  UnsoldProductsResponseDTO filterUnsoldProducts(Integer pageId);
}
