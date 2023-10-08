package com.example.cook.bookMark.controller;

import com.example.cook.bookMark.service.PostBookMarkService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/bookMark/{postId}")
public class PostBookMarkController {

  private final PostBookMarkService postBookMarkService;

  @PostMapping
  public ResponseEntity<String> create(@PathVariable Long postId, Principal principal) {
    postBookMarkService.bookMarkPost(postId,principal);
    return ResponseEntity.ok("BookMark created successfully!");
  }

}
