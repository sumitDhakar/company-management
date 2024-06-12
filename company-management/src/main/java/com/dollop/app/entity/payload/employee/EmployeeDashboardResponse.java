package com.dollop.app.entity.payload.employee;

import java.util.List;

import com.dollop.app.entity.Holidays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeDashboardResponse {
	
	private Double totalProjects;
	
	private Double LeaveTaken;
	
	private Double totalPendingTask;
	
	private Double Remaining;
	
	private Double TotalTasks;
	
	private Double Remaining1;
	private Double overTimeHours;
	
	
	private Long Approved;
	private Double takenLeaves;
	
	
	private List<Holidays> upCommingHolidays;
	
	
	 
	   
}
