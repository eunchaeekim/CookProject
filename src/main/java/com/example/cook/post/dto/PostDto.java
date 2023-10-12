package com.example.cook.post.dto;


import com.example.cook.post.Amount;
import com.example.cook.post.Category;
import com.example.cook.post.Time;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {

  private Long id; //id
  private String cookTitle; // 글 제목
  private String cookThumbnailUrl; // 글 썸네일
  private String cookName; // 요리 이름
  private Category category; // 요리 카테고리
  private Amount cookAmount; // 몇인분
  private Time cookTime; // 요리 시간
  //private boolean isPublic; // 게시글 공개여부
  private List<IngredientDto> ingredients;
  private List<CookMethodDto> cookMethods; // 요리 방법


}

