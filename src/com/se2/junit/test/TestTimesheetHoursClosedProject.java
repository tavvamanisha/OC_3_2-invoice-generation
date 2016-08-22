package com.se2.junit.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.se2.invoice.forms.dataConnectionObject;
import com.se2.invoice.generate.GenerateInvoice;

public class TestTimesheetHoursClosedProject {
	
	String[] args;

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void test() {
		
		dataConnectionObject dco = new dataConnectionObject();
		Connection conn;
		try {
			conn = dco.getConnection();
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM people_timesheet "
					+ "WHERE PersonName=? AND ProjectNumber=? AND Week_start_date=?");
			pst.setString(1, "Amber Monarrez");
			pst.setString(2, "1001");
			pst.setString(3, "2016-08-08");
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()){
				int mon = rs.getInt("mon");
				int tue = rs.getInt("tue");
				int wed = rs.getInt("wed");
				int thu = rs.getInt("thu");
				int fri = rs.getInt("fri");
				int sat = rs.getInt("sat");
				int sun = rs.getInt("sun");
				
				assertEquals(0, mon+tue+wed+thu+fri+sat+sun);
			} else {
				fail();
			}
			
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
