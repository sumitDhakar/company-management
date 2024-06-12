package com.dollop.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dollop.app.entity.Promotion;
import com.dollop.app.entity.Users;

public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

	public Page<Promotion> findByIsDeleted(PageRequest pageRequest, Boolean isDelete);

	public Optional<Promotion> findByIdAndIsDeleted(Integer id, boolean b);

	  // Find the latest promotion for a specific employee
   public  Optional<Promotion> findTopByEmployeeOrderByPromotionDateDesc(Users employee);

public Optional<Promotion> findTopByEmployeeAndIsDeletedOrderByPromotionDateDesc(Users user, boolean b);


}
