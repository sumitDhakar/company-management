package com.dollop.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.entity.Attendance;
import com.dollop.app.entity.Clients;

public interface ClientsRepository extends JpaRepository<Clients,Integer> {

	public Page<Clients> findByDeleted(PageRequest page,Boolean deleted);
	
	public	Optional<Clients> findByIdAndDeleted(Integer id, boolean b);
	
	

    @Query("SELECT c FROM Clients c WHERE (c.companyName=:companyName or c.email=:email) AND c.deleted =:b AND c.id!=:id")
	public Clients existsByCompanyNameOrEmailAndDeletedAndIdIsNot(String companyName, String email, boolean b,Integer id);
   
	public Boolean existsByCompanyNameAndEmailAndDeleted(String companyName, String email, boolean b);
	
	
}
