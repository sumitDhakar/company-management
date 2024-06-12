package com.dollop.app.serviceImpl.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.constant.AppConstants;
import com.dollop.app.entity.Clients;

import com.dollop.app.entity.Projects;
import com.dollop.app.entity.UserRoles;
import com.dollop.app.entity.Users;
import com.dollop.app.entity.payload.ClientsRequest;
import com.dollop.app.entity.payload.ClientsResponse;
import com.dollop.app.entity.payload.UsersRequest;
import com.dollop.app.entity.payload.UsersResponse;
import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.exception.UserNotFoundException;
import com.dollop.app.fileUtils.FileService;
import com.dollop.app.repository.ClientsRepository;
import com.dollop.app.repository.UserRolesRepository;
import com.dollop.app.repository.UsersRepository;
import com.dollop.app.service.admin.IClientsService;

@Service
public class ClientsServiceImpl implements IClientsService {

	@Autowired
	public ClientsRepository clientsRepository;
	@Autowired
	public UsersRepository usersRepository;

	@Autowired
	public UserRolesRepository rolesRepository;
	@Autowired
	private ModelMapper modelMapper;

	@Value("${userprofile.file.path}")
	private String DIRECTORY;
	

	@Autowired
	private FileService fileService;


	public ClientsResponse clientsToClientsResponse(Clients clients) {
		return this.modelMapper.map(clients, ClientsResponse.class);
	}

	public Clients clientsRequestToClients(ClientsRequest clientsRequest) {
		return this.modelMapper.map(clientsRequest, Clients.class);
	}

	public Clients clientsResponseToClients(ClientsResponse clientsResponse) {
		return this.modelMapper.map(clientsResponse, Clients.class);
	}

	// add client
	@Override
	public ClientsResponse addClients(ClientsRequest clients, MultipartFile profileImage) {
	
        Boolean clientExists = clientsRepository.existsByCompanyNameAndEmailAndDeleted(clients.getCompanyName(), clients.getEmail(),false);
        if (clientExists) {
            throw new ResourceNotFoundException("Client with the same company name and email already exists");
        }
		Clients client=this.clientsRequestToClients(clients);
		
		

		if (profileImage != null) {
			String profileName= fileService.uploadFileInFolder(profileImage, AppConstants.CLIENT_ROLE);
			System.err.println("IMAGE :: "+profileName);
			
			client.setCreatedAt(new Date(System.currentTimeMillis()));
			client.setOriginalName(profileImage.getOriginalFilename());
			

			client.setProfileName(profileName);

		}

		else {
			client.setOriginalName("profileImage");
			client.setProfileName("defaultUserImage.png");
			client.setCreatedAt(Date.valueOf(LocalDate.now()));
			
		}	
		return this.clientsToClientsResponse(this.clientsRepository.save(client));
	}

	// update Client
	@Override
	public ClientsResponse updateClients(ClientsRequest clients ,MultipartFile profileImage) {
		Clients client = this.clientsRepository.findById(clients.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.CLIENTS_NOT_FOUND + clients.getId()));
        Clients clientExists = clientsRepository.existsByCompanyNameOrEmailAndDeletedAndIdIsNot(clients.getCompanyName(),clients.getEmail(),false,client.getId());
        if (clientExists!=null) {
            throw new ResourceNotFoundException("Client with the same company name and email already exists");
        }

		client.setAddress(clients.getAddress());
		client.setCity(clients.getCity());
		client.setCompanyName(clients.getCompanyName());
		client.setCountry(clients.getCountry());
		client.setCreatedDate(clients.getCreatedDate());
		client.setCurrency(clients.getCurrency());
		client.setCurrencySymbol(clients.getCurrencySymbol());
		client.setDeleted(clients.getDeleted());
		client.setDisableOnlinePayment(clients.getDisableOnlinePayment());
		client.setPhone(clients.getPhone());
		client.setStarredBy(clients.getStarredBy());
		client.setState(clients.getState());
		client.setVatNumber(clients.getVatNumber());
		client.setWebsite(clients.getWebsite());
		client.setZip(clients.getZip());
		client.setDob(clients.getDob());
		client.setEmail(clients.getEmail());
		client.setGender(clients.getGender());
		client.setFirstName(clients.getFirstName());
		client.setLastName(clients.getLastName());
		client.setPassword(clients.getPhone());
		client.setCreatedAt(clients.getCreatedAt());
		
		client.setClientGroups(clients.getClientGroups());
		//client.setOwner(clients.getOwner());
		

		if (profileImage != null) {
			String profileName= fileService.uploadFileInFolder(profileImage, AppConstants.CLIENT_ROLE);
			System.err.println("IMAGE :: "+profileName);
			
			client.setCreatedAt(new Date(System.currentTimeMillis()));
			client.setOriginalName(profileImage.getOriginalFilename());
			

			client.setProfileName(profileName);

		}

		else {
			client.setOriginalName("profileImage");
			//client.setProfileName("defaultUserImage.png");
		}					
		return this.clientsToClientsResponse(this.clientsRepository.save(client));

	}
	
	
	
	

	// get client by id
	@Override
	public ClientsResponse getClientsById(Integer id) {
		Clients client = this.clientsRepository.findByIdAndDeleted(id, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.CLIENTS_NOT_FOUND + id));
		return this.clientsToClientsResponse(client);

	}

	// get all Clients
	@Override
	public Page<ClientsResponse> getAllClients(Integer pageNo, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize,Sort.by(Direction.DESC, "id"));
		Page<Clients> page = this.clientsRepository.findByDeleted(pageRequest, false);

		return page.map(c -> this.clientsToClientsResponse(c));
	}

	// delete Client
	@Override
	public Boolean deleteClients(Integer id) {
		
		Clients client = this.clientsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.CLIENTS_NOT_FOUND + id));
		client.setDeleted(true);
		this.clientsRepository.save(client);
		return true;
	}

	// update Client status
	@Override
	public ClientsResponse updateClientStatus(Integer id, String status) {
		Clients client = this.clientsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.CLIENTS_NOT_FOUND + id));
		client.setStatus(status);
		return this.clientsToClientsResponse(this.clientsRepository.save(client));

	}

	// search client based on fields
	@Override
	public Page<ClientsResponse> searchClient(Integer pageNo, Integer pageSize, ClientsRequest client) {
//		
		System.err.println(client);
		client.setStatus(null);
		client.setDisableOnlinePayment(null);
		client.setLabels(null);
		client.setCreatedDate(null);
		client.setDeleted(false);
		client.setId(null);
		client.setProfileName(null);
		client.setOriginalName(null);
		client.setProjects(null);
	
	
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues() // ignoring null values of variable
				.withIgnoreCase() // ignoring case of letters
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // contains for string
				.withMatcher("id", match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id)))
		.withMatcher("projects.id", match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id))); 
																												// default
																												// value
																												// ignore
																												// of
																												// variable

		Example<Clients> example = Example.of(this.clientsRequestToClients(client), matcher);
		PageRequest pageable = PageRequest.of(pageNo, pageSize);
		Page<Clients> page = this.clientsRepository.findAll(example, pageable);
		System.out.println(page);

		return page.map(c -> this.clientsToClientsResponse(c));
	}

	@Override
	public Page<ClientsResponse> getAllClientsOrderby() {
		PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("createdDate").descending());
		Page<Clients> page = this.clientsRepository.findByDeleted(pageRequest, false);

		return page.map(c -> this.clientsToClientsResponse(c));

	}

}
