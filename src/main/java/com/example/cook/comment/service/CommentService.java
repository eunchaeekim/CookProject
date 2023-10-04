package com.example.cook.comment.service;

import com.example.cook.comment.Comment;
import com.example.cook.comment.dto.CommentDto;
import com.example.cook.comment.repository.CommentRepository;
import com.example.cook.post.Post;
import com.example.cook.post.repository.PostRepository;
import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  // 댓글 작성
  public void createComment(Long postId, CommentDto commentDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String email = userDetails.getUsername();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("포스팅을 찾을 수 없습니다."));

    Comment comment = new Comment();
    comment.setContent(commentDto.getContent());
    comment.setUser(user);
    comment.setPost(post);

    commentRepository.save(comment);

  }


  // 게시글에 달린 댓글 모두 불러오기
  public Page<Comment> findAllComments(Long postId, Pageable pageable) {
    return commentRepository.findAllByPostId(postId, pageable);
  }

  // 댓글 삭제
  public void deleteComment(Long postId, Long commentId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String email = userDetails.getUsername();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("포스팅을 찾을 수 없습니다."));

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

    // 게시판의 username과 로그인 유저의 username 비교
    if (!email.equals(comment.getUser().getEmail())) {
      throw new RuntimeException("작성자만 삭제할 수 있습니다.");
    } else {
      commentRepository.delete(comment);
    }

  }

  // 댓글 수정
  public void updateComment(Long postId, Long commentId, CommentDto commentDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String email = userDetails.getUsername();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("포스팅을 찾을 수 없습니다."));

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));

    // 댓글의 username과 로그인 유저의 username 비교
    if (!email.equals(comment.getUser().getEmail())) {
      throw new RuntimeException("작성자만 수정할 수 있습니다.");
    } else {
      comment.setContent(commentDto.getContent());
      commentRepository.save(comment);
    }
  }

}

