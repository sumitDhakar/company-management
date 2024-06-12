package com.dollop.app.service.admin;

import org.springframework.data.domain.Page;
import com.dollop.app.entity.payload.TrainerRequest;
import com.dollop.app.entity.payload.TrainerResponse;
import com.dollop.app.entity.payload.TrainingListResponse;
import com.dollop.app.entity.payload.TrainingTypeResponse;


public interface ITrainersService {
	public TrainerResponse addTrainers(TrainerRequest trainers); 
	   
	   public TrainerResponse getTrainersById(Integer id);
	   
	   public TrainerResponse updateTrainers(TrainerRequest trainers);
	   
	   
	   
	   public Page<TrainerResponse> getAllTrainers(Integer pageNo,Integer pageSize);
	   
	   public Boolean deleteTrainers(Integer id);
	   
	   public Page<TrainerResponse> getAllTrainerByDeletedAndStatus(Integer pageNo,Integer pageSize);
	   
	   
	   public TrainerResponse getTrainingListByStatus(Integer id,String status);
				
	 
}
