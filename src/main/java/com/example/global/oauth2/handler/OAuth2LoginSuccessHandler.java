package com.example.global.oauth2.handler;

import com.example.cook.user.Role;
import com.example.cook.user.repository.UserRepository;
import com.example.global.jwt.service.JwtService;
import com.example.global.oauth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * OAuth2 로그인이 성공한다면, OAuth2LoginSuccessHandler의 로직이 실행
 */

/**
 * 1. 처음 OAuth2 로그인한 유저일 때
 * authentication.getPrincipal()로 받아온 CustomOAuth2User의 getRole이 GUEST라면,
 * AccessToken을 발급하고 요청 헤더에 실어서 회원가입 추가 폼으로 리다이렉트 시킵니다.
 *
 * 2. 이미 한 번 이상 OAuth2 로그인했던 유저일 때
 * authentication.getPrincipal()로 받아온 CustomOAuth2User의 getRole이 GUEST가 아니라면,
 * 추가 정보를 기입하고 이미 한 번 로그인 했던 유저이므로, Token만 발급하여 헤더에 실어 보냅니다.
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtService jwtService;
  private final UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    log.info("OAuth2 Login 성공!");
    try {
      CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

      // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 리다이렉트
      if(oAuth2User.getRole() == Role.GUEST) {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.sendRedirect("oauth2/sign-up"); // 프론트의 회원가입 추가 정보 입력 폼으로 리다이렉트

        jwtService.sendAccessAndRefreshToken(response, accessToken, null);
      } else {
        loginSuccess(response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성
      }
    } catch (Exception e) {
      throw e;
    }

  }

  private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
    String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
    String refreshToken = jwtService.createRefreshToken();
    response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
    response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

    jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
  }
}