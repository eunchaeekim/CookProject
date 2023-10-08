package com.example.cook.postRecommend.controller;

import com.example.cook.postRecommend.service.PostRecommendService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/recommend/{postId}")
public class PostRecommendController {

  private final PostRecommendService postRecommendService;

  @PostMapping
  public ResponseEntity<String> create(@PathVariable Long postId, Principal principal) {
    postRecommendService.recommendPost(postId, principal);
    return ResponseEntity.ok("Recommend created successfully!");
  }
}

