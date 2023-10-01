package com.example.cook.comment.service;

import com.example.cook.comment.Comment;
import com.example.cook.comment.dto.CommentDto;
import com.example.cook.comment.repository.CommentRepository;
import com.example.cook.global.jwt.service.JwtService;
import com.example.cook.post.Post;
import com.example.cook.post.dto.PostDto;
import com.example.cook.post.repository.PostRepository;
import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final JwtService jwtService;
  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  // 댓글 작성
  public void createComment(Long postId, CommentDto commentDto, HttpServletRequest request) {
    Optional<String> token = jwtService.extractAccessToken(request);
    User user = token
        .filter(jwtService::isTokenValid) // 토큰 유효성을 검사합니다.
        .flatMap(jwtService::extractEmail) // 이메일을 추출하고 Optional로 감싸줍니다.
        .filter(Objects::nonNull) // null 값이 아닌 경우를 필터링합니다.
        .flatMap(email -> userRepository.findByEmail(email)) // 이메일을 사용하여 사용자를 검색합니다.
        .orElse(null); // 사용자가 없는 경우 null을 할당합니다.

    if (user == null) {
      throw new RuntimeException("사용자를 찾을 수 없습니다.");
    }

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("포스팅을 찾을 수 없습니다."));

    Comment comment = new Comment();
    comment.setContent(commentDto.getContent());
    comment.setUser(user);
    comment.setPost(post);

    commentRepository.save(comment);

  }


  // 게시글에 달린 댓글 모두 불러오기
  public List<Comment> findAllComments(Long postId) {
    List<Comment> comments = commentRepository.findAllByPostId(postId);
    return comments;
  }

  // 댓글 삭제
  public void deleteComment(Long postId, Long commentId, HttpServletRequest request) {
    Optional<String> token = jwtService.extractAccessToken(request);
    String email = token
        .filter(jwtService::isTokenValid) // 토큰 유효성을 검사합니다.
        .flatMap(jwtService::extractEmail) // 이메일을 추출
        .orElse(null); // 사용자가 없는 경우 null을 할당합니다.

    if (email == null) {
      throw new RuntimeException("사용자를 찾을 수 없습니다.");
    }

    Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
      throw new RuntimeException("댓글을 찾을 수 없습니다. ");
    });
    // 게시판의 username과 로그인 유저의 username 비교
    if (email.equals(comment.getUser().getEmail())) {
      commentRepository.delete(comment);
    } else {
      throw new RuntimeException("작성자만 삭제할 수 있습니다.");
    }

  }

  // 댓글 수정
  public void updateComment(Long postId, Long commentId, CommentDto commentDto, HttpServletRequest request) {
    Optional<String> token = jwtService.extractAccessToken(request);
    String email = token
        .filter(jwtService::isTokenValid) // 토큰 유효성을 검사합니다.
        .flatMap(jwtService::extractEmail) // 이메일을 추출
        .orElse(null); // 사용자가 없는 경우 null을 할당합니다.

    if (email == null) {
      throw new RuntimeException("사용자를 찾을 수 없습니다.");
    }

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

    // 댓글의 username과 로그인 유저의 username 비교
    if (email.equals(comment.getUser().getEmail())) {
      comment.setContent(commentDto.getContent());

      commentRepository.save(comment);
    } else {
      throw new RuntimeException("작성자만 수정할 수 있습니다.");
    }
  }

}

