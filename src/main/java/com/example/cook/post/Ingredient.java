package com.example.cook.post;

import com.example.cook.post.dto.IngredientDto;
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
public class Ingredient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Ingredient_id")
  private Long id;

  private String cookIngredient; // 식재료 이름
  private String cookIngredientAmount; // 식재료 양

  @ManyToOne
  @JoinColumn(name = "post_Id")
  private Post post;

  public static Ingredient createIngredientFromDto(IngredientDto ingredientDto, Post post) {
    Ingredient ingredient = new Ingredient();
    ingredient.setCookIngredient(ingredientDto.getCookIngredient());
    ingredient.setCookIngredientAmount(ingredientDto.getCookIngredientAmount());
    ingredient.setPost(post);
    return ingredient;
  }

  public static IngredientDto createIngredientDtoFromEntity(Ingredient ingredient, Post post) {
    IngredientDto ingredientDto = new IngredientDto();
    ingredientDto.setId(ingredient.getId());
    ingredientDto.setCookIngredient(ingredient.getCookIngredient());
    ingredientDto.setCookIngredientAmount(ingredient.getCookIngredientAmount());
    return ingredientDto;
  }


}
