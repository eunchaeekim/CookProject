package com.example.cook.postRecommend.service;

import com.example.cook.post.Post;
import com.example.cook.post.repository.PostRepository;
import com.example.cook.postRecommend.PostRecommend;
import com.example.cook.postRecommend.repository.PostRecommendRepository;
import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostRecommendService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostRecommendRepository postRecommendRepository;

  public void recommendPost(Long postId) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

    String email = userDetails.getUsername();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    Optional<Post> post = postRepository.findById(postId);

    if (post.isEmpty()) {
      throw new RuntimeException("포스팅 글이 존재하지 않습니다");
    }

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
