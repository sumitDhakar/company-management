package com.dollop.app.entity.payload;

import java.time.LocalDate;

import com.dollop.app.entity.Projects;
import com.dollop.app.entity.Tasks;
import com.dollop.app.entity.Users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSheetResponse {
	private  Integer id;

	private Integer assignedHour;

	private LocalDate timeSheetDate;
	
	private Integer hours;

	private Integer workedHours;

	public String description;

	private Users   user;

	private Projects projectId;
	
	private Tasks taskId;
	
	private Boolean deleted=false;

}
