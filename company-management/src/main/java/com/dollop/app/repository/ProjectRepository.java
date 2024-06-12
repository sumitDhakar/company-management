package com.dollop.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.entity.Clients;
import com.dollop.app.entity.Projects;
import com.dollop.app.entity.Users;

import jakarta.transaction.Transactional;

public interface ProjectRepository extends JpaRepository<Projects, Integer>{

	public Page<Projects> findByprojectMembersAndDeleted(Pageable pageable,Users user,Boolean deleted);

	
	public Page<Projects> findByDeleted(Pageable pageable,Boolean deleted);

	
	public Page<Projects> findByClientIdAndDeleted(PageRequest pageRequest, Clients client, boolean deleted);


    public Optional<Projects> findByIdAndDeleted(Integer id, boolean b);
    
    @Query(value="SELECT p FROM Projects p " +
            "LEFT JOIN ProjectMembers pr ON p.id = pr.projectId " +
            "WHERE pr.userId = :userId AND p.deleted = false",nativeQuery =  true)
     Page<Projects> findDeletedProjectsByUserId(Long userId, Pageable pageable);

    @Query(value="SELECT p from Projects  p INNER JOIN ProjectMembers  m ON m.projectId.id=p.id and m.userId.id=:userId")
    Page<Projects> getProjectsByProjectMemberUserId(Integer userId,Pageable page);
	
    @Modifying
    @Transactional
    @Query(value="UPDATE projects SET status ='Completed'\r\n"
    		+ "WHERE deadline = CURDATE() and deleted = false And status != 'Completed';\r\n"
    		+ "",nativeQuery = true)
    public void updateProjectStatus();
	public boolean existsByTitleAndClientIdAndDeletedAndIdNot(String trim, Clients c, boolean b, Integer id);
	public boolean existsByTitleAndClientIdAndDeleted(String trim, Clients c, boolean b);

}
