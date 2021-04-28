package com.greenfoxacademy.springwebapp.exceptionhandling.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorDTO {
  private String status;
  private String message;

  public ErrorDTO(String message) {
    this.message = message;
    this.status = "error";
  }
}
