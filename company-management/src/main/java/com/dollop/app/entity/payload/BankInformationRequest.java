package com.dollop.app.entity.payload;

import com.dollop.app.entity.Users;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class BankInformationRequest {
	
private Integer id;
	@CustomValidator(type = Type.NAME)
	private String bankName;
	@NotNull(message ="Bank Account noumber is requrd")
	@Min(value = 111111111)

	private Long bankAccountNo;
	
	private Boolean isDelete;
	@CustomValidator(type = Type.REQUIRED)
	private String ifscCode;
	@CustomValidator(type = Type.REQUIRED)
	private String panNo;
	private Integer userId ;
}
