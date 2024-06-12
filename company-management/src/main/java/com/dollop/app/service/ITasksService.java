package com.dollop.app.service;

import java.sql.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import com.dollop.app.entity.Users;
import com.dollop.app.entity.extrapayload.ProjectMemberListForTask;
import com.dollop.app.entity.payload.LeaveApplicationRequest;
import com.dollop.app.entity.payload.LeaveApplicationResponse;
import com.dollop.app.entity.payload.TasksRequest;
import com.dollop.app.entity.payload.TasksResponse;

public interface ITasksService {

	public TasksResponse addTasks(TasksRequest tasks);

	public List<TasksResponse> addMultipleTask(List<TasksRequest> task);

	public TasksResponse updateTasks(TasksRequest tasks);
	
	public TasksResponse getTasksById(Long id);

	public Page<TasksResponse> getAllTasks1(Integer pageNo, Integer pageSize);

	public Boolean deleteTasks(Long id);
	public Boolean updateStatus(Long id,String status);
	
	

	
	public Page<TasksResponse> getAllTasksProjectId(Integer pageNo,Integer pageSize,Integer projectId,String status);
	public Page<TasksResponse> getAllTasksProjectId(Integer pageNo,Integer pageSize,Integer projectId);
	public Page<TasksResponse> getAllAssignedTasksByProjectIdAndCurrentUser(Integer pageNo,Integer pageSize,Integer projectId,String email);

	public Page<TasksResponse> searchTasks(Integer pageNo, Integer pageSize, TasksRequest tasks);

	

	int calculateHoursBetweenDates(Date startDate, Date endDate);
	
	public List<ProjectMemberListForTask> findAllUserOfProject(Integer ProjectId);
	

	public  boolean checkExistanceOfProject(Integer  projectId); 
	
}
