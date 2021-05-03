package com.greenfoxacademy.greenbayapp.bid.models.dtos;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BidDetailsDTO {
  private Long id;
  private Integer bidPrice;
  private LocalDateTime bidTime;

  private Long productId;
  private String productName;
  private Long bidderId;
  private String bidderName;
}
