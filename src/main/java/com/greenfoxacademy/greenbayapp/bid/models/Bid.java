package com.greenfoxacademy.greenbayapp.bid.models;

import com.greenfoxacademy.greenbayapp.product.models.Product;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bids")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bid {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private Integer bidPrice;

  @NotNull
  private LocalDateTime bidTime;

  @NotNull
  @ManyToOne
  private Product product;

  @NotNull
  @ManyToOne
  private UserEntity bidder;

}
