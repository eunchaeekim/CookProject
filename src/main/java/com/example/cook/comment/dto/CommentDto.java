package com.example.cook.comment.dto;

import com.example.cook.comment.Comment;
import com.example.cook.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
  private Long id;
  private String content;
  private User user;

}
