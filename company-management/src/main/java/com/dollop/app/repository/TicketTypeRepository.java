package com.dollop.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.entity.TicketTypes;

public interface TicketTypeRepository extends JpaRepository<TicketTypes, Integer>{
	
	public Page<TicketTypes> findByIsDeleted(Pageable pageable,Boolean deleted);

	public Optional<TicketTypes> findByIdAndIsDeleted(Integer id, boolean b);
	 @Query("SELECT COUNT(t) > 0 FROM TicketTypes t WHERE  t.title=:Title")
	public boolean existsByTitle(String Title);

}
