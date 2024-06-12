package com.dollop.app.entity;

import com.dollop.app.validatorService.CustomValidator;
import com.dollop.app.validatorService.CustomValidator.Type;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class InvoicesItems {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
  
	private Integer id;
	// colum text
	@CustomValidator(type = Type.NAME)
	private String title;
	// colum text
	@CustomValidator(type = Type.DESCRIPTION)
	private String description;
	  @NotNull(message = "quantity must not be null")
	    @Min(value = 0, message = "quantity must be a positive number")
	 @Max(value = 50,message= "quantity less than shoud be 50")
	private Double quantity;
	  @NotNull(message = "unitCost must not be null")
	    @Min(value = 0, message = "unitCost must be a positive number")
	 @Max(value = 500000,message= "unitCost must be a positive number")

	private Double unitCost;
//	  @NotNull(message = "rate must not be null")
//	    @Min(value = 0, message = "rate must be a positive number")
	
	private Double rate;
	  @NotNull(message = "total must not be null")
	    @Min(value = 0, message = "total must be a positive number")
	
	private Double total;
//	  @NotNull(message = "rate must not be null")
//	    @Min(value = 0, message = "rate must be a positive number")
	
	private Double grandTotal;

    @ManyToOne
    @JoinColumn(name = "invoices_id")
    @JsonIgnoreProperties(value={"invoicesItems"})
    private Invoices invoices;

	private Boolean deleted;

	public InvoicesItems(Integer id, String title, String description, Double quantity, Double unitCost, Double total,
			Double grandTotal, Boolean deleted) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.quantity = quantity;
		this.unitCost = unitCost;
		this.total = total;
		this.grandTotal = grandTotal;
		this.deleted = deleted;
	}
	
}
