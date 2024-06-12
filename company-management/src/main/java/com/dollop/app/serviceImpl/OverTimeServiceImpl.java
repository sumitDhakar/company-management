package com.dollop.app.serviceImpl;

import java.sql.Date;
import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dollop.app.constant.AppConstants;
import com.dollop.app.entity.OverTime;
import com.dollop.app.entity.Users;
import com.dollop.app.entity.payload.OverTimeRequest;
import com.dollop.app.entity.payload.OverTimeresponse;
import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.exception.ResourcesAlreadyExists;
import com.dollop.app.repository.OverTimeRepository;
import com.dollop.app.repository.UsersRepository;
import com.dollop.app.service.IOverTimeService;
import com.dollop.app.validatorService.AllBasicMethodsReq;

@Service
public class OverTimeServiceImpl implements IOverTimeService {

	@Autowired
	private OverTimeRepository overTimeRepository;
	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private AllBasicMethodsReq allBasicMehtods;

	public OverTimeresponse overTimeToOverTimeResponse(OverTime overTime) {
		return this.modelMapper.map(overTime, OverTimeresponse.class);
	}

	public OverTime overTimeRequestToOverTime(OverTimeRequest overTimeRequest) {
		return this.modelMapper.map(overTimeRequest, OverTime.class);
	}

	@Override
	public OverTimeresponse createOverTime(OverTimeRequest overTimeRequest, String currentUser) {
//		boolean checkEqual = LocalDate.now().equals(overTimeRequest.getOverTimeDate().toLocalDate());
//		if (!checkEqual) {
//			throw new ResourcesAlreadyExists("OverTime Date Should be on current Date Only");
//		}

		Users overTimeUser = this.usersRepository.findByEmailAndDeleted(currentUser, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + currentUser));
		boolean checkExistance = this.overTimeRepository
				.existsByOverTimeDateAndUserId(overTimeRequest.getOverTimeDate(), overTimeUser);
		if (checkExistance) {
			throw new ResourcesAlreadyExists("OverTime Already Applied");
		}
		OverTime overtime = overTimeRequestToOverTime(overTimeRequest);
		overtime.setOverTimeDate(Date.valueOf(LocalDate.now()));
		this.allBasicMehtods.checkDayAsSundayOrHoliday(overtime.getOverTimeDate());
		
		overtime.setUserId(overTimeUser);
		overtime.setStatus("Pending");
		overtime = this.overTimeRepository.save(overtime);

		return this.overTimeToOverTimeResponse(overtime);

	}

	@Override
	public Page<OverTimeresponse> getAllOverTime(Integer pageIndex, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by(Direction.DESC, "id"));
		Page<OverTime> page = this.overTimeRepository.findByIsDeleted(pageRequest, false);
		return page.map(ot -> this.overTimeToOverTimeResponse(ot));
	}

	@Override
	public String deleteOvertime(Integer id) {
		OverTime overtime = this.overTimeRepository.findByIdAndIsDeleted(id, false);

		if (overtime != null) {
			overtime.setIsDeleted(true);
			return "OverTime Update Succesfully With ID => " + this.overTimeRepository.save(overtime).getId();

		}
		return "No OverTime Found With this Id ";
	}

	@Override
	public OverTimeresponse getOverTimeById(Integer id) {
		OverTime overtime = this.overTimeRepository.findByIdAndIsDeleted(id, false);
		return this.overTimeToOverTimeResponse(overtime);
	}

	@Override
	public OverTimeresponse updateOverTime(OverTimeRequest overTimeRequest) {
		// overTimeRequest.setIsDeleted(false);
		OverTime overtime = overTimeRepository.findById(overTimeRequest.getId()).get();
		overtime.setIsDeleted(false);
		overtime.setDescription(overTimeRequest.getDescription());
		overtime.setStatus(overTimeRequest.getStatus());
		overtime.setOverTimeDate(overTimeRequest.getOverTimeDate());
		overtime.setOverTimeHours(overTimeRequest.getOverTimeHours());
		overtime = this.overTimeRepository.save(overtime);

		return this.overTimeToOverTimeResponse(overtime);
	}

	@Override
	public Page<OverTimeresponse> searchOverTime(Integer pageIndex, Integer pageSize, OverTimeRequest overTimeRequest) {
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues() // ignoring null values of variable
				.withIgnoreCase() // ignoring case of letters
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // contains for string
				.withMatcher("id", match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id))) // for
				.withMatcher("approvedBy.id",
						match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id))) // for
				.withMatcher("overTimeHours",
						match -> match.transform(value -> value.map(id -> ((Long) id == 0) ? null : id))) // for
				.withMatcher("userId.id",
						match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id))); // for
		// for

		Example<OverTime> example = Example.of(this.overTimeRequestToOverTime(overTimeRequest), matcher);
		PageRequest pageable = PageRequest.of(pageIndex, pageSize);

		Page<OverTime> page = this.overTimeRepository.findAll(example, pageable);

		return page.map(ot -> this.overTimeToOverTimeResponse(ot));
	}

	@Override
	public Object getTotalOverTimeHours(LocalDate startDate, LocalDate endDate) {
		Object data = overTimeRepository.findTotalWorkedHoursBetweenDates(startDate, endDate);

		return data;
	}

	@Override
	public OverTimeresponse OverTimeStatus(Integer id, String status, String currentUser) {
		Users approveBy = this.usersRepository.findByEmailAndDeleted(currentUser, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + currentUser));

		OverTime over = this.overTimeRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.OVER_LIST_NOT_FOUND + id));
		over.setStatus(status);
		over.setApprovedBy(approveBy);
		return this.overTimeToOverTimeResponse(this.overTimeRepository.save(over));

	}

	@Override
	public Page<OverTimeresponse> getAllOverTimeOfCurrentUser(Integer pageIndex, Integer pageSize, String currentUser) {
		Users user = this.usersRepository.findByEmailAndDeleted(currentUser, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + currentUser));
		PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by(Direction.DESC, "id"));
		Page<OverTime> page = this.overTimeRepository.findByIsDeletedAndUserId(pageRequest, false, user);
		return page.map(ot -> this.overTimeToOverTimeResponse(ot));

	}

}
