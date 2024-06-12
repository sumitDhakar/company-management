package com.dollop.app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dollop.app.entity.Projects;
import com.dollop.app.entity.Resignation;
import com.dollop.app.entity.Tasks;
import com.dollop.app.entity.Users;

public interface TasksRepository extends JpaRepository<Tasks, Long> {

	public Page<Tasks> findAllByOrderByIdDesc(Pageable page);

	public Optional<Tasks> findByIdAndDeleted(Long id, boolean b);

	public Page<Tasks> findByProjectIdAndDeleted(PageRequest pageRequest, Projects project, boolean b);

	 @Query("SELECT DISTINCT t FROM Tasks t  " +
	            "JOIN t.taskMembers tm " +
	            "JOIN tm.members u " +
	            "WHERE t.projectId.id = :projectId And t.deleted =:b  " +
	            "AND u.id = :userId")
	 public Page<Tasks> findByProjectIdAndDeletedAndCurrentUser(PageRequest pageRequest, Integer projectId, boolean b,Integer userId);

	public Page<Tasks> findByProjectIdAndDeletedAndStatus(PageRequest pageRequest, Projects project, boolean b,
			String status);

	@Query("select count(*) AS totalTasks ,"
			+ "sum(CASE WHEN FUNCTION('DATE',t.deadline) < FUNCTION('Date',:currentDate) AND t.status !='Completed' THEN 1 ELSE 0 END) AS OVERDUE,  "
			+ "sum(CASE WHEN t.status ='Completed' Then 1 ELSE 0 END  )AS Completed"
			+ ",sum(CASE WHEN t.status ='Inprogress' Then 1 ELSE 0 END  )AS Inprogress"
			+ ",sum(CASE WHEN t.status ='OnHold' Then 1 ELSE 0 END  )AS OnHold"
			+ ",sum(CASE WHEN t.status ='Pending' Then 1 ELSE 0 END  )AS Pending"
			+ ",sum(CASE WHEN t.status ='Review' Then 1 ELSE 0 END  )AS Review" + " FROM Tasks t")
	public Object findByStatusDetailsOfTasks(@Param("currentDate") LocalDate currentDate);

	
	@Query(value ="SELECT"
			+ "    SUM(CASE WHEN t.status IN ('Completed') THEN 1 ELSE 0 END) AS completedOrReviewTask,"
			+ "    SUM(CASE WHEN t.status NOT IN ('Completed') THEN 1 ELSE 0 END) AS pendingTask "
			+ "FROM "
			+ "    tasks t "
			+ "WHERE "
			+ "    t.deleted = false And t.project_id = :pId ",nativeQuery = true)
	public  List<Object[]> getAllProjectTaskCounts(Integer pId);
	
	
	@Query(value ="SELECT u.id,u.first_name,u.last_name,u.email,u.profile_name FROM users u \r\n"
			+ "            JOIN project_members pm ON u.id = pm.user_id \r\n"
			+ "            WHERE pm.project_id = :projectId",nativeQuery =true) 
    public List<Object[]> findUserByprojectId(Integer projectId);
	
  
}
