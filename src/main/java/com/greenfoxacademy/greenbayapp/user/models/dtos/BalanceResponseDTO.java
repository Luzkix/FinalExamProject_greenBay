package com.greenfoxacademy.greenbayapp.user.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BalanceResponseDTO {
  private Long userId;
  private String username;
  private Integer balance;
}
