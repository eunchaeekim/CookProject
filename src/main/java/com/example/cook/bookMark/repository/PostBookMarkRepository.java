package com.example.cook.bookMark.repository;

import com.example.cook.bookMark.PostBookMark;
import com.example.cook.post.Post;
import com.example.cook.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostBookMarkRepository extends JpaRepository<PostBookMark, Long> {

  Optional<PostBookMark> findByPostAndUser(Post post, User user);


}
