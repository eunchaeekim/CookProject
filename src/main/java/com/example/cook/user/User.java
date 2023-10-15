package com.example.cook.user;

import com.example.cook.bookMark.PostBookMark;
import com.example.cook.comment.Comment;
import com.example.cook.costManagement.CostManagement;
import com.example.cook.follow.Follow;
import com.example.cook.post.Post;
import com.example.cook.postRecommend.PostRecommend;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "USERS")
@AllArgsConstructor
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id")
  private Long id;

  private String email; // 이메일
  private String password; // 비밀번호
  private int age;
  private String city; // 사는 도시

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  private SocialType socialType; // KAKAO, NAVER, GOOGLE

  private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

  private String refreshToken; // 리프레시 토큰

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  List<Post> boards = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  List<Comment> comments = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  List<PostRecommend> postRecommends = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  List<PostBookMark> postBookMarks = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "follower")
  List<Follow> followers = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "following")
  List<Follow> followings = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "user")
  List<CostManagement> costManagements = new ArrayList<>();



  // 유저 권한 설정 메소드
  public void authorizeUser() {
    this.role = Role.USER;
  }

  // 비밀번호 암호화 메소드
  public void passwordEncode(PasswordEncoder passwordEncoder) {
    this.password = passwordEncoder.encode(this.password);
  }

  public void updateRefreshToken(String updateRefreshToken) {
    this.refreshToken = updateRefreshToken;
  }

}