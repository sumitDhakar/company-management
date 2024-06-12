package com.dollop.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class TaskMembers {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="task_id")
	@JsonIgnoreProperties(value={"taskMembers"})
	private Tasks task;
	
	@ManyToOne
	@JsonIgnoreProperties(value={"designation","userRoles","experienceInformations","familyInformations","educationInformations"})
	private Users members;
	
	private Boolean isLeader=false;
		
}
