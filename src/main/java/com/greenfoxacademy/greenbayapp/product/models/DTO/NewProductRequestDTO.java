package com.greenfoxacademy.greenbayapp.product.models.DTO;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewProductRequestDTO {
  @NotNull(message = "Item name can´t be empty!")
  private String name;
  @NotNull(message = "Item description can´t be empty!")
  private String description;
  @NotNull(message = "Photo URL can´t be empty!")
  @URL(message = "Wrong URL format!")
  private String photoUrl;
  @NotNull(message = "Starting price can´t be empty!")
  @Min(value = 1, message = "Starting price must be > 0!")
  private Integer startingPrice;
  @NotNull(message = "Purchase price can´t be empty!")
  @Min(value = 1, message = "Purchase price must be > 0!")
  private Integer purchasePrice;
}
