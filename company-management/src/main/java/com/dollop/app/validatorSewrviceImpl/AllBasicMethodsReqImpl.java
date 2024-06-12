package com.dollop.app.validatorSewrviceImpl;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dollop.app.exception.ResourceNotFoundException;
import com.dollop.app.repository.ClientsRepository;
import com.dollop.app.repository.HolidaysRepository;
import com.dollop.app.validatorService.AllBasicMethodsReq;

@Component
public class AllBasicMethodsReqImpl implements AllBasicMethodsReq {

	@Autowired
	private HolidaysRepository holidaysService;
	@Autowired
	private ClientsRepository clientsRepository;

	@Override
	public void checkDayAsSundayOrHoliday(Date checkDate) {
		LocalDate dateToCheck = checkDate.toLocalDate();
		if (dateToCheck.getDayOfWeek() == DayOfWeek.SUNDAY) {
			throw new ResourceNotFoundException("There is A Sunday On This Date.");

		}

		else if (this.holidaysService.existsByHolidayDate(checkDate)) {
			throw new ResourceNotFoundException("There is A Holiday On This Date.");

		}

	}

	@Override
	public void isEndDateGreaterOrNot(Date date1, Date date2) {
		// Convert SQL dates to milliseconds
		long millisDate1 = date1.getTime();
		long millisDate2 = date2.getTime();

		// Calculate the difference in milliseconds between the two dates
		long difference = millisDate1 - millisDate2;

		// Number of milliseconds in a day
		long oneDayInMillis = 24 * 60 * 60 * 1000;

		// Check if the difference is greater than or equal to one day

		if (!(difference >= oneDayInMillis)) {
			throw new ResourceNotFoundException("End Date Should Be One Day Or More Greater Than Start Date");
		}
	}

	@Override
	public void checkDateGreaterThanTodayDate(Date givenDate) {
		// Convert SQL date to LocalDate
		LocalDate givenSqlDate = givenDate.toLocalDate();

		// Get current LocalDate
		LocalDate currentDate = LocalDate.now();

		// Compare LocalDate objects
		if( !givenSqlDate.isAfter(currentDate)) {
			throw new ResourceNotFoundException("Holiday Date Should Be Greater Than Today");
		}
	}

	@Override
	public void checkClientExistence(Integer id) {
		if (!this.clientsRepository.existsById(id)) {
			throw new ResourceNotFoundException("No Such Client Found");
		}

	}

}
