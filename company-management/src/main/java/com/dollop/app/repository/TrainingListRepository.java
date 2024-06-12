package com.dollop.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.dollop.app.entity.Trainers;
import com.dollop.app.entity.TrainingList;
import com.dollop.app.entity.TrainingType;
import com.dollop.app.entity.Users;

public interface TrainingListRepository extends JpaRepository<TrainingList,Integer>{
	public Page<TrainingList>   findByStatus(Pageable pageble,String status);
	

	public Optional<TrainingList> findByIdAndDeleted(Integer id, boolean b);


	public Page<TrainingList> findByDeleted(PageRequest pageRequest, boolean b);


	public boolean existsByDeletedAndTrainersAndTrainingTypeAndIdNot(boolean b, Trainers trainers,
			TrainingType trainingType, Integer id);


	public boolean existsByDeletedOrTrainersAndTrainingType(boolean b, Trainers trainers, TrainingType trainingType);


	public boolean existsByDeletedAndTrainersAndTrainingType(boolean b, Trainers trainers, TrainingType trainingType);


	public boolean existsByDeletedAndTrainersAndTrainingTypeAndEmployeeId(boolean b, Trainers trainers,
			TrainingType trainingType, Integer employee);


	
}
