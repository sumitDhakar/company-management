package com.dollop.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.entity.TrainingList;
import com.dollop.app.entity.TrainingType;

public interface TrainingTypeRepository extends JpaRepository<TrainingType,Integer>{
	public Page<TrainingType>   findByStatus(Pageable pageble,String status);

	public Page<TrainingType> findByIsDelete(PageRequest pageRequest, boolean b);

	public Optional<TrainingType> findByIdAndIsDelete(Integer id, boolean b);

	public Page<TrainingType> findByIsDeleteAndStatus(PageRequest pageRequest, boolean b, String string);
	
	public boolean existsByTypeAndIsDelete(String trim, boolean b);

	public boolean existsByTypeAndIsDeleteAndIdNot(String trim, boolean b, Integer id);

}
