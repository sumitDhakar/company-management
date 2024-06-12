package com.dollop.app.entity.payload;

import java.sql.Date;

import com.dollop.app.entity.Users;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverTimeRequest {

	private Integer id;

	
	private Integer userId;

	private Date overTimeDate;
	 @NotNull(message = "overTimeHours must not be null")
	 @Max(value=5,message = "overTimeHours must be 5")
	
	 @Min(value = 0, message = "overTimeHours must be a positive number")
	private Double overTimeHours;
	 
	 
		
	private String overTimeType;
	
	private Integer approvedBy;
	
	@CustomValidator(type = Type.REQUIRED)
	private String description;
	
	private Boolean isDeleted=false;
	private String status="Pending";
}
