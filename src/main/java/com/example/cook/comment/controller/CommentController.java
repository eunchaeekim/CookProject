package com.example.cook.comment.controller;

import com.example.cook.comment.Comment;
import com.example.cook.comment.dto.CommentDto;
import com.example.cook.comment.service.CommentService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentController {

  private final CommentService commentService;

  @Autowired
  public CommentController(CommentService commentService) {
    this.commentService = commentService;
  }

  // 댓글 작성
  @PostMapping("/comments/{postId}")
  public ResponseEntity<String> createComment(@PathVariable Long postId, @RequestBody CommentDto commentDto, HttpServletRequest request) {
    commentService.createComment(postId, commentDto, request);
    return ResponseEntity.ok("comment created successfully!");
  }

  // 게시글에 달린 댓글 모두 불러오기
  @GetMapping("/comments/{postId}")
  public List<Comment> getAllComments(@PathVariable Long postId) {
    return commentService.findAllComments(postId);
  }

  // 댓글 삭제
  @DeleteMapping("/comments/{postId}/{commentId}")
  public ResponseEntity<String> deleteComment(@PathVariable Long postId, @PathVariable Long commentId, HttpServletRequest request) {
    commentService.deleteComment(postId, commentId, request);

    return ResponseEntity.ok("comment deleted successfully!");
  }

  // 댓글 수정
  @PutMapping("/comments/{postId}/{commentId}")
  public ResponseEntity<String> updatePost(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody CommentDto commentDto, HttpServletRequest request) {
    commentService.updateComment(postId, commentId, commentDto, request);
    return ResponseEntity.ok("comment updated successfully!");
  }


}

