
package com.dollop.app.serviceImpl.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.constant.AppConstants;
import com.dollop.app.entity.Clients;
import com.dollop.app.entity.ProjectFiles;
import com.dollop.app.entity.Projects;
import com.dollop.app.entity.Users;
import com.dollop.app.entity.extrapayload.ProjectResponseList;
import com.dollop.app.entity.payload.admin.ProjectForAllResponse;
import com.dollop.app.entity.payload.admin.ProjectRequest;
import com.dollop.app.entity.payload.admin.ProjectResponse;
import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.exception.ResourcesAlreadyExists;
import com.dollop.app.exception.UserNotFoundException;
import com.dollop.app.fileUtils.FileService;
import com.dollop.app.repository.ClientsRepository;
import com.dollop.app.repository.ProjectFileRepository;
import com.dollop.app.repository.ProjectMembersRepository;
import com.dollop.app.repository.ProjectRepository;
import com.dollop.app.repository.TasksRepository;
import com.dollop.app.repository.UsersRepository;
import com.dollop.app.service.admin.IClientsService;
import com.dollop.app.service.admin.IProjectFilesService;
import com.dollop.app.service.admin.IProjectService;
import com.dollop.app.service.admin.IUsersService;
import com.dollop.app.validatorService.AllBasicMethodsReq;

@Service
public class AdminProjectServiceImpl implements IProjectService {

	@Value("${project.file.path}")
	public String DIRECTORY;
	@Autowired

	private AllBasicMethodsReq adAllBasicMethodsReq;

	@Autowired
	private ClientsRepository clientsRepository;

	@Autowired
	private ProjectRepository projectRepository;
	@Autowired
	private TasksRepository tasksRepository;
	@Autowired
	private ProjectMembersRepository projeMembersRepository;

	@Autowired
	private IUsersService iUsersService;

	@Autowired
	private ProjectFileRepository projectFileRepository;

	@Autowired
	private IProjectFilesService filesService;

	@Autowired
	private UsersRepository userRepository;

	@Autowired
	private FileService fileService;

	@Autowired
	private AllBasicMethodsReq allBasicMehtods;

	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private IClientsService clientsService;

	public ProjectResponse projectToProjectResponse(Projects project) {
		// modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		return this.modelMapper.map(project, ProjectResponse.class);
	}

	public Projects projectRequestToProject(ProjectRequest projectRequest) {
		return this.modelMapper.map(projectRequest, Projects.class);
	}

	public ProjectForAllResponse projectToProjectForAllResponse(Projects project) {
		return this.modelMapper.map(project, ProjectForAllResponse.class);
	}

