package com.example.cook.follow.repository;

import com.example.cook.follow.Follow;
import com.example.cook.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
  Optional<Follow> findByFollowerAndFollowing(User follower, User following);
}
