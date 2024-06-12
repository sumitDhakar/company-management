package com.dollop.app.controller.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dollop.app.entity.payload.admin.TaskMemberRequest;
import com.dollop.app.service.admin.ITaskMembersService;
import com.dollop.app.utils.ChangeTaskAssignedPayload;

@RestController
@RequestMapping("/rise/admin/taskMembers")
@CrossOrigin("*")
public class TaskMemberController {

	@Autowired
	private ITaskMembersService iTaskMembersService;

	// add project member
	@PostMapping("/")
	public ResponseEntity<?> addTaskMembers(@RequestBody List<TaskMemberRequest> memberRequest) {
		return this.iTaskMembersService.addMembersToTask(memberRequest);

	}
	// add project member
		@PutMapping("/changeLeader")
		public ResponseEntity<?> changeLeadreOfTaskMembers(@RequestBody ChangeTaskAssignedPayload memberRequest) {
			System.err.println(memberRequest);
			return this.iTaskMembersService.changeTaskLeadEmployee(memberRequest);

		}
	// delete Project member by id
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteTaskMemberByID(@PathVariable Long id) {
		return this.iTaskMembersService.deleteTaskMembers(id);
	}
}
