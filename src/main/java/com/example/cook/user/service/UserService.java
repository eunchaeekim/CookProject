package com.example.cook.user.service;

import com.example.cook.user.Role;
import com.example.cook.user.User;
import com.example.cook.user.dto.UserSignUpDto;
import com.example.cook.user.repository.UserRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public void signUp(UserSignUpDto userSignUpDto) throws Exception {

    if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()) {
      throw new Exception("이미 존재하는 이메일입니다.");
    }


    User user = User.builder()
        .email(userSignUpDto.getEmail())
        .password(userSignUpDto.getPassword())
        .age(userSignUpDto.getAge())
        .city(userSignUpDto.getCity())
        .role(Role.USER)
        .build();

    user.passwordEncode(passwordEncoder);
    userRepository.save(user);
  }

}