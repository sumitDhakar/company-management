package com.dollop.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.entity.TaskMembers;
import com.dollop.app.entity.Tasks;
import com.dollop.app.entity.Users;

import jakarta.transaction.Transactional;

public interface TaskMemberRepository extends JpaRepository<TaskMembers, Long> {

	public Optional<TaskMembers> findByTaskAndMembers(Tasks t, Users employee);

	public Optional<TaskMembers> findByTaskAndIsLeader(Tasks t,boolean isLeader);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM `task_members` WHERE is_leader =true and task_id = :t", nativeQuery = true)
	public void deleteByTaskAndIsLeader(Long t);

	@Transactional
	@Modifying
	@Query(value = "DELETE FROM `task_members` WHERE is_leader =false and task_id = :t and id =:tmId", nativeQuery = true)
	public void deleteTaskMemberBytaskIdAndMemberid(Long t, Long tmId);

}
