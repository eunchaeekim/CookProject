package com.example.cook.post.controller;

import com.example.cook.post.Post;
import com.example.cook.post.dto.PostDto;
import com.example.cook.post.service.PostService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;

  @Autowired
  public PostController(PostService postService) {
    this.postService = postService;
  }

  // 글 등록
  @PostMapping
  public ResponseEntity<String> createPost(@RequestBody PostDto postDto, HttpServletRequest request) {
    postService.createPost(postDto, request);
    return ResponseEntity.ok("Post created successfully!");
  }

  // 글 수정
  @PutMapping("/{postId}")
  public ResponseEntity<String> updatePost(@PathVariable Long postId, @RequestBody PostDto postDto,  HttpServletRequest request) {
    postService.updatePost(postId, postDto, request);
    return ResponseEntity.ok("Post updated successfully!");
  }

  // 글 삭제
  @DeleteMapping("/{postId}")
  public ResponseEntity<String> deletePost(@PathVariable Long postId, HttpServletRequest request) {
    postService.deletePost(postId, request);
    return ResponseEntity.ok("Post deleted successfully!");
  }

  // 전체 목록 조회
  @GetMapping
  public List<Post> getAllPosts() {
    return postService.findAllPosts();
  }
}



