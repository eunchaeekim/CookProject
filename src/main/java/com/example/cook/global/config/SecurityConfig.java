package com.example.cook.global.config;

import com.example.cook.global.jwt.filter.JwtAuthenticationProcessingFilter;
import com.example.cook.global.jwt.service.JwtService;
import com.example.cook.global.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.example.cook.global.login.handler.LoginFailureHandler;
import com.example.cook.global.login.handler.LoginSuccessHandler;
import com.example.cook.global.login.service.LoginService;
import com.example.cook.global.oauth2.handler.OAuth2LoginFailureHandler;
import com.example.cook.global.oauth2.handler.OAuth2LoginSuccessHandler;
import com.example.cook.global.oauth2.service.CustomOAuth2UserService;
import com.example.cook.user.repository.UserRepository;
import com.example.cook.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * 인증은 CustomJsonUsernamePasswordAuthenticationFilter에서 authenticate()로 인증된 사용자로 처리
 * JwtAuthenticationProcessingFilter는 AccessToken, RefreshToken 재발급
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final LoginService loginService;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final ObjectMapper objectMapper;
  private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
  private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
  private final CustomOAuth2UserService customOAuth2UserService;


  /**
   * 세부적인 보안 기능 설정 API를 제공하는 HttpSecurity를 파라미터로 받아서
   * HttpSecurity의 보안 기능 설정 API를 사용하여
   * 최종적으로 여러 설정을 마친 SecurityFilterChain 객체를 반환하는 메소드
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .formLogin().disable() // FormLogin 사용 X (자체 Login으로 로그인을 진행하기 때문)
        .httpBasic().disable() // httpBasic 사용 X ( JWT 토큰을 사용한 로그인(Bearer 방식)이기 때문)
        .csrf().disable() // csrf 보안 사용 X (REST API를 사용하여 서버에 인증 정보를 저장하지 않고, 요청 시 인증 정보(JWT 토큰, OAuth2)를 담아서 요청)
        .headers().frameOptions().disable() // Spring Security는 기본적으로 X-Frame-Options Click jacking 공격 막기 설정이 되어있습니다.  h2 콘솔(iframe)을 사용하기 때문에 disable로 h2 콘솔에 접근 가능
        .and()

        // 세션 사용하지 않으므로 STATELESS로 설정
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()

        //== URL별 권한 관리 옵션 ==//
        // 인증/인가 설정 시 HttpServletRequest를 이용한다는 것을 의미
        .authorizeRequests()

        // 아이콘, css, js 관련
        // 기본 페이지, css, image, js 하위 폴더에 있는 자료들은 인증 없이 모두 접근 가능, h2-console에 접근 가능
        .antMatchers("/","/css/**","/images/**","/js/**","/favicon.ico","/h2-console/**").permitAll()
        .antMatchers("/sign-up","/posts","/posts/{postId}","/comments/*").permitAll() // 회원가입, 포스팅, 댓글 접근 가능
        .anyRequest().authenticated() // 위의 경로 이외에는 모두 인증된 사용자만 접근 가능
        .and()
        //== 소셜 로그인 설정 ==//
        .oauth2Login()
        .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
        .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
        .userInfoEndpoint().userService(customOAuth2UserService); // customUserService 설정

    // 원래 스프링 시큐리티 필터 순서가 LogoutFilter 이후에 로그인 필터 동작
    // 따라서, LogoutFilter 이후에 우리가 만든 필터 동작하도록 설정
    // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
    /**
     * B 필터 이후에 A 필터가 동작하도록 하는 메소드입니다.
     * 원래 스프링 시큐리티 필터 동작 순서가 LogoutFilter 이후에 로그인 필터가 동작하기 때문에,
     * 우리가 만든 커스텀 JSON 로그인 필터인 CustomJsonUsernamePasswordAuthenticationFilter를
     * LogoutFilter 이후에 동작하도록 설정하였습니다.
     */
    http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
    /**
     * addFilterAfter(A, B)와는 반대로, B 필터 이전에 A 필터가 동작하도록 하는 메소드입니다.
     * 우리가 만든 커스텀 JSON 로그인 필터 동작 전에 JWT 인증 필터가 동작하도록 하기 위해
     * JwtAuthenticationProcessingFilter를 CustomJsonUsernamePasswordAuthenticationFilter 전으로 설정하였습니다.
     */
    http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  /**
   * AuthenticationManager 설정 후 등록
   * PasswordEncoder를 사용하는 AuthenticationProvider 지정 (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용)
   * FormLogin(기존 스프링 시큐리티 로그인)과 동일하게 DaoAuthenticationProvider 사용
   * UserDetailsService는 커스텀 LoginService로 등록
   * 또한, FormLogin과 동일하게 AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
   *
   */
  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(loginService);
    return new ProviderManager(provider);
  }

  /**
   * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
   */
  @Bean
  public LoginSuccessHandler loginSuccessHandler() {
    return new LoginSuccessHandler(jwtService, userRepository);
  }

  /**
   * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
   */
  @Bean
  public LoginFailureHandler loginFailureHandler() {
    return new LoginFailureHandler();
  }

  /**
   * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록
   * 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
   * setAuthenticationManager(authenticationManager())로 위에서 등록한 AuthenticationManager(ProviderManager) 설정
   * 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한 handler 설정
   */
  @Bean
  public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
    CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
        = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
    customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
    customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
    customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
    return customJsonUsernamePasswordLoginFilter;
  }

  @Bean
  public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
    JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, userRepository);
    return jwtAuthenticationFilter;
  }


}