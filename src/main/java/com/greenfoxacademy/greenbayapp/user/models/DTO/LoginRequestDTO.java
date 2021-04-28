package com.greenfoxacademy.greenbayapp.user.models.DTO;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {
  @NotBlank(message = "Username can´t be empty!")
  private String username;

  @NotBlank(message = "Password can´t be empty!")
  private String password;
}
