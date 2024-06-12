package com.dollop.app.entity.payload;

import java.sql.*;
import java.util.List;

import com.dollop.app.entity.Projects;
import com.dollop.app.entity.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClientsResponse {
	private Integer id;

	private String firstName;

	private String lastName;

	private Date createdAt;

	private String password;

	private String companyName;
	// colum text
	private String address;
	private String gender;
	private Date dob;
	
	private String originalName = "profileImage";

	private String profileName = "defaultUserImage.png";

	private String city;

	private String state;

	private String zip;

	private String country;

	private Date createdDate;
	// colum text
	private String website;

	private String ClientGroups;

	private String phone;

	private String currencySymbol;
	// colum mtext
	private String starredBy;
	// colum text
	private String vatNumber;

	private String gstNumber;

	private String status = "Active";

	private String email;

	@JsonIgnoreProperties(value = { "clientId" })
	private List<Projects> projects;

	private String currency;

	@JoinColumn(name = "client_id")
	private List<String> labels;

	private Boolean deleted;


	private Boolean disableOnlinePayment;
}
