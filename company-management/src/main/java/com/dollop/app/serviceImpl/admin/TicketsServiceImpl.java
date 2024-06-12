
package com.dollop.app.serviceImpl.admin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.constant.AppConstants;
import com.dollop.app.entity.ProjectFiles;
import com.dollop.app.entity.Tickets;
import com.dollop.app.entity.TicketsFiles;
import com.dollop.app.entity.Users;
import com.dollop.app.entity.payload.TicketsRequest;
import com.dollop.app.entity.payload.TicketsResponse;
import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.exception.ResourcesAlreadyExists;
import com.dollop.app.exception.UserNotFoundException;
import com.dollop.app.fileUtils.FileService;
import com.dollop.app.repository.ClientsRepository;
import com.dollop.app.repository.TicketTypeRepository;
import com.dollop.app.repository.TicketsFileRepository;
import com.dollop.app.repository.TicketsRepository;
import com.dollop.app.repository.UsersRepository;
import com.dollop.app.service.admin.ITicketsService;
import com.dollop.app.validatorService.AllBasicMethodsReq;

@Service
public class TicketsServiceImpl implements ITicketsService {

	@Autowired
	public TicketsRepository ticketsRepository;
	
	@Autowired
	private AllBasicMethodsReq allBasicMehtods;

	@Autowired
	public TicketsFileRepository ticketsFileRepository;

	@Autowired
	public UsersRepository userService;

	@Autowired
	public ClientsRepository clientService;

	@Autowired
	private TicketTypeRepository ticketTypeService;

	@Autowired
	private FileService fileService;

	@Value("${tickets.file.path}")
	public String DIRECTORY;

	@Autowired
	private ModelMapper modelMapper;

	public TicketsResponse ticketsToTicketsResponse(Tickets tickets) {
		return this.modelMapper.map(tickets, TicketsResponse.class);
	}

	public Tickets ticketsRequestToTickets(TicketsRequest ticketsRequest) {
		return this.modelMapper.map(ticketsRequest, Tickets.class);
	}

	// update tickets
	@Override
	public TicketsResponse updateTickets(TicketsRequest tickets) {
		
		
		boolean isExists = this.ticketsRepository.existsByTitleAndRequestedByAndDeletedAndClientAndIdNot(
				tickets.getTitle(), tickets.getRequestedBy(), false, tickets.getClient(), tickets.getId());
		if (isExists) {
			throw new ResourcesAlreadyExists("Ticket With This Title ,Client Or Requested By Already present ");
		}
		Tickets ticket = this.ticketsRepository.findById(tickets.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TICKETS_NOT_FOUND + tickets.getId()));
		ticket.setClient(tickets.getClient());
		ticket.setLastActivityAt(new Date(System.currentTimeMillis()));
		ticket.setStatus(tickets.getStatus());
		ticket.setTicketTypeId(tickets.getTicketTypeId());
		ticket.setDescription(tickets.getDescription());
		ticket.setTitle(tickets.getTitle());
		ticket.setRequestedBy(tickets.getRequestedBy());
		ticket.setLabels(tickets.getLabels());
		return this.ticketsToTicketsResponse(this.ticketsRepository.save(ticket));
	}

	// tickets get all
	@Override
	public Page<TicketsResponse> getAllTickets(Integer pageNo, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "id"));
		Page<Tickets> page = this.ticketsRepository.findByDeleted(pageRequest, false);
		page.getContent().parallelStream().forEach(a -> {
			a.setTicketsFiles(null);
		});

		return page.map(t -> this.ticketsToTicketsResponse(t));

	}

	// tickets get by id
	@Override
	public TicketsResponse getTicketsById(Integer id) {
		Tickets ticket = this.ticketsRepository.findByIdAndDeleted(id, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TICKETS_NOT_FOUND + id));
		List<TicketsFiles> images = new ArrayList<>();
		List<TicketsFiles> files = new ArrayList<>();

		if (ticket.getTicketsFiles() != null)
			for (TicketsFiles f : ticket.getTicketsFiles()) {
				if (f.getFileName().contains(".png") || f.getFileName().contains(".jpg")
						|| f.getFileName().contains(".jpeg")) {
					images.add(f);
				} else {
					files.add(f);
				}
			}
		TicketsResponse ticketsResponse = this.ticketsToTicketsResponse(ticket);
		ticketsResponse.setImages(images);
		ticketsResponse.setTicketsFiles(files);

		return ticketsResponse;
	}

	// tickets delete by id
	@Override
	public Boolean deleteTickets(Integer id) {
		Tickets ticket = this.ticketsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TICKETS_NOT_FOUND + id));
		ticket.setDeleted(true);
		this.ticketsRepository.save(ticket);
		return true;
	}

	@Override
	public Page<TicketsResponse> searchTickets(Integer pageNo, Integer pageSize, TicketsRequest tickets) {

		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues() // ignoring null values of variable
				.withIgnoreCase() // ignoring case of letters
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) // contains for string
				.withMatcher("id", match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id))) // for
				.withMatcher("clientId.id",
						match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id)))
				.withMatcher("ticketsFiles.TicketsFiles.id",
						match -> match.transform(value -> value.map(id -> ((Long) id == 0) ? null : id)))
				.withMatcher("ticketTypeId.id",
						match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id)))
				.withMatcher("assigned.id",
						match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id)))
				.withMatcher("createdBy.id",
						match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id)))
				.withMatcher("requestedBy.id",
						match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id)))
				.withMatcher("followers.Users.id",
						match -> match.transform(value -> value.map(id -> ((Integer) id == 0) ? null : id)));
		// default
		// value
		// ignore
		// of
		// variable
		Tickets exampleTickets = this.ticketsRequestToTickets(tickets);
