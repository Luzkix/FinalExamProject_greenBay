package com.greenfoxacademy.greenbayapp.globalexceptionhandling;

public class AuthorizationException extends RuntimeException {

  public AuthorizationException(String message) {
    super(message);
  }
}
