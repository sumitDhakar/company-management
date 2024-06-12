package com.dollop.app.serviceImpl.admin;
import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.constant.AppConstants;
import com.dollop.app.entity.Roles;
import com.dollop.app.entity.UserRegistration;
import com.dollop.app.entity.UserRoles;
import com.dollop.app.entity.Users;
import com.dollop.app.entity.payload.UserRegistrationRequest;
import com.dollop.app.entity.payload.UserRegistrationResponse;
import com.dollop.app.entity.payload.UsersRequest;
import com.dollop.app.entity.payload.UsersResponse;
import com.dollop.app.exception.UserAlreadyPresentException;
import com.dollop.app.exception.UserNotFoundException;
import com.dollop.app.fileUtils.FileService;
import com.dollop.app.listPayloads.UserListResponse;
import com.dollop.app.repository.UserRegistrationRepository;
import com.dollop.app.repository.UserRolesRepository;
import com.dollop.app.repository.UsersRepository;
import com.dollop.app.service.admin.IUsersService;
import com.dollop.app.serviceImpl.AuthenticationServiceImpl;

@Service
@EnableWebSecurity
public class AdminUsersServiceImpl implements IUsersService, UserDetailsService {
	@Autowired
	public UsersRepository usersRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private FileService fileService;

	@Autowired
	private UserRolesRepository userRolesRepository;

	@Autowired
	private AuthenticationServiceImpl registrationServiceForUser;

	@Value("${userprofile.file.path}")
	private String DIRECTORY;

	@Value("${default.user}")
	private String defaultUser;

	public UsersResponse userToUserResponse(Users user) {
		UsersResponse response = this.modelMapper.map(user, UsersResponse.class);
		response.setPassword(null);
		return response;
	}

	public UserListResponse userToUserListResponse(Users user) {
		UserListResponse response = this.modelMapper.map(user, UserListResponse.class);
		return response;
	}

	public Users clientsResponseToUsers(UsersResponse usersResponse) {

		return this.modelMapper.map(usersResponse, Users.class);
	}

	public Users userRequestToUser(UsersRequest usersRequest) {
		return this.modelMapper.map(usersRequest, Users.class);
	}

	// add user
	@Override
	public UsersResponse addUser(UsersRequest users, String role, MultipartFile file) {
		boolean isPresent = this.usersRepository.existsByEmailAndDeleted(users.getEmail(), false);
		if (isPresent) {
			throw new UserAlreadyPresentException(AppConstants.USER_ALREADY_PRESENT + users.getEmail());
		}
		Users user = this.userRequestToUser(users);

		Set<UserRoles> userRoles = new HashSet<>();
		UserRoles userRole = new UserRoles();

		Roles roles = new Roles();
		switch (role) {
		case AppConstants.ADMIN_ROLE:
			roles.setId(AppConstants.ADMIN_ROLE_ID);
			break;
		case AppConstants.EMPLOYEE_ROLE:
			roles.setId(AppConstants.EMPLOYEE_ROLE_ID);
			break;
		case AppConstants.USER_ROLE:
			roles.setId(AppConstants.USER_ROLE_ID);
			break;
		case AppConstants.CLIENT_ROLE:
			roles.setId(AppConstants.CLIENT_ROLE_ID);
		}

		userRole.setRoles(roles);
		userRoles.add(userRole);
		user.setUserRoles(userRoles);
		user.setUserRoles(userRoles);
		UserRegistrationRequest ur = new UserRegistrationRequest(null, user.getEmail(),
				user.getFirstName() + " " + user.getLastName(), users.getPassword(), true, null, null);
		UserRegistrationResponse registration = this.registrationServiceForUser.registration(ur);
		user.setPassword(registration.getPassword());
		if (file != null) {
			String profileName = fileService.uploadFileInFolder(file, AppConstants.USER_ROLE);

			user.setCreatedAt(new Date(System.currentTimeMillis()));
			user.setOriginalName(file.getOriginalFilename());
			user.setProfileName(profileName);

		}

		else {
			user.setOriginalName("profileImage");
			user.setProfileName("defaultUser");
			user.setCreatedAt(Date.valueOf(LocalDate.now()));

		}

		return this.userToUserResponse(this.usersRepository.save(user));

	}

