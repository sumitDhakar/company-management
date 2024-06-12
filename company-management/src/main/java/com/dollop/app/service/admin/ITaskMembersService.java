package com.dollop.app.service.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.dollop.app.entity.TaskMembers;
import com.dollop.app.entity.payload.admin.TaskMemberRequest;
import com.dollop.app.entity.payload.admin.TaskMemberResponse;
import com.dollop.app.utils.ChangeTaskAssignedPayload;

public interface ITaskMembersService {
	public TaskMemberResponse taskMemberToTaskMemberResponse(TaskMembers memberRequest);

	public TaskMembers taskMemberRequestToTaskMembert(TaskMemberRequest memberRequest);

	public ResponseEntity<List<TaskMemberResponse>> addMembersToTask(List<TaskMemberRequest> membersList);

	public ResponseEntity<Boolean> deleteTaskMembers(Long memberId);
	
	public ResponseEntity<Boolean> changeTaskLeadEmployee(ChangeTaskAssignedPayload cta);

}
