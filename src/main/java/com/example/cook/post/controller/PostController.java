package com.example.cook.post.controller;

import com.example.cook.post.dto.PostDto;
import com.example.cook.post.service.PostService;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  // 글 등록
  @PostMapping
  public ResponseEntity<String> createPost(@RequestBody PostDto postDto, Principal principal) {
    postService.createPost(postDto, principal);
    return ResponseEntity.ok("Post created successfully!");
  }

  // 글 수정
  @PutMapping("/{postId}")
  public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody PostDto postDto, Principal principal) {
    postService.updatePost(postId, postDto, principal);
    return ResponseEntity.ok("Post updated successfully!");
  }

  // 글 삭제
  @DeleteMapping("/{postId}")
  public ResponseEntity<String> deletePost(@PathVariable Long postId, Principal principal) {
    postService.deletePost(postId, principal);
    return ResponseEntity.ok("Post deleted successfully!");
  }

  // 전체 목록 조회
  @GetMapping
  public ResponseEntity<?> getAllPosts(Pageable pageable) {
    Page<PostDto> posts = postService.findAllPosts(pageable);
    return ResponseEntity.ok(posts);
  }
}
