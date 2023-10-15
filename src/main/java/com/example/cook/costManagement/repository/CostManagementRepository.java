package com.example.cook.costManagement.repository;

import com.example.cook.costManagement.CostManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostManagementRepository extends JpaRepository<CostManagement, Long> {

}