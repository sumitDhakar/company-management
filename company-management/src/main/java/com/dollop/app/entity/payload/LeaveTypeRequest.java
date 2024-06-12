package com.dollop.app.entity.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeRequest {
	
	private Integer id;
	//enum
	private String status;
	// colum text
	private String title;

	


	private Boolean deleted;
}
