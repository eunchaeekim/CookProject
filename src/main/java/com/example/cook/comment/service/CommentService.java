package com.example.cook.comment.service;

import com.example.cook.comment.Comment;
import com.example.cook.comment.dto.CommentDto;
import com.example.cook.comment.repository.CommentRepository;
import com.example.cook.exception.impl.NotExistCommentException;
import com.example.cook.exception.impl.NotExistPostException;
import com.example.cook.exception.impl.NotExistUserException;
import com.example.cook.exception.impl.OnlyWirterException;
import com.example.cook.post.Post;
import com.example.cook.post.repository.PostRepository;
import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  // 댓글 작성
  public void createComment(Long postId, CommentDto commentDto, Principal principal) {
    String email = principal.getName();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(NotExistUserException::new);

    Post post = postRepository.findById(postId)
        .orElseThrow(NotExistPostException::new);

    Comment comment = new Comment();
    comment.setContent(commentDto.getContent());
    comment.setUser(user);
    comment.setPost(post);

    commentRepository.save(comment);
  }

  // 게시글에 달린 댓글 모두 불러오기
  public Page<CommentDto> findAllComments(Long postId, Pageable pageable) {
    Page<Comment> comments = commentRepository.findAllByPostId(postId, pageable);
    return comments.map(comment -> {
      CommentDto commentDto = new CommentDto();
      commentDto.setId(comment.getId());
      commentDto.setContent(comment.getContent());
      return commentDto;
    });
  }


  // 댓글 삭제
  public void deleteComment(Long postId, Long commentId, Principal principal) {
    String email = principal.getName();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(NotExistUserException::new);

    Post post = postRepository.findById(postId)
        .orElseThrow(NotExistPostException::new);

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(NotExistCommentException::new);

    // 게시판의 username과 로그인 유저의 username 비교
    if (!email.equals(comment.getUser().getEmail())) {
      throw new OnlyWirterException();
    }

    commentRepository.delete(comment);

  }

  // 댓글 수정
  public void updateComment(Long postId, Long commentId, CommentDto commentDto, Principal
      principal) {
    String email = principal.getName();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(NotExistUserException::new);

    Post post = postRepository.findById(postId)
        .orElseThrow(NotExistPostException::new);

    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(NotExistCommentException::new);

    // 댓글의 username과 로그인 유저의 username 비교
    if (!email.equals(comment.getUser().getEmail())) {
      throw new OnlyWirterException();
    } else {
      comment.setContent(commentDto.getContent());
      commentRepository.save(comment);
    }
  }
}