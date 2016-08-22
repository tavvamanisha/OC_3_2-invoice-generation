package com.se2.junit.test;

import static org.junit.Assert.*;

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

public class TestClientByProject {

	@Before
	public void setUp() throws Exception {
		/*String[] args = new String[16];
		args[0]="Accountant";
		args[1]="Parag";
		args[2]="1003";
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
		args[15]="17/08/2016";*/
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
			PreparedStatement pst = conn.prepareStatement("SELECT C.Name FROM Client C, Project P "
					+ "WHERE C.Client_Number=P.Client AND P.ProjectNumber=?");
			pst.setInt(1, 1003);
			ResultSet rs = pst.executeQuery();
			
			if(rs.next()){
				assertEquals("Morlong Associates", rs.getString("Name"));
			}
			
		} catch (IOException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
