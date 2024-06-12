package com.dollop.app.entity.payload.admin;

import com.dollop.app.entity.Roles;
import com.dollop.app.entity.Users;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleRequest {
private Integer id;
	

	private Integer roleId;
	

	private Integer userId;

}
