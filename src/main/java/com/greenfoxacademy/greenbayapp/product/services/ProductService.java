package com.greenfoxacademy.greenbayapp.product.services;

import com.greenfoxacademy.greenbayapp.product.models.DTO.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.DTO.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;

public interface ProductService {
  NewProductResponseDTO postNewProduct(NewProductRequestDTO request, UserEntity user);

}
