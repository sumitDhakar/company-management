package com.dollop.app.serviceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

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
import com.dollop.app.entity.Promotion;
import com.dollop.app.entity.Users;
import com.dollop.app.entity.payload.PromotionRequest;
import com.dollop.app.entity.payload.PromotionResponse;
import com.dollop.app.exception.EarlyProcessException;
import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.exception.ResourcesAlreadyExists;
import com.dollop.app.repository.PromotionRepository;
import com.dollop.app.repository.UsersRepository;
import com.dollop.app.service.IPromotionService;
import com.dollop.app.validatorService.AllBasicMethodsReq;

@Service
public class PromotionServiceImpl implements IPromotionService {

	@Autowired
	private PromotionRepository promotionRepository;
	@Autowired
	private AllBasicMethodsReq allBasicMehtods;
	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private ModelMapper modelMapper;

	public PromotionResponse promotionToPromotionResponse(Promotion promotion) {
		return this.modelMapper.map(promotion, PromotionResponse.class);
	}

	public Promotion promotionRequestToPromotion(PromotionRequest promotionRequest) {
		return this.modelMapper.map(promotionRequest, Promotion.class);
	}

	@Override
	public PromotionResponse addPromotion(PromotionRequest promotionRequest) {
//		this.allBasicMehtods.checkDayAsSundayOrHoliday(promotionRequest.getPromotionDate());
//		boolean checkEqual = LocalDate.now().equals(promotionRequest.getPromotionDate().toLocalDate());
		boolean checkDesignation = promotionRequest.getDesignationFrom()
				.equals(promotionRequest.getDesignationTo().getTitle());

		if (checkDesignation) {
			throw new ResourcesAlreadyExists("Promotion Should be on Another Designation Not The Same As Current");

		}

		Users user = this.usersRepository.findByIdAndDeleted(promotionRequest.getEmployee().getId(), false)
				.orElseThrow(() -> new ResourceNotFoundException(
						AppConstants.USER_NOT_FOUND + promotionRequest.getEmployee().getId()));
		if (user != null) {
			if (!this.isFirstDateSixMonthsGreater(promotionRequest.getPromotionDate(), user.getCreatedAt())) {
				throw new EarlyProcessException(AppConstants.EARLY_PROCESS_EXCEPTION);
			}

			else {
				Optional<Promotion> optionalPromotion = this.promotionRepository
						.findTopByEmployeeAndIsDeletedOrderByPromotionDateDesc(user, false);
				if (optionalPromotion.isPresent()
						&& !this.isFirstDateSixMonthsGreater(promotionRequest.getPromotionDate(),
								optionalPromotion.get().getPromotionDate())
						&& !promotionRequest.getDesignationTo().getId().equals(user.getDesignation().getId())) {
					throw new EarlyProcessException(AppConstants.EARLY_PROCESS_EXCEPTION);
				} else {
					Promotion promotion = this.promotionRequestToPromotion(promotionRequest);
					promotion.setPromotionDate(Date.valueOf(LocalDate.now()));

					this.allBasicMehtods.checkDayAsSundayOrHoliday(promotion.getPromotionDate());

					user.setDesignation(promotion.getDesignationTo());
					this.usersRepository.save(user);
					return this.promotionToPromotionResponse(this.promotionRepository.save(promotion));
				}
			}
		}

		return null;
	}

	public boolean isFirstDateSixMonthsGreater(Date date1, Date date2) {
		// Convert SQL dates to milliseconds
		long millisDate1 = date1.getTime();
		long millisDate2 = date2.getTime();

		// Calculate the difference in milliseconds
		long timeDiff = millisDate1 - millisDate2;

		// Convert milliseconds to months (approximation)
		long monthsDiff = timeDiff / (30L * 24 * 60 * 60 * 1000);

		// Check if the difference is 6 months or greater
		return monthsDiff >= 6;
	}

	@Override
	public PromotionResponse updatePromotion(PromotionRequest promotionRequest) {
		Promotion promotion = this.promotionRepository.findById(promotionRequest.getId()).orElseThrow(
				() -> new ResourceNotFoundException(AppConstants.PROMOTION_NOT_FOUND + promotionRequest.getId()));
		Users user = this.usersRepository.findByIdAndDeleted(promotion.getEmployee().getId(), false)
				.orElseThrow(() -> new ResourceNotFoundException(
						AppConstants.USER_NOT_FOUND + promotionRequest.getEmployee().getId()));

		if (user != null) {
			if (!this.isFirstDateSixMonthsGreater(promotionRequest.getPromotionDate(), user.getCreatedAt())) {
				throw new EarlyProcessException(AppConstants.EARLY_PROCESS_EXCEPTION);
			} else if (promotion.getDesignationFrom().equals(promotionRequest.getDesignationTo().getTitle())) {
				throw new ResourcesAlreadyExists("Promotion Should be on Another Designation Not The Same As Current");

			}

			else {
				Optional<Promotion> optionalPromotion = this.promotionRepository
						.findTopByEmployeeOrderByPromotionDateDesc(user);
				if (optionalPromotion.isPresent()
						&& !this.isFirstDateSixMonthsGreater(promotionRequest.getPromotionDate(),
								optionalPromotion.get().getPromotionDate())) {
					throw new EarlyProcessException(AppConstants.EARLY_PROCESS_EXCEPTION);
				} else {
					promotion.setDesignationFrom(user.getDesignation().getTitle());
					user.setDesignation(promotion.getDesignationTo());

					this.usersRepository.save(user);
					promotion.setDesignationTo(user.getDesignation());
					promotion.setPromotionDate(promotionRequest.getPromotionDate());
					return this.promotionToPromotionResponse(this.promotionRepository.save(promotion));
				}
			}
		}

		return null;

	}

	@Override
	public Page<PromotionResponse> getAllPromotions(Integer pageNo, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "id"));
		Page<Promotion> page = this.promotionRepository.findByIsDeleted(pageRequest, false);
//	System.out.println(page.getContent());
		return page.map(promotions -> this.promotionToPromotionResponse(promotions));
	}

	@Override
	public Boolean deletePromotion(Integer id) {
		Promotion promotion = this.promotionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.PROMOTION_NOT_FOUND + id));
		promotion.setIsDeleted(true);
		this.promotionRepository.save(promotion);
		return true;
	}

	@Override
	public Page<PromotionResponse> serchPromotions(Integer pageNo, Integer pageSize,
			PromotionRequest promotionRequest) {

		promotionRequest.getEmployee();
		promotionRequest.getEmployee().setIsAdmin(null);
		promotionRequest.getEmployee().setIsPrime(null);
		promotionRequest.getEmployee().setEnableWebNotification(null);
		promotionRequest.getEmployee().setEnableEmailNotification(null);
		promotionRequest.getEmployee().setDisableLogin(null);

		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues() // ignoring null values of variable
				.withIgnoreCase() // ignoring case of letters
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // contains for string
				.withMatcher("id", match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id))) // for
				.withMatcher("designationTo.id",
						match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id))) // for
				.withMatcher("employee.id",
						match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id))); // for

		Example<Promotion> example = Example.of(this.promotionRequestToPromotion(promotionRequest), matcher);
		PageRequest pageable = PageRequest.of(pageNo, pageSize);

		Page<Promotion> page = this.promotionRepository.findAll(example, pageable);

		return page.map(u -> this.promotionToPromotionResponse(u));
	}

	@Override
	public PromotionResponse getPromotionById(Integer promotionId) {
		Promotion promotion = this.promotionRepository.findByIdAndIsDeleted(promotionId, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.PROMOTION_NOT_FOUND + promotionId));
		return this.promotionToPromotionResponse(promotion);
	}

}
