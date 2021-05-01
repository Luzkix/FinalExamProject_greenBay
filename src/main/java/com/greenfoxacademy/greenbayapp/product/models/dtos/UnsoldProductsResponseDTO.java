package com.greenfoxacademy.greenbayapp.product.models.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UnsoldProductsResponseDTO {
  private List<UnsoldProductDTO> unsoldProducts;
}