	// add project
	@Override
	public ProjectResponse addProject(ProjectRequest projectRequest, List<MultipartFile> files, String email) {
		this.allBasicMehtods.checkDayAsSundayOrHoliday(projectRequest.getStartDate());
		this.allBasicMehtods.isEndDateGreaterOrNot(projectRequest.getDeadline(), projectRequest.getStartDate());
		this.allBasicMehtods.checkClientExistence(projectRequest.getClientId().getId());

		Clients c = new Clients();
		c.setId(projectRequest.getClientId().getId());
		boolean exists = this.projectRepository.existsByTitleAndClientIdAndDeleted(projectRequest.getTitle().trim(), c,
				false);
		if (exists) {

			throw new ResourcesAlreadyExists("Project Already Exists With This Title And Cleant");
		}
		Projects project = this.projectRequestToProject(projectRequest);
		Users user = this.userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND + email));
		List<ProjectFiles> projectFiles = new ArrayList<>();
		if (files != null)
			files.forEach(a -> {

				ProjectFiles pf = new ProjectFiles();
				pf.setCreatedAt(new Date(System.currentTimeMillis()));
				pf.setOriginalName(a.getOriginalFilename());
				pf.setFileSize((double) a.getSize());
				pf.setUploadedBy(user);

				// system file upload
				// String fileName = StringUtils.cleanPath(pf.getFileName());
				String profileName = fileService.uploadFileInFolde(a, AppConstants.USER_ROLE);
				pf.setFileName(profileName);
				projectFiles.add(pf);
//
			});
		project.setProjectFiles(projectFiles);
		project.setCreatedBy(user);
		project.setPriority("Low");
		project = this.projectRepository.save(project);

		return this.projectToProjectResponse(project);
	}

	// update project
	@Override
	public ProjectResponse updateProject(ProjectRequest projectRequest, List<MultipartFile> files) {
		this.allBasicMehtods.checkClientExistence(projectRequest.getClientId().getId());
		Clients c = new Clients();
		c.setId(projectRequest.getClientId().getId());
		boolean exists = this.projectRepository.existsByTitleAndClientIdAndDeletedAndIdNot(
				projectRequest.getTitle().trim(), c, false, projectRequest.getId());
		if (exists) {

			throw new ResourcesAlreadyExists("Project Already Exists With This Title And Cleant");
		}
		Projects project = this.projectRepository.findById(projectRequest.getId()).orElseThrow(
				() -> new ResourceNotFoundException(AppConstants.PROJECT_NOT_FOUND + projectRequest.getId()));
		project.setClientId(this.clientsService.clientsRequestToClients(projectRequest.getClientId()));
		project.setStartDate(projectRequest.getStartDate());
//		project.setDeadline(projectRequest.getDeadline());
		project.setDescription(projectRequest.getDescription());
		project.setLabels(projectRequest.getLabels());
		project.setPrice(projectRequest.getPrice());
		project.setDeadline(projectRequest.getDeadline());
		project.setTitle(projectRequest.getTitle());
		project.setStatus(projectRequest.getStatus());
		project.setDeleted(false);
		project = this.projectRepository.save(project);

		return this.projectToProjectResponse(project);
	}

	// get project by id
	@Override
	public ProjectResponse getProjectById(Integer id) {
		Projects project = this.projectRepository.findByIdAndDeleted(id, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.PROJECT_NOT_FOUND + id));

		List<ProjectFiles> images = new ArrayList<>();
		List<ProjectFiles> files = new ArrayList<>();

		for (ProjectFiles f : project.getProjectFiles()) {
			if (f.getOriginalName() != null && f.getOriginalName().endsWith(".png")
					|| f.getOriginalName().endsWith(".jpg") || f.getOriginalName().endsWith(".jpeg")) {
				files.add(f);
				images.add(f);
			}
		}
		project.getProjectFiles().removeAll(files);
		List<Object[]> allProjectTaskCounts = tasksRepository.getAllProjectTaskCounts(project.getId());

		ProjectResponse projectResponse = this.projectToProjectResponse(project);
		projectResponse.setImages(images);
		for (Iterator<Object[]> iterator = allProjectTaskCounts.iterator(); iterator.hasNext();) {
			Object[] objects = iterator.next();

			projectResponse.setCompletedTask(Integer.parseInt(objects[0] != null ? objects[0].toString() : "0"));
			projectResponse.setLeftTask(Integer.parseInt(objects[1] != null ? objects[1].toString() : "0"));

			double calculations = 0d;
			Integer sumOfTasks=projectResponse.getCompletedTask() + projectResponse.getLeftTask();
			if (projectResponse.getCompletedTask() > 0
					&& sumOfTasks > 0)
				calculations = this.getCalculations(projectResponse.getCompletedTask(),
						sumOfTasks);
			projectResponse.setProgress(calculations);
		}

		return projectResponse;
	}

	// get all projects
	@Override
	public Page<ProjectResponse> getAllProjects(Integer pageNo, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "id"));
		Page<Projects> page = this.projectRepository.findByDeleted(pageRequest, false);
		return this.addProjectTaskDetails(page);
	}

	@Override
	public Page<ProjectResponse> getAllProjectsByUserId(Integer pageNo, Integer pageSize, Integer id) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);// TODO Auto-generated method stub
		Page<Projects> page = this.projectRepository.getProjectsByProjectMemberUserId(id, pageRequest);
		return this.addProjectTaskDetails(page);
	}

	// get all projects of client by client id
	@Override
	public Page<ProjectResponse> getAllProjectsByClientId(Integer pageNo, Integer pageSize, Integer id) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize);
		Clients client = this.clientsService.clientsResponseToClients(this.clientsService.getClientsById(id));
		Page<Projects> page = this.projectRepository.findByClientIdAndDeleted(pageRequest, client, false);
		return this.addProjectTaskDetails(page);
	}

	// delete project by id
	@Override
	public Boolean deleteProject(Integer id) {
		Projects project = this.projectRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.PROJECT_NOT_FOUND + id));
		project.setDeleted(true);
		this.projectRepository.save(project);
		return true;
	}

	@Override
	public Page<ProjectResponse> getLeadsOfProjects(Integer pageNo, Integer pageSize) {

		return null;
	}

	// for system upload
	public ProjectResponse systemUpload(ProjectRequest projectRequest, List<MultipartFile> files) {
		// save only path of file and save the file in local folder

		for (MultipartFile file : files) {
			String fileName = StringUtils.cleanPath(file.getOriginalFilename());
			Path path = Paths.get(System.getProperty("user.dir") + DIRECTORY, fileName);
			try {
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public ResponseEntity<Resource> downloadFile(Long id) {
		ProjectFiles file = this.projectFileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.FILE_NOT_FOUND + id));

		try {
			Path path = Paths.get(System.getProperty("user.dir") + DIRECTORY).toAbsolutePath().normalize()
					.resolve("flipkart-app.zip"); //

			if (!Files.exists(path))
				throw new ResourceNotFoundException("FIle not Found " + path);
			Resource resource = new UrlResource(path.toUri());
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("File-Name", "example1.zip");
			httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(path)))
					.headers(httpHeaders).body(resource);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Page<ProjectResponse> getAllProjectsByOrderBy() {
		PageRequest pageRequest = PageRequest.of(0, 6, Sort.by("createdDate").descending());
		Page<Projects> page = this.projectRepository.findByDeleted(pageRequest, false);
		return this.addProjectTaskDetails(page);
	}

	@Override
	public List<ProjectResponseList> getAllProjectsByCurrentUser(String email) {
		Users user = this.userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND + email));
		List<Object[]> projectsList = this.projeMembersRepository.findBYUserId(user.getId());
		List<ProjectResponseList> responseList = projectsList.stream()
				.map(array -> new ProjectResponseList((Integer) array[0], (String) array[1])) // adjust the casting
																								// based on your actual
																								// types
				.collect(Collectors.toList());

		if (responseList.isEmpty()) {
			throw new ResourceNotFoundException(AppConstants.NO_PROJECTS_ASSIGNED + "with email => " + email);
		}

		return responseList;
	}

	@Override
	public List<ProjectResponse> searchProjects(ProjectRequest projectRequest) {
		projectRequest.setPrice(null);
		ExampleMatcher exampleMatcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase() // ignoring
																											// case of
																											// letters
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // contains for string
				.withMatcher("id", match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id)));// for

		Example<Projects> proExample = Example.of(this.projectRequestToProject(projectRequest), exampleMatcher);

		List<Projects> projects = this.projectRepository.findAll(proExample);

		return projects.stream().map(u -> this.projectToProjectResponse(u)).collect(Collectors.toList());
	}

	@Override
	public ResponseEntity<?> updateProjectStatus(Integer id, String status, String priority) {
		Map<String, Object> response = new HashMap<>();
		Projects projects = this.projectRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.PROJECT_NOT_FOUND + id));
		if (!priority.isEmpty())
			projects.setPriority(priority);
		if (!status.isEmpty())
			projects.setStatus(status);
		Projects projects2 = this.projectRepository.save(projects);
		if (Objects.nonNull(projects2)) {
			response.put("message", "Update SuccessFully");
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		response.put("message", "Update Failed");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@Override
	public Page<ProjectResponse> addProjectTaskDetails(Page<Projects> page) {
		page.getContent().parallelStream().forEach(a -> {
			a.setProjectFiles(null);

		});
		Page<ProjectResponse> map = page.map(p -> this.projectToProjectResponse(p));
		map.getContent().parallelStream().forEach(a -> {
			List<Object[]> allProjectTaskCounts = tasksRepository.getAllProjectTaskCounts(a.getId());

			for (Iterator<Object[]> iterator = allProjectTaskCounts.iterator(); iterator.hasNext();) {
				Object[] objects = iterator.next();

				a.setCompletedTask(Integer.parseInt(objects[0] != null ? objects[0].toString() : "0"));
				a.setLeftTask(Integer.parseInt(objects[1] != null ? objects[1].toString() : "0"));
				double calculations = 0d;
				if (a.getCompletedTask() > 0 && (a.getCompletedTask() + a.getLeftTask()) > 0)
					calculations = this.getCalculations(a.getCompletedTask(), a.getCompletedTask() + a.getLeftTask());
				a.setProgress(calculations);
			}

		});
		return map;
	}

	public double getCalculations(double value, double total) {
		return Double.parseDouble(String.format("%.2f", (value / total) * 100));
	}
}
