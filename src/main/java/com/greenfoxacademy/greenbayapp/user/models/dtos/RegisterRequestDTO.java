package com.greenfoxacademy.greenbayapp.user.models.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {
  @NotBlank(message = "Username can´t be empty!")
  @Size(max = 100, message = "Username is too long!")
  private String username;

  @NotBlank(message = "Email can´t be empty!")
  @Email(message = "Wrong format of email addess!")
  @Size(max = 100, message = "Email address is too long!")
  private String email;

  @NotBlank(message = "Password can´t be empty!")
  @Size(min = 4, max = 12, message = "Password must consist between 8-12 characters!")
  private String password;
}
