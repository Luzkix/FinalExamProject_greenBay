package com.greenfoxacademy.greenbayapp.user.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.greenfoxacademy.greenbayapp.bid.models.Bid;
import com.greenfoxacademy.greenbayapp.product.models.Product;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String username;

  @NotNull
  private String email;

  @NotNull
  @JsonIgnore
  private String password;

  private Integer balance = 0;

  @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
  @LazyCollection(LazyCollectionOption.FALSE)
  private Set<Product> productsSelling;

  @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL)
  @LazyCollection(LazyCollectionOption.FALSE)
  private Set<Product> productsBought;

  @OneToMany(mappedBy = "bidder", cascade = CascadeType.ALL)
  @LazyCollection(LazyCollectionOption.FALSE)
  private Set<Bid> bids;

  public UserEntity(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }
}
