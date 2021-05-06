package com.greenfoxacademy.greenbayapp.globalexceptionhandling;

public class NotFoundException extends RuntimeException {

  public NotFoundException() {
    super("The item was not found!");
  }
}