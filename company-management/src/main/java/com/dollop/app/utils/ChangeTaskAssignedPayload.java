package com.dollop.app.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeTaskAssignedPayload {

	private Long taskId;
	private Integer userId;
	
}
