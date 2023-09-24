package com.example.cook.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpDto {

  private String email;
  private String password;
  private int age;
  private String city;
}