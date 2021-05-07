package com.greenfoxacademy.greenbayapp.globalexceptionhandling;

public class LowBidException extends RuntimeException {

  public LowBidException() {
    super("Bid price is too low!");
  }
}