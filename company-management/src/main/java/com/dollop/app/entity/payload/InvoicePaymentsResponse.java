package com.dollop.app.entity.payload;

import java.sql.Date;

import com.dollop.app.entity.Invoices;
import com.dollop.app.entity.PaymentMethods;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

@JsonInclude(value = Include.NON_NULL)
public class InvoicePaymentsResponse {

	
	private Integer id;
	  @NotNull(message = "amount must not be null")
	    @Min(value = 0, message = "amount must be a positive number")

	private Long amount;
	
	private Date paymentDate;
	
	private PaymentMethods paymentMethodId;
	
	@CustomValidator(type = Type.DESCRIPTION)
	private String note;
	@CustomValidator(type = Type.REQUIRED)
	@NotNull(message = "amount must not be null")
	private String status;
	
	@NotNull(message = "amount must not be null")
	private Invoices invoiceId;
	private Boolean deleted;
	
	
	private String transactionId;
	
	private Integer createdBy;
	}
