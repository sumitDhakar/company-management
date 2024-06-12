package com.dollop.app.entity.payload;

import java.util.Date;

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

public class FamilyInformationsRequest {
	
	private Integer id;
	@CustomValidator(type = Type.NAME)
	private String name;
	@CustomValidator(type = Type.BOTH)
	private String relationship; 
	
	private Date dateOfBirth;
	
	private Boolean isDelete;

	@CustomValidator(type = Type.PHONE)
	private String phone;

}
