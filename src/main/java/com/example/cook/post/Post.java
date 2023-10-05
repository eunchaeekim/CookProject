package com.example.cook.post;

import com.example.cook.bookMark.PostBookMark;
import com.example.cook.comment.Comment;
import com.example.cook.postRecommend.PostRecommend;
import com.example.cook.user.BaseEntity;
import com.example.cook.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "post_id")
  private Long id;

  private String cookTitle; // 글 제목
  private String cookThumbnailUrl; // 글 썸네일
  private String cookName; // 요리 이름

  @Enumerated(EnumType.STRING)
  private Category category; // 요리 카테고리

  @Enumerated(EnumType.STRING)
  private Amount cookAmount; // 몇인분

  @Enumerated(EnumType.STRING)
  private Time cookTime; // 요리 시간
 // private boolean isPublic; // 게시글 공개 여부

  @ManyToOne
  @JoinColumn(name = "user_Id")
  private User user;

  @JsonIgnore
  @OneToMany(mappedBy = "post")
  List<Comment> comments = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "post")
  List<PostRecommend> postRecommends = new ArrayList<>();

  @JsonIgnore
  @OneToMany(mappedBy = "post")
  List<PostBookMark> postBookMarks = new ArrayList<>();

}
