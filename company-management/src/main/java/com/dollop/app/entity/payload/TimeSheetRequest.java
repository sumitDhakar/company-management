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
public class TimeSheetRequest {
	private  Integer id;

	private Integer assignedHour;
	private Integer workedHours;

	private LocalDate timeSheetDate;
	
	private Integer hours;
	

	public String description;


    private Users   user;


private Projects projectId;
	
	private Tasks taskId;
	private Boolean deleted;

}
