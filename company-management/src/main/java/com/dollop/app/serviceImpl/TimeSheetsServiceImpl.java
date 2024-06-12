package com.dollop.app.serviceImpl;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dollop.app.constant.AppConstants;
import com.dollop.app.entity.Projects;
import com.dollop.app.entity.Tasks;
import com.dollop.app.entity.Taxes;
import com.dollop.app.entity.TimeSheets;
import com.dollop.app.entity.Users;
import com.dollop.app.entity.payload.TimeSheetRequest;
import com.dollop.app.entity.payload.TimeSheetResponse;
import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.exception.ResourcesAlreadyExists;
import com.dollop.app.repository.TasksRepository;
import com.dollop.app.repository.TimeSheetRepository;
import com.dollop.app.repository.UsersRepository;
import com.dollop.app.service.ITimesheetsService;

@Service
public class TimeSheetsServiceImpl implements ITimesheetsService {

	@Autowired
	private TimeSheetRepository timeSheetRepository;
	@Autowired
	private UsersRepository  usersRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private TasksRepository tasksRepository;


	public TimeSheets timeSheetRequestToTimeSheet(TimeSheetRequest timeSheetRequest) {
		return this.modelMapper.map(timeSheetRequest, TimeSheets.class);
	}
	public TimeSheetResponse timeSheettoTimeSheetResponse(TimeSheets timeSheets) {
		return this.modelMapper.map(timeSheets, TimeSheetResponse.class);
	}
	

	@Override
	public TimeSheetResponse addTimeSheet(TimeSheetRequest timeSheets, String userEmailCurrent)  {
		boolean checkEqual = LocalDate.now().equals(timeSheets.getTimeSheetDate());
		if (!checkEqual) {
			throw new ResourcesAlreadyExists("TimeSheet  Should be Of current Date Only");
		}
		


		Users currentUser = this.usersRepository.findByEmailAndDeleted(userEmailCurrent,false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND_ + userEmailCurrent));
		boolean checkExistance = this.timeSheetRepository.existsByTimeSheetDateAndUser(timeSheets.getTimeSheetDate(),currentUser);
		if (checkExistance) {
			throw new ResourcesAlreadyExists("TimeSheet Already Given");
		}
Tasks task = this.tasksRepository.findByIdAndDeleted(timeSheets.getTaskId().getId(),false)
			.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TASK_NOT_FOUND + timeSheets.getTaskId().getId()));		
	if(task.getAssignedHours()>=timeSheets.getHours())
	task.setRemaningHours(task.getRemaningHours()-timeSheets.getHours());	
	this.tasksRepository.save(task);
	TimeSheets timeSheet = this.timeSheetRequestToTimeSheet(timeSheets);
		timeSheet.setUser(currentUser);
		
		return this.timeSheettoTimeSheetResponse(this.timeSheetRepository.save(timeSheet));


	}

	@Override
	public TimeSheetResponse getTimeSheetBy(Integer id) {
		TimeSheets timesheet = this.timeSheetRepository.findByIdAndDeleted(id,false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TIMESHEETS_NOT_FOUND + id));

		return   this.timeSheettoTimeSheetResponse(timesheet);
	}

	@Override
	public TimeSheetResponse updateTimeSheet(TimeSheetRequest timeSheets) {
		TimeSheets timeSheet = this.timeSheetRepository.findById(timeSheets.getId()).orElseThrow(
				() -> new ResourceNotFoundException(AppConstants.TIMESHEETS_NOT_FOUND + timeSheets.getId()));
//		timeSheet.setAssignedHours(timeSheets.getAssignedHours());
		timeSheet.setDescription(timeSheets.getDescription());
//		timeSheet.setHours(timeSheets.getHours());
		timeSheet.setTimeSheetDate(timeSheets.getTimeSheetDate());
		timeSheet.setUser(timeSheets.getUser());
//		timeSheet.setWorkedHours(timeSheets.getWorkedHours());
		
		return this.timeSheettoTimeSheetResponse(timeSheet);
	}

	

	@Override
	public Page<TimeSheetResponse> getAllTimeSheets(Integer pageNo, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "id"));
			
		Page<TimeSheets> timeSheet = this.timeSheetRepository.findAll(pageRequest);	
			return timeSheet.map( sheet -> this.timeSheettoTimeSheetResponse(sheet));	
			}

	@Override
	public Page<TimeSheetResponse> getAllTimeSheetsByClientId(Integer pageNo, Integer pageSize, Integer id) {
//		Clients client = new Clients();
//		client.setId(id);
//		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
//	  Page<TimeSheets> pages = this.timeSheetRepository.findAllByClientId(pageRequest, client);
//		return pages.map(t -> this.timeSheettoTimeSheetResponse(t));
        return null;
	}

	@Override
	public Page<TimeSheetResponse> getAllTimeSheetsByProjectId(Integer PageNo, Integer pageSize, Integer id) {
		Projects project = new Projects();
		project.setId(id);
		PageRequest pageRequest = PageRequest.of(PageNo, pageSize);
		Page<TimeSheets> pages = this.timeSheetRepository.findByProjectId(pageRequest, project);
		return pages.map(t -> this.timeSheettoTimeSheetResponse(t));

	}

	public Page<TimeSheetResponse> getAllTimeSheetsByTaskId(Integer PageNo, Integer pageSize, Long id) {
		Tasks task = new Tasks();
		task.setId(id);
		PageRequest pageRequest = PageRequest.of(PageNo, pageSize);
		Page<TimeSheets> pages =this.timeSheetRepository.findByTaskId(pageRequest, task);
		return pages.map(t -> this.timeSheettoTimeSheetResponse(t));


	}
	@Override
	public Boolean deleteTimeSheet(Integer id) {
		TimeSheets timeSheet= this.timeSheetRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TIMESHEETS_NOT_FOUND + id));

		timeSheet.setDeleted(true);
			this.timeSheetRepository.deleteById(id);
			return true;
			}

}  
