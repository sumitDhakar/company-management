package com.dollop.app.entity.payload;

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

import com.dollop.app.entity.Clients;
import com.dollop.app.entity.InvoicesItems;
import com.dollop.app.entity.Projects;
import com.dollop.app.entity.Taxes;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class InvoicesRequest {
	private Integer id;

	private String iId;

	@NotNull(message =" Clients IS REQUIRED")
	private Clients clientId;
	@NotNull(message =" Projects IS REQUIRED")
	


	@NotNull(message = "Clients Is REQUIRED")
	private Projects projectId;

	
	private Date billDate;
	
	private List<InvoicesItems> invoicesItems = new ArrayList<>();
	
	  @NotNull(message = "discountPercentage must not be null")
	    @Min(value = 0, message = "discountPercentage must be a positive number")
	
	private Integer discountPercentage;
	private Date dueDate;
	// colum mtext
	@CustomValidator(type = Type.DESCRIPTION)
	private String note;
	
	private Date lastEmailSentDate;
	//enum
	private String status;
	@NotNull(message = "Tasex is Required")
	private Taxes taxId;

	  @NotNull(message = "Discount must not be null")
	    @Min(value = 0, message = "Discount must be a positive number")
//	 @Max(value=30,message = "Discount must be 30%")
	private Integer discount;
	  @NotNull(message = "total must not be null")
	    @Min(value = 0, message = "total must be a positive number")
	
	  private Long total;
	  @NotNull(message = "grandTotal must not be null")
	    @Min(value = 0, message = "grandTotal must be a positive number")
	
	  private Long grandTotal;
//	  @NotNull(message = "paidAmount must not be null")
//	    @Min(value = 0, message = "paidAmount must be a positive number")
//	
	 private Long paidAmount;
//	  @NotNull(message = "taxCost must not be null")
//	    @Min(value = 0, message = "taxCost must be a positive number")
	
	private Long taxCost;
	
	private Boolean recurring;
	
	private Integer recurringInvoiceId;
	
	private Integer repeatEvery;
	//enum
	private String repeatType;
	
	private Integer noOfCycles;
	
	private Date nextRecurringDate;
	
	private Integer noOfCyclesCompleted;
	
	private Boolean deleted;

	public InvoicesRequest(String iId, Clients clientId, Projects projectId, Date billDate,
			List<InvoicesItems> invoicesItems, Integer discountPercentage, Date dueDate, String note,
			Date lastEmailSentDate, String status, Taxes taxId, Integer discount, Long total, Long grandTotal,
			Long paidAmount, Long taxCost, Boolean recurring, Integer recurringInvoiceId, Integer repeatEvery,
			String repeatType, Integer noOfCycles, Date nextRecurringDate, Integer noOfCyclesCompleted,
			Boolean deleted) {
		super();
		this.iId = iId;
		this.clientId = clientId;
		this.projectId = projectId;
		this.billDate = billDate;
		this.invoicesItems = invoicesItems;
		this.discountPercentage = discountPercentage;
		this.dueDate = dueDate;
		this.note = note;
		this.lastEmailSentDate = lastEmailSentDate;
		this.status = status;
		this.taxId = taxId;
		this.discount = discount;
		this.total = total;
		this.grandTotal = grandTotal;
		this.paidAmount = paidAmount;
		this.taxCost = taxCost;
		this.recurring = recurring;
		this.recurringInvoiceId = recurringInvoiceId;
		this.repeatEvery = repeatEvery;
		this.repeatType = repeatType;
		this.noOfCycles = noOfCycles;
		this.nextRecurringDate = nextRecurringDate;
		this.noOfCyclesCompleted = noOfCyclesCompleted;
		this.deleted = deleted;
	}
	

}