	@Override
	public UsersResponse addUserBasicDetails(UsersRequest users, MultipartFile file) {
		
		Optional<Users> isPresent = this.usersRepository.findByEmailAndDeleted(users.getEmail(),false);
		if (!isPresent.isPresent())
			throw new UserAlreadyPresentException(AppConstants.USER_NOT_FOUND + users.getEmail());
		Users user = isPresent.get();

		user.setDob(users.getDob());
		user.setPhone(users.getPhone());
		user.setAddress(users.getAddress());
		user.setAlternativeAddress(users.getAlternativeAddress());
		user.setFirstName(users.getFirstName());
		user.setLastName(users.getLastName());
		user.setAlternativePhone(users.getAlternativePhone());
		user.setGender(users.getGender());

		if (file != null) {
			String profileName = fileService.uploadFileInFolder(file, AppConstants.USER_ROLE);
			System.err.println("IMAGE :: " + profileName);

			user.setCreatedAt(new Date(System.currentTimeMillis()));
			user.setOriginalName(file.getOriginalFilename());

			user.setProfileName(profileName);

		}

		else {
			user.setOriginalName("profileImage");
			// user.setProfileName("defaultUserImage.png");
		}

		return this.userToUserResponse(this.usersRepository.save(user));

	}

	// update user
	@Override
	public UsersResponse updateUser(UsersRequest users) {
		Users user = this.usersRepository.findByIdAndDeleted(users.getId(),false)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_ + users.getId()));
		user.setAddress(users.getAddress());
		user.setAlternativeAddress(users.getAlternativeAddress());
		user.setDob(users.getDob());
		user.setEmail(users.getEmail());
		user.setGender(users.getGender());
		user.setSkype(users.getSkype());
		user.setSsn(users.getSsn());
		user.setStatus(users.getStatus());
		user.setFirstName(users.getFirstName());

		if (users.getDesignation().getId() > 0)
			user.setDesignation(users.getDesignation());

		user.setStickyNote(users.getStickyNote());
		user.setNote(users.getNote());
		user.setNotificationCheckedAt(users.getNotificationCheckedAt());
		user.setMessageCheckedAt(users.getMessageCheckedAt());
		user.setLastName(users.getLastName());
		user.setJobTitle(users.getJobTitle());
		user.setPhone(users.getPhone());
		user.setIsPrime(users.getIsPrime());
		return this.userToUserResponse(this.usersRepository.save(user));
	}

	// get all users
	@Override
	public Page<UsersResponse> getAllUsers(Integer pageNo, Integer pageSize, Principal p) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "id"));
		Page<Users> page = this.usersRepository.findByDeletedAndEmailNot(pageRequest, false, p.getName());

		return page.map(u -> this.userToUserResponse(u));

	}

	// get all employee
	@Override
	public Page<UserListResponse> getAllUserByRole(Integer pageNo, Integer pageSize, Integer roleId,
			String currentUserEmail) {

		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "id"));
		Roles roles = new Roles();
		roles.setId(roleId);
		Page<Users> page = this.userRolesRepository.getByRoles(pageRequest, roles.getId(), currentUserEmail);
		return page.map(u -> this.userToUserListResponse(u));
	}

	// get all owner
//	public Page<UsersResponse> getAllOwner(Integer pageNo, Integer pageSize) {
//		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
//		Roles roles = new Roles();
//		roles.setId(3);
//		Page<Users> page = this.userRolesRepository.getByRoles(pageRequest,roles.getId());
//		return page.map(u -> this.userToUserResponse(u));
//	}

	@Autowired
	private UserRegistrationRepository registrationRepository;

	// delete user
	@Override
	public Boolean deleteUsers(Integer id) {
		Users user = this.usersRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_ + id));
		user.setDeleted(true);
		UserRegistration regirteredUser = this.registrationRepository.findByEmailAndActive(user.getEmail(), true)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_ + user.getId()));
		regirteredUser.setActive(false);
		this.registrationRepository.save(regirteredUser);
		this.usersRepository.save(user);
		return true;
	}

	// get user by first Name
	@Override
	public UsersResponse getUserByfirstName(String firstName) {
		Users user = this.usersRepository.findByfirstName(firstName)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_ + firstName));

		return this.userToUserResponse(user);

	}

	// get user by job title
	@Override
	public UsersResponse getUserByjobTitle(String jobTitle) {
		Users user = this.usersRepository.findByjobTitle(jobTitle)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_ + jobTitle));
		return this.userToUserResponse(user);
	}

	// get user by id
	@Override
	public UsersResponse getUserById(Integer id) {
		Users user = this.usersRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_ + id));
		return this.userToUserResponse(user);
	}

	// search based on fields
	@Override
	public Page<UsersResponse> searchUser(Integer pageNo, Integer pageSize, UsersRequest user) {
		// setting example
user.setAlternativeAddress(null);//		System.err.println(user.getDesignation());
		if (user.getDesignation().getId() == 0)
			user.setDesignation(null);
		user.setIsAdmin(null);
		user.setIsPrime(null);
		user.setEnableWebNotification(null);
		user.setEnableEmailNotification(null);
		user.setDisableLogin(null);
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues() // ignoring null values of variable
				.withIgnoreCase() // ignoring case of letters
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // contains for string
				.withMatcher("id", match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id))); // for

		Example<Users> example = Example.of(this.userRequestToUser(user), matcher);
		PageRequest pageable = PageRequest.of(pageNo, pageSize);

		Page<Users> page = this.usersRepository.findAll(example, pageable);

		return page.map(u -> this.userToUserResponse(u));

	}

	public List<Object[]> fetchAdminDashboardDetails() {
		LocalDate date = LocalDate.now();
		return this.usersRepository.fetchAdminDashboardDetails();
	}

	// authenticating user
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Users user = this.usersRepository.findByEmailAndDeleted(email, false)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_ + email));
		if (user != null) {
			Set<UserRoles> userRoles = user.getUserRoles();

			List<SimpleGrantedAuthority> authorities = userRoles.stream()
					.map(r -> new SimpleGrantedAuthority(r.getRoles().getTitle())).collect(Collectors.toList());

			return new User(email, user.getPassword(), authorities);
		}
		throw new UserNotFoundException(AppConstants.USER_NOT_FOUND + " " + email);
	}

	public Users findByEmail(String email) {
		return this.usersRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND + email));
	}

	@Override
	public List<UsersResponse> getByUserRole(String role) {
		Integer roleid = this.userRolesRepository.findByTitle(role);
		List<Users> userList = this.userRolesRepository.getUsersByRoles(roleid);
		return userList.stream().map(u -> this.userToUserResponse(u)).collect(Collectors.toList());
	}

	@Override
	public List<UsersResponse> searchUsers(String searchTerm, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<Users> userPage = usersRepository.searchUsers(searchTerm, pageable);
		return userPage.stream().map(u -> this.userToUserResponse(u)).collect(Collectors.toList());
	}

	@Override
	public Page<UsersResponse> getAllUsersForEmployeeList(Integer pageNo, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "id"));
		Page<Users> page = this.usersRepository.findByDeleted(pageRequest, false);

		return page.map(u -> this.userToUserResponse(u));
}

//	@Override
//	public Page<UserListResponse> getAllEmployeeUSerList(Integer pageNo, Integer pageSize) {
//		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
//		Roles roles = new Roles();
//		roles.setId(2);
//		Page<Users> page = this.userRolesRepository.getByRoles(pageRequest,roles.getId());
//		return page.map(u -> this.userToUserListResponse(u));
//
//	}

}