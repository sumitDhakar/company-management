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
import com.dollop.app.entity.Resignation;
import com.dollop.app.entity.Users;
import com.dollop.app.entity.payload.ResignationRequest;
import com.dollop.app.entity.payload.ResignationResponse;
import com.dollop.app.exception.EarlyProcessException;
import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.exception.ResourcesAlreadyExists;
import com.dollop.app.repository.ResignationRepository;
import com.dollop.app.repository.UsersRepository;
import com.dollop.app.service.IResignationService;
import com.dollop.app.validatorService.AllBasicMethodsReq;

@Service
public class ResignationServiceImpl implements IResignationService {

	@Autowired
	public ResignationRepository resignationRepository;
	@Autowired
	private AllBasicMethodsReq allBasicMehtods;

	@Autowired
	public UsersRepository usersRepository;

	@Autowired
	private ModelMapper modelMapper;

	public ResignationResponse resignationToResignationResponse(Resignation resignation) {
		return this.modelMapper.map(resignation, ResignationResponse.class);
	}

	public Resignation resignationRequestToResignation(ResignationRequest resignation) {
		return this.modelMapper.map(resignation, Resignation.class);
	}

	@Override
	public ResignationResponse addResignation(String resignationReason, String email) {

		if (this.getResignationByEmployee(email) != null) {
			throw new ResourcesAlreadyExists("You Have Already Applied For Resignation");
		}
		Resignation resignations = new Resignation();
		resignations.setResigning(Date.valueOf(LocalDate.now()));
		this.allBasicMehtods.checkDayAsSundayOrHoliday(resignations.getResigning());
		Users user = this.usersRepository.findByEmailAndDeleted(email, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND_ + email));
		if (user != null) {
			if (!this.isFirstDateSixMonthsGreater(resignations.getResigning(), user.getCreatedAt(), 6l)) {
				throw new EarlyProcessException(AppConstants.EARLY_PROCESS_EXCEPTION);
			}

			else {
				resignations.setDepartment(user.getDesignation().getDepartment().getTitle());
				resignations.setReason(resignationReason);
				resignations.setEmployee(user);
				resignations.setIsDelete(false);
//				user.setDeleted(true);
//				this.usersRepository.save(user);
				return this.resignationToResignationResponse(this.resignationRepository.save(resignations));
			}
		}
		return null;
	}

	public boolean isFirstDateSixMonthsGreater(Date date1, Date date2, long difference) {
		// Convert SQL dates to milliseconds
		long millisDate1 = date1.getTime();
		long millisDate2 = date2.getTime();

		// Calculate the difference in milliseconds
		long timeDiff = millisDate1 - millisDate2;

		// Convert milliseconds to months (approximation)
		long monthsDiff = timeDiff / (30L * 24 * 60 * 60 * 1000);

		// Check if the difference is 6 months or greater
		return monthsDiff >= difference;
	}

	@Override
	public ResignationResponse updateResignation(ResignationRequest resignation) {
		Resignation resignations = this.resignationRepository.findById(resignation.getId()).orElseThrow(
				() -> new ResourceNotFoundException(AppConstants.RESIGNATIONS_NOT_FOUND + resignation.getId()));

		Users user = this.usersRepository.findByIdAndDeleted(resignation.getEmployee().getId(), false).orElseThrow(
				() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + resignation.getEmployee().getId()));
		if (user != null) {

			Boolean check = resignation.getNoticeDate().after(resignations.getResigning());
			System.err.println(check);
			System.err.println(check);
			System.err.println(check);
			if (!check) {
				throw new EarlyProcessException(AppConstants.EARLY_PROCESS_EXCEPTION);
			}

			resignations.setNoticeDate(resignation.getNoticeDate());
			return this.resignationToResignationResponse(this.resignationRepository.save(resignations));
		}
		return null;
	}

	@Override
	public ResignationResponse getResignationById(Integer id) {
		Resignation resignation = this.resignationRepository.findByIdAndIsDelete(id, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.RESIGNATIONS_NOT_FOUND + id));
		return this.resignationToResignationResponse(resignation);

	}

	@Override
	public Page<ResignationResponse> getAllResignation(Integer pageNo, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "id"));
		Page<Resignation> page = this.resignationRepository.findByIsDelete(pageRequest, false);

		return page.map(c -> this.resignationToResignationResponse(c));
	}

	@Override
	public Boolean deleteResignation(Integer id) {
		Resignation resignation = this.resignationRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.RESIGNATIONS_NOT_FOUND + id));
		resignation.setIsDelete(true);
		Users user = this.usersRepository.findByIdAndDeleted(resignation.getEmployee().getId(), true).orElseThrow(
				() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + resignation.getEmployee().getId()));
		user.setDeleted(false);
		this.usersRepository.save(user);
		this.resignationRepository.save(resignation);
		return true;

	}

	@Override
	public Page<ResignationResponse> searchResignation(Integer pageNo, Integer pageSize,
			ResignationRequest resignationRequest) {
		resignationRequest.getEmployee().setDesignation(null);
		resignationRequest.getEmployee();
		resignationRequest.getEmployee().setIsAdmin(null);
		resignationRequest.getEmployee().setIsPrime(null);
		resignationRequest.getEmployee().setEnableWebNotification(null);
		resignationRequest.getEmployee().setEnableEmailNotification(null);
		resignationRequest.getEmployee().setDisableLogin(null);

		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues() // ignoring null values of variable
				.withIgnoreCase() // ignoring case of letters
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // contains for string
				.withMatcher("id", match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id)))// for
				.withMatcher("employee.id",
						match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id)));

		// for

		Example<Resignation> example = Example.of(this.resignationRequestToResignation(resignationRequest), matcher);
		PageRequest pageable = PageRequest.of(pageNo, pageSize);
		Page<Resignation> page = this.resignationRepository.findAll(example, pageable);

		return page.map(u -> this.resignationToResignationResponse(u));
	}

	@Override
	public ResignationResponse getResignationByEmployee(String cEmail) {
		Users user = this.usersRepository.findByEmailAndDeleted(cEmail, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND_ + cEmail));
		Resignation resignationOfCurrentEmployee = this.resignationRepository.findByEmployee(user);
		if (resignationOfCurrentEmployee != null) {
			ResignationResponse resignationToResignationResponse = this
					.resignationToResignationResponse(resignationOfCurrentEmployee);
			return resignationToResignationResponse;
		}
		return null;
//		return false;
	}

}
