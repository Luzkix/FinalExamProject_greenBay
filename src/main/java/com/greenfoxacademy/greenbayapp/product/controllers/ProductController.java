package com.greenfoxacademy.greenbayapp.product.controllers;

import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.product.services.ProductService;
import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProductController {
  public static final String PRODUCT_URI = "/product";

  private ProductService productService;

  @PostMapping(PRODUCT_URI)
  public ResponseEntity<?> postNewProduct(@RequestBody @Valid NewProductRequestDTO request, Authentication auth) {
    UserEntity user = ((CustomUserDetails) auth.getPrincipal()).getUser();
    NewProductResponseDTO response = productService.postNewProduct(request, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

}
