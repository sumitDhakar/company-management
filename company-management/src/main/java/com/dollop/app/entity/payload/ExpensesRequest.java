package com.dollop.app.entity.payload;

import java.sql.Date;
import java.util.List;

import com.dollop.app.entity.ExpenseCategories;
import com.dollop.app.entity.ExpensesFiles;
import com.dollop.app.entity.GoalList;
import com.dollop.app.entity.Projects;
import com.dollop.app.entity.Users;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ExpensesRequest {
	private Integer id;

	private Date expenseDate;
	@CustomValidator(type = Type.DESCRIPTION)
	private String description;
	@NotNull(message = "amount must not be null")
	@Min(value = 0, message = "amount must be a positive number")
	@Max(value = 99000)
	private Double amount;

	private List<ExpensesFiles> expensesFiles;
	// colum textexpensesFiles
	@CustomValidator(type = Type.REQUIRED)
	private String title;
	@NotNull(message = "User Is required")
	private Users userId;

	private Boolean deleted;

	private String status;

	@CustomValidator(type = Type.REQUIRED)
	private String paidBy;
}
