package com.example.global.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.cook.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

  /**
   * 유저가 로그인 시도: 클라이언트에서 로그인 요청을 보내고, 이 요청을 서버가 받습니다.
   * createAccessToken 메소드 호출: createAccessToken 메소드는 AccessToken을 생성합니다.
   * createRefreshToken 메소드 호출: createRefreshToken 메소드는 RefreshToken을 생성합니다.
   * 로그인 성공 후, 클라이언트에게 sendAccessAndRefreshToken 메소드를 사용하여 클라이언트에게 AccessToken 및 RefreshToken을 전달합니다.
   * 클라이언트가 요청을 할 때마다 AccessToken을 사용: 클라이언트가 보호된 엔드포인트에 요청을 보낼 때, 요청 헤더에 AccessToken을 포함하여 보냅니다.
   * 서버에서 AccessToken 유효성 검사: isTokenValid 메소드를 사용하여 AccessToken의 유효성을 검사합니다.
   * AccessToken이 만료되면 클라이언트는 RefreshToken을 사용하여 새로운 AccessToken을 요청할 수 있습니다.
   */

  @Value("${jwt.secretKey}")
  private String secretKey;

  @Value("${jwt.access.expiration}")
  private Long accessTokenExpirationPeriod;

  @Value("${jwt.refresh.expiration}")
  private Long refreshTokenExpirationPeriod;

  @Value("${jwt.access.header}")
  private String accessHeader;

  @Value("${jwt.refresh.header}")
  private String refreshHeader;

  /**
   * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
   * JWT의 헤더에 들어오는 값: 'Authorization(Key)=Bearer{토큰}(Value)' 형식
   */
  private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
  private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
  private static final String EMAIL_CLAIM = "email";
  private static final String BEARER = "Bearer";

  private final UserRepository userRepository;

  /**
   * AccessToken 생성 메소드
   */
  public String createAccessToken(String email) {
    Date now = new Date();
    return JWT.create() // JWT 토큰을 생성하는 빌더 반환
        .withSubject(ACCESS_TOKEN_SUBJECT) // JWT의 Subject 지정 -> AccessToken 이므로 AccessToken
        .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // 토큰 만료 시간 설정

        // 클레임으로는 email 하나만 사용합니다.
        // 추가적으로 식별자나, 이름 등의 정보를 더 추가하셔도 됩니다.
        // 추가하실 경우 .withClaim(클래임 이름, 클래임 값)으로 설정
        .withClaim(EMAIL_CLAIM, email)
        .sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용, application-jwt.yml에서 지정한 secret 키로 암호화
  }

  /**
   * RefreshToken 생성
   * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() 없음
   */
  public String createRefreshToken() {
    Date now = new Date();
    return JWT.create()
        .withSubject(REFRESH_TOKEN_SUBJECT)
        .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
        .sign(Algorithm.HMAC512(secretKey));
  }

  /**
   * AccessToken 재발급 시 (ex. 토큰 만료) 헤더에 실어서 보내는 메소드
   */
  public void sendAccessToken(HttpServletResponse response, String accessToken) {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setHeader(accessHeader, accessToken);
    log.info("재발급된 AccessToken: {}", accessToken);
  }

  /**
   * 로그인 시 AccessToken & RefreshToken을 헤더에 실어서 보내는 메소드
   */
  public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
    response.setStatus(HttpServletResponse.SC_OK);
    setAccessTokenHeader(response, accessToken);
    setRefreshTokenHeader(response, refreshToken);
    log.info("AccessToken, RefreshToken 헤더 설정 완료");
  }

  /**
   * 헤더에서 RefreshToken 추출
   * 토큰 형식: Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
   * 헤더를 가져온 후 "Bearer"를 삭제 (""로 replace)
   */
  public Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(refreshHeader))
        .filter(refreshToken -> refreshToken.startsWith(BEARER))
        .map(refreshToken -> refreshToken.replace(BEARER, ""));
  }

  /**
   * 헤더에서 AccessToken 추출
   * 토큰 형식: Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
   * 헤더를 가져온 후 "Bearer"를 삭제 (""로 replace)
   */
  public Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(accessHeader))
        .filter(accessToken -> accessToken.startsWith(BEARER))
        .map(accessToken -> accessToken.replace(BEARER, ""));
  }

  /**
   * AccessToken에서 Email 추출
   * 추출 전에 JWT.require()로 검증기 생성
   * verify로 AccessToken 검증 후
   * 유효하다면 getClaim()으로 이메일 추출
   * 유효하지 않다면 빈 Optional 객체 반환
   */
  public Optional<String> extractEmail(String accessToken) {
    try {
      // 토큰 유효성 검사하는데에 사용할 알고리즘이 있는 JWT verifier builder 반환
      return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
          .build() // 반환된 빌더로 JWT verifier 생성
          .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
          .getClaim(EMAIL_CLAIM) // claim (Email) 가져오기
          .asString());
    } catch (Exception e) {
      log.error("액세스 토큰이 유효하지 않습니다. {}", e.getMessage());
      return Optional.empty();
    }
  }

  /**
   * AccessToken 헤더 설정
   */
  public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
    response.setHeader(accessHeader, accessToken);
  }

  /**
   * RefreshToken 헤더 설정
   */
  public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
    response.setHeader(refreshHeader, refreshToken);
  }

  /**
   * RefreshToken DB 저장 (업데이트)
   * 유저 회원가입 시에 유저 Entity가 저장될 때는, RefreshToken이 발급되기 전이기 때문에
   * DB에 RefreshToken Column에 null로 저장된다.
   * 따라서, 로그인 시 RefreshToken을 발급하면서, 발급한 RefreshToken을 DB에 저장하는 메소드이다.
   * 이후에 OAuth2Login 성공시 처리하는 LoginSuccessHandler에서 사용할 예정
   */
  public void updateRefreshToken(String email, String refreshToken) {
    userRepository.findByEmail(email)
        .ifPresentOrElse(
            user -> user.updateRefreshToken(refreshToken),
            () -> new Exception("일치하는 회원이 없습니다.")
        );
  }

  /**
   * 토큰의 유효성을 검사하는 메소드 isTokenValid()
   * 매 인증시마다 (클라이언트가 토큰을 헤더에 담아서 요청할 때마다) 토큰 검증 단계를 거치게 된다.
   * 각 AccessToken, RefreshToken의 유효성을 검사할 때 사용되는 메소드이다.
   * JWT.require()로 토큰 유효성을 검사하는 로직이 있는 JWT verifier builder를 반환한다.
   * 그 후 반환된 builder를 사용하여 .verify(accessToken)로 Token을 검증한다.
   * 토큰이 유효하지 않으면 예외를 발생시켜 false를 반환하고, 유효하다면 true를 반환한다.
   */
  public boolean isTokenValid(String token) {
    try {
      JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
      return true;
    } catch (Exception e) {
      log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
      return false;
    }
  }
}
