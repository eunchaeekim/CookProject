package com.example.cook.costManagement.service;

import com.example.cook.costManagement.CostManagement;
import com.example.cook.costManagement.dto.CostManagementDto;
import com.example.cook.costManagement.repository.CostManagementRepository;
import com.example.cook.post.Ingredient;
import com.example.cook.post.Post;
import com.example.cook.post.repository.PostRepository;
import com.example.cook.post.service.PostService;
import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CostManagementService {

  private final CostManagementRepository costManagementRepository;
  private final UserRepository userRepository;
  private final PostRepository postRepository;

  // 식비 등록 (직접 사용자가 구매한 식재료 정보 입력)
  public void createCostManagement(CostManagementDto costManagementDto, Principal principal) {
    String email = principal.getName();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    CostManagement costManagement = new CostManagement();
    costManagement.setUser(user);
    costManagement.setManagementIngredient(costManagementDto.getManagementIngredient());
    costManagement.setManagementIngredientCost(costManagementDto.getManagementIngredientCost());
    costManagement.setManagementIngredientBuyDate(costManagementDto.getManagementIngredientBuyDate());

    costManagementRepository.save(costManagement);

  }

  // 식비 등록 (레시피 글 작성 시 입력한 식재료 이름 목록 가져오기, 금액과 구입 날짜는 직접 기입)
  public void getCostManagement(Long postId, CostManagementDto costManagementDto, Principal principal) {
    String email = principal.getName();

    // UserDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new RuntimeException("포스팅이 존재하지 않습니다."));

    // 게시물에 연결된 식재료 가져오기
    List<Ingredient> ingredients = post.getIngredients();

    List<CostManagement> costManagements = new ArrayList<>();

    for (Ingredient ingredient : ingredients) {
      String ingredientName = ingredient.getCookIngredient();

      CostManagement costManagement = new CostManagement();
      costManagement.setUser(user);
      costManagement.setManagementIngredient(ingredientName);
      costManagement.setManagementIngredientCost(costManagementDto.getManagementIngredientCost());
      costManagement.setManagementIngredientBuyDate(costManagementDto.getManagementIngredientBuyDate());

      // 각 CostManagement 엔티티를 리스트에 추가
      costManagements.add(costManagement);
    }

    // CostManagement 엔티티 리스트를 저장
    costManagementRepository.saveAll(costManagements);
  }


  public void updateCostManagement(Long costManagementId, CostManagementDto costManagementDto,
      Principal principal) {
    String email = principal.getName();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    CostManagement costManagement = costManagementRepository.findById(costManagementId)
        .orElseThrow(() -> new RuntimeException("식비 내용이 존재하지 않습니다."));

    // 게시판의 username과 로그인 유저의 username 비교
    if (!email.equals(costManagement.getUser().getEmail())) {
      throw new RuntimeException("작성자만 수정할 수 있습니다.");
    }

    costManagement.setUser(user);
    costManagement.setManagementIngredient(costManagementDto.getManagementIngredient());
    costManagement.setManagementIngredientCost(costManagementDto.getManagementIngredientCost());
    costManagement.setManagementIngredientBuyDate(costManagementDto.getManagementIngredientBuyDate());

    costManagementRepository.save(costManagement);

  }

  public void deleteCostManagement(Long costManagementId, Principal principal) {
    String email = principal.getName();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

    CostManagement costManagement = costManagementRepository.findById(costManagementId)
        .orElseThrow(() -> new RuntimeException("식비 내용이 존재하지 않습니다."));

    // 게시판의 username과 로그인 유저의 username 비교
    if (!email.equals(costManagement.getUser().getEmail())) {
      throw new RuntimeException("작성자만 삭제할 수 있습니다.");
    }

    costManagementRepository.delete(costManagement);

  }

  public Page<CostManagementDto> findAllCostManagements(Pageable pageable) {
    Page<CostManagement> costManagements = costManagementRepository.findAll(pageable);
    return costManagements.map(costManagement -> {
      CostManagementDto costManagementDto = new CostManagementDto();
      costManagementDto.setId(costManagement.getId());
      costManagementDto.setManagementIngredient(costManagement.getManagementIngredient());
      costManagementDto.setManagementIngredientCost(costManagement.getManagementIngredientCost());
      costManagementDto.setManagementIngredientBuyDate(costManagement.getManagementIngredientBuyDate());

      return costManagementDto;
    });
  }

}
