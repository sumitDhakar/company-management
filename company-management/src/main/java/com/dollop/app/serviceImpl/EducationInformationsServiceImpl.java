package com.dollop.app.serviceImpl;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dollop.app.constant.AppConstants;
import com.dollop.app.entity.EducationInformations;
import com.dollop.app.entity.Users;
import com.dollop.app.entity.payload.EducationInformationsRequest;
import com.dollop.app.entity.payload.EducationInformationsResponse;
import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.repository.EducationInformationsRepository;
import com.dollop.app.repository.UsersRepository;
import com.dollop.app.service.IEducationInformationsService;

@Service
public class EducationInformationsServiceImpl implements IEducationInformationsService{

	


	@Autowired
	public EducationInformationsRepository educationInformationsRepository;
	@Autowired
	public UsersRepository usersRepository;

	@Autowired
	private ModelMapper modelMapper;

	public EducationInformationsResponse educationInformationsToEducationInformationsResponse(EducationInformations educationInformations) {
		return this.modelMapper.map(educationInformations, EducationInformationsResponse.class);
	}

	public EducationInformations educationInformationsRequestToEducationInformations(EducationInformationsRequest educationInformationsRequest) {
		return this.modelMapper.map(educationInformationsRequest, EducationInformations.class);
	}
	
	
	int count=0;
	@Override
	public List<EducationInformationsResponse> addEducationInformations(List<EducationInformationsRequest> educationInformationsRequest,Integer userId) {
		educationInformationsRequest.stream().forEach(m->{
	Date startDate=	educationInformationsRequest.get(count).getStartingDate();
	Date completeDate=	educationInformationsRequest.get(count).getCompleteDate();
	 if (completeDate.before(startDate)) {
        throw new ResourceNotFoundException("cant fill complete date before start date");
	 }
	  count++;
		});
		Users users = this.usersRepository.findByIdAndDeleted(userId, false)
		.orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + userId));
   List<EducationInformationsResponse> list = new ArrayList<>(); 
				educationInformationsRequest.stream().forEach(m->{
//					m.setUser(users);
					EducationInformations educationInformations = this.educationInformationsRequestToEducationInformations(m);
					educationInformations.setUser(users);
					list.add(this.educationInformationsToEducationInformationsResponse(this.educationInformationsRepository.save(educationInformations)));
				});
//		return this.educationInformationsToEducationInformationsResponse(this.educationInformationsRepository.save(educationInformations));
	return list;
	}

	@Override
	public List<EducationInformationsResponse> getAllEducationInformations(Integer userProfileId) {
		List<EducationInformations> educationInformations = this.educationInformationsRepository.findByUserAndIsDelete(new Users(userProfileId),false);
		 

		
		return 	educationInformations.stream()
			    .map(ef -> this.educationInformationsToEducationInformationsResponse(ef))
			    .collect(Collectors.toList());}

	@Override
	public EducationInformationsResponse getEducationInformationsById(Integer educationInformationsId) {
		EducationInformations educationInformations = this.educationInformationsRepository.findByIdAndIsDelete(educationInformationsId, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.EDUCATION_INFORMATIONS + educationInformationsId));
		return this.educationInformationsToEducationInformationsResponse(educationInformations);
	}

}
