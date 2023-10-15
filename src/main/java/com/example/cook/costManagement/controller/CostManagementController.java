package com.example.cook.costManagement.controller;

import com.example.cook.costManagement.dto.CostManagementDto;
import com.example.cook.costManagement.service.CostManagementService;
import java.security.Principal;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/costManagement")
public class CostManagementController {

  private final CostManagementService costManagementService;

  // 식비 등록 (직접 사용자가 구매한 식재료 정보 입력)
  @PostMapping
  public ResponseEntity<String> createCostManagement(
      @RequestBody CostManagementDto costManagementDto, Principal principal) {
    costManagementService.createCostManagement(costManagementDto, principal);
    return ResponseEntity.ok("CostManagement created successfully!");
  }

  // 식비 등록 (레시피 글 작성 시 입력한 식재료 이름 목록 가져오기, 금액과 구입 날짜는 직접 기입)
  @GetMapping("/{postId}")
  public ResponseEntity<String> getCostManagement(
      @PathVariable Long postId, @RequestBody CostManagementDto costManagementDto, Principal principal) {
    costManagementService.getCostManagement(postId, costManagementDto, principal);
    return ResponseEntity.ok("CostManagement got successfully!");
  }


  // 식비 수정
  @PutMapping("/{costManagementId}")
  public ResponseEntity<String> updateCostManagement(@PathVariable Long costManagementId,
      @RequestBody CostManagementDto costManagementDto, Principal principal) {
    costManagementService.updateCostManagement(costManagementId, costManagementDto, principal);
    return ResponseEntity.ok("CostManagement updated sucessfully!");
  }


  // 식비 삭제
  @DeleteMapping("/{costManagementId}")
  public ResponseEntity<String> deleteCostManagement(@PathVariable Long costManagementId, Principal principal) {
    costManagementService.deleteCostManagement(costManagementId, principal);
    return ResponseEntity.ok("CostManagement deleted successfully!");
  }

  // 전체 목록 조회
  @GetMapping
  public ResponseEntity<?> getAllCostManagements(Pageable pageable) {
    Page<CostManagementDto> costManagements = costManagementService.findAllCostManagements(pageable);
    return ResponseEntity.ok(costManagements);
  }

  // 기간별 사용한 식비 금액 계산
  @GetMapping("/calculate")
  public ResponseEntity<Long> calculateCostByDateRange(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      Principal principal) {
    Long totalCost = costManagementService.calculateCostManagementByDateRange(startDate, endDate, principal);
    return ResponseEntity.ok(totalCost);
  }
}
