package com.example.global.login.service;

import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

  private final UserRepository userRepository;

  /*
  DaoAuthenticationProvider는 사용자 정보를 검색하고 해당 정보를 UserDetails 객체로 변환하여 Spring Security의 인증 프로세스에 활용합니다.
  이로써 사용자가 제공한 자격 증명과 데이터베이스에서 검색한 사용자 정보를 비교하여 로그인 인증(authentication)을 수행할 수 있습니다.
  (step)
  1.DaoAuthenticationProvider가 꺼내어 파라미터로 설정해준 username(email)로 DB의 해당하는 User 찾는 로직
  2. 해당 유저의 username(email), password, Role을 담아서 UserDetails의 User 객체를 생성하는 로직
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));

    return org.springframework.security.core.userdetails.User.builder()
        .username(user.getEmail())
        .password(user.getPassword())
        .roles(user.getRole().name())
        .build();
  }

  /**
   * 여기서, User의 역할 Enum인 Role의 키에 "ROLE_"을 접두사로 붙였는지 알 수 있습니다.
   * UserDetails의 User 객체를 생성할 때, UserBuilder 객체를 사용하여 build를 하는데,
   * roles 필드를 build할 때 파라미터로 들어온 role들이 "ROLE_"으로 시작하지 않으면, 예외를 발생시키는 것을 알 수 있습니다.

    public UserBuilder roles(String... roles) {
        List<GrantedAuthority> authorities = new ArrayList<>(roles.length);
        for (String role : roles) {
            Assert.isTrue(!role.startsWith("ROLE_"),
                    () -> role + " cannot start with ROLE_ (it is automatically added)");
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        }
        return authorities(authorities);
    }
   */
}