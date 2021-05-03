package com.greenfoxacademy.greenbayapp;

import java.time.LocalDateTime;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //(exclude = { SecurityAutoConfiguration.class })
public class GreenBayApplication {

  public static void main(String[] args) {
    SpringApplication.run(GreenBayApplication.class, args);
  }

}
