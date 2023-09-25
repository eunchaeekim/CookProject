package com.example.cook.global.oauth2;

/**
 * 이후에 OAuth2UserService에서 사용할 OAuth2User 객체를 커스텀한 클래스입니다. (OAuth2User 객체에서 필드 추가를 위해)
 *
 * DefaultOAuth2User를 상속받고, email과 role 필드를 추가로 가집니다.
 * email과 role 필드를 따로 커스텀하여 추가하는 이유는 다음과 같습니다.
 *
 * ※ email : OAuth 로그인 시 처음 로그인일 경우, 내 서비스에서 Resource Server가 제공하지 않는 정보가 필요할 경우에,
 * Resource Server가 아닌 내 서비스에서 해당 정보를 사용자에게 입력 받아야합니다. (ex - 사는 도시, 나이 등)
 * 이때, 어떤 유저가 OAuth 로그인한지 내 서비스의 서버 입장에서는 알 수가 없으므로,
 * OAuth 로그인 시 임의의 Email을 생성하여 AccessToken을 발급받아서 회원 식별용으로 AccessToken을 사용합니다.
 * (이후에 OAuth2LoginSuccessHandler에서 해당 이메일로 Token 발급 & 처리)
 *
 * ※ role : OAuth 로그인 시 위의 추가 정보(사는 도시, 나이 등)을 입력했는지 (처음 OAuth 로그인인지)를 판단하기 위해 필요합니다.
 * 처음 로그인하는 유저를 Role.GUEST로 설정하고, 이후에 추가 정보를 입력해서 회원가입을 진행하면, Role.USER로 업데이트하는 식으로 설정했습니다.
 * 이렇게 하면, OAuth 로그인 회원 중 Role.GUEST인 회원은 처음 로그인이므로 SuccessHandler에서 추가 정보(사는 도시, 나이 등)를 입력하는 URL로 리다이렉트합니다.
 * (이후에 OAuth2LoginSuccessHandler에서 해당 이메일로 Token 발급 & 처리)
 *
 * 결국, CustomOAuth2User를 구현하는 이유는, Resource Server에서 제공하지 않는 추가 정보들을 내 서비스에서 가지고 있기 위함입니다.
 * 따라서, Resourc Server에서 제공하는 정보만 사용해도 된다면 굳이 CustomOAuth2User를 구현하지 않고, 일반 DefalutOAuth2User를 사용하면 됩니다.
 */

import com.example.cook.user.Role;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

  private String email;
  private Role role;

  /**
   * Constructs a {@code DefaultOAuth2User} using the provided parameters.
   *
   * @param authorities      the authorities granted to the user
   * @param attributes       the attributes about the user
   * @param nameAttributeKey the key used to access the user's "name" from
   *                         {@link * getAttributes()}
   */
  public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
      Map<String, Object> attributes, String nameAttributeKey,
      String email, Role role) {
    super(authorities, attributes, nameAttributeKey);
    this.email = email;
    this.role = role;
  }
}