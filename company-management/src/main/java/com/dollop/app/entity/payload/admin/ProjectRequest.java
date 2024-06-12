package com.dollop.app.entity.payload.admin;

import java.sql.Date;
import java.util.List;

import com.dollop.app.entity.Tasks;
import com.dollop.app.entity.payload.ClientsRequest;
import com.dollop.app.entity.payload.UsersRequest;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {
	private Integer id;
	
//	@CustomValidator(type = Type.NAME)
	private String title;
//	@CustomValidator(type = Type.DESCRIPTION)
	private String description;
	//@CustomValidator
	private String priority;
	
	private List<String> labels;
	
	private String starred_by;
	
	private Date deadline;
	
	private Date startDate;
	
	private Date createdDate;
	
	private ClientsRequest clientId;
	
	private UsersRequest createdBy;
	
	private String status="Progress";
	
	private Double price;
	
	private String projectType;
		
	@JsonIgnoreProperties(value= {"projectId"})
	private List<ProjectMemberRequest> projectMembers;
	
	@JsonIgnoreProperties(value= {"projectId"})
	private List<ProjectFilesRequest> projectFiles;

	@JsonIgnoreProperties(value= {"projectId"})
	private List<CommentsRequest> projectComments;
	
	private Boolean deleted;

}
