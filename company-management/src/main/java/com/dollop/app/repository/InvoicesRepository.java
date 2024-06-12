package com.dollop.app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.entity.Invoices;

import jakarta.transaction.Transactional;

public interface InvoicesRepository extends JpaRepository<Invoices, Integer> {
	public Page<Invoices> findByDeleted(PageRequest page, Boolean deleted);

	public Optional<Invoices> findByIdAndDeleted(Integer id, boolean b);

	@Transactional
	@Modifying
	@Query(value = " Update  Invoices   i set i.status = 'overDue' where date(i.due_date) < date(:now)   ", nativeQuery = true)
	public void changeStatusOfInvoice(LocalDate now);

	@Query(value="SELECT * FROM Invoices i WHERE (i.paid_amount < i.grand_total OR i.paid_amount = 0) AND i.deleted = false",nativeQuery = true)
	public List<Invoices> findAllUnpaidOrZeroPaidInvoices();

}
