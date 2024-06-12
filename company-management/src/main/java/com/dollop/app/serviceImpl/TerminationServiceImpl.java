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
import com.dollop.app.entity.Termination;
import com.dollop.app.entity.payload.TerminationRequest;
import com.dollop.app.entity.payload.TerminationResponse;
import com.dollop.app.entity.payload.UsersResponse;
import com.dollop.app.exception.EarlyProcessException;
import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.repository.TerminationRepository;
import com.dollop.app.repository.TerminationTypeRepository;
import com.dollop.app.service.ITerminationService;
import com.dollop.app.validatorService.AllBasicMethodsReq;
import com.dollop.app.service.admin.IUsersService;

@Service
public class TerminationServiceImpl implements ITerminationService {

	@Autowired
	public TerminationRepository terminationRepository;
	
	@Autowired
	private AllBasicMethodsReq allBasicMehtods;


	@Autowired
	public IUsersService usersService;

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	public TerminationTypeRepository terminationTypeRepository;

	public TerminationResponse terminationToTerminationResponse(Termination termination) {
		return this.modelMapper.map(termination, TerminationResponse.class);
	}

	public Termination terminationRequestToTermination(TerminationRequest terminationRequest) {
		return this.modelMapper.map(terminationRequest, Termination.class);
	}

	@Override
	public TerminationResponse addTermination(TerminationRequest terminationRequest) {
		
		UsersResponse user = this.usersService.getUserById(terminationRequest.getEmployee().getId());
		if (user != null) {
			Termination terminationReques = this.terminationRequestToTermination(terminationRequest);
			terminationReques.setTerminationType(this.terminationTypeRepository
					.findByIdAndIsDelete(terminationRequest.getTerminationType().getId(), false)
					.orElseThrow(() -> new ResourceNotFoundException("Termination Type  Not Found")));
			user.setDeleted(true);
			terminationReques.setTerminationDate(Date.valueOf(LocalDate.now()));
			this.allBasicMehtods.checkDayAsSundayOrHoliday(terminationReques.getTerminationDate());
			this.usersService.deleteUsers(user.getId());
			return this.terminationToTerminationResponse(this.terminationRepository.save(terminationReques));
		}
		return null;
	}
	private boolean isFirstDateSixMonthsGreater(Date date1, Date date2, long l) {
		// Convert SQL dates to milliseconds
		long millisDate1 = date1.getTime();
		long millisDate2 = date2.getTime();

		// Calculate the difference in milliseconds
		long timeDiff = millisDate1 - millisDate2;

		// Convert milliseconds to months (approximation)
		long monthsDiff = timeDiff / (30L * 24 * 60 * 60 * 1000);

		// Check if the difference is 6 months or greater
		return monthsDiff >= l;
	}

	@Override
	public TerminationResponse updateTermination(TerminationRequest terminationRequest) {
		Termination terminationReque = this.terminationRepository.findById(terminationRequest.getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						AppConstants.TERMINATION_LIST_NOT_FOUND + terminationRequest.getId()));

		terminationReque.setDepartment(terminationRequest.getDepartment());
		if (!this.isFirstDateSixMonthsGreater(terminationRequest.getTerminationDate(),
				terminationReque.getEmployee().getCreatedAt(), 6l)) {
			throw new EarlyProcessException(AppConstants.EARLY_PROCESS_EXCEPTION);
		}
		if (!this.isFirstDateSixMonthsGreater(terminationRequest.getNoticeDate(),
				terminationRequest.getTerminationDate(), 1l)) {
			throw new EarlyProcessException(AppConstants.Notice_Date_Exception);
		}

		terminationReque.setReason(terminationRequest.getReason());
		terminationReque.setTerminationDate(terminationRequest.getTerminationDate());
		terminationReque.setTerminationType(terminationRequest.getTerminationType());

		return this.terminationToTerminationResponse(this.terminationRepository.save(terminationReque));

	}

	@Override
	public TerminationResponse getTerminationById(Integer id) {
		Termination terminationReques = this.terminationRepository.findByIdAndIsDelete(id, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TERMINATION_LIST_NOT_FOUND + id));
		return this.terminationToTerminationResponse(terminationReques);

	}

	@Override
	public Page<TerminationResponse> getAllTermination(Integer pageNo, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "id"));
				Page<Termination> page = this.terminationRepository.findByIsDelete(pageRequest, false);

		return page.map(c -> this.terminationToTerminationResponse(c));

	}

	@Override
	public Boolean deleteTermination(Integer id) {
		Termination terminationReques = this.terminationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TERMINATION_LIST_NOT_FOUND + id));
		terminationReques.setIsDelete(true);
		this.terminationRepository.save(terminationReques);
//		Users user = this.usersRepository.findByIdAndDeleted(terminationReques.getEmployee().getId(), true)
//				.orElseThrow(() -> new ResourceNotFoundException(
//						AppConstants.USER_NOT_FOUND + terminationReques.getEmployee().getId()));
//		user.setDeleted(false);
//		this.usersRepository.save(user);
		
		return true;
	}

	@Override
	public Page<TerminationResponse> searchTermination(Integer pageNo, Integer pageSize,
			TerminationRequest terminationRequest) {
		// goalList.setGoalType(null);
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues() // ignoring null values of variable
				.withIgnoreCase() // ignoring case of letters
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // contains for string
				.withMatcher("id", match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id)))// for
				.withMatcher("goalType.id",
						match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id))); // for

		Example<Termination> example = Example.of(this.terminationRequestToTermination(terminationRequest), matcher);
		PageRequest pageable = PageRequest.of(pageNo, pageSize);

		Page<Termination> page = this.terminationRepository.findAll(example, pageable);

		return page.map(u -> this.terminationToTerminationResponse(u));
	}

}
