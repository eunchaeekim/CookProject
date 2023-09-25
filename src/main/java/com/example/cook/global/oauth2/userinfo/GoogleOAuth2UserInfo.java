package com.example.cook.global.oauth2.userinfo;

import java.util.Map;

/**
 * 구글은 앞의 네이버, 카카오와 달리 유저 정보가 감싸져 있지 않기 때문에
 * 바로 get으로 유저 정보 Key를 사용해서 꺼내면 됩니다.
 */

/**
 * 구글의 유저 정보 Response JSON 예시
 * {
 *    "sub": "식별값",
 *    "name": "name",
 *    "given_name": "given_name",
 *    "picture": "https//lh3.googleusercontent.com/~~",
 *    "email": "email",
 *    "email_verified": true,
 *    "locale": "ko"
 * }
 */
public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

  public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
    super(attributes);
  }

  @Override
  public String getId() {
    return (String) attributes.get("sub");
  }


}
