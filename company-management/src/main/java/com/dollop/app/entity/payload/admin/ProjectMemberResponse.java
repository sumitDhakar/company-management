package com.dollop.app.entity.payload.admin;

import com.dollop.app.entity.Projects;
import com.dollop.app.entity.Users;
import com.dollop.app.entity.payload.UsersResponse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberResponse {

	private Long id;
	@JsonIgnoreProperties(value={"projectMembers","projectFiles","projectComments","createdBy","tasks"})
	private Projects projectId;
	
	private Users userId;
	
	private Boolean isLeader;
	 
	private Boolean deleted=false;
}
