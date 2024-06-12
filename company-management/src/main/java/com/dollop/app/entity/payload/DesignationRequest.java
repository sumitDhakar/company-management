package com.dollop.app.entity.payload;

import java.util.List;

import com.dollop.app.entity.Department;
import com.dollop.app.entity.Users;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesignationRequest {
	private Integer id;
	@CustomValidator(type = Type.NAME)
	private String title;
@NotNull(message = "Not Null Department")
	private Department department;
private Boolean isDeleted=false;

	}
