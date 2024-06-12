package com.dollop.app.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dollop.app.entity.GoalList;
import com.dollop.app.entity.Promotion;
import com.dollop.app.entity.Resignation;
import com.dollop.app.entity.Users;

public interface ResignationRepository extends JpaRepository<Resignation, Integer> {

	public Page<Resignation> findByIsDelete(PageRequest pageRequest, boolean b);

	public Optional<Resignation> findByIdAndIsDelete(Integer id, boolean b);

	public Boolean existsByEmployee(Users user);

	public Resignation findByEmployee(Users user);

	public List<Resignation> findByNoticeDateAndIsDelete(Date valueOf, boolean b);


}
