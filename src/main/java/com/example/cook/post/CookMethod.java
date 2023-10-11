package com.example.cook.post;

import com.example.cook.post.dto.CookMethodDto;
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
public class CookMethod {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "cook_method_id")
  private Long Id;

  private String cookMethodDescription;
  private String cookMethodPhotoUrl;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  public static CookMethod createCookMethodFromDto(CookMethodDto cookMethodDto, Post post) {
    CookMethod cookMethod = new CookMethod();
    cookMethod.setCookMethodDescription(cookMethodDto.getCookMethodDescription());
    cookMethod.setCookMethodPhotoUrl(cookMethod.cookMethodPhotoUrl);
    cookMethod.setPost(post);
    return cookMethod;
  }

  public static CookMethodDto createCookMethoDtoFromEntity(CookMethod cookMethod, Post post) {
    CookMethodDto cookMethodDto = new CookMethodDto();
    cookMethodDto.setId(cookMethod.getId());
    cookMethodDto.setCookMethodDescription(cookMethod.getCookMethodDescription());
    cookMethodDto.setCookMethodPhotoUrl(cookMethod.getCookMethodPhotoUrl());
    return cookMethodDto;
  }

}
