package com.dollop.app.entity.payload;

import java.util.List;

import com.dollop.app.entity.Department;
import com.dollop.app.entity.Users;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmergencyContactRequest {
	
private Integer id;
	@CustomValidator(type = Type.NAME)
	private String name;
	@CustomValidator(type = Type.NAME)
	private String name1;
	@CustomValidator(type = Type.BOTH)
	private String relationship;
	@CustomValidator(type = Type.BOTH)
	private String relationship1;
	@CustomValidator(type = Type.PHONE)
	private String phone;
	
	private Boolean isDelete;
	@CustomValidator(type = Type.PHONE)
	private String phone1;
	@CustomValidator(type = Type.PHONE)
	private String phone2;
//	@CustomValidator(type = Type.PHONE)
	private String Phone3;
	
	private Integer userId ;
	


}
