package com.example.cook.costManagement;

import com.example.cook.user.User;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CostManagement {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cost_management_id")
  private Long id;

  private String managementIngredient; // 식재료
  private Long managementIngredientCost; // 식재료 가격

  private LocalDate managementIngredientBuyDate; // 식재료 구입 날짜

  @ManyToOne
  @JoinColumn(name = "user_Id")
  private User user;


}
