package com.dollop.app.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dollop.app.entity.Projects;
import com.dollop.app.entity.Tasks;
import com.dollop.app.entity.TimeSheets;
import com.dollop.app.entity.Users;

public interface TimeSheetRepository extends JpaRepository<TimeSheets, Integer> {

//	
// public TimeSheets findByTotalTimeBetween(Timestamp startTime,Timestamp endTime);

//	public Page<TimeSheets> findAllByClientId(Pageable page, Clients client);

	public Page<TimeSheets> findByProjectId(Pageable page, Projects project);

	public Page<TimeSheets> findByTaskId(Pageable page, Tasks task);

	public Optional<TimeSheets> findByIdAndDeleted(Integer id, boolean b);

	public boolean existsByTimeSheetDateAndUser(LocalDate timeSheetDate, Users currentUser);

}
