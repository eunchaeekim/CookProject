package com.example.cook.post.service;

import static com.example.cook.post.CookMethod.createCookMethoDtoFromEntity;
import static com.example.cook.post.CookMethod.createCookMethodFromDto;
import static com.example.cook.post.Ingredient.createIngredientDtoFromEntity;
import static com.example.cook.post.Ingredient.createIngredientFromDto;

import com.example.cook.exception.impl.NotExistPostException;
import com.example.cook.exception.impl.NotExistUserException;
import com.example.cook.exception.impl.OnlyWirterException;
import com.example.cook.post.CookMethod;
import com.example.cook.post.Ingredient;
import com.example.cook.post.Post;
import com.example.cook.post.dto.CookMethodDto;
import com.example.cook.post.dto.IngredientDto;
import com.example.cook.post.dto.PostDto;
import com.example.cook.post.repository.CookMethodRepository;
import com.example.cook.post.repository.IngredientRepository;
import com.example.cook.post.repository.PostRepository;
import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
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
  private final IngredientRepository ingredientRepository;
  private final CookMethodRepository cookMethodRepository;

  // 포스팅 등록
  public void createPost(PostDto postDto, Principal principal) {
    String email = principal.getName();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(NotExistUserException::new);

    Post post = new Post();
    post.setUser(user);
    post.setCookTitle(postDto.getCookTitle());
    post.setCookThumbnailUrl(postDto.getCookThumbnailUrl());
    post.setCookName(postDto.getCookName());
    post.setCategory(postDto.getCategory());
    post.setCookAmount(postDto.getCookAmount());
    post.setCookTime(postDto.getCookTime());

    // Ingredients 추가 코드
    List<Ingredient> ingredients = postDto.getIngredients().stream()
        .map(ingredientDto -> createIngredientFromDto(ingredientDto, post))
        .collect(Collectors.toList());

    post.setIngredients(ingredients);

    /*
    List<Ingredient> ingredients = new ArrayList<>();
    for (IngredientDto ingredientDto : postDto.getIngredients()) {
      Ingredient ingredient = createIngredientFromDto(ingredientDto, post);
      ingredients.add(ingredient);
    }
    post.setIngredients(ingredients);
     */


    // CookMethods 추가 코드
    List<CookMethod> cookMethods = postDto.getCookMethods().stream()
        .map(cookMethodDto -> createCookMethodFromDto(cookMethodDto, post))
        .collect(Collectors.toList());

    post.setCookMethods(cookMethods);


    postRepository.save(post);
  }

  // 포스팅 수정
  @Transactional
  public void updatePost(Long postId, PostDto postDto, Principal principal) {
    String email = principal.getName();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(NotExistUserException::new);

    Post post = postRepository.findById(postId)
        .orElseThrow(NotExistPostException::new);

    // 게시판의 username과 로그인 유저의 username 비교
    if (!email.equals(post.getUser().getEmail())) {
      throw new OnlyWirterException();
    }

    ingredientRepository.deleteByPostId(postId);


    // Ingredients 추가 및 업데이트 코드
    for (IngredientDto ingredientDto : postDto.getIngredients()) {
      Ingredient ingredient = createIngredientFromDto(ingredientDto, post);
      post.getIngredients().add(ingredient);
    }

    cookMethodRepository.deleteByPostId(postId);

    // CookMethods 추가 및 업데이트 코드
    for (CookMethodDto cookMethodDto : postDto.getCookMethods()) {
      CookMethod cookMethod = createCookMethodFromDto(cookMethodDto,post);
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
        .orElseThrow(NotExistUserException::new);

    Post post = postRepository.findById(postId)
        .orElseThrow(NotExistPostException::new);

    // 게시판의 username과 로그인 유저의 username 비교
    if (!email.equals(post.getUser().getEmail())) {
      throw new OnlyWirterException();
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
      List<IngredientDto> ingredientDtos = post.getIngredients().stream()
          .map(ingredient -> createIngredientDtoFromEntity(ingredient, post))
          .collect(Collectors.toList());
      postDto.setIngredients(ingredientDtos);

      // CookMethods 추가 코드
      List<CookMethodDto> cookMethodDtos = post.getCookMethods().stream()
          .map(cookMethod -> createCookMethoDtoFromEntity(cookMethod, post))
          .collect(Collectors.toList());
      postDto.setCookMethods(cookMethodDtos);

      return postDto;
    });
  }
}
