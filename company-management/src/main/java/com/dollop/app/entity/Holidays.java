package com.dollop.app.entity;


import java.sql.Date;

import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Holidays {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@CustomValidator(type = Type.NAME)
	private String title;
	
	private String day;
	
	private Boolean deleted;
	
	
	private Date holidayDate;
	
	
	
}
