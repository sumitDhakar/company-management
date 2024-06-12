package com.dollop.app.entity.payload;

import java.util.List;

import com.dollop.app.entity.Clients;
import com.dollop.app.entity.TicketTypes;
import com.dollop.app.entity.TicketsFiles;
import com.dollop.app.entity.TicketsMembers;
import com.dollop.app.entity.Users;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TicketsRequest {
	private Integer id;
	@NotNull(message = "Clients is Requrid")
	private Clients client;
	@NotNull(message = "Clients is Requrid")

	private TicketTypes ticketTypeId;
	@CustomValidator(type = Type.NAME)
	private String title;

	// enum

	private String status;

	private List<TicketsFiles> ticketsFiles;

	private List<TicketsMembers> ticketMembers;

	@CustomValidator(type = Type.DESCRIPTION)
	private String description;
	@NotNull(message = "requestedBy is REQUIRED")
	private Users requestedBy;

	@CustomValidator(type = Type.REQUIRED)
	private String labels;

	private Boolean deleted = false;
}
