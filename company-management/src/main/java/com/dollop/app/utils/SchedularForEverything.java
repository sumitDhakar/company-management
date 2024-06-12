package com.dollop.app.utils;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dollop.app.repository.GoalListRepository;
import com.dollop.app.repository.ProjectRepository;

@Component
public class SchedularForEverything {

	@Autowired
	public GoalListRepository goalListRepository;

	@Autowired
	public ProjectRepository projectRepository;

	@Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
	public void updateProgress() {
		this.goalListRepository.updateProgressOfGoalList();
	}

	public Double calculateProgress(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			// Handle the case where start date or end date is not set
			return 0.0;
		}

		LocalDate currentDate = LocalDate.now();

		// Ensure the current date is within the goal's start and end date
		if (currentDate.isBefore(startDate.toLocalDate())) {
			return 0.0; // The goal has not started yet
		} else if (currentDate.isAfter(endDate.toLocalDate())) {
			return 100.0; // The goal has already ended
		}

		// Calculate the progress based on the current date relative to start and end
		// dates
		long totalDays = ChronoUnit.DAYS.between(startDate.toLocalDate(), endDate.toLocalDate());
		long daysPassed = ChronoUnit.DAYS.between(startDate.toLocalDate(), currentDate);

//        return (double) (daysPassed / totalDays * 100);
		BigDecimal result = new BigDecimal(((double) daysPassed / totalDays * 100));
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

	}

	@Scheduled(cron = "0 0 0 * * ?") // Run daily at midnight
	private void updateProjectStatus() {
		this.projectRepository.updateProjectStatus();
	}

	
	
	 @Scheduled(cron = "59 59 23 31 12 ?") // Cron expression for running on the last day of the year at 23:59:59
	    public void yearlyTask() {
	        // Your task or function to be executed annually
	        System.out.println("Yearly task executed on " + LocalDateTime.now());
	    }
	
}
