package com.dollop.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dollop.app.entity.Department;
import com.dollop.app.entity.Policy;

public interface PolicyRepository extends JpaRepository<Policy, Integer>{


	public Boolean existsByNameAndDepartmentAndIsDeleted(String name, Department department, boolean b);

	
	public Boolean existsByNameAndIsDeletedAndDepartmentAndIdIsNot(String name, boolean b, Department department,
			Integer id);
	
	

}
