package loginController;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;

import com.google.gson.Gson;

import controllers.LoginController;
import dataBase.DataBase;
import employee.Employee;
import ocsf.server.ConnectionToClient;
import subscriber.Subscriber;

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
		 emp=new Employee(id, password, firstName, lastName, email, typeOfEmployee, ParkName);
	}

	@Test
	void testClientLogin() throws SQLException {
		DataBase.setInstance(db);
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
		assertEquals(gson.toJson(sub),cont.loginRouter("clientLogin","abcd", con) );
		verify(con,times(2)).setInfo(anyString(), anyObject());
		
	}
	
	
	
	
// ?	
	@Test
	void testClientLoginIsnotFound() throws SQLException
	{
		DataBase.setInstance(db);
		when(db.search(anyString())).thenReturn(rs);
		when(rs.getRow()).thenReturn(0);
		assertEquals("update faild",cont.loginRouter("clientLogin","1234", con) );
		
	}
	
	@Test
	void testClientLoginAllreadyConnected() throws SQLException
	{
		DataBase.setInstance(db);
		when(db.search(anyString())).thenReturn(rs);
		when(rs.getRow()).thenReturn(1);
		when(rs.getInt("connected")).thenReturn(1);
		assertEquals("all ready connected",cont.loginRouter("clientLogin","1234", con) );
	}
// is it need to check error ?
	
	
	@Test
	void employeeLogIn() throws SQLException
	{
		DataBase.setInstance(db);
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
		assertEquals(gson.toJson(emp),cont.loginRouter("employeeLogIn",gson.toJson(new Employee("1234","5678")), con) );
		
	}
}
