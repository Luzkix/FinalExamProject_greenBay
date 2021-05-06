package com.greenfoxacademy.greenbayapp.globalexceptionhandling;

public class NotEnoughDollarsException extends RuntimeException {

  public NotEnoughDollarsException() {
    super("Not enought greenBay dollars!");
  }
}