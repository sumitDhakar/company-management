package com.dollop.app.repository;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dollop.app.entity.OverTime;
import com.dollop.app.entity.Users;

public interface OverTimeRepository extends JpaRepository<OverTime, Integer> {

	Page<OverTime> findByIsDeleted(PageRequest pageRequest, boolean b);

	OverTime findByIdAndIsDeleted(Integer id, boolean b);

	@Query("SELECT SUM(ot.overTimeHours) AS totalHours, COUNT(DISTINCT ot.userId.id) AS employeeCount ,"
			+ "SUM(CASE WHEN ot.status = 'Pending' THEN 1 ELSE 0 END) AS pending , "
			+ "SUM(CASE WHEN ot.status = 'Rejected' THEN 1 ELSE 0 END) AS rejectedCount "

			+ " FROM OverTime ot " + "WHERE ot.isDeleted=false And ot.overTimeDate BETWEEN :startDate AND :endDate  ")
	public Object findTotalWorkedHoursBetweenDates(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);

	public Page<OverTime> findByIsDeletedAndUserId(PageRequest pageRequest, boolean b, Users user);

	public boolean existsByOverTimeDateAndUserId(Date overTimeDate, Users overTimeUser);
}
