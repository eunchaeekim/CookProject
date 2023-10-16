package com.example.cook.costManagement.service;

import com.example.cook.costManagement.CostManagement;
import com.example.cook.costManagement.dto.CostManagementDto;
import com.example.cook.costManagement.repository.CostManagementRepository;
import com.example.cook.exception.impl.NotExistCostListException;
import com.example.cook.exception.impl.NotExistPostException;
import com.example.cook.exception.impl.NotExistUserException;
import com.example.cook.exception.impl.OnlyWirterException;
import com.example.cook.post.Ingredient;
import com.example.cook.post.Post;
import com.example.cook.post.repository.PostRepository;
import com.example.cook.user.User;
import com.example.cook.user.repository.UserRepository;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
        .orElseThrow(NotExistUserException::new);

    CostManagement costManagement = CostManagement.createCostManagementFromDto(costManagementDto, user);
    costManagementRepository.save(costManagement);

  }

  // 식비 등록 (레시피 글 작성 시 입력한 식재료 이름 목록 가져오기, 금액과 구입 날짜는 직접 기입)
  public void getCostManagement(Long postId, CostManagementDto costManagementDto, Principal principal) {
    String email = principal.getName();

    // UserDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(NotExistUserException::new);

    Post post = postRepository.findById(postId)
        .orElseThrow(NotExistPostException::new);

    // 게시물에 연결된 식재료 가져오기
    List<Ingredient> ingredients = post.getIngredients();

    List<CostManagement> costManagements = ingredients.stream()
        .map(ingredient -> {
          String ingredientName = ingredient.getCookIngredient();
          return CostManagement.createCostManagementFromDto(costManagementDto, user, ingredientName);
        })
        .collect(Collectors.toList());

    // CostManagement 엔티티 리스트를 저장
    costManagementRepository.saveAll(costManagements);
  }


  public void updateCostManagement(Long costManagementId, CostManagementDto costManagementDto,
      Principal principal) {
    String email = principal.getName();

    // userDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(NotExistUserException::new);

    CostManagement costManagement = costManagementRepository.findById(costManagementId)
        .orElseThrow(NotExistCostListException::new);

    // 게시판의 username과 로그인 유저의 username 비교
    if (!email.equals(costManagement.getUser().getEmail())) {
      throw new OnlyWirterException();
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
        .orElseThrow(NotExistUserException::new);

    CostManagement costManagement = costManagementRepository.findById(costManagementId)
        .orElseThrow(NotExistCostListException::new);

    // 게시판의 username과 로그인 유저의 username 비교
    if (!email.equals(costManagement.getUser().getEmail())) {
      throw new OnlyWirterException();
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

  // 기간별 사용한 식비 금액 계산
  public Long calculateCostManagementByDateRange(LocalDate startDate, LocalDate endDate, Principal principal) {
    String email = principal.getName();

    // UserDetails에서 사용자 정보 가져오기
    User user = userRepository.findByEmail(email)
        .orElseThrow(NotExistUserException::new);

    // 사용자의 식비 목록을 가져옵니다.
    List<CostManagement> costManagements = costManagementRepository.findByUser(user);

    // 기간 내에 속하는 항목만 필터링하고 금액을 더합니다.
    Long totalCost = costManagements.stream()
        .filter(costManagement -> (!costManagement.getManagementIngredientBuyDate().isBefore(startDate))
            && (!costManagement.getManagementIngredientBuyDate().isAfter(endDate)))
        .mapToLong(CostManagement::getManagementIngredientCost)
        .sum();

    return totalCost;
  }

}
