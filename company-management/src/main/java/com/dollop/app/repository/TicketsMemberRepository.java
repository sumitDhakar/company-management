package com.dollop.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.entity.Tickets;
import com.dollop.app.entity.TicketsMembers;
import com.dollop.app.entity.Users;

import jakarta.transaction.Transactional;

public interface TicketsMemberRepository extends JpaRepository<TicketsMembers, Long> {

	
	public Page<TicketsMembers> findByTickets(Pageable pageable,Tickets t);
	
	public Page<TicketsMembers> findByFollowers(Pageable pageable,Users userId);
	
	public Optional<TicketsMembers> findByTicketsAndFollowers(Tickets t,Users userId);
	

    public Optional<TicketsMembers> findByIdAndDeleted(Long id, boolean b);

    @Transactional
    @Modifying
    @Query("Delete From TicketsMembers tm where tm.id =:id")
	public void deleteBytaskMemberId(Long id);
    
//    @Query(value ="select  p.id  , p.title from `projects` p where id in (Select pm.project_id_id from `project_members` pm where pm.user_id_id=1)",nativeQuery =true) 
//    public List<Object[]> findBYUserId(Integer userId);

}
