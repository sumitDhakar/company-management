package com.dollop.app.entity.payload;

import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
@CustomValidator(type = Type.EMAIL)
	private String email;
@CustomValidator(type = Type.PASSWORD)
	private String password;
	
}
