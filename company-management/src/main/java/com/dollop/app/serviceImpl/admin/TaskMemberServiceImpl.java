package com.dollop.app.serviceImpl.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dollop.app.constant.AppConstants;
import com.dollop.app.entity.TaskMembers;
import com.dollop.app.entity.Tasks;
import com.dollop.app.entity.Users;
import com.dollop.app.entity.payload.admin.TaskMemberRequest;
import com.dollop.app.entity.payload.admin.TaskMemberResponse;
import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.repository.TaskMemberRepository;
import com.dollop.app.service.admin.ITaskMembersService;
import com.dollop.app.utils.ChangeTaskAssignedPayload;

@Service
public class TaskMemberServiceImpl implements ITaskMembersService {
	@Autowired
	private TaskMemberRepository taskMemberRepository;



	@Autowired
	private ModelMapper modelMapper;

	@Override
	public TaskMemberResponse taskMemberToTaskMemberResponse(TaskMembers memberRequest) {
		return this.modelMapper.map(memberRequest, TaskMemberResponse.class);
	}

	@Override
	public TaskMembers taskMemberRequestToTaskMembert(TaskMemberRequest memberRequest) {
		return this.modelMapper.map(memberRequest, TaskMembers.class);
	}

	@Override
	public ResponseEntity<List<TaskMemberResponse>> addMembersToTask(List<TaskMemberRequest> membersList) {
		List<TaskMembers> list = membersList.stream().map(m -> this.taskMemberRequestToTaskMembert(m))
				.collect(Collectors.toList());
		List<TaskMembers> taskMembers = new ArrayList<>();
		for (TaskMembers l : list) {
			Tasks t = new Tasks();
			t.setId(l.getTask().getId());
			Users employee = new Users();
			employee.setId(l.getMembers().getId());
			Optional<TaskMembers> memb = this.taskMemberRepository.findByTaskAndMembers(t, employee);
			if (memb.isEmpty())
				taskMembers.add(l);
		}
		taskMembers = this.taskMemberRepository.saveAll(taskMembers);
		return new ResponseEntity<List<TaskMemberResponse>>(
				taskMembers.stream().map(m -> this.taskMemberToTaskMemberResponse(m)).collect(Collectors.toList()),
				HttpStatus.CREATED);

	}

	@Override
	public ResponseEntity<Boolean> deleteTaskMembers(Long memberId) {
		TaskMembers member = this.taskMemberRepository.findById(memberId)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TASK_MEMBER_NOT_FOUND + memberId));
		if (member == null)
			throw new IllegalArgumentException("SomeThing Went Wrong!!");

		this.taskMemberRepository.deleteTaskMemberBytaskIdAndMemberid(member.getTask().getId(), member.getId());
		return ResponseEntity.ok(true);
	}

	@Override
	public ResponseEntity<Boolean> changeTaskLeadEmployee(ChangeTaskAssignedPayload cta) {
		Tasks t = new Tasks();
		t.setId(cta.getTaskId());
//		this.taskMemberRepository.deleteByTaskAndIsLeader(t.getId());
		Users employee = new Users();
		employee.setId(cta.getUserId());
//		TaskMembers tm = new TaskMembers();
//		tm.setMembers(employee);
//		tm.setTask(t);
//		tm.setIsLeader(true);
		
		TaskMembers tm = this.taskMemberRepository.findByTaskAndIsLeader(t,true)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstants.TASK_NOT_FOUND + t.getId()));
            tm.setMembers(employee);
		this.taskMemberRepository.save(tm);

		return ResponseEntity.ok(true);
	}

}
