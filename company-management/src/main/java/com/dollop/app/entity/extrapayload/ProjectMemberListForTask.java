package com.dollop.app.entity.extrapayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectMemberListForTask {

	private Integer id;

	private String firstName;

	private String lastName;
	private String email;
	private String profileName;
	
	
	
}
