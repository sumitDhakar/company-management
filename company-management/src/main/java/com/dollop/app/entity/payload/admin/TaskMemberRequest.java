package com.dollop.app.entity.payload.admin;

import com.dollop.app.entity.Tasks;
import com.dollop.app.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskMemberRequest {
	private Long id;
	private Tasks task;
	
	@JsonIgnoreProperties(value={"designation","userRoles","experienceInformations","familyInformations","educationInformations"})
	private Users members;
	
	private Boolean isLeader=false;

}
