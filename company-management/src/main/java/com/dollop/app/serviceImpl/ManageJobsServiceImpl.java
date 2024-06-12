package com.dollop.app.serviceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dollop.app.constant.AppConstants;
import com.dollop.app.entity.ManageJobs;
import com.dollop.app.entity.payload.ManageJobsRequest;
import com.dollop.app.entity.payload.ManageJobsResponse;
import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.exception.ResourcesAlreadyExists;
import com.dollop.app.repository.ManageJobsRepository;
import com.dollop.app.service.IManageJobsService;

@Service
public class ManageJobsServiceImpl implements IManageJobsService {

	@Autowired
	public ManageJobsRepository manageJobsRepository;

	@Autowired
	private ModelMapper modelMapper;

	public ManageJobsResponse manageJobsToManageJobsResponse(ManageJobs manageJobs) {
		return this.modelMapper.map(manageJobs, ManageJobsResponse.class);
	}

	public ManageJobs manageJobsRequestToManageJobs(ManageJobsRequest manageJobsRequest) {
		return this.modelMapper.map(manageJobsRequest, ManageJobs.class);
	}

	@Override
	public ManageJobsResponse addManageJobs(ManageJobsRequest mj) {
		if(this.manageJobsRepository.existsByJobTypeAndStartDateAndIsDeletdAndDepartmentAndJobTitle(mj.getJobType(),mj.getStartDate(),false,mj.getDepartment(),mj.getJobTitle())) {
			throw new ResourcesAlreadyExists("You Have Already Created This Job On This Date");
		}
		
		else if(this.manageJobsRepository.existsByJobTypeAndStartDateAndExpiredDateAndIsDeletdAndDepartmentAndJobTitle(mj.getJobType(),mj.getStartDate(),mj.getExpiredDate(),false,mj.getDepartment(),mj.getJobTitle())) {
			throw new ResourcesAlreadyExists("You Have Already Created This Job On This Date Or between This Dates");
		}
		
		ManageJobs manageJobs = this.manageJobsRequestToManageJobs(mj);
		
		return this.manageJobsToManageJobsResponse(this.manageJobsRepository.save(manageJobs));

	}

	@Override
	public ManageJobsResponse updateManageJobs(ManageJobsRequest manageJobsRequest) {
		ManageJobs manageJobs = this.manageJobsRepository.findById(manageJobsRequest.getId())
				.orElseThrow(() -> new ResourceNotFoundException(
						AppConstants.MANAGE_JOBS_LIST_NOT_FOUND + manageJobsRequest.getId()));
		manageJobs.setAge(manageJobsRequest.getAge());
		manageJobs.setDepartment(manageJobsRequest.getDepartment());
		manageJobs.setDescription(manageJobsRequest.getDescription());
		manageJobs.setExperience(manageJobsRequest.getExperience());
		manageJobs.setIsDeletd(manageJobsRequest.getIsDeletd());
		manageJobs.setJobLocation(manageJobsRequest.getJobLocation());
		manageJobs.setJobTitle(manageJobsRequest.getJobTitle());
		manageJobs.setJobType(manageJobsRequest.getJobType());
		manageJobs.setNoOfVacancies(manageJobsRequest.getNoOfVacancies());
		manageJobs.setSalaryFrom(manageJobsRequest.getSalaryFrom());
		manageJobs.setSalaryTo(manageJobsRequest.getSalaryTo());
		manageJobs.setStartDate(manageJobsRequest.getStartDate());
		manageJobs.setStatus(manageJobsRequest.getStatus());

		return this.manageJobsToManageJobsResponse(this.manageJobsRepository.save(manageJobs));

	}

	@Override
	public ManageJobsResponse getManageJobsById(Integer id) {

		ManageJobs manageJobs = this.manageJobsRepository.findByIdAndIsDeletd(id, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.MANAGE_JOBS_LIST_NOT_FOUND + id));
		return this.manageJobsToManageJobsResponse(manageJobs);
	}

	@Override
	public Page<ManageJobsResponse> getAllManageJobs(Integer pageNo, Integer pageSize, String forType) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "id"));
		Page<ManageJobs> manageJobData = null;
		if (forType.equals("manageJobs"))
			manageJobData = this.manageJobsRepository.findByIsDeletd(pageRequest, false);
		else if ((forType.equals("applyJob")))
			manageJobData = this.manageJobsRepository.findByIsDeletedAndStatusAndDateBetween( false,"Open",Date.valueOf(LocalDate.now()),pageRequest);
		return manageJobData.map(c -> this.manageJobsToManageJobsResponse(c));
	}

	@Override
	public Boolean deleteManageJobs(Integer id) {

		ManageJobs manageJobs = this.manageJobsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.MANAGE_JOBS_LIST_NOT_FOUND + id));
		manageJobs.setIsDeletd(true);
		this.manageJobsRepository.save(manageJobs);
		return true;
	}

	@Override
	public ManageJobsResponse updateManageJobsStatus(Integer id, String status, String ofType) {
		ManageJobs manageJobs = this.manageJobsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.MANAGE_JOBS_LIST_NOT_FOUND + id));

		if (ofType.equals("status")) {
			manageJobs.setStatus(status);
		} else if (ofType.equals("jobType")) {
			manageJobs.setJobType(status);
		} else {
			throw new ResourceNotFoundException(AppConstants.MANAGE_JOBS_LIST_NOT_FOUND + id);
		}

		return this.manageJobsToManageJobsResponse(this.manageJobsRepository.save(manageJobs));

	}

	@Override
	public ManageJobsResponse updateManageJobType(Integer id, String jobType) {
		ManageJobs manageJobs = this.manageJobsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.MANAGE_JOBS_LIST_NOT_FOUND + id));
		manageJobs.setStatus(jobType);
		return this.manageJobsToManageJobsResponse(this.manageJobsRepository.save(manageJobs));

	}

	@Override
	public Page<ManageJobsResponse> searchManageJobs(Integer pageNo, Integer pageSize,
			ManageJobsRequest manageJobsRequest) {
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues() // ignoring null values of variable
				.withIgnoreCase() // ignoring case of letters
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // contains for string
				.withMatcher("id", match -> match.transform(value -> value.map(id -> ((Long) id == 0) ? null : id)))// for
				.withMatcher("amount", match -> match.transform(value -> value.map(id -> ((Long) id == 0) ? null : id)))
				.withMatcher("assetUser.id",
						match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id))); // for

		Example<ManageJobs> example = Example.of(this.manageJobsRequestToManageJobs(manageJobsRequest), matcher);
		PageRequest pageable = PageRequest.of(pageNo, pageSize);

		Page<ManageJobs> page = this.manageJobsRepository.findAll(example, pageable);

		return page.map(u -> this.manageJobsToManageJobsResponse(u));
	}

	@Override
	public ResponseEntity<?> updateManageJobViews(Integer id) {
		Map<String, String> response = new HashMap<>();
		ManageJobs manageJobs = this.manageJobsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.MANAGE_JOBS_LIST_NOT_FOUND + id));
		manageJobs.setNoOfViews(manageJobs.getNoOfViews() + 1);
		if (this.manageJobsRepository.save(manageJobs) != null) {
			response.put("message", "Views Updated");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		response.put("message", "something went wrong");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}
}
