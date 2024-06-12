package com.dollop.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.entity.Department;
import com.dollop.app.entity.Taxes;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

	public Optional<Department> findByTitle(String dept);

	public List<Department> findByIsDeleted(Boolean isDeleted);

	public Optional<Department> findByIdAndIsDeleted(Integer id, boolean b);

	public boolean existsByTitle(String title);
	@Query("SELECT d FROM Department d WHERE d.isDeleted = false ORDER BY d.id DESC" )
    List<Department> findByIsDeletedOrderByDepartmentId();

	public Boolean existsByTitleAndIsDeletedOrIdNot(String trim, boolean b, Integer id);

	  @Query("SELECT d FROM Department d WHERE (d.title=:title) AND d.isDeleted =:b AND d.id!=:id")
			public Department  existsByTitleAndIsDeletedAndIdIsNot(String title, boolean b,Integer id);

	public boolean existsByTitleAndIsDeleted(String title, boolean b);
		
}
