package com.greenfoxacademy.greenbayapp.product.controllers;

import com.greenfoxacademy.greenbayapp.globalexceptionhandling.AuthorizationException;
import com.greenfoxacademy.greenbayapp.globalexceptionhandling.InvalidInputException;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductResponseDTO;
import com.greenfoxacademy.greenbayapp.product.models.dtos.ProductDetailsResponseDTO;
import com.greenfoxacademy.greenbayapp.product.services.ProductService;
import com.greenfoxacademy.greenbayapp.security.CustomUserDetails;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProductController {
  public static final String PRODUCT_URI = "/product";
  //public static final String PRODUCT_ID_URI = PRODUCT_URI + "/{id}";
  public static final String ACTIVE_DEALS_URI = "/activedeals";


  private ProductService productService;

  @PostMapping(PRODUCT_URI)
  public ResponseEntity<?> postNewProduct(@RequestBody @Valid NewProductRequestDTO request, Authentication auth) {
    UserEntity user = ((CustomUserDetails) auth.getPrincipal()).getUser();
    NewProductResponseDTO response = productService.postNewProduct(request, user);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping(ACTIVE_DEALS_URI)
  public ResponseEntity<?> getSellableProducts(@RequestParam (required = false, name = "page", defaultValue = "1")
                                                     Integer pageId, Authentication auth) throws InvalidInputException {
    if (pageId < 1) throw new InvalidInputException("Invalid page number! Page must be > 0!");
    return ResponseEntity.ok().body(productService.filterUnsoldProducts(pageId));
  }

  @GetMapping(PRODUCT_URI+"/{id}")
  public ResponseEntity<?> getProductDetails(@PathVariable Long id, Authentication auth)
      throws InvalidInputException, AuthorizationException {
    UserEntity user = ((CustomUserDetails) auth.getPrincipal()).getUser();
    ProductDetailsResponseDTO responseDTO = productService.getProductDetails(id, user);

    return ResponseEntity.ok().body(responseDTO);
  }

}
