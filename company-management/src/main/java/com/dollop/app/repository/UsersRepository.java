package com.dollop.app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;

import com.dollop.app.entity.UserRoles;
import com.dollop.app.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {

	public Optional<Users> findByfirstName(String firstName);

	public Optional<Users> findByjobTitle(String jobTitle);

	public Page<Users> findByDeleted(Pageable page, Boolean deleted);

	public Page<Users> findByDeletedAndEmailNot(Pageable page, Boolean deleted, String meail);

	public Page<Users> findByUserRolesInAndDeleted(Pageable pageable, Set<UserRoles> userType, Boolean deleted);

	public Optional<Users> findByIdAndDeleted(Integer id, boolean b);

	@Query(value = "SELECT 'projects' as projects, COUNT(*) as count FROM projects where deleted=false UNION ALL \r\n"
			+ " SELECT 'clients' as clients, COUNT(*) as count FROM clients where deleted=false  UNION ALL \r\n"
			+ " SELECT 'tasks' as tasks, COUNT(*) as count FROM tasks where deleted=false UNION ALL \r\n"
			+ "	SELECT 'user_roles' AS user_roles, COUNT(ur.id) AS count\r\n"
			+ "FROM Users u JOIN user_roles ur ON u.id = ur.u_id\r\n"
			+ "WHERE ur.role_id = 2 and u.deleted=false;", nativeQuery = true)
	public List<Object[]> fetchAdminDashboardDetails();

//	SELECT Count(*) from attendance a where a.in_time LIKE %:date% order by a.in_time limit 1"
	@Query(value = "SELECT 'projects' as projects,  COUNT(*) as projectTotal,SUM(CASE WHEN p.status = 'Completed' THEN 1 ELSE 0 END) AS projectstatus  FROM projects p where deleted=false UNION ALL "
			+ "			SELECT 'invoices' as invoives,   COUNT(*) as invoiceTotal , SUM(CASE WHEN i.status != 'Paid' THEN 1 ELSE 0 END) AS pendingInvoice "
			+ "			FROM Invoices i where i.deleted=false UNION ALL "
			+ "			SELECT 'tickets' as tickets, COUNT(*) as ticketstotal  , SUM(CASE WHEN t.status = 'Open' THEN 1 ELSE 0 END) AS openTickets  "
			+ "			 FROM Tickets t where t.deleted=false  UNION ALL "
			+ "			SELECT 'tickets' as tickets, COUNT(*) as ticketstotal  , SUM(CASE WHEN t.status = 'Closed' THEN 1 ELSE 0 END) AS openTickets  "
			+ "			 FROM Tickets t where t.deleted=false   UNION ALL"
			+ "      SELECT 'attendance' as attendance, COUNT(Distinct (a.user_id)) as totalEmployees  , SUM(CASE WHEN  Date(a.in_time)=Date(?1)THEN 1 ELSE 0 END ) AS absentEmployee  "
			+ "		 FROM attendance a where Date(a.in_time)=Date(?1)    ", nativeQuery = true)
	public List<Object[]> fetchAdminDashboardStatistics(LocalDate date);

	@Query("select u from Users u where u.email =:email and u.deleted = false")
	public Optional<Users> findByEmail(String email);


	@Query("Select u.Id from Users as u where u.email=:email")
	public Optional<Integer> getIdByEmail(String email);

	@Query(value = " select count(*) from projects p inner join project_members pm on pm.project_id=p.id  WHERE pm.user_id =:userId and p.deleted =false "
			+ " UNION ALL select count(*) from tasks t INNER join task_members tm on tm.task_id=t.id where tm.members_id=:userId and t.status = :status and t.deleted =false"
			+ " UNION ALL select count(*) from tasks t INNER join task_members tm on tm.task_id=t.id where tm.members_id=:userId  "
			+ " UNION ALL select count(*) from leave_applications  WHERE  applicant_id_id =:userId  and deleted =false and checked_by_id  IS NOT NULL AND status = 'Approved'  "
			+ "UNION ALL SELECT COALESCE(SUM(over_time_hours), 0) AS totalHours FROM over_time WHERE user_id_id =:userId "
			+ "  AND is_deleted = false AND approved_by_id IS NOT NULL AND status = 'Approved' "
			+ "  AND MONTH(over_time_date) = MONTH(CURDATE())    AND YEAR(over_time_date) = YEAR(CURDATE()) ", nativeQuery = true)

	public List<Object[]> fetchEmployeeDashboardDetails(Integer userId, String status);

	@Query(value = "SELECT 'amount' AS category, " + "       IF(SUM(amount) IS NULL, 0, SUM(amount)) AS amount, "
			+ "       YEAR(payment_date) AS billDate  " + " FROM invoice_payments \r\n"
			+ " WHERE YEAR(payment_date) BETWEEN YEAR(CURDATE() - INTERVAL 4 YEAR) AND YEAR(CURDATE())\r\n"
			+ " GROUP BY YEAR(payment_date)\r\n" + " UNION ALL " + " SELECT 'net_salary' AS category,\r\n"
			+ "       IF(SUM(net_salary) IS NULL, 0, SUM(net_salary)) AS outcome,  "
			+ "       YEAR(payment_date) AS year  " + " FROM staff_salary "
			+ " WHERE YEAR(payment_date) BETWEEN YEAR(CURDATE() - INTERVAL 4 YEAR) AND YEAR(CURDATE())  "
			+ " GROUP BY YEAR(payment_date);\r\n" + "", nativeQuery = true)
	public List<Object[]> fetchAdminChartData();

	@Query(value = " SELECT category, IFNULL(value, 0) AS amount, month FROM (\r\n"
			+ "    SELECT 'amount' AS category,\r\n" + "        SUM(amount) AS value,\r\n"
			+ "        MONTH(payment_date) AS month  \r\n" + "    FROM invoice_payments\r\n"
			+ "    WHERE YEAR(payment_date) = YEAR(CURDATE())\r\n"
			+ "        AND MONTH(payment_date) IN (MONTH(CURDATE()), MONTH(CURDATE() - INTERVAL 1 MONTH)) And deleted=false "
			+ "    GROUP BY MONTH(payment_date)\r\n" + "    \r\n" + "    UNION ALL\r\n" + "    \r\n"
			+ "    SELECT 'users' AS category,\r\n" + "        COUNT(DISTINCT u.id) AS value,\r\n"
			+ "        MONTH(u.created_at) AS month\r\n" + "    FROM users u\r\n"
			+ "    JOIN user_roles ur ON ur.role_id = 2\r\n" + "    WHERE YEAR(u.created_at) = YEAR(CURDATE())\r\n"
			+ "        AND MONTH(u.created_at) IN (MONTH(CURDATE()), MONTH(CURDATE() - INTERVAL 1 MONTH)) AND u.deleted=false\r\n"
			+ "    GROUP BY MONTH(u.created_at) \r\n" + "    \r\n" + "    UNION ALL\r\n" + "    \r\n"
			+ "    SELECT 'expenses' AS category,\r\n" + "        SUM(amount) AS value,\r\n"
			+ "        MONTH(expense_date) AS month\r\n" + "    FROM expenses\r\n"
			+ "    WHERE YEAR(expense_date) = YEAR(CURDATE())\r\n"
			+ "        AND MONTH(expense_date) IN (MONTH(CURDATE()), MONTH(CURDATE() - INTERVAL 1 MONTH))  And status='Approved' And deleted=false "
			+ "    GROUP BY MONTH(expense_date)\r\n" + ") AS CombinedResults;", nativeQuery = true)
	public List<Object[]> fetchAdminMonthOverAllStatics();

	public Optional<Users> findByEmailAndDeleted(String email, boolean b);

	@Query("SELECT u FROM Users u WHERE " + "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
			+ "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	Page<Users> searchUsers(@Param("searchTerm") String searchTerm, Pageable pageable);

	boolean existsByEmailAndIdNot(String email, Long userId);

	public boolean existsByEmailAndIdNot(String email, Integer id);

	public boolean existsByEmailAndDeleted(String email, boolean b);

	public boolean existsByEmailAndDeletedAndIdNot(String email, boolean b, Integer id);

}