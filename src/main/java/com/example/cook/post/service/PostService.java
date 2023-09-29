package com.example.cook.post.service;

import com.example.cook.global.jwt.service.JwtService;
import com.example.cook.post.Post;
import com.example.cook.post.dto.PostDto;
import com.example.cook.post.repository.PostRepository;
import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final JwtService jwtService;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  // 포스팅 등록
  public void createPost(PostDto postDto, HttpServletRequest request) {
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

    Post post = new Post();
    post.setUser(user);
    post.setCookTitle(postDto.getCookTitle());
    post.setCookThumbnailUrl(postDto.getCookThumbnailUrl());
    post.setCookName(postDto.getCookName());
    post.setCategory(postDto.getCategory());
    post.setCookAmount(postDto.getCookAmount());
    post.setCookTime(postDto.getCookTime());

    postRepository.save(post);
  }

  // 포스팅 수정
  public void updatePost(Long postId, PostDto postDto, HttpServletRequest request) {
    Optional<String> token = jwtService.extractAccessToken(request);
    String email = token
        .filter(jwtService::isTokenValid) // 토큰 유효성을 검사합니다.
        .flatMap(jwtService::extractEmail) // 이메일을 추출
        .orElse(null); // 사용자가 없는 경우 null을 할당합니다.

    if (email == null) {
      throw new RuntimeException("사용자를 찾을 수 없습니다.");
    }

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("포스팅이 존재하지 않습니다."));

    // 게시판의 username과 로그인 유저의 username 비교
    if (email.equals(post.getUser().getEmail())) {

      post.setCookTitle(postDto.getCookTitle());
      post.setCookThumbnailUrl(postDto.getCookThumbnailUrl());
      post.setCookName(postDto.getCookName());
      post.setCategory(postDto.getCategory());
      post.setCookAmount(postDto.getCookAmount());
      post.setCookTime(postDto.getCookTime());

      postRepository.save(post);
    } else {
      throw new RuntimeException("작성자만 수정할 수 있습니다.");
    }
  }

  // 포스팅 삭제
  public void deletePost(Long postId, HttpServletRequest request) {
    Optional<String> token = jwtService.extractAccessToken(request);
    String email = token
        .filter(jwtService::isTokenValid) // 토큰 유효성을 검사합니다.
        .flatMap(jwtService::extractEmail) // 이메일을 추출
        .orElse(null); // 사용자가 없는 경우 null을 할당합니다.

    if (email == null) {
      throw new RuntimeException("사용자를 찾을 수 없습니다.");
    }

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("포스팅이 존재하지 않습니다."));

    // 게시판의 username과 로그인 유저의 username 비교
    if (email.equals(post.getUser().getEmail())) {
      postRepository.delete(post);
    } else {
      throw new RuntimeException("작성자만 삭제할 수 있습니다.");
    }
  }

  // 모든 포스트 조회
  public List<Post> findAllPosts() {
    List<Post> post = postRepository.findAll();
    if (post.isEmpty()) {
      throw new RuntimeException("포스팅이 존재하지 않습니다.");
    }
    return post;
  }
}
