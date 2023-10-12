package com.example.cook.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CookMethodDto {
  private Long Id;

  private String cookMethodDescription;
  private String cookMethodPhotoUrl;
}
