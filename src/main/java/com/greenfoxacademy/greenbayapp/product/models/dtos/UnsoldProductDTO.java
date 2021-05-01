package com.greenfoxacademy.greenbayapp.product.models.dtos;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnsoldProductDTO {
  private Long id;
  private String name;
  private String photoUrl;
  private Integer highestBid;
  private LocalDateTime enlistingTime;
}
