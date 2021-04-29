package com.greenfoxacademy.greenbayapp.product.models;

import com.greenfoxacademy.greenbayapp.bid.models.Bid;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

  private String name;
  private String description;
  private String url;
  private Integer startingPrice;
  private Integer purchasePrice;
  private Boolean sold;

  @ManyToOne
  private UserEntity seller;
  @ManyToOne
  private UserEntity buyer;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
  List<Bid> bids;
}
