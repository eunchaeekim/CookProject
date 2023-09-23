package com.example.cook.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
  /*
  자체 로그인 시에는 상관없이 회원가입 시 USER로 Role을 정하여 DB에 저장.
  OAuth2 로그인 시에는 첫 로그인 시에 Role을 Guest로 설정하고, 추가 정보 입력 시 User로 업데이트되게 구현
  */

  /*
  key 필드를 추가하여 "ROLE_"을 붙인 이유는, 스프링 시큐리티에서는 권한(Role) 코드에
  항상 "ROLE_" 접두사가 앞에 붙어야하기 때문에 "ROLE_"을 설정
  */

  GUEST("ROLE_GUEST"), USER("ROLE_USER");

  private final String key;
}