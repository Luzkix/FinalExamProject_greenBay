package com.greenfoxacademy.greenbayapp.globalexceptionhandling;

public class InvalidInputException extends RuntimeException {

  public InvalidInputException(String message) {
    super(message);
  }
}