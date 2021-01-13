package loginController;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import com.google.gson.Gson;
import controllers.LoginController;
import dataBase.DataBase;
import employee.Employee;
import ocsf.server.ConnectionToClient;
import subscriber.Subscriber;

/**
 * Test the department manager visitors report.
 * @author Shay Maryuma
 * @author Ziv Tziyonit
 *
 */
class LoginControllerTest {
	Gson gson=new Gson();
	@Mock
	Employee employee;
	@Mock 
	ConnectionToClient con;
	@Mock
	 DataBase db;
	@Mock
	ResultSet rs;
	
	LoginController cont;
	String id;
	String password;
	String subscriberid;
	String firstName;
	String lastName;
	String phone;
	String email;
	String numOfMembers;
	String creditCardNumber;
	String subscriberTypre;
	String typeOfEmployee;
	String ParkName;
	Subscriber sub;
	Subscriber subscriber2;
	Employee emp;
	
	/**
	 * Create mocks for connection, prepareStatement, database, result set and department manager system controller.
	 * Include expected outputs for two subscribers and one employee.
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		con = mock(ConnectionToClient.class);
		employee=mock(Employee.class);
		db= mock(DataBase.class);
		rs=mock(ResultSet.class);
		cont=LoginController.getInstance();
		
		id="311379911";
		subscriberid="6116955";
		firstName="ziv";
		lastName="tziyonit";
		phone="0542345635";
		email="zivi94@gmail.com";
		numOfMembers="6";
		password="5678";
		creditCardNumber="3123432534";
		typeOfEmployee="park manager";
		subscriberTypre="family";
		ParkName="Banias";
		
		sub = new Subscriber(id,subscriberid,firstName,lastName,phone,email,numOfMembers,creditCardNumber,subscriberTypre);
		subscriber2 = new Subscriber("1234", null, null, null, null, null, null, null, null);
		emp = new Employee(id, password, firstName, lastName, email, typeOfEmployee, ParkName);
		
	}

	/**
	 * Check if subscriber has logged in to the server successfully.
	 * Expected: gson.toJson(sub)
	 * @throws SQLException
	 */
	@Test
	void testClientLogin() throws SQLException {
		DataBase.setInstance(db);
		String expected = gson.toJson(sub);
		when(db.search(anyString())).thenReturn(rs);
		when(rs.getRow()).thenReturn(1);
		when(rs.getInt("connected")).thenReturn(0);
		when(rs.getString("id")).thenReturn(id);
		when(rs.getString("subscriberid")).thenReturn(subscriberid);
		when(rs.getString("firstName")).thenReturn(firstName);
		when(rs.getString("lastName")).thenReturn(lastName);
		when(rs.getString("phone")).thenReturn(phone);
		when(rs.getString("email")).thenReturn(email);
		when(rs.getString("numOfMembers")).thenReturn(numOfMembers);
		when(rs.getString("creditCardNumber")).thenReturn(creditCardNumber);
		when(rs.getString("subscriberTypre")).thenReturn(subscriberTypre);
		when(DataBase.getInstance().update(anyString())).thenReturn(true);
		assertEquals(expected,cont.loginRouter("clientLogin","abcd", con) );
		verify(con,times(2)).setInfo(anyString(), anyObject());		
	}

	/**
	 * Check if guest has logged in to the server successfully.
	 * Expected:  gson.toJson(subscriber2)
	 * @throws SQLException
	 */
	@Test
	void testGuestLogin() throws SQLException
	{
		DataBase.setInstance(db);
		String expected =  gson.toJson(subscriber2);
		when(db.search(anyString())).thenReturn(rs);
		when(rs.getRow()).thenReturn(0);
		when(db.update(anyString())).thenReturn(true);
		assertEquals(expected,cont.loginRouter("clientLogin","1234", con) );
		verify(con,times(2)).setInfo(anyString(), anyObject());
	}
		
	/**
	 * Check if the query that we want to send to the database has failed.
	 * Expected "update faild".
	 * @throws SQLException
	 */
	@Test
	void testGuestLoginUpdateFailed() throws SQLException
	{
		DataBase.setInstance(db);
		String expected = "update faild";
		when(db.search(anyString())).thenReturn(rs);
		when(rs.getRow()).thenReturn(0);
		when(db.update(anyString())).thenReturn(false);
		assertEquals(expected,cont.loginRouter("clientLogin","1234", con) );
	}
	
	
	/**
	 * Check if the guest is already connected to the server.
	 * Connection is determined by value 1 for connected and 0 for disconnected.
	 * Expected "all ready connected". (already connected)
	 * @throws SQLException
	 */
	@Test
	void testGuestLoginAlreadyConnected() throws SQLException
	{
		DataBase.setInstance(db);
		String expected = "all ready connected";
		when(db.search(anyString())).thenReturn(rs);
		when(rs.getRow()).thenReturn(0).thenReturn(1); 
		assertEquals(expected,cont.loginRouter("clientLogin","1234", con) );	
	}
	
