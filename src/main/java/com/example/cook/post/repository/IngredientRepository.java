package com.example.cook.post.repository;

import com.example.cook.post.Ingredient;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

 Optional<Ingredient> deleteByPostId(Long postId);

}
