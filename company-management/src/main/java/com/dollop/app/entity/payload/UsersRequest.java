package com.dollop.app.entity.payload;

import java.sql.Date;
import org.springframework.web.multipart.MultipartFile;

import com.dollop.app.entity.Clients;
import com.dollop.app.entity.Designation;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UsersRequest {

	private Integer id;
 @CustomValidator(type = Type.NAME)
	private String firstName;
    @CustomValidator(type = Type.NAME)
	private String lastName;

	private Boolean isAdmin;
	@CustomValidator(type = Type.EMAIL)
	private String email;
	@CustomValidator(type = Type.PASSWORD)
	private String password;

	private String originalName;

	private String profileName;

	private String status;

	private Date messageCheckedAt;
//@NotNull(message ="Clients is Requrid" )
	private Clients clienId;

	private Date notificationCheckedAt;

	private Boolean isPrime;

	private String jobTitle;

	private Boolean disableLogin;

	private String note;
   @CustomValidator(type = Type.BOTH)
	private String alternativeAddress;
	@CustomValidator(type = Type.PHONE)
	private String phone;
	@CustomValidator(type = Type.BOTH)
	private String address;
	@CustomValidator(type = Type.PHONE)
	private String alternativePhone;
	
     private Date dob;
	@CustomValidator(type = Type.SSN)
	private String ssn;
@CustomValidator(type = Type.GENDER)
	private String gender;
	// colum mtext
	private String stickyNote;
	// colum text
	private String skype;

	private Boolean enableWebNotification;

	private Boolean enableEmailNotification;

	private Date createdAt;

	private Boolean deleted;
	private Designation designation;
	

}
