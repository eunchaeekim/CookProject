package com.example.cook.costManagement.repository;

import com.example.cook.costManagement.CostManagement;
import com.example.cook.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostManagementRepository extends JpaRepository<CostManagement, Long> {


  List<CostManagement> findByUser(User user);

}