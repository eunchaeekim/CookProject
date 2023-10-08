package com.example.cook.postRecommend.service;

import com.example.cook.post.Post;
import com.example.cook.post.repository.PostRepository;
import com.example.cook.postRecommend.PostRecommend;
import com.example.cook.postRecommend.repository.PostRecommendRepository;
import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import java.security.Principal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostRecommendService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostRecommendRepository postRecommendRepository;

  public void recommendPost(Long postId, Principal principal) {
    String email = principal.getName();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    Optional<Post> post = postRepository.findById(postId);

    if (post.isEmpty()) {
      throw new RuntimeException("포스팅 글이 존재하지 않습니다");
    }

    // 이전에 좋아요 누른 적 있는지 확인 하여 상태 변경
    postRecommendRepository.findByPostAndUser(post.get(), user)
        .ifPresentOrElse(
            existingRecommend -> postRecommendRepository.delete(existingRecommend),
            () -> postRecommendRepository.save(PostRecommend.of(post.get(), user))
        );
  }
}
