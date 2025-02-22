package com.dollop.app.entity.payload;

import java.util.List;

import com.dollop.app.entity.Designation;

import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentRequest {

	private Integer id;
   @CustomValidator(type = Type.NAME)	
	private String title;
	@CustomValidator(type = Type.DESCRIPTION)
	private String description;
	
	private List<Designation> designations ;
	
	private Boolean isActive;
	
	private Boolean isDeleted;

}
