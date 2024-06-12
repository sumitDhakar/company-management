package com.dollop.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.dollop.app.entity.GoalType;
import com.dollop.app.entity.payload.GoalTypeRequest;
import com.dollop.app.entity.payload.GoalTypeResponse;
import com.dollop.app.entity.payload.TrainingTypeResponse;

public interface IGoalTypeService {

	 public GoalTypeResponse addGoalType (GoalTypeRequest goalType); 
	   
	   public GoalTypeResponse updateGoalType(GoalTypeRequest goalType);
	   
	   public GoalTypeResponse getGoalTypeById(Integer id);
	   
	   public Page<GoalTypeResponse> getAllGoalType(Integer pageNo,Integer pageSize);
	   
	   public Boolean deleteGoalType(Integer id);
	  
	   public GoalTypeResponse updateGoalTypeStatus(Integer id,String status);
	  
	   public Page<GoalTypeResponse> searchGoalType(Integer pageNo, Integer pageSize, GoalTypeRequest goalType);

	   public List<GoalTypeResponse> getAllGoalTypeList();

	   public Page<GoalTypeResponse> getAllGoalTypeByDeletedAndStatus(Integer pageNo,Integer pageSize);

	
		
}
