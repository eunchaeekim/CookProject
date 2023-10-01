package com.example.cook.postRecommend.service;

import com.example.cook.global.jwt.service.JwtService;
import com.example.cook.post.Post;
import com.example.cook.post.repository.PostRepository;
import com.example.cook.postRecommend.PostRecommend;
import com.example.cook.postRecommend.repository.PostRecommendRepository;
import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostRecommendService {

  private final JwtService jwtService;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostRecommendRepository postRecommendRepository;

  public void recommendPost(Long postId, HttpServletRequest request) {

    Optional<Post> post = postRepository.findById(postId);

    if (post.isEmpty()) {
      throw new RuntimeException("포스팅 글이 존재하지 않습니다");
    }

    Optional<String> token = jwtService.extractAccessToken(request);

    User user = token
        .filter(jwtService::isTokenValid) // 토큰 유효성을 검사합니다.
        .flatMap(jwtService::extractEmail) // 이메일을 추출하고 Optional로 감싸줍니다.
        .filter(Objects::nonNull) // null 값이 아닌 경우를 필터링합니다.
        .flatMap(email -> userRepository.findByEmail(email)) // 이메일을 사용하여 사용자를 검색합니다.
        .orElse(null); // 사용자가 없는 경우 null을 할당합니다.

    // 이전에 좋아요 누른 적 있는지 확인
    Optional<PostRecommend> recommend = postRecommendRepository.findByPostAndUser(post.get(), user);
    if (recommend.isEmpty()) {  // 좋아요 누른적 없음
      PostRecommend postRecommend = PostRecommend.of(post.get(), user);
      postRecommendRepository.save(postRecommend);
    } else { // 좋아요 누른 적 있음
      postRecommendRepository.delete(recommend.get()); // 좋아요 눌렀던 정보를 지운다.
      postRecommendRepository.flush();
    }
  }
}
