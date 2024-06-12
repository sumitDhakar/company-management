package com.dollop.app.entity;


import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Tasks {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private Long id;

	private String title;
	

	private String description;

	@CollectionTable(name = "Task_labels")
	@ElementCollection
	@Column(name = "Task_label")
	private List<String> Tasklabels;

	private Boolean deleted;

	private Date startDate;

	private Date deadline;
	// enum
	private String status;

	private String points;

	@ManyToOne
	@JoinColumn(name="projectId")
	@JsonIgnoreProperties(value= {"tasks", "userRoles", "designation"})
	private Projects projectId;


	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
	@JoinColumn(name="task_id")
	@JsonIgnoreProperties(value = {"task","deleted"})	
	private List<TaskMembers> taskMembers;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "task_id")
	private List<Comments> comments;

	
	private Integer assignedHours;
	
	private Integer remaningHours;
	
	
	
	
}