package com.example.cook.bookMark.service;


import com.example.cook.bookMark.PostBookMark;
import com.example.cook.bookMark.repository.PostBookMarkRepository;
import com.example.cook.post.Post;
import com.example.cook.post.repository.PostRepository;
import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import java.security.Principal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostBookMarkService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final PostBookMarkRepository postBookMarkRepository;

  public void bookMarkPost(Long postId, Principal principal) {
    String email = principal.getName(); // 현재 사용자의 이메일 또는 사용자 식별자를 가져옵니다.

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("포스팅 글이 존재하지 않습니다"));

    // 이전에 좋아요 누른 적 있는지 확인
    Optional<PostBookMark> bookMark = postBookMarkRepository.findByPostAndUser(post, user);
    if (bookMark.isEmpty()) {  // 북마크 누른적 없음
      PostBookMark postBookMark = PostBookMark.of(post, user);
      postBookMarkRepository.save(postBookMark);
    } else { // 좋아요 누른 적 있음
      postBookMarkRepository.delete(bookMark.get()); // 북마크 눌렀던 정보를 지운다.
      postBookMarkRepository.flush();
    }
  }
}
