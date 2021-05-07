package com.greenfoxacademy.greenbayapp.user.models.dtos;

import com.greenfoxacademy.greenbayapp.user.models.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class BalanceResponseDTO {
  private Long userId;
  private String username;
  private Integer balance;
}
