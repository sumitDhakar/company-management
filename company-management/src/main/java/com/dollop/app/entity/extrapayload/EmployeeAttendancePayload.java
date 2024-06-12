package com.dollop.app.entity.extrapayload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class EmployeeAttendancePayload {

	
	private String firstInTime;
	private String lastOutTime;
	private String totalHours;
	private String extraWorkedHours;
	private String breakHours;
}
