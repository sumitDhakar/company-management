package com.dollop.app.entity.payload;

import java.sql.Date;
import java.util.List;

import com.dollop.app.entity.Department;
import com.dollop.app.entity.Designation;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManageJobsRequest {

	private Integer id;
	@NotNull(message = "jobTitle must not be null")

	private Designation jobTitle;
	@NotNull(message = "department must not be null")

	private Department department;

	@CustomValidator(type = Type.REQUIRED)
	private String jobLocation;

	@NotNull(message = "noOfVacancies must not be null")
	@Min(value = 0, message = "no Of Vacancies must be a positive number")
	@Max(value = 50, message = "no Of Vacancies less than shoud be 50")
	private Integer noOfVacancies;
	@NotNull(message = "experience must not be null")
	@Min(value = 0, message = "experience must be a positive number")
	@Max(value = 35, message = "experience less than shoud be 35")

	private String experience;
	@NotNull(message = "age must not be null")
	@Min(value = 15, message = "age garate than shoud be 15")
	@Max(value = 65, message = "age less than shoud be 65")

	private Integer age;
	@NotNull(message = "salaryFrom must not be null")
	@Min(value = 8000, message = "salaryFrom must be a 8000 salary from")

	private Double salaryFrom;

	@NotNull(message = "salaryFrom must not be null")
	@Min(value = 8000, message = "salaryFrom must be a 8000 salary from")

	private Double salaryTo;

	private String jobType;

	private String status;

	private Date startDate;

	private Date expiredDate;

	private Boolean isDeletd;

	@CustomValidator(type = Type.DESCRIPTION)
	private String description;

}
