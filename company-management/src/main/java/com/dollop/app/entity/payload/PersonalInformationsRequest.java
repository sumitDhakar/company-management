package com.dollop.app.entity.payload;

import java.util.Date;
import java.util.List;

import com.dollop.app.entity.Department;
import com.dollop.app.entity.Users;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class PersonalInformationsRequest {
	
	private Integer id;
	
	@CustomValidator(type = Type.REQUIRED)
	private String passportNo;

	private Date passportExpiryDate;
	@CustomValidator(type = Type.REQUIRED)
	private String 	nationality;

	@CustomValidator(type = Type.REQUIRED)
	private String 	tel;
	
	@CustomValidator(type = Type.REQUIRED)
	private String 	religion;
	
	@CustomValidator(type = Type.REQUIRED)
	private String 	maritalstatus;
	
	@CustomValidator(type = Type.REQUIRED)
	private String 	employmentOfSpouse;
@Min(value = 0,message = "Children Should be Zero Or More")
@Max(value = 15,message = "Only Tell Us About 15 Children")
	private Integer 	noOfChildren;
	
	private Boolean isDelete;
	
	private Integer user;

}
