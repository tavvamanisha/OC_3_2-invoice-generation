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

public class TestTimesheetSubmitted {
	
	String[] args;

	@Before
	public void setUp() throws Exception {
		/*args = new String[16];
		args[0]="Accountant";
		args[1]="Parag";
		args[2]="9000";
		args[3]="Morlong Associates";
		args[4]="7 Eads St";
		args[5]="ABCD";
		args[6]="Cook";
		args[7]="IL";
		args[8]="60632";
		args[9]="100003";
		args[10]="Net 20 days";
		args[11]="Monthly";
		args[12]="Project";
		args[13]="parag2306@gmail.com";
		args[14]="Project Management";
		args[15]="2016.08.17";
		
		GenerateInvoice generateInvoice = new GenerateInvoice();
		args = generateInvoice.createPDF(args);*/
		
		//GenerateInvoice.updateDB(args);
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
				assertEquals("Submitted", rs.getString("People_ts_status"));
			} else {
				fail();
			}
			
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
