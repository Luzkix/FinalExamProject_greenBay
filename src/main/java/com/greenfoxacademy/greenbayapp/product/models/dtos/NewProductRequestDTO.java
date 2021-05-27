package com.greenfoxacademy.greenbayapp.product.models.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
  @NotBlank(message = "Item name can´t be empty!")
  @Size(max = 100, message = "Item name is too long!")
  private String name;
  @NotBlank(message = "Item description can´t be empty!")
  @Size(max = 250, message = "Item description is too long!")
  private String description;
  @NotBlank(message = "Photo URL can´t be empty!")
  @URL(message = "Wrong URL format!")
  @Size(max = 300, message = "Photo URL is too long!")
  private String photoUrl;
  @NotNull(message = "Starting price can´t be empty!")
  @Min(value = 1, message = "Starting price must be > 0!")
  private Integer startingPrice;
  @NotNull(message = "Purchase price can´t be empty!")
  @Min(value = 1, message = "Purchase price must be > 0!")
  private Integer purchasePrice;
}
