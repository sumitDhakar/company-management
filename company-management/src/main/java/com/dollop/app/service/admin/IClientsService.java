package com.dollop.app.service.admin;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.entity.Clients;
import com.dollop.app.entity.payload.ClientsRequest;
import com.dollop.app.entity.payload.ClientsResponse;

public interface IClientsService {
	
	  public ClientsResponse clientsToClientsResponse(Clients clients);
	
	  public Clients clientsRequestToClients(ClientsRequest clientsRequest);
	
	  public Clients clientsResponseToClients(ClientsResponse clientsResponse);

	   public ClientsResponse addClients(ClientsRequest clients, MultipartFile profileImage); 
	   
	   public ClientsResponse updateClients(ClientsRequest clients , MultipartFile profileImage);
	   
	   public ClientsResponse getClientsById(Integer id);
	   
	   public Page<ClientsResponse> getAllClients(Integer pageNo,Integer pageSize);
	   public Page<ClientsResponse> getAllClientsOrderby();
	   
	   public Boolean deleteClients(Integer id);
	  
	   public ClientsResponse updateClientStatus(Integer id,String status);
	   
	   public Page<ClientsResponse> searchClient(Integer pageNo,Integer pageSize,ClientsRequest client);
	
}
