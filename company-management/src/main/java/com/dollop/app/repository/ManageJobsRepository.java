package com.dollop.app.repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.entity.Department;
import com.dollop.app.entity.Designation;
import com.dollop.app.entity.ManageJobs;

public interface ManageJobsRepository extends JpaRepository<ManageJobs, Integer> {

	public Optional<ManageJobs> findByIdAndIsDeletd(Integer id, boolean b);

	public Page<ManageJobs> findByIsDeletd(PageRequest pageRequest, boolean b);

	@Query("SELECT mj FROM ManageJobs mj WHERE mj.isDeletd = :isDeleted AND mj.status = :status AND :todayDate BETWEEN mj.startDate AND mj.expiredDate")
	Page<ManageJobs> findByIsDeletedAndStatusAndDateBetween(boolean isDeleted, String status, Date todayDate,
			Pageable pageable);

	public boolean existsByJobTypeAndStartDateAndIsDeletdAndDepartmentAndJobTitle(String jobType, Date startDate,
			boolean b, Department department, Designation jobTitle);

	@Query(" SELECT CASE WHEN COUNT(mj) > 0 THEN true ELSE false END FROM ManageJobs mj WHERE mj.isDeletd = :isDeleted AND mj.jobType = :jobType AND mj.department = :department AND mj.jobTitle = :jobTitle AND ( :startDate BETWEEN mj.startDate AND mj.expiredDate Or :expiredDate BETWEEN mj.startDate AND mj.expiredDate)")
	public boolean existsByJobTypeAndStartDateAndExpiredDateAndIsDeletdAndDepartmentAndJobTitle(String jobType,
			Date startDate, Date expiredDate, boolean isDeleted, Department department, Designation jobTitle);
}
