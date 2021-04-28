package com.greenfoxacademy.greenbayapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //(exclude = { SecurityAutoConfiguration.class })
public class GreenBayApplication {

  public static void main(String[] args) {
    SpringApplication.run(GreenBayApplication.class, args);
  }

}
