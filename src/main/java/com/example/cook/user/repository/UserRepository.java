package com.example.cook.user.repository;

import com.example.cook.user.SocialType;
import com.example.cook.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByNickname(String nickname);

  Optional<User> findByRefreshToken(String refreshToken);

  /**
   OAuth2로그인 구현 시 사용하는 메소드.
   OAuth2로그인 시 정보제공을 동의 하여 로그인을 진행하면,
   추가정보(ag,city)를 제외한 정보를 받아 DB에 저장 된다
   따라서 이후에 추가 정보를 입력하기 위해 해당 회원을 DB에서 찾는 메소드가 필요하다
   그때,DB에 소셜타입(NAVER,KAKAO,GOOGLE,...)과 소셜 식별값이 저장 되기 떄문에
   이 두 가지로 추가정보를 입력하지 않은 회원을 가져오는 메소드를 추가
   */
  Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

}

