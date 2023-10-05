package com.example.cook.follow.controller;

import com.example.cook.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/followUser/{userId}")
public class FollowController {

  private final FollowService followService;

  @PostMapping
  public ResponseEntity<String> followUser(@PathVariable Long userId) {
    followService.follow(userId);
    return ResponseEntity.ok("Follow created successfully!");
  }
}
