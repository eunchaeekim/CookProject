package com.example.cook.post.controller;

import com.example.cook.post.Post;
import com.example.cook.post.dto.PostDto;
import com.example.cook.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  // 글 등록
  @PostMapping
  public ResponseEntity<String> createPost(@RequestBody PostDto postDto) {
    postService.createPost(postDto);
    return ResponseEntity.ok("Post created successfully!");
  }

  // 글 수정
  @PutMapping("/{postId}")
  public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody PostDto postDto) {
    postService.updatePost(postId, postDto);
    return ResponseEntity.ok("Post updated successfully!");
  }

  // 글 삭제
  @DeleteMapping("/{postId}")
  public ResponseEntity<String> deletePost(@PathVariable Long postId) {
    postService.deletePost(postId);
    return ResponseEntity.ok("Post deleted successfully!");
  }

  // 전체 목록 조회
  @GetMapping
  public ResponseEntity<?> getAllPosts(Pageable pageable) {
    Page<Post> posts = postService.findAllPosts(pageable);
    return ResponseEntity.ok(posts);
  }
}
