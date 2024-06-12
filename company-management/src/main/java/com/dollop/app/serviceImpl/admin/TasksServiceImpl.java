package com.dollop.app.serviceImpl.admin;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.dollop.app.constant.AppConstants;
import com.dollop.app.entity.Projects;
import com.dollop.app.entity.Tasks;
import com.dollop.app.entity.Users;
import com.dollop.app.entity.extrapayload.ProjectMemberListForTask;
import com.dollop.app.entity.extrapayload.ProjectResponseList;
import com.dollop.app.entity.payload.TasksRequest;
import com.dollop.app.entity.payload.TasksResponse;
import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.exception.UserNotFoundException;
import com.dollop.app.repository.ProjectRepository;
import com.dollop.app.repository.TasksRepository;
import com.dollop.app.repository.UsersRepository;
import com.dollop.app.service.ITasksService;
import com.dollop.app.utils.PageResponse;
import com.dollop.app.validatorService.AllBasicMethodsReq;

@Service
public class TasksServiceImpl implements ITasksService {
	@Autowired
	private TasksRepository tasksRepository;
	@Autowired
	public ModelMapper modelMapper;

	@Autowired
	public UsersRepository usersRepository;

	@Autowired
	public ProjectRepository proRepository;

	@Autowired
	private AllBasicMethodsReq allBasicMehtods;

	public TasksResponse taskToTasksResponse(Tasks tasks) {
		return this.modelMapper.map(tasks, TasksResponse.class);
	}

	public Tasks taskRequestToTasks(TasksRequest taskRequest) {
		return this.modelMapper.map(taskRequest, Tasks.class);
	}

	@Override
	public TasksResponse addTasks(TasksRequest tasks) {
		this.allBasicMehtods.checkDayAsSundayOrHoliday(tasks.getStartDate());
		this.checkExistanceOfProject(tasks.getProjectId().getId());
		int hours = this.calculateHoursBetweenDates(tasks.getStartDate(), tasks.getDeadline());
		Tasks task = (this.taskRequestToTasks(tasks));
		task.setAssignedHours(hours);
		task.setRemaningHours(hours);

		task = this.tasksRepository.save(task);
		return this.taskToTasksResponse(task);

	}

