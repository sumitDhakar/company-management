package com.dollop.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dollop.app.entity.Designation;
import com.dollop.app.entity.TimeSheets;
import com.dollop.app.entity.Trainers;
import com.dollop.app.entity.payload.TrainerResponse;

public interface TrainersRepository extends JpaRepository<Trainers, Integer> {
	public Page<Trainers> findByStatus(PageRequest pageRequest, String status);

	public Optional<Trainers> findByIdAndIsDeleted(Integer id, boolean b);

	public Page<Trainers> findByIsDeleted(PageRequest page, boolean b);

	public Page<Trainers> findByIsDeletedAndStatus(PageRequest pageRequest, boolean b, String string);

	public boolean existsByRoleAndEmailAndIsDeletedAndIdNot(Designation role, String trim, boolean b, Integer id);

	public boolean existsByEmailAndIsDeletedAndRole(String email, boolean b, Designation role);

}
