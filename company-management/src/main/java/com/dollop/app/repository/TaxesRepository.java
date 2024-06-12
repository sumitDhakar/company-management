package com.dollop.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dollop.app.entity.Clients;
import com.dollop.app.entity.Taxes;

public interface TaxesRepository extends JpaRepository<Taxes, Integer>{
	
public Page<Taxes> findByDeleted(PageRequest page,Boolean deleted);
	
	public	Optional<Taxes> findByIdAndDeleted(Integer id, boolean b);
	
	public Page<Taxes> findByStatus(PageRequest pageRequest, String status);

	public Page<Taxes> findAllByDeleted(PageRequest page, boolean b);

	public Page<Taxes> findAllByDeletedAndStatus(PageRequest page, boolean b, String string);

	  @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Taxes e " +
	           "WHERE e.deleted = :deleted " +
	           "AND (e.title = :title OR e.percentage = :percentage )")
	public    boolean existsByDeletedAndTitleOrPercentage(
	        @Param("deleted") boolean deleted,
	        @Param("title") String title,
	        @Param("percentage") Double percentage
	    );

	  
	  @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM Taxes e " +
		       "WHERE e.deleted = :deleted  " +
		       "AND (e.title = :title And e.percentage = :percentage Or e.percentage = :percentage ) AND e.id <> :excludeId ")
		boolean existsByDeletedAndTitleOrPercentageAndNotId(
		    @Param("deleted") boolean deleted,
		    @Param("title") String title,
		    @Param("percentage") Double percentage,
		    @Param("excludeId") Integer excludeId
		);
	

	  @Query("SELECT t FROM Taxes t WHERE (t.title=:title or t.percentage=:percentage) AND t.deleted =:b AND t.id!=:id")
		public Taxes existsByTitleOrPercentageAndDeletedAndIdIsNot(String title, Double percentage, boolean b,Integer id);
	   
	  
}
