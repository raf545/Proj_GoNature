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

/**
 * Test the department manager visitors report.
 * 
 * @author Shay Maryuma
 * @author Ziv Tziyonit
 *
 */
class DepartmentManagerSystemControllerTest {
	private int i = 0;
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

	/**
	 * Create mocks for connection, prepareStatement, database, result set and
	 * department manager system controller.
	 * 
	 * @throws Exception
	 */
	@BeforeEach
	void setUp() throws Exception {

		con = mock(Connection.class);
		pre = mock(PreparedStatement.class);
		db = mock(DataBase.class);
		rs = mock(ResultSet.class);
		cont = DepartmentManagerSystemController.getInstance();
	}

	/**
	 * Check if the entry function in department manager's class is working
	 * properly. arr1 is the array that holds hours of entering the park. arr2 is
	 * the array that holds the amount of visitors in the specific hour. index i
	 * will be the size of the table of the result set. expected output: "12,6 14,5
	 * "
	 * 
	 * @throws SQLException
	 */
	@Test
	void testEntery() throws SQLException {
		i = 0;
		int[] arr1 = new int[] { 12, 14 };
		int[] arr2 = new int[] { 6, 5 };

		String expected = "12,6 14,5 ";
		DataBase.setInstance(db);
		when(DataBase.getInstance().getConnection()).thenReturn(con);
		when(con.prepareStatement(anyString())).thenReturn(pre);
		when(db.search(pre)).thenReturn(rs);
		when(rs.next()).thenAnswer(new Answer<Boolean>() {

			// We will override the function in order to achieve an iterator the size of the
			// expected answer.
			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {
				if (i < 2) {
					i++;
					return true;
				}
				return false;
			}
		});
		when(rs.getRow()).thenReturn(2);
		when(rs.getInt(1)).thenAnswer((a) -> {
			return arr1[0];
		}).thenAnswer((a) -> {
			return arr1[1];
		});
		when(rs.getInt(2)).thenAnswer((a) -> {
			return arr2[0];
		}).thenAnswer((a) -> {
			return arr2[1];
		});
		assertEquals(expected, cont.getFunc("getEntryDetailsByHours", "x 1 1 1 x x x", null));
		;
	}

	/**
	 * Check if the exit function in department manager's class is working properly.
	 * arr1 is the array that holds hours of exiting the park. arr2 is the array
	 * that holds the amount of visitors in the specific hour. index i will be the
	 * size of the table of the result set. expected output: "17,8 12,3 "
	 * 
	 * @throws SQLException
	 */
	@Test
	void testExit() throws SQLException {
		i = 0;
		int[] expectedHours = new int[] { 17, 12 };
		int[] expectedAmounts = new int[] { 8, 3 };

		String expected = "17,8 12,3 ";
		DataBase.setInstance(db);
		when(DataBase.getInstance().getConnection()).thenReturn(con);
		when(con.prepareStatement(anyString())).thenReturn(pre);
		when(db.search(pre)).thenReturn(rs);
		when(rs.next()).thenAnswer(new Answer<Boolean>() {

			@Override
			public Boolean answer(InvocationOnMock invocation) throws Throwable {

				if (i < expectedHours.length) {
					i++;
					return true;
				}
				return false;
			}

		});
		when(rs.getRow()).thenReturn(2);
		when(rs.getInt(1)).thenAnswer((a) -> {
			return expectedHours[0];
		}).thenAnswer((a) -> {
			return expectedHours[1];
		});
		when(rs.getInt(2)).thenAnswer((a) -> {
			return expectedAmounts[0];
		}).thenAnswer((a) -> {
			return expectedAmounts[1];
		});
		assertEquals(expected, cont.getFunc("getExitDetailsByHours", "x 1 1 1 x x x", null));
		;

	}

}
