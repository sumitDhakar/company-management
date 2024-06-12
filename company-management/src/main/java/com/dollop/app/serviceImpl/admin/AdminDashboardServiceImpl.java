package com.dollop.app.serviceImpl.admin;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dollop.app.entity.payload.admin.AdminDashBoardResponse;
import com.dollop.app.repository.TasksRepository;
import com.dollop.app.repository.UsersRepository;
import com.dollop.app.service.admin.IAdminDashboardService;
import com.dollop.app.service.admin.IUsersService;

@Service
public class AdminDashboardServiceImpl implements IAdminDashboardService {

	@Autowired
	private IUsersService usersService;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private TasksRepository tasksRepository;

	  public static Date getFirstDayOfYear(int year) {
	        // Create a java.sql.Date for January 1 of the given year
	        return Date.valueOf(year + "-01-01");
	    }
	// fetching details of admin details
	@Override
	public AdminDashBoardResponse fetchDetails() {
		List<Object[]> details = this.usersService.fetchAdminDashboardDetails();
		AdminDashBoardResponse response = new AdminDashBoardResponse();
		LocalDate date = LocalDate.now();
		List<Object[]> fetchAdminDashboardStatistics = this.usersRepository.fetchAdminDashboardStatistics(date);
		List<Object[]> fetchAdminMonthOverAllStatics = this.usersRepository.fetchAdminMonthOverAllStatics();
		List<Object[]> fetchBarChartData = this.fetchBarChartData();
		List<Double> netProfit = new ArrayList<>();
		List<Double> revenue = new ArrayList<>();
		Integer cYear   = LocalDate.now().getYear();
		List<Date> years = new ArrayList<>();
           years.addAll(Arrays.asList(getFirstDayOfYear(cYear-4),getFirstDayOfYear(cYear-3),getFirstDayOfYear(cYear-2),getFirstDayOfYear(cYear-1),getFirstDayOfYear(cYear)));
		Map<String, Map<Date, Double>> categoryDataMap = new HashMap<>();
		for (Object[] item : fetchBarChartData) {
			String category = (String) item[0];
			Double value = (Double) item[1];
//			Date year = (Date) item[2];
        
			// If the category is not present in the map, add it
			if (!categoryDataMap.containsKey(category)) {
				categoryDataMap.put(category, new HashMap<>());
			}
			// Put the value in the map for the corresponding category and year
			categoryDataMap.get(category).put(years.get(0), value);

			// Add the year to the years list if it's not already present
			
		}
		List<Long> months = new ArrayList<>();
		months.addAll(Arrays.asList((long)LocalDate.now().getMonth().getValue()-1,(long)LocalDate.now().getMonth().getValue()));
			Map<String, Map<Long, Double>> categoryDataMap2 = new HashMap<>();
		for (Object[] item : fetchAdminMonthOverAllStatics) {
			String category = (String) item[0];
			Double value = (Double) item[1];
			long month = (Long) item[2];

			// If the category is not present in the map, add it
			if (!categoryDataMap2.containsKey(category)) {
				categoryDataMap2.put(category, new HashMap<>());
			}

			// Put the value in the map for the corresponding category and year
			categoryDataMap2.get(category).put(month, value);

			// Add the year to the years list if it's not already present
			
		}

		// Populate netProfit and revenue arrays based on the map
		for (int i=0;i<5;i++) {
			// If the category is "amount", get the value from the map, or put 0 if not
			// present
			netProfit.add(categoryDataMap.getOrDefault("amount", new HashMap<>()).getOrDefault(years.get(i), 0.0));

			// If the category is "net_salary", get the value from the map, or put 0 if not
			// present
			revenue.add(categoryDataMap.getOrDefault("net_salary", new HashMap<>()).getOrDefault(years.get(i), 0.0));
//			System.err.println("netProfit  "+netProfit);
//			System.err.println("revenue  "+revenue);
		}
		List<Double> employeeCount = new ArrayList<>();
		List<Double> earningsCount = new ArrayList<>();
		List<Double> expensesCount = new ArrayList<>();
		
		
//		System.err.println("earningsCount  "+categoryDataMap2.get("amount"));
//		System.err.println("employeeCount  "+categoryDataMap2.get("users"));
//		System.err.println("expensesCount  "+categoryDataMap2.get("expenses"));
//	
		for (int i=0;i<months.size();i++) {	
			earningsCount.add(categoryDataMap2.getOrDefault("amount", new HashMap<>()).getOrDefault(months.get(i), 0.0));
			employeeCount.add(categoryDataMap2.getOrDefault("users", new HashMap<>()).getOrDefault(months.get(i), 0.0));
			expensesCount.add(categoryDataMap2.getOrDefault("expenses", new HashMap<>()).getOrDefault(months.get(i), 0.0));
//			System.err.println("earningsCount  "+earningsCount);
//			System.err.println("employeeCount  "+employeeCount);
//			System.err.println("expensesCount  "+expensesCount);
			
	   	}
         List<Double> allMonthStatics = new ArrayList<>();
         allMonthStatics.addAll(employeeCount);
         allMonthStatics.addAll(earningsCount);
         allMonthStatics.addAll(expensesCount);
           
		details.forEach(object -> {

			response.setTotalProjects((Long) details.get(0)[1]);
			response.setTotalClients((Long) details.get(1)[1]);
			response.setTotalTasks((Long) details.get(2)[1]);
			response.setTotalEmployees((Long) details.get(3)[1]);
			response.setStaticsOfCompany(fetchAdminDashboardStatistics);
			response.setNetProfit(netProfit);
			response.setRevenue(revenue);
			response.setYears(Arrays.asList((long)cYear-4,(long)cYear-3,(long)cYear-2,(long)cYear-1,(long)cYear));
			response.setAllMonthStatics(allMonthStatics);
		});
		return response;
	}

	@Override
	public Object fetchTaskDetailsForAdminDash() {
		LocalDate currentDate = LocalDate.now();

		Object data = tasksRepository.findByStatusDetailsOfTasks(currentDate);

		return data;
	}

	@Override
	public List<Object[]> fetchBarChartData() {

		List<Object[]> fetchAdminChartData = this.usersRepository.fetchAdminChartData();
		
		return fetchAdminChartData;
	}

	@Override
	public List<Object[]> fetchAdminDashBoardOverAllStatics() {

		return this.fetchAdminDashBoardOverAllStatics();
	}

}
