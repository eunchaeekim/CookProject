package com.example.cook.postRecommend.controller;

import com.example.cook.postRecommend.service.PostRecommendService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts/recommend/{postId}")
public class PostRecommendController {

  private final PostRecommendService postRecommendService;

  @Autowired
  public PostRecommendController(PostRecommendService postRecommendService) {
    this.postRecommendService = postRecommendService;
  }

  @PostMapping
  public ResponseEntity<String> create(@PathVariable Long postId, HttpServletRequest request) {
    postRecommendService.recommendPost(postId, request);
    return ResponseEntity.ok("Recommend created successfully!");
  }

}

