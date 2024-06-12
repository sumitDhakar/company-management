package com.dollop.app.serviceImpl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.dollop.app.constant.AppConstants;
import com.dollop.app.entity.Clients;
import com.dollop.app.entity.GoalList;
import com.dollop.app.entity.Taxes;

import com.dollop.app.entity.payload.TaxesRequest;
import com.dollop.app.entity.payload.TaxesResponse;
import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.exception.ResourcesAlreadyExists;
import com.dollop.app.repository.TaxesRepository;
import com.dollop.app.service.ITaxesService;

@Service
public class TaxesServiceImpl implements ITaxesService {

	@Autowired
	private TaxesRepository taxesRepository;

	@Autowired
	private ModelMapper modelMapper;

	public TaxesResponse taxesToTaxesResponse(Taxes taxes) {
		return this.modelMapper.map(taxes, TaxesResponse.class);
	}

	public Taxes TaxesResquestToTaxes(TaxesRequest taxesRequest) {
		return this.modelMapper.map(taxesRequest, Taxes.class);
	}

	@Override
	public TaxesResponse addTaxes(TaxesRequest taxes) {
		   boolean taxesExists =this. taxesRepository.existsByDeletedAndTitleOrPercentage(false,taxes.getTitle(), taxes.getPercentage());
		    if (taxesExists) {
	            throw new ResourcesAlreadyExists("Taxes with the same Tax name and Percentage already exists");
	        }
		Taxes taxe = this.taxesRepository.save(this.TaxesResquestToTaxes(taxes));
		return this.taxesToTaxesResponse(taxe);
	}

	@Override
	public TaxesResponse getTaxesById(Integer id) {
		Taxes taxe = this.taxesRepository.findByIdAndDeleted(id, false)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TAXES_NOT_FOUND + id));

		return this.taxesToTaxesResponse(taxe);
	}

	@Override
	public TaxesResponse updateTaxes(TaxesRequest taxes) {
//		boolean taxesExists =this. taxesRepository.existsByDeletedAndTitleOrPercentageAndNotId(false,taxes.getTitle(), taxes.getPercentage(),taxes.getId());
//	    if (taxesExists) {
//            throw new ResourcesAlreadyExists("Taxes with the same Tax name or Percentage already exists");
//        }
//		  boolean taxesExists =this. taxesRepository.existsByDeletedAndTitleOrPercentageAndNotId(false,taxes.getTitle(), taxes.getPercentage(),taxes.getId());
//		    if (taxesExists) {
//	            throw new ResourcesAlreadyExists("Taxes with the same Tax name and Percentage already exists");
//	        }
//	
		 Taxes taxesExists = taxesRepository.existsByTitleOrPercentageAndDeletedAndIdIsNot(taxes.getTitle(),taxes.getPercentage(),false,taxes.getId());
	        if (taxesExists!=null) {
	            throw new ResourcesAlreadyExists("Taxe With Same Name Or Percentage Already Present");
	        }

		
		Taxes taxe = this.taxesRepository.findById(taxes.getId())
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TAXES_NOT_FOUND + taxes.getId()));
		taxe.setPercentage(taxes.getPercentage());
		taxe.setTitle(taxes.getTitle());
		return this.taxesToTaxesResponse(this.taxesRepository.save(taxe));

	}

	@Override
	public Page<TaxesResponse> getAllTaxes(Integer pageNo, Integer pageSize) {
		PageRequest pageRequest = PageRequest.of(pageNo, pageSize, Sort.by(Direction.DESC, "id"));
				Page<Taxes> taxes = this.taxesRepository.findAllByDeleted(pageRequest, false);

		return taxes.map(t -> this.taxesToTaxesResponse(t));
	}
	
	@Override
	public Page<TaxesResponse> getAllTaxesByStatus(Integer pageNo, Integer pageSize) {
		PageRequest page = PageRequest.of(pageNo, pageSize);
		Page<Taxes> taxes = this.taxesRepository.findAllByDeletedAndStatus(page, false,"Active");

		return taxes.map(t -> this.taxesToTaxesResponse(t));
	}

	@Override
	public Boolean deleteTaxes(Integer id) {
		Taxes tax = this.taxesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TAXES_NOT_FOUND + id));

		tax.setDeleted(true);
		this.taxesRepository.save(tax);
		return true;
	}

	@Override
	public Page<TaxesResponse> getTaxesByStatus(Integer pageNo, Integer pageSize, String status) {
		PageRequest page = PageRequest.of(pageNo, pageSize);
		Page<Taxes> originalPage = this.taxesRepository.findByStatus(page, status);

		return originalPage.map(t -> this.taxesToTaxesResponse(t));

	}

	@Override
	public TaxesResponse updateTaxesStatus(Integer id, String status) {
		Taxes Taxe = this.taxesRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(" goalLis Not Found with id : " + id));
		Taxe.setStatus(status);
		return this.taxesToTaxesResponse(this.taxesRepository.save(Taxe));

	}

}
