package com.dollop.app.entity.payload;

import java.sql.Date;
import java.util.List;

import org.hibernate.annotations.Cache;

import com.dollop.app.entity.Projects;
import com.dollop.app.entity.TaskMembers;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TasksRequest {
	private Long id;
  @CustomValidator(type = Type.NAME)

	private String title;
  @CustomValidator(type = Type.DESCRIPTION)

	private String description;
	
//@CustomValidator(type = Type.REQUIRED)
	private List<String> Tasklabels;

	private Boolean deleted;

	private Date startDate;

	private Date deadline;
	// enum
	private String status="Progress";
@CustomValidator(type = Type.REQUIRED)
	private String points;

	private Projects projectId;

//	@ManyToOne
//	private Milestones milestoneId;
//	private Users assignedTo;
//
//	private List<Users> collaborators;
	private List<TaskMembers> taskMembers;

	private Integer assignedHours;
	
	private Integer remaningHours;
	

}
