package com.example.cook.post.service;

import com.example.cook.post.CookMethod;
import com.example.cook.post.Ingredient;
import com.example.cook.post.Post;
import com.example.cook.post.dto.CookMethodDto;
import com.example.cook.post.dto.IngredientDto;
import com.example.cook.post.dto.PostDto;
import com.example.cook.post.repository.PostRepository;
import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  // 포스팅 등록
  public void createPost(PostDto postDto, Principal principal) {
    String email = principal.getName();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    Post post = new Post();
    post.setUser(user);
    post.setCookTitle(postDto.getCookTitle());
    post.setCookThumbnailUrl(postDto.getCookThumbnailUrl());
    post.setCookName(postDto.getCookName());
    post.setCategory(postDto.getCategory());
    post.setCookAmount(postDto.getCookAmount());
    post.setCookTime(postDto.getCookTime());

    // Ingredients 추가 코드
    List<Ingredient> ingredients = new ArrayList<>();
    for (IngredientDto ingredientDto : postDto.getIngredients()) {
      Ingredient ingredient = new Ingredient();
      ingredient.setCookIngredient(ingredientDto.getCookIngredient());
      ingredient.setCookIngredientAmount(ingredientDto.getCookIngredientAmount());
      ingredient.setPost(post);
      ingredients.add(ingredient);
    }

    post.setIngredients(ingredients);

    // CookMethods 추가 코드
    List<CookMethod> cookMethods = new ArrayList<>();
    for (CookMethodDto cookMethodDto : postDto.getCookMethods()) {
      CookMethod cookMethod = new CookMethod();
      cookMethod.setCookMethodDescription(cookMethodDto.getCookMethodDescription());
      cookMethod.setCookMethodPhotoUrl(cookMethodDto.getCookMethodPhotoUrl());
      cookMethod.setPost(post);
      cookMethods.add(cookMethod);
    }

    post.setCookMethods(cookMethods);

    postRepository.save(post);
  }

  // 포스팅 수정
  @Transactional
  public void updatePost(Long postId, PostDto postDto, Principal principal) {
    String email = principal.getName();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("포스팅이 존재하지 않습니다."));

    // 게시판의 username과 로그인 유저의 username 비교
    if (!email.equals(post.getUser().getEmail())) {
      throw new RuntimeException("작성자만 수정할 수 있습니다.");
    }

    // Ingredients 관계 제거 및 삭제
    List<Ingredient> existingIngredients = new ArrayList<>(post.getIngredients());

    for (Ingredient ingredient : existingIngredients) {
      ingredient.setCookIngredient(null);
      ingredient.setCookIngredientAmount(null);
      ingredient.setPost(null);
    }

    post.getIngredients().clear();

    // CookMethods 관계 제거 및 삭제
    List<CookMethod> existingCookMethods = new ArrayList<>(post.getCookMethods());

    for (CookMethod cookMethod : existingCookMethods) {
      cookMethod.setCookMethodDescription(null);
      cookMethod.setCookMethodPhotoUrl(null);
      cookMethod.setPost(null);
    }

    post.getCookMethods().clear();

    // Ingredients 추가 및 업데이트 코드
    for (IngredientDto ingredientDto : postDto.getIngredients()) {
      Ingredient ingredient = new Ingredient();
      ingredient.setCookIngredient(ingredientDto.getCookIngredient());
      ingredient.setCookIngredientAmount(ingredientDto.getCookIngredientAmount());
      ingredient.setPost(post);
      post.getIngredients().add(ingredient);
    }

    // CookMethods 추가 및 업데이트 코드
    for (CookMethodDto cookMethodDto : postDto.getCookMethods()) {
      CookMethod cookMethod = new CookMethod();
      cookMethod.setCookMethodDescription(cookMethodDto.getCookMethodDescription());
      cookMethod.setCookMethodPhotoUrl(cookMethodDto.getCookMethodPhotoUrl());
      cookMethod.setPost(post);
      post.getCookMethods().add(cookMethod);
    }

    // 나머지 포스트 정보 업데이트
    post.setCookTitle(postDto.getCookTitle());
    post.setCookThumbnailUrl(postDto.getCookThumbnailUrl());
    post.setCookName(postDto.getCookName());
    post.setCategory(postDto.getCategory());
    post.setCookAmount(postDto.getCookAmount());
    post.setCookTime(postDto.getCookTime());

    postRepository.save(post);
  }

  // 포스팅 삭제
  public void deletePost(Long postId, Principal principal) {
    String email = principal.getName();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("포스팅이 존재하지 않습니다."));

    // 게시판의 username과 로그인 유저의 username 비교
    if (!email.equals(post.getUser().getEmail())) {
      throw new RuntimeException("작성자만 삭제할 수 있습니다.");
    }

    // 포스트와 관련된 Ingredient 및 CookMethod 정보를 삭제
    List<Ingredient> ingredients = post.getIngredients();
    List<CookMethod> cookMethods = post.getCookMethods();

    // 관련된 Ingredient 및 CookMethod 정보를 삭제
    for (Ingredient ingredient : ingredients) {
      ingredient.setPost(null); // Ingredient와의 연관 관계 해제
    }
    for (CookMethod cookMethod : cookMethods) {
      cookMethod.setPost(null); // CookMethod와의 연관 관계 해제
    }

    postRepository.delete(post);
  }

  // 모든 포스트 조회
  public Page<PostDto> findAllPosts(Pageable pageable) {
    Page<Post> posts = postRepository.findAll(pageable);
    return posts.map(post -> {
      PostDto postDto = new PostDto();
      postDto.setId(post.getId());
      postDto.setCookTitle(post.getCookTitle());
      postDto.setCookThumbnailUrl(post.getCookThumbnailUrl());
      postDto.setCookName(post.getCookName());
      postDto.setCategory(post.getCategory());
      postDto.setCookAmount(post.getCookAmount());
      postDto.setCookTime(post.getCookTime());

      // Ingredients 추가 코드
      List<IngredientDto> ingredientDtos = new ArrayList<>();
      for (Ingredient ingredient : post.getIngredients()) {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setId(ingredient.getId());
        ingredientDto.setCookIngredient(ingredient.getCookIngredient());
        ingredientDto.setCookIngredientAmount(ingredient.getCookIngredientAmount());
        ingredientDtos.add(ingredientDto);
      }
      postDto.setIngredients(ingredientDtos);

      // CookMethods 추가 코드
      List<CookMethodDto> cookMethodDtos = new ArrayList<>();
      for (CookMethod cookMethod : post.getCookMethods()) {
        CookMethodDto cookMethodDto = new CookMethodDto();
        cookMethodDto.setId(cookMethod.getId());
        cookMethodDto.setCookMethodDescription(cookMethod.getCookMethodDescription());
        cookMethodDto.setCookMethodPhotoUrl(cookMethod.getCookMethodPhotoUrl());
        cookMethodDtos.add(cookMethodDto);
      }
      postDto.setCookMethods(cookMethodDtos);

      return postDto;
    });
  }

}
