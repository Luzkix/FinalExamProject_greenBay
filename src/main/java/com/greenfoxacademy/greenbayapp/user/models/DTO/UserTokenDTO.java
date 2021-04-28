package com.greenfoxacademy.greenbayapp.user.models.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserTokenDTO {
  private String status;
  private String token;

  public UserTokenDTO(String token) {
    this.status = "ok";
    this.token = token;
  }
}
