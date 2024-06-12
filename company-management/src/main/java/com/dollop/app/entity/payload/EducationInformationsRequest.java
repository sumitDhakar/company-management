package com.dollop.app.entity.payload;

import java.sql.Date;

import com.dollop.app.entity.EducationInformations;
import com.dollop.app.entity.Users;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor


public class EducationInformationsRequest {
private Integer id;

@CustomValidator(type = Type.NAME)
private String institution;
	@CustomValidator(type = Type.NAME)
	private String subject;
	private Date startingDate;
	
	private Boolean isDelete;
	private Date completeDate;
	@CustomValidator(type = Type.REQUIRED)
	private String degree;
	@CustomValidator(type = Type.REQUIRED)
	private String grade;
	
	


}