	/**
	 * Check if the subscriber is already connected to the server.
	 * Connection is determined by value 1 for connected and 0 for disconnected.
	 * Expected "all ready connected". (already connected)
	 * @throws SQLException
	 */
	@Test
	void testClientLoginAlreadyConnected() throws SQLException
	{
		DataBase.setInstance(db);
		String expected = "all ready connected";
		when(db.search(anyString())).thenReturn(rs);
		when(rs.getRow()).thenReturn(1);
		when(rs.getInt("connected")).thenReturn(1);
		assertEquals(expected,cont.loginRouter("clientLogin","1234", con) );
	}

	
	/**
	 * Check if the query(update) has failed.
	 * Expected: "error"
	 * @throws SQLException
	 */
	@Test
	void testClientLoginError() throws SQLException
	{
		DataBase.setInstance(db);
		String expected = "error";
		when(db.search(anyString())).thenReturn(rs);
		when(rs.getRow()).thenReturn(1);
		when(rs.getInt("connected")).thenReturn(0);
		when(rs.getString("id")).thenReturn(id);
		when(rs.getString("subscriberid")).thenReturn(subscriberid);
		when(rs.getString("firstName")).thenReturn(firstName);
		when(rs.getString("lastName")).thenReturn(lastName);
		when(rs.getString("phone")).thenReturn(phone);
		when(rs.getString("email")).thenReturn(email);
		when(rs.getString("numOfMembers")).thenReturn(numOfMembers);
		when(rs.getString("creditCardNumber")).thenReturn(creditCardNumber);
		when(rs.getString("subscriberTypre")).thenReturn(subscriberTypre);
		when(DataBase.getInstance().update(anyString())).thenReturn(false);
		assertEquals(expected,cont.loginRouter("clientLogin","1234", con) );
		verify(con,times(2)).setInfo(anyString(), anyObject());
	}
	
	
	/**
	 * Check if employee has logged in to the server successfully.
	 * Expected: gson.toJson(emp)
	 * @throws SQLException
	 */
	@Test
	void testEmployeeLogIn() throws SQLException
	{
		DataBase.setInstance(db);
		String expected = gson.toJson(emp);
		when(db.search(anyString())).thenReturn(rs);
		when(employee.getEmployeeId()).thenReturn(id);
		when(rs.getRow()).thenReturn(1);
		when(rs.getInt("connected")).thenReturn(0);
		when(employee.getPassword()).thenReturn(password);
		when(rs.getString("password")).thenReturn(password);
		when(rs.getString("employeeId")).thenReturn(id);
		when(rs.getString("password")).thenReturn(password);
		when(rs.getString("name")).thenReturn(firstName);
		when(rs.getString("lasstName")).thenReturn(lastName);
		when(rs.getString("email")).thenReturn(email);
		when(rs.getString("typeOfEmployee")).thenReturn(typeOfEmployee);
		when(rs.getString("parkName")).thenReturn(ParkName);
		when(employee.getEmployeeId()).thenReturn(id);
		when(db.update(anyString())).thenReturn(true);
		assertEquals(expected,cont.loginRouter("employeeLogIn",gson.toJson(new Employee("1234","5678")), con) );	
	}
	
	/**
	 * Check if the query that we sent to the server has returned 0 rows.
	 * @throws SQLException
	 * Expected: "employee not found"
	 */
	@Test
	void testEmployeeLogInNotFound() throws SQLException
	{
		DataBase.setInstance(db);
		String expected = "employee not found";
		when(db.search(anyString())).thenReturn(rs);
		when(employee.getEmployeeId()).thenReturn(id);
		when(rs.getRow()).thenReturn(0);	
		assertEquals(expected,cont.loginRouter("employeeLogIn",gson.toJson(new Employee("1234","5678")), con) );	
	}
	
	
	
	/**
	 * Check if the employee is already connected to the server.
	 * Connection is determined by value 1 for connected and 0 for disconnected.
	 * Expected "all ready connected". (already connected)
	 * @throws SQLException
	 */
	@Test
	void testEmployeeLogInAlreadyConnected() throws SQLException
	{
		DataBase.setInstance(db);
		String expected = "already connected";
		when(db.search(anyString())).thenReturn(rs);
		when(employee.getEmployeeId()).thenReturn(id);
		when(rs.getRow()).thenReturn(1);
		when(rs.getInt("connected")).thenReturn(1);	
		assertEquals(expected,cont.loginRouter("employeeLogIn",gson.toJson(new Employee("1234","5678")), con) );	
	}
		 
	/**
	 * Check if the password that the employee has entered is different than the data in the DB.
	 * Expected: "wrong password"
	 * @throws SQLException
	 */
	@Test
	void testEmployeeLogInWrongPasword() throws SQLException
	{
		DataBase.setInstance(db);
		String expected = "wrong password";
		when(db.search(anyString())).thenReturn(rs);
		when(employee.getEmployeeId()).thenReturn(id);
		when(rs.getRow()).thenReturn(1);
		when(rs.getInt("connected")).thenReturn(0);
		when(employee.getPassword()).thenReturn(password);
		when(rs.getString("password")).thenReturn(password);
		when(rs.getString("employeeId")).thenReturn(id);
		when(rs.getString("password")).thenReturn(password);
		when(rs.getString("name")).thenReturn(firstName);
		when(rs.getString("lasstName")).thenReturn(lastName);
		when(rs.getString("email")).thenReturn(email);
		when(rs.getString("typeOfEmployee")).thenReturn(typeOfEmployee);
		when(rs.getString("parkName")).thenReturn(ParkName);
		when(employee.getEmployeeId()).thenReturn(id);
		when(db.update(anyString())).thenReturn(false);
		assertEquals(expected,cont.loginRouter("employeeLogIn",gson.toJson(new Employee("1234","5679")), con) );	
	}
	
	
	
}
