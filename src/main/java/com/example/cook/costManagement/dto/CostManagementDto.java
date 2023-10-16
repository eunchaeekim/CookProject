package com.example.cook.costManagement.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostManagementDto {

  private Long id;

  private String managementIngredient; // 식재료
  private Long managementIngredientCost; // 식재료 가격
  private LocalDate managementIngredientBuyDate; // 식재료 구입 날짜

}
