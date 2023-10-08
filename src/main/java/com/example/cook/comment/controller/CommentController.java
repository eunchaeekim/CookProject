package com.example.cook.comment.controller;

import com.example.cook.comment.dto.CommentDto;
import com.example.cook.comment.service.CommentService;
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
@RequestMapping("/comments/{postId}")
public class CommentController {

  private final CommentService commentService;

  // 댓글 작성
  @PostMapping
  public ResponseEntity<String> createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto, Principal principal) {
    commentService.createComment(postId, commentDto, principal);
    return ResponseEntity.ok("comment created successfully!");
  }

  // 게시글에 달린 댓글 모두 불러오기
  @GetMapping
  public ResponseEntity<?> getAllComments(@PathVariable Long postId, Pageable pageable) {
    Page<CommentDto> comments = commentService.findAllComments(postId, pageable);
    return ResponseEntity.ok(comments);
  }

  // 댓글 삭제
  @DeleteMapping("/{commentId}")
  public ResponseEntity<String> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, Principal principal) {
    commentService.deleteComment(postId, commentId, principal);
    return ResponseEntity.ok("comment deleted successfully!");
  }

  // 댓글 수정
  @PutMapping("/{commentId}")
  public ResponseEntity<String> updateComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentDto commentDto, Principal principal) {
    commentService.updateComment(postId, commentId, commentDto, principal);
    return ResponseEntity.ok("comment updated successfully!");
  }
}