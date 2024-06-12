package com.dollop.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.entity.ModulePermissions;

public interface ModulePermissionsRepository extends JpaRepository<ModulePermissions, Integer>{

	
	@Query(value="SELECT m.id, m.name\r\n"
			+ "FROM module_permissions AS m\r\n"
			+ "LEFT JOIN permissions AS p ON m.id = p.module_permissions_id AND p.role_permissions = :rId\r\n"
			+ "GROUP BY m.id, m.name;",nativeQuery = true)
	List<ModulePermissions>  getModulePermissionByRoleId(Integer rId);
	
	 
}
