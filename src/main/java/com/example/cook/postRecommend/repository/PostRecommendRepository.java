package com.example.cook.postRecommend.repository;

import com.example.cook.post.Post;
import com.example.cook.postRecommend.PostRecommend;
import com.example.cook.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRecommendRepository extends JpaRepository<PostRecommend, Long> {

  Optional<PostRecommend> findByPostAndUser(Post post, User user);


}
