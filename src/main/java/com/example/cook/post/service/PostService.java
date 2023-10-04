package com.example.cook.post.service;

import com.example.cook.post.Post;
import com.example.cook.post.dto.PostDto;
import com.example.cook.post.repository.PostRepository;
import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  // 포스팅 등록
  public void createPost(PostDto postDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String email = userDetails.getUsername();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

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
  public void updatePost(Long postId, PostDto postDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String email = userDetails.getUsername();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("포스팅이 존재하지 않습니다."));

    // 게시판의 username과 로그인 유저의 username 비교
    if (!email.equals(post.getUser().getEmail())) {
      throw new RuntimeException("작성자만 수정할 수 있습니다.");
    }

    post.setCookTitle(postDto.getCookTitle());
    post.setCookThumbnailUrl(postDto.getCookThumbnailUrl());
    post.setCookName(postDto.getCookName());
    post.setCategory(postDto.getCategory());
    post.setCookAmount(postDto.getCookAmount());
    post.setCookTime(postDto.getCookTime());

    postRepository.save(post);
  }

  // 포스팅 삭제
  public void deletePost(Long postId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String email = userDetails.getUsername();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("포스팅이 존재하지 않습니다."));

    // 게시판의 username과 로그인 유저의 username 비교
    if (!email.equals(post.getUser().getEmail())) {
      throw new RuntimeException("작성자만 삭제할 수 있습니다.");
    }

    postRepository.delete(post);
  }

  // 모든 포스트 조회
  public Page<Post> findAllPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }
}
