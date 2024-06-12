package com.dollop.app.entity.payload;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {
    
	private Integer id;
//	@CustomValidator(type = Type.EMAIL)
	private String email;
//	@CustomValidator(type = Type.NAME)
	private String userName;
//	@CustomValidator(type = Type.PASSWORD)
	private String password;
	
	private Boolean active;
	
	private LocalDateTime generatedTime;
	@NotNull(message = "otp is required")
	private String otp;
	
}
