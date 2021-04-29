package com.greenfoxacademy.greenbayapp.product.models;

import com.greenfoxacademy.greenbayapp.bid.models.Bid;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @NotNull
  private String name;
  @NotNull
  private String description;
  @NotNull
  private String photoUrl;
  @NotNull
  private Integer startingPrice;
  @NotNull
  private Integer purchasePrice;

  private Boolean sold;
  private LocalDateTime enlistingTime;
  private LocalDateTime soldTime;

  @ManyToOne
  private UserEntity seller;
  @ManyToOne
  private UserEntity buyer;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  private List<Bid> bids;
}