	@Override
	public TasksResponse updateTasks(TasksRequest tasks) {
		this.checkExistanceOfProject(tasks.getProjectId().getId());
		Tasks task = this.tasksRepository.findById(tasks.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TASK_NOT_FOUND + tasks.getId()));
		task.setDeadline(task.getDeadline());
		task.setDeleted(task.getDeleted());
		task.setDescription(task.getDescription());
		task.setPoints(task.getPoints());
		task.setStartDate(task.getStartDate());
		task.setTitle(task.getTitle());
		task.setTasklabels(task.getTasklabels());
		return this.taskToTasksResponse(this.tasksRepository.save(task));
	}

	@Override
	public Boolean deleteTasks(Long id) {
		Tasks task = this.tasksRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TASK_NOT_FOUND + id));
		task.setDeleted(true);
		this.tasksRepository.save(task);
		return true;
	}

	@Override
	public TasksResponse getTasksById(Long id) {
		Tasks task = this.tasksRepository.findByIdAndDeleted(id, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TASK_NOT_FOUND + id));
		return taskToTasksResponse(task);
	}

	@Override
	public List<TasksResponse> addMultipleTask(List<TasksRequest> task) {
//		this.checkExistanceOfProject(task[0].getProjectId().getId());
		List<Tasks> list = new ArrayList<>();
		task.forEach(a -> {
			list.add(this.taskRequestToTasks(a));
		});

		List<Tasks> saved = this.tasksRepository.saveAll(list);

		return saved.stream().map(a -> this.taskToTasksResponse(a)).collect(Collectors.toList());

	}

	@Override
	public Page<TasksResponse> getAllTasks1(Integer pageNo, Integer pageSize) {
		PageRequest page = PageRequest.of(pageNo, pageSize);
		Page<Tasks> task = this.tasksRepository.findAllByOrderByIdDesc(page);
		List<TasksResponse> list = task.getContent().stream().map(a -> this.taskToTasksResponse(a))
				.collect(Collectors.toList());
		return new PageResponse<>(list, task, list.size());
	}

	@Override
	public Page<TasksResponse> searchTasks(Integer pageNo, Integer pageSize, TasksRequest tasks) {
		// TODO Auto-generated method stub
		return null;
	}

	// get all tasks of project id
	@Override
	public Page<TasksResponse> getAllTasksProjectId(Integer pageNo, Integer pageSize, Integer projectId) {
		this.checkExistanceOfProject(projectId);
		Projects project = new Projects();
		project.setId(projectId);

		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
		Page<Tasks> page = this.tasksRepository.findByProjectIdAndDeleted(pageRequest, project, false);
		return page.map(t -> this.taskToTasksResponse(t));
	}

	@Override
	public Page<TasksResponse> getAllTasksProjectId(Integer pageNo, Integer pageSize, Integer projectId,
			String status) {
		Projects project = new Projects();
		project.setId(projectId);
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
		Page<Tasks> page = this.tasksRepository.findByProjectIdAndDeletedAndStatus(pageRequest, project, false, status);
		return page.map(t -> this.taskToTasksResponse(t));
	}

	@Override
	public Boolean updateStatus(Long id, String status) {
		Tasks task = this.tasksRepository.findByIdAndDeleted(id, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TASK_NOT_FOUND + id));

		task.setStatus(status);
		return this.tasksRepository.save(task) != null ? true : false;
	}


	
	@Override
	public int calculateHoursBetweenDates(Date startDate, Date endDate) {
		int totalHours = 0;

		// Parse input strings to Date objects
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = (startDate);
		Date eDate = (endDate);

		// Loop through each day between the start and end dates
		while (currentDate.compareTo(eDate) <= 0) {
			// Check if the current day is not a Sunday (day 0)
			if (getCurrentDayOfWeek(currentDate) != 0) {
				totalHours += 8; // Add 8 hours for each non-Sunday day
			}

			// Move to the next day
			currentDate.setTime(currentDate.getTime() + 24 * 60 * 60 * 1000);
		}

		return totalHours;
	}

	private int getCurrentDayOfWeek(Date date) {
		// Convert Sunday (day 0) to 7
		int dayOfWeek = date.getDate();
		return (dayOfWeek == 0) ? 7 : dayOfWeek;
	}

	@Override
	public List<ProjectMemberListForTask> findAllUserOfProject(Integer ProjectId) {

		Projects projects = new Projects();
		projects.setId(ProjectId);
		List<Object[]> allprojectMembers = this.tasksRepository.findUserByprojectId(projects.getId());

		List<ProjectMemberListForTask> responseList = allprojectMembers.stream()
				.map(array -> new ProjectMemberListForTask((Integer) array[0], (String) array[1], (String) array[2],
						(String) array[3], (String) array[4])) // adjust the casting
				// based on your actual
				// types
				.collect(Collectors.toList());

		if (responseList.isEmpty()) {
			throw new ResourceNotFoundException(AppConstants.No_MEMBERSFOR_PROJECT);
		}

		return responseList;

	}

	@Override
	public boolean checkExistanceOfProject(Integer projectId) {
		Projects project = this.proRepository.findById(projectId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.PROJECT_NOT_FOUND + projectId));

		return true;
	}

	@Override
	public Page<TasksResponse> getAllAssignedTasksByProjectIdAndCurrentUser(Integer pageNo, Integer pageSize,
			Integer projectId, String userEmail) {
		this.checkExistanceOfProject(projectId);
		Users user=this.usersRepository.findByEmail(userEmail).orElseThrow(()->new ResourceNotFoundException(AppConstants.USER_NOT_FOUND_+userEmail));
		
		Projects project = new Projects();
		project.setId(projectId);

		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
		Page<Tasks> page = this.tasksRepository.findByProjectIdAndDeletedAndCurrentUser(pageRequest, project.getId(), false,user.getId());
		return page.map(t -> this.taskToTasksResponse(t));

	}

}
