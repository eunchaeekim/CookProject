package com.example.cook.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientDto {
  private Long id;

  private String cookIngredient; // 식재료 이름
  private String cookIngredientAmount; // 식재료 양

}
