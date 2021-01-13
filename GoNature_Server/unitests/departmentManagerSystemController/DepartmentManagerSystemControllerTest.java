package departmentManagerSystemController;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import controllers.DepartmentManagerSystemController;
import dataBase.DataBase;

class DepartmentManagerSystemControllerTest {
	private int i=0;
	@Mock
	Connection con;
	@Mock
	PreparedStatement pre;
	@Mock
	PreparedStatement quer;
	@Mock
	 DataBase db;
	@Mock
	ResultSet rs;
	DepartmentManagerSystemController cont;
	@BeforeEach
	void setUp() throws Exception {

		con = mock(Connection.class);
		pre = mock(PreparedStatement.class);
		db= mock(DataBase.class);
		rs=mock(ResultSet.class);
		cont=DepartmentManagerSystemController.getInstance();
		
	}

	@Test
	void testEntery() throws SQLException {
		i=0;
		int[] arr1 = new int[] {12,14};
		int[] arr2 = new int[] {6,5};
		
		DataBase.setInstance(db);
		when(DataBase.getInstance().getConnection()).thenReturn(con);
		when(con.prepareStatement(anyString())).thenReturn(pre);
		when(db.search(pre)).thenReturn(rs);
		
		when(rs.next()).thenAnswer(new Answer<Boolean>() {

			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
			
				if (i<2)
				{
					i++;
					return true;
				}
				return false;
			}
			
		});
		when(rs.getRow()).thenReturn(2);
		when (rs.getInt(1)).thenAnswer((a)->{return arr1[0];}).thenAnswer((a)->{return arr1[1];});
		when (rs.getInt(2)).thenAnswer((a)->{return arr2[0];}).thenAnswer((a)->{return arr2[1];});
		
	
		
	
		assertEquals("12,6 14,5 ", cont.getFunc("getEntryDetailsByHours","x 1 1 1 x x x",null));;
	}
	
	
	
	
	
	
	
	@Test
	void testExit() throws SQLException {
		i=0;
		int[] arr1 = new int[] {17,12};
		int[] arr2 = new int[] {8,3};
		
		DataBase.setInstance(db);
		when(DataBase.getInstance().getConnection()).thenReturn(con);
		when(con.prepareStatement(anyString())).thenReturn(pre);
		when(db.search(pre)).thenReturn(rs);
		
		when(rs.next()).thenAnswer(new Answer<Boolean>() {

			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
			
				if (i<2)
				{
					i++;
					return true;
				}
				return false;
			}
			
		});
		when(rs.getRow()).thenReturn(2);
		when (rs.getInt(1)).thenAnswer((a)->{return arr1[0];}).thenAnswer((a)->{return arr1[1];});
		when (rs.getInt(2)).thenAnswer((a)->{return arr2[0];}).thenAnswer((a)->{return arr2[1];});
		
	
		
	
		assertEquals("17,8 12,3 ", cont.getFunc("getExitDetailsByHours","x 1 1 1 x x x",null));;
		
		
		
	}
	

}
