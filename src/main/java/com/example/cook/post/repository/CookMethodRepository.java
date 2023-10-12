package com.example.cook.post.repository;

import com.example.cook.post.CookMethod;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CookMethodRepository extends JpaRepository<CookMethod, Long> {
  Optional<CookMethod> deleteByPostId(Long postId);

}
