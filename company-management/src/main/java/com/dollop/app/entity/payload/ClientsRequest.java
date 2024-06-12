package com.dollop.app.entity.payload;

import java.sql.*;
import java.util.List;

import com.dollop.app.entity.Projects;
import com.dollop.app.entity.Users;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ClientsRequest {
	private Integer id;
@CustomValidator(type = Type.NAME)
	private String firstName;
@CustomValidator(type = Type.NAME)
	private String lastName;

	private Date createdAt;
@CustomValidator(type = Type.PASSWORD)
	private String password;
@CustomValidator(type = Type.BOTH)
	private String companyName;
	// colum text
@CustomValidator(type = Type.BOTH)
	private String address;
@CustomValidator(type = Type.GENDER)
	private String gender;
	private Date dob;
	
	@CustomValidator(type = Type.BOTH)
	private String city;
@CustomValidator(type = Type.BOTH)
	private String state;

	private String zip;
@CustomValidator(type = Type.BOTH)
	private String country;

	private Date createdDate;
	// colum text
	private String website;

	private String ClientGroups;
@CustomValidator(type = Type.PHONE)
	private String phone;

	private String originalName = "profileImage";

	private String profileName = "defaultUserImage.png";

@CustomValidator(type = Type.REQUIRED)
	private String currencySymbol;
	// colum mtext
	private String starredBy;
	// colum text
	private String vatNumber;

	private String gstNumber;

	private String status = "Active";
     @CustomValidator(type = Type.EMAIL)
	private String email;
	
	private List<Projects> projects;

@CustomValidator(type = Type.REQUIRED)
	private String currency;

	@JoinColumn(name = "client_id")
	private List<String> labels;

	private Boolean deleted;

//@CustomValidator(type = Type.REQUIRED)
	private Boolean disableOnlinePayment;
}
