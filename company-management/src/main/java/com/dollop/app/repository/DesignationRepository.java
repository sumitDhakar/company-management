package com.dollop.app.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.entity.Department;
import com.dollop.app.entity.Designation;

public interface DesignationRepository extends JpaRepository<Designation, Integer> {

	public List<Designation> findByDepartment(Department departmentId);

	public List<Designation> findByIsDeleted(Boolean isDeleted);

	public Optional<Designation> findByIdAndIsDeleted(Integer id, boolean b);

	
	public Optional<Designation> findByTitle(String title);

	@Query("SELECT d FROM Designation d WHERE d.isDeleted = false ORDER BY d.id DESC" )	
	public List<Designation> findByIsDeletedOrderByDesignationId();
	 @Query("SELECT COUNT(d) > 0 FROM Designation d WHERE d.title = :Title")
		public boolean existsByTitle( String Title);

	public boolean existsByTitleAndDepartmentAndIsDeleted(String title, Department department, boolean b);

	public boolean existsByTitleAndIsDeletedAndDepartmentAndIdIsNot(String title, boolean b, Department department,
			Integer id);

}
