package com.dollop.app.entity.payload;

import java.sql.Date;
import java.util.List;

import com.dollop.app.entity.Projects;
import com.dollop.app.entity.TaskMembers;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TasksResponse {

	private Long id;

	private String title;

	private String description;
	

//	private List<String> Tasklabels;

	private Boolean deleted;

	private Date startDate;

	private Date deadline;
	// enum
	private String status;

	private String points;

//	@JsonIgnoreProperties(value= {"projectComments","projectFiles","projectMembers","createdBy","clientId"})
//	private Projects projectId;

//	@ManyToOne
//	private Milestones milestoneId;
//	private Users assignedTo;
//
//	private List<Users> collaborators;
	@JsonIgnoreProperties(value = { "task","members.userRoles","members.designation" })
	private List<TaskMembers> taskMembers;
	private Integer assignedHours;
	
	private Integer remaningHours;
	
}
