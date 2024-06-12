package com.dollop.app.validatorService;

import java.sql.Date;

public interface AllBasicMethodsReq {

	public void checkDayAsSundayOrHoliday(Date checkDate);

	public void isEndDateGreaterOrNot(Date date1, Date date2);

	public void checkClientExistence(Integer id);

	public void checkDateGreaterThanTodayDate(Date givenDate);
}
