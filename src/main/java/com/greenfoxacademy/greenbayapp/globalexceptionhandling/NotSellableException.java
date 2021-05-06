package com.greenfoxacademy.greenbayapp.globalexceptionhandling;

public class NotSellableException extends RuntimeException {

  public NotSellableException() {
    super("The item was already sold!");
  }
}