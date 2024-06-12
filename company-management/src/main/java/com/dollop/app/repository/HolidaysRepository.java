package com.dollop.app.repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dollop.app.entity.Department;
import com.dollop.app.entity.GoalType;
import com.dollop.app.entity.Holidays;
import com.dollop.app.entity.Taxes;

public interface HolidaysRepository extends JpaRepository<Holidays, Integer> {

	public Optional<Holidays> findByIdAndDeleted(Integer id, boolean b);

	@Query(value = "Select * from holidays h where h.holiday_date like %:date%", nativeQuery = true)
	public List<Holidays> getHolidaysOfMonth(String date);

	@Query(value = " select *  from holidays h where date(h.holiday_date) > date(:now) ORDER BY h.holiday_date ASC limit 3 ", nativeQuery = true)
	public List<Holidays> fetchEmployeeDashboardDetailsHoliday(LocalDate now);

	 @Query("SELECT CASE WHEN COUNT(h) > 0 THEN true ELSE false END FROM Holidays"
	 		+ " h WHERE ( h.holidayDate =:holidayDate  OR h.title =:title)"
	 		+ " OR  FUNCTION('YEAR',:holidayDate) < FUNCTION('YEAR',CURRENT_DATE)"
	 		+ " OR  FUNCTION('YEAR',:holidayDate) > FUNCTION('YEAR',CURRENT_DATE) "
	 		+ " OR  h.title= :title AND  FUNCTION('YEAR', h.holidayDate) = FUNCTION('YEAR',:holidayDate) ")
	boolean existsByHolidayDateAndTitle(Date holidayDate, String title);

	 
	 
	 
	 @Query("SELECT COUNT(h) > 0 FROM Holidays h WHERE (h.holidayDate = :holidayDate And h.title = :title) OR  (h.holidayDate != :holidayDate AND h.title = :title  ) ")
	//  @Query("SELECT COUNT(t) > 0 FROM Holidays t WHERE (t.title=:title or t.holidayDate=:holidayDate) AND t.deleted =:b AND t.id!=:id")
		
	 public Boolean checkexistByTitleAndHolidayDate(String title, Date holidayDate);
	 
	 @Query("SELECT COUNT(h) > 0 FROM Holidays h WHERE (h.holidayDate = :holidayDate And h.title = :title) OR  (h.holidayDate = :holidayDate AND h.title != :title  ) ")
		//  @Query("SELECT COUNT(t) > 0 FROM Holidays t WHERE (t.title=:title or t.holidayDate=:holidayDate) AND t.deleted =:b AND t.id!=:id")
			
		 public Boolean checkexistByTitleAndHolidayDate2(String title, Date holidayDate);

	  @Query("SELECT COUNT(t) > 0 FROM Holidays t WHERE (t.title=:title And t.holidayDate=:holidayDate) AND t.deleted =:b AND t.id!=:id")
			public Boolean existsByTitleAndHolidayDateAndDeletedAndIdIsNot(String title,Date holidayDate, boolean b,Integer id);

	public boolean existsByHolidayDate(Date checkDate);
		
	  
}
