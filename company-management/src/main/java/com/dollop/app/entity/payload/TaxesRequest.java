package com.dollop.app.entity.payload;

import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class TaxesRequest {

	private Integer id;
@CustomValidator(type = Type.NAME)
	private String title;
@NotNull(message = "percentage must not be null")
@Min(value = 0, message = "percentage must be a positive number")
@Max(value = 20, message = "percentage must be less than 20")
	private Double percentage;

	private Boolean deleted;
	
	private String status;
}
