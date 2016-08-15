package com.se2.invoice.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.jdesktop.swingx.JXDatePicker;

import com.se2.invoice.forms.Home;
import com.se2.invoice.forms.dataConnectionObject;

public class Project_Report extends JFrame {

	private JPanel contentPane;
	
	JTable table = new JTable();
	DefaultTableModel model = new DefaultTableModel();
	BufferedImage backgroundImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Project_Report frame = new Project_Report(args);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Project_Report(String[] args) {
		
		try {
			backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		dataConnectionObject dco = new dataConnectionObject();
		Connection conn;
		String[][] obj1 = new String[10000][4];
		int i = 0, projectno = 0;
		
		try {
			conn = dco.getConnection();
			PreparedStatement pst = conn.prepareStatement("SELECT CURDATE()-P.Last_invoice_date AS InvoiceDateDiff, "
					+ "CURDATE()-P.StartDate AS StartDateDiff, "
					+ "P.ProjectNumber, P.ProjectName, P.ProjectManager, "
					+ "P.EndDate-P.StartDate AS TIMELINE, P.BUDGET, "
					+ "C.Client_Number, C.Name, C.Address1, C.Address2, C.City, C.State, C.ZIP, C.Email, "
					+ "C.Contact, C.Invoice_Frequency, C.Billing_terms, C.Invoice_Grouping "
					+ "FROM `project` P, client C "
					+ "WHERE P.deactivated=0 " + "AND P.Status IN ('In Progress', 'In Progres') "
					+ "AND C.deactivated=0 "
					+ "AND C.Client_Number=P.Client");

			ResultSet rs = pst.executeQuery();
			
			HashMap<String, String> hashProjectClientNumber = new HashMap<String, String>();
			HashMap<String, String> hashProjectClientName = new HashMap<String, String>();
			HashMap<String, String> hashProjectClientAdd1 = new HashMap<String, String>();
			HashMap<String, String> hashProjectClientAdd2 = new HashMap<String, String>();
			HashMap<String, String> hashProjectClientCity = new HashMap<String, String>();
			HashMap<String, String> hashProjectClientState = new HashMap<String, String>();
			HashMap<String, String> hashProjectClientZip = new HashMap<String, String>();
			HashMap<String, String> hashProjectClientEmail = new HashMap<String, String>();
			HashMap<String, String> hashProjectClientInvoiceGrouping = new HashMap<String, String>();
			HashMap<String, String> hashProjectClientInvoiceFrequency = new HashMap<String, String>();
			HashMap<String, String> hashProjectClientPaymentTerms = new HashMap<String, String>();

			while (rs.next()) {
				
				int timeline=rs.getInt("TIMELINE");
				int startDateDiff = rs.getInt("StartDateDiff");
				float timelineCompletion = startDateDiff*100/timeline;
				
				obj1[i][0] = String.valueOf(rs.getInt("P.ProjectNumber"));
				obj1[i][1] = rs.getString("P.ProjectName");
				obj1[i][2] = String.valueOf(timelineCompletion);
				
				projectno=rs.getInt("ProjectNumber");
				hashProjectClientAdd1.put(obj1[i][0], rs.getString("Address1"));
				hashProjectClientAdd2.put(obj1[i][0], rs.getString("Address2"));
				hashProjectClientCity.put(obj1[i][0], rs.getString("City"));
				hashProjectClientEmail.put(obj1[i][0], rs.getString("Email"));
				hashProjectClientInvoiceGrouping.put(obj1[i][0], rs.getString("Invoice_Grouping"));
				hashProjectClientName.put(obj1[i][0], rs.getString("Name"));
				hashProjectClientNumber.put(obj1[i][0], rs.getString("Client_Number"));
				hashProjectClientState.put(obj1[i][0], rs.getString("State"));
				hashProjectClientZip.put(obj1[i][0], rs.getString("ZIP"));
				hashProjectClientInvoiceFrequency.put(obj1[i][0], rs.getString("Invoice_Frequency"));
				hashProjectClientPaymentTerms.put(obj1[i][0], rs.getString("Billing_terms"));
				
				if (rs.getString("InvoiceDateDiff") == null) {
					obj1[i][3] = "0";
				} else {
					PreparedStatement pst1 = conn.prepareStatement("SELECT Total_amount_due from invoice_details WHERE Project_id=?");
					pst1.setInt(1, projectno);
					ResultSet rs1 = pst1.executeQuery();
					int amount=0;
					while(rs1.next()){
						amount = amount+rs1.getInt("Total_amount_due");
					}
					obj1[i][3] = String.valueOf(amount);
				}
				i++;
			}
			
			
		} catch (IOException | SQLException e2) {
			e2.printStackTrace();
		}

		

		setTitle("Project Report");
		setIconImage(backgroundImage);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1350, 772);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		// private JTable table;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(36, 58, 1266, 664);
		scrollPane.setForeground(Color.GRAY);
		scrollPane.setBackground(Color.GRAY);
		scrollPane.setAutoscrolls(true);
		scrollPane.setToolTipText("");

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Home frame = new Home(args);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setVisible(true);
				dispose();
			}
		});
		btnBack.setBounds(304, 16, 108, 23);

		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setForeground(Color.BLACK);
		table.setFillsViewportHeight(true);
		table.setBackground(SystemColor.control);
		table.setName("table");
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getGridColor();
		table.getTableHeader().getBackground().getBlue();
		table.setAutoCreateRowSorter(true);
		
		String[] header = new String[4];

		header[0] = "Project Number";
		header[1] = "Project Name";
		header[2] = "Timeline Completion";
		header[3] = "Budget Completion";

		String[][] obj = Arrays.copyOfRange(obj1, 0, i);

		table.setModel(new DefaultTableModel(obj, header));
		scrollPane.setViewportView(table);

		contentPane.setLayout(null);
		scrollPane.setViewportView(table);
		contentPane.add(scrollPane);
		contentPane.add(btnBack);
	}

}
