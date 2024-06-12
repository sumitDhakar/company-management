package com.dollop.app.entity.payload;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.dollop.app.entity.Clients;
import com.dollop.app.entity.EstimateItems;
import com.dollop.app.entity.Projects;
import com.dollop.app.entity.Taxes;
import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EstimatesRequest {

	private Integer id;

	private String eid;
@NotNull(message ="Clients is required" )
	private Clients clientId;
@NotNull(message ="Projects is required" )
	private Projects projectId;

	private Date billDate;

	private Date dueDate;
	// colum mtext
	@CustomValidator(type = Type.DESCRIPTION)
	private String note;

	private Date lastEmailSentDate;
	// enum
	private String status;
//	@CustomValidator(type = Type.REQUIRED)
	@NotNull(message ="Taxes is required" )

	private Taxes taxeId;
//	@CustomValidator(type = Type.REQUIRED)
	  @NotNull(message = "Discount must not be null")
	    @Min(value = 0, message = "Discount must be a positive number")
	private Integer discount;

	  @NotNull(message = "discountPercentage must not be null")
	    @Min(value = 0, message = "discountPercentage must be a positive number")

	private Integer discountPercentage;
//	@CustomValidator(type = Type.REQUIRED)
	  @NotNull(message = "total must not be null")
	    @Min(value = 0, message = "total must be a positive number")
	
	  private Long total;
//	@CustomValidator(type = Type.REQUIRED)
	  @NotNull(message = "taxCost must not be null")
	    @Min(value = 0, message = "taxCost must be a positive number")
	
	  private Long taxCost;
//	@CustomValidator(type = Type.REQUIRED)
	  @NotNull(message = "grandTotal must not be null")
	    @Min(value = 0, message = "grandTotal must be a positive number")
	
	  private Long grandTotal;

	private Boolean deleted;
	private List<EstimateItems> estimateItems = new ArrayList<>();

	public EstimatesRequest(String eid, Clients clientId, Projects projectId, Date billDate, Date dueDate, String note,
			Date lastEmailSentDate, String status, Taxes taxeId, Integer discount, Integer discountPercentage,
			Long total, Long taxCost, Long grandTotal, Boolean deleted, List<EstimateItems> estimateItems) {
		super();
		this.eid = eid;
		this.clientId = clientId;
		this.projectId = projectId;
		this.billDate = billDate;
		this.dueDate = dueDate;
		this.note = note;
		this.lastEmailSentDate = lastEmailSentDate;
		this.status = status;
		this.taxeId = taxeId;
		this.discount = discount;
		this.discountPercentage = discountPercentage;
		this.total = total;
		this.taxCost = taxCost;
		this.grandTotal = grandTotal;
		this.deleted = deleted;
		this.estimateItems = estimateItems;
	}

}
