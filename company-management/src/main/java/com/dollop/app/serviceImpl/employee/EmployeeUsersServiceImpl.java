package com.dollop.app.serviceImpl.employee;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.constant.AppConstants;
import com.dollop.app.entity.ProjectFiles;
import com.dollop.app.entity.Users;
import com.dollop.app.entity.payload.UsersRequest;
import com.dollop.app.entity.payload.UsersResponse;
import com.dollop.app.exception.ResourcesAlreadyExists;
import com.dollop.app.exception.UserNotFoundException;
import com.dollop.app.fileUtils.FileService;
import com.dollop.app.repository.UsersRepository;
import com.dollop.app.service.employee.IUsersService;
import com.dollop.app.validatorService.AllBasicMethodsReq;

@Service
public class EmployeeUsersServiceImpl implements IUsersService {
	@Autowired
	public UsersRepository usersRepository;
	@Autowired
	public AllBasicMethodsReq allBasicMethodsReq;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${userprofile.file.path}")
	private String DIRECTORY;
	
	@Value("${default.user}")
	private String defaultUser;
	
	

	@Autowired
	private FileService fileService;

	public UsersResponse userToUserResponse(Users user) {
		return this.modelMapper.map(user, UsersResponse.class);
	}

	public Users userRequestToUser(UsersRequest usersRequest) {
		return this.modelMapper.map(usersRequest, Users.class);
	}

	// add employee
	@Override
	public UsersResponse addUser(UsersRequest users, MultipartFile profileImage) {

		Users user = this.userRequestToUser(users);
		if (profileImage != null) {
			String uploadFileInFolder = fileService.uploadFileInFolder(profileImage,AppConstants.USER_ROLE);
			System.err.println("IMAGE :: "+uploadFileInFolder);
			user.setCreatedAt(new Date(System.currentTimeMillis()));
		this.allBasicMethodsReq.checkDayAsSundayOrHoliday(user.getCreatedAt());
			user.setOriginalName(profileImage.getOriginalFilename());
		
			user.setProfileName(UUID.randomUUID().toString() + profileImage.getOriginalFilename());

			// system file upload
			String fileName = StringUtils.cleanPath(user.getProfileName());

			Path path = Paths.get(System.getProperty("user.dir") + DIRECTORY, fileName);
			try {
				Files.copy(profileImage.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		else {
			user.setOriginalName("profileImage");
			user.setProfileName("default.user");
		}
		return this.userToUserResponse(this.usersRepository.save(user));

	}

	// get employee by id
	@Override
	public UsersResponse getUsersById(Integer id) {
		Users user = this.usersRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_ + id));
		return this.userToUserResponse(user);
	}

	// update employee
	@Override
	public UsersResponse updateUser(UsersRequest users,MultipartFile profileImage ) {
			Users user = this.usersRepository.findByIdAndDeleted(users.getId(),false)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_ + users.getId()));
			 boolean emailExists =this.usersRepository.existsByEmailAndDeletedAndIdNot(users.getEmail(),false, users.getId());
		        if (emailExists) {
		        	 throw new ResourcesAlreadyExists("this Email is Already Exists");   
		        }
		user.setAddress(users.getAddress());
		user.setAlternativeAddress(users.getAlternativeAddress());
		user.setDob(users.getDob());
		user.setEmail(users.getEmail());
		user.setGender(users.getGender());
		user.setSsn(users.getSsn());
		user.setFirstName(users.getFirstName());
		user.setLastName(users.getLastName());
		user.setPhone(users.getPhone());
		user.setAlternativePhone(users.getAlternativePhone());
		user.setAddress(users.getAddress());
		
			if (profileImage != null) {
				String profileName= fileService.uploadFileInFolder(profileImage, AppConstants.EMPLOYEE_ROLE);
				//System.err.println("IMAGE :: "+profileName);
				
				user.setCreatedAt(new Date(System.currentTimeMillis()));
				user.setOriginalName(profileImage.getOriginalFilename());
				

				user.setProfileName(profileName);

			}
			
		return this.userToUserResponse(this.usersRepository.save(user));
	}

	@Override
	public Page<UsersResponse> searchUsers(int pageNO, int pageSize, UsersRequest usersRequest) {
		// TODO Auto-generated method stub
		return null;
	}


	
}