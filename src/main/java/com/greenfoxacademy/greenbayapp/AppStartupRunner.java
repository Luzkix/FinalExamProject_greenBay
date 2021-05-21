package com.greenfoxacademy.greenbayapp;

import com.greenfoxacademy.greenbayapp.bid.services.BidService;
import com.greenfoxacademy.greenbayapp.product.models.Product;
import com.greenfoxacademy.greenbayapp.product.models.dtos.NewProductRequestDTO;
import com.greenfoxacademy.greenbayapp.product.services.ProductService;
import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import com.greenfoxacademy.greenbayapp.user.models.dtos.RegisterRequestDTO;
import com.greenfoxacademy.greenbayapp.user.repositories.UserRepository;
import com.greenfoxacademy.greenbayapp.user.services.UserService;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppStartupRunner implements ApplicationRunner {
  @Autowired
  UserService userService;
  @Autowired
  UserRepository userRepository;
  @Autowired
  ProductService productService;
  @Autowired
  BidService bidService;

  @Override
  public void run(ApplicationArguments args) throws Exception {

    log.info("Checking if database is empty");
    boolean databaseNotEmpty = userRepository.existsById(1L);
    if(!databaseNotEmpty) createDefaultObjectsIntoDtb();

  }

  private void createDefaultObjectsIntoDtb() {
    List<UserEntity> users = createDefaultUsers();
    List<Product> products = createDefaultProducts(users);
    //createDefaultBids(users, products);   - not working, some issues with lazy initiation of bid class
  }

  private List<UserEntity> createDefaultUsers() {
    log.info("Creating default users");

    RegisterRequestDTO zdenekDTO = new RegisterRequestDTO("zdenek","zdenek@test.cz","password");
    RegisterRequestDTO petrDTO = new RegisterRequestDTO("petr","petr@test.cz","password");
    UserEntity zdenek = userService.registerNewUser(zdenekDTO);
    UserEntity petr = userService.registerNewUser(petrDTO);

    userService.increaseDollars(zdenek,1000);
    userService.increaseDollars(petr, 1000);

    return Arrays.asList(zdenek, petr);
  }

  private List<Product> createDefaultProducts(List<UserEntity> users) {
    log.info("Creating default items");

    UserEntity zdenek = users.get(0);
    UserEntity petr = users.get(1);

    return Arrays.asList(
        productService.setUpNewProduct(new NewProductRequestDTO("car1","blueCar","http://localhost:8080",100,500), zdenek),
        productService.setUpNewProduct(new NewProductRequestDTO("car2","blueCar","http://localhost:8080",100,500), zdenek),
        productService.setUpNewProduct(new NewProductRequestDTO("car3","blueCar","http://localhost:8080",100,500), zdenek),
        productService.setUpNewProduct(new NewProductRequestDTO("car4","blueCar","http://localhost:8080",100,500), zdenek),
        productService.setUpNewProduct(new NewProductRequestDTO("car5","blueCar","http://localhost:8080",100,500), zdenek),
        productService.setUpNewProduct(new NewProductRequestDTO("car6","blueCar","http://localhost:8080",100,500), zdenek),
        productService.setUpNewProduct(new NewProductRequestDTO("car7","redCar","http://localhost:8080",50,250), petr),
        productService.setUpNewProduct(new NewProductRequestDTO("car8","redCar","http://localhost:8080",50,250), petr),
        productService.setUpNewProduct(new NewProductRequestDTO("car9","redCar","http://localhost:8080",50,250), petr),
        productService.setUpNewProduct(new NewProductRequestDTO("car10","redCar","http://localhost:8080",50,250), petr),
        productService.setUpNewProduct(new NewProductRequestDTO("car11","redCar","http://localhost:8080",50,250), petr)
    );
  }

  private void createDefaultBids(List<UserEntity> users, List<Product> products) {
    log.info("Creating default bids");

    UserEntity zdenek = users.get(0);
    UserEntity petr = users.get(1);

    //zdenek is bidding to petr's product with id 7 - lower price than buy price
    bidService.doBidding(7L,100,zdenek);
    //zdenek is bidding again to petr's product with id 7 - still lower price than buy price
    bidService.doBidding(7L,200,zdenek);
    //zdenek is bidding to petr's product with id 8 - higher price than buy price = product is sold to zdenek
    bidService.doBidding(8L,300,zdenek);

    //petr is bidding to zdenek's product with id 1 - lower price than buy price
    bidService.doBidding(1L,400,petr);
    //petr is bidding to zdenek's product with id 2 - higher price than buy price = product is sold to petr
    bidService.doBidding(2L,500,petr);
  }
}
