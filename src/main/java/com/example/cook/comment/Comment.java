package com.example.cook.comment;

import com.example.cook.post.Post;
import com.example.cook.user.BaseEntity;
import com.example.cook.user.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
public class Comment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "comment_Id")
  private Long id;


  @Column(nullable = false)
  private String content;

  @ManyToOne
  @JoinColumn(name = "post_Id")
  private Post post;

  @ManyToOne
  @JoinColumn(name = "user_Id")
  private User user;


}