//if(tickets.getAssigned()!=null&&tickets.getAssigned()>0) {
//	Users user =new Users();
//	user.setId(tickets.getAssigned());
//	exampleTickets.setAssigned(user);
//}
		Example<Tickets> example = Example.of(exampleTickets, matcher);
		PageRequest pageable = PageRequest.of(pageNo, pageSize);
		Page<Tickets> page = this.ticketsRepository.findAll(example, pageable);

		return page.map(a -> this.ticketsToTicketsResponse(a));
	}

	// create ticket
	@Override
	public TicketsResponse addTickets(TicketsRequest ticketsRequest, List<MultipartFile> files, String email) {
		
		boolean isExists = this.ticketsRepository.existsByTitleAndRequestedByAndDeletedAndClient(
				ticketsRequest.getTitle(), ticketsRequest.getRequestedBy(), false, ticketsRequest.getClient());
		if (isExists) {
			throw new ResourcesAlreadyExists("Ticket With This Title ,Client Or Requested By Already present ");
		}
		Users createdBy = this.userService.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.USER_NOT_FOUND + email));

		Tickets tickets = this.ticketsRequestToTickets(ticketsRequest);

		tickets.setCreatedBy(createdBy);
		tickets.setTicketsFiles(this.createTicketsFiles(files, createdBy, null));
		tickets = this.ticketsRepository.save(tickets);

		return this.ticketsToTicketsResponse(tickets);

	}

	@Override
	public Boolean deleteTicketFilesByTicketFileId(Long id) {
		TicketsFiles ticketsFiles = this.ticketsFileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.PROJECT_FILE_NOT_FOUND + id));

		this.ticketsFileRepository.delete(ticketsFiles);

		Path path = Paths.get(System.getProperty("user.dir") + DIRECTORY).toAbsolutePath().normalize()
				.resolve(ticketsFiles.getFileName()); //
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public ResponseEntity<Resource> downloadFile(Long id) {
		TicketsFiles file = this.ticketsFileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.FILE_NOT_FOUND + id));
		try {
			Path path = Paths.get(System.getProperty("user.dir") + DIRECTORY).toAbsolutePath().normalize()
					.resolve(file.getFileName()); //

			if (!Files.exists(path))
				throw new ResourceNotFoundException(AppConstants.FILE_NOT_FOUND + path);
			Resource resource = new UrlResource(path.toUri());
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.add("File-Name", file.getOriginalName());
			httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;File-Name=" + file.getOriginalName());
			return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(path)))
					.headers(httpHeaders).body(resource);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public TicketsResponse changeTicketsStatus(Integer id, String status, String ofType) {
		Tickets over = this.ticketsRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TICKETS_NOT_FOUND + id));
		if (ofType.equals("status")) {
			over.setStatus(status);
		} else if (ofType.equals("priority")) {
			over.setLabels(status);
		} else {
			throw new ResourceNotFoundException(AppConstants.TICKETS_NOT_FOUND + id);
		}
		return this.ticketsToTicketsResponse(this.ticketsRepository.save(over));

	}

	private List<TicketsFiles> createTicketsFiles(List<MultipartFile> files, Users createdBy, Tickets t) {
		List<TicketsFiles> ticketsFiles = new ArrayList<>();
		if (files != null) {

			for (MultipartFile a : files) {

				TicketsFiles tf = new TicketsFiles();
				tf.setDeleted(false);
				tf.setOriginalName(tf.getOriginalName());
				tf.setTicketsId(t);
				tf.setUploadedBy(createdBy);
				String profileName = fileService.uploadFileInFolde(a, AppConstants.USER_ROLE);
				tf.setFileName(profileName);
				ticketsFiles.add(tf);
			}
			;
		}

		return ticketsFiles;

	}

	@Override
	public List<TicketsFiles> addTicketFile(List<MultipartFile> files, Integer ticketId, String uploadedBy) {
		Tickets tickets = this.ticketsRepository.findById(ticketId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TICKETS_NOT_FOUND + ticketId));
		Users user = this.userService.findByEmail(uploadedBy)
				.orElseThrow(() -> new UserNotFoundException(AppConstants.USER_NOT_FOUND_ + uploadedBy));

		List<TicketsFiles> allfiles = tickets.getTicketsFiles();

		if (files != null) {
			List<TicketsFiles> createTicketsFiles = this.createTicketsFiles(files, user, tickets);

			allfiles.addAll(createTicketsFiles);

		}
		tickets.setTicketsFiles(allfiles);
		tickets = this.ticketsRepository.save(tickets);
		return tickets.getTicketsFiles();
	}

	@Override
	public Boolean deleteTicketFileById(Long id) {
		TicketsFiles ticketFiles = this.ticketsFileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.PROJECT_FILE_NOT_FOUND + id));

		this.ticketsFileRepository.delete(ticketFiles);

		Path path = Paths.get(System.getProperty("user.dir") + DIRECTORY).toAbsolutePath().normalize()
				.resolve(ticketFiles.getFileName()); //
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public ResponseEntity<Object> getAllStaticsOfTickets() {

		return ResponseEntity.ok(this.ticketsRepository.getAllStaticsOfTickets());
	}
}
