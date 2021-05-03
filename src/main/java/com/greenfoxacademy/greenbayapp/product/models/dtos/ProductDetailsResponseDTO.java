package com.greenfoxacademy.greenbayapp.product.models.dtos;

import com.greenfoxacademy.greenbayapp.bid.models.dtos.BidDetailsDTO;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailsResponseDTO {
  private Long id;
  private String name;
  private String description;
  private String photoUrl;
  private Integer startingPrice;
  private Integer purchasePrice;
  private Boolean sold;
  private Integer soldPrice;
  private LocalDateTime enlistingTime;
  private LocalDateTime soldTime;

  private Long sellerId;
  private String sellerName;
  private Long buyerId;
  private String buyerName;

  private List<BidDetailsDTO> bids;
}
