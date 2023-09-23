package com.example.global.oauth2.userinfo;

import java.util.Map;
/**
 * 소셜 타입별로 유저 정보를 가지는 추상클래스입니다.
 * OAuth2UserInfo 추상클래스를 상속받아 각 소셜 타입의 유저 정보 클래스를 구현
 */

public abstract class OAuth2UserInfo {

  // 추상클래스를 상속받는 클래스에서만 사용할 수 있도록 protected 제어자를 사용
  protected Map<String, Object> attributes;

  // 생성자 파라미터로 각 소셜 타입별 유저 정보 attributes를 주입받아서
  //각 소셜 타입별 유저 정보 클래스가 소셜 타입에 맞는 attributes를 주입받아 가지도록 함
  public OAuth2UserInfo(Map<String, Object> attributes) {
    this.attributes = attributes;
  }

  // 서비스에 사용하고 싶은 유저 정보들을 가져오는 메소드를 생성
  // 각 소셜에서 제공하는 정보 중에 사용하고 싶은 정보가 있다면 더 추가해서 사용
  public abstract String getId(); //소셜 식별 값 : 구글 - "sub", 카카오 - "id", 네이버 - "id"

  public abstract String getNickname();

}