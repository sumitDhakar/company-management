package com.dollop.app.entity.payload;

import java.sql.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.dollop.app.entity.ManageJobs;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.annotation.Generated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppliedCandidateRequest {

	private Long id;
@CustomValidator(type = Type.NAME)
	private String candidateName;
@CustomValidator(type = Type.EMAIL)
	private String candidateEmail;
	@CustomValidator(type = Type.DESCRIPTION)
	private String message;
	
	private String originalName;
	
	private ManageJobs manageJobs;
	
	private Date applyDate;
	@CustomValidator(type = Type.PHONE)
	private String mobileNo;
	private String status="Open";

}
