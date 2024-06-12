package com.dollop.app.entity.payload;

import java.sql.Date;
import com.dollop.app.entity.LeaveTypes;
import com.dollop.app.entity.Users;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveApplicationRequest {
private Integer id;
	
	private Date startDate;
	
	private Date endDate;	
//	@CustomValidator(type = Type.REQUIRED)
	private Double totalHours;
//	@CustomValidator(type = Type.REQUIRED)
	
	private Double totalDays;

	@NotNull(message = "LeaveTypes Is REQUIRED" )
	private LeaveTypes leaveTypeId;

	
	private Users applicantId;

	@CustomValidator(type = Type.DESCRIPTION)
	private String reason;
	
	private Integer createdBy;
	
	private Users checkedBy;
	
	
	private Date createdAt; 
	
	
	private Date checkedAt; 
	//enum
	private String status="Pending"; 
	
	private Boolean deleted=false;
}
