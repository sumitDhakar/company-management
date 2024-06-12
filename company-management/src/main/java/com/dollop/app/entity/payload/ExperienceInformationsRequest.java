package com.dollop.app.entity.payload;

import java.sql.Date;

import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExperienceInformationsRequest {

	private Integer id;
@CustomValidator(type = Type.NAME)
	private String companyName;
@CustomValidator(type = Type.BOTH)
	private String location;
@CustomValidator(type = Type.BOTH)
	private String jobPosition;

	private Date periodFrom;

	private Boolean isDelete;

	private Date periodTo;
}
