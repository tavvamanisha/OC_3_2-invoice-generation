package com.se2.invoice.generate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.se2.invoice.forms.Home;
import com.se2.invoice.forms.dataConnectionObject;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InvoiceList extends JFrame {

	private JPanel contentPane;
	private BufferedImage backgroundImage;

	JTable table = new JTable();
	DefaultTableModel model = new DefaultTableModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InvoiceList frame = new InvoiceList(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 * @throws SQLException
	 */
	public InvoiceList(String[] args) throws IOException, SQLException {

		try {
			backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		
		if(day==Calendar.WEDNESDAY){
			dataConnectionObject dco = new dataConnectionObject();
			Connection conn = dco.getConnection();

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
			String[][] obj1 = new String[10000][4];
			int i = 0, projectno = 0;

			while (rs.next()) {
				if (rs.getString("InvoiceDateDiff") == null) {
					// 1st Invoice generation.
					obj1[i][0] = rs.getString("P.ProjectNumber");
					obj1[i][1] = rs.getString("P.ProjectName");
					
					int timeline=rs.getInt("TIMELINE");
					int startDateDiff = rs.getInt("StartDateDiff");
					float timelineCompletion = startDateDiff*100/timeline;
					
					obj1[i][2] = String.valueOf(timelineCompletion);
					obj1[i][3] = "0";
					
				} else {
					
					int date = calendar.get(Calendar.DAY_OF_MONTH);
					
					if(date>= 4 && date <=10){
						if(day==Calendar.WEDNESDAY){
							if("Monthly-Cal".equalsIgnoreCase(rs.getString("Invoice_Frequency"))){
								projectno = rs.getInt("ProjectNumber");
								obj1[i][0]=String.valueOf(projectno);
								obj1[i][1] = rs.getString("P.ProjectName");
								int timeline=rs.getInt("TIMELINE");
								int startDateDiff = rs.getInt("StartDateDiff");
								float timelineCompletion = startDateDiff*100/timeline;
								obj1[i][2] = String.valueOf(timelineCompletion);
								PreparedStatement pst1 = conn.prepareStatement("SELECT Total_amount_due from invoice_details WHERE Project_id=?");
								pst1.setInt(1, projectno);
								ResultSet rs1 = pst1.executeQuery();
								int amount=0;
								while(rs1.next()){
									amount = amount+rs1.getInt("Total_amount_due");
								}
								obj1[i][3] = String.valueOf(amount);
							}
						}
					}
					
					if (rs.getInt("InvoiceDateDiff") == 14 && "BiWeekly".equalsIgnoreCase(rs.getString("Invoice_Frequency"))) {
						projectno = rs.getInt("ProjectNumber");
						obj1[i][0]=String.valueOf(projectno);
						obj1[i][1] = rs.getString("P.ProjectName");
						int timeline=rs.getInt("TIMELINE");
						int startDateDiff = rs.getInt("StartDateDiff");
						float timelineCompletion = startDateDiff*100/timeline;
						obj1[i][2] = String.valueOf(timelineCompletion);
						PreparedStatement pst1 = conn.prepareStatement("SELECT Total_amount_due from invoice_details WHERE Project_id=?");
						pst1.setInt(1, projectno);
						ResultSet rs1 = pst1.executeQuery();
						int amount=0;
						while(rs1.next()){
							amount = amount+rs1.getInt("Total_amount_due");
						}
						obj1[i][3] = String.valueOf(amount);
						
					}else if (rs.getInt("InvoiceDateDiff") == 28 && "Monthly".equalsIgnoreCase(rs.getString("Invoice_Frequency"))) {
						projectno = rs.getInt("ProjectNumber");
						obj1[i][0]=String.valueOf(projectno);
						obj1[i][1] = rs.getString("P.ProjectName");
						int timeline=rs.getInt("TIMELINE");
						int startDateDiff = rs.getInt("StartDateDiff");
						float timelineCompletion = startDateDiff*100/timeline;
						
						obj1[i][2] = String.valueOf(timelineCompletion);
						PreparedStatement pst1 = conn.prepareStatement("SELECT Total_amount_due from invoice_details WHERE Project_id=?");
						pst1.setInt(1, projectno);
						ResultSet rs1 = pst1.executeQuery();
						int amount=0;
						while(rs1.next()){
							amount = amount+rs1.getInt("Total_amount_due");
						}
						obj1[i][3] = String.valueOf(amount);
						
					}
				}
				i++;
			}

			setTitle("Generate Invoice");
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

			JButton btnGenerate = new JButton("Generate");
			btnGenerate.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					int row = table.getSelectedRow();
					if(row==-1){
						JFrame parent = new JFrame();
				        JOptionPane.showMessageDialog(parent,"Please select a row.");
					}else{
						String today = new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());
						String projectNum = (String)table.getModel().getValueAt(row, 0);
						
						String[] ar = new String[1];
						ar[0]=today+"_"+projectNum+".pdf";
						GenerateInvoice.main(ar);
						
						JFrame parent = new JFrame();
				        JOptionPane.showMessageDialog(parent,"Invoice for project "+projectNum+" has been generated and saved successfully.");
						
					}
					
					
				}
			});
			btnGenerate.setBounds(443, 16, 115, 23);

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
			/*header[4] = "Timeline Completion";
			header[5] = "Budget Completion";
			header[6] = "After Invoice Timeline";
			header[7] = "After Invoice Budget";*/

			// String[][] obj = new String[12][12];

			// dataConnectionObject dco1 = new dataConnectionObject();
			// Connection conn1 = dco1.getConnection();
			// PreparedStatement pst1 = conn1.prepareStatement("Select * from
			// client");
			// ResultSet rs1 = pst1.executeQuery();

			// String[][] obj11 = new String[10000][13];
			// int i1 = 0;

			// while(rs1.next()){
			/*
			 * obj11[i1][0]=rs1.getString("Client_Number");
			 * obj11[i1][1]=rs1.getString("Name");
			 * obj11[i1][2]=rs1.getString("Address1");
			 * obj11[i1][3]=rs1.getString("Address2");
			 * obj11[i1][4]=rs1.getString("City");
			 * obj11[i1][5]=rs1.getString("State");
			 * obj11[i1][6]=rs1.getString("ZIP");
			 * obj11[i1][7]=rs1.getString("Email");
			 * obj11[i1][8]=rs1.getString("Contact");
			 * obj11[i1][9]=rs1.getString("Invoice_Frequency");
			 * obj11[i1][10]=rs1.getString("Billing_terms");
			 * obj11[i1][11]=rs1.getString("Invoice_Grouping");
			 * obj11[i1][12]=rs1.getString("deactivated");
			 */

			/*
			 * boolean deactive = rs.getBoolean("deactivated");
			 * if(deactive)obj1[i][12]="Deactive"; else obj1[i][12]="Active";
			 */

			// i1++;
			// }

			String[][] obj = Arrays.copyOfRange(obj1, 0, i);

			table.setModel(new DefaultTableModel(obj, header));
			table.getColumnModel().getColumn(3).setResizable(false);
			scrollPane.setViewportView(table);

			table.setModel(new DefaultTableModel(obj, header));
			// table.setModel(model);
			table.getColumnModel().getColumn(3).setResizable(false);
			contentPane.setLayout(null);
			scrollPane.setViewportView(table);
			contentPane.add(scrollPane);
			contentPane.add(btnBack);
			contentPane.add(btnGenerate);
			
		} else {
			JFrame parent = new JFrame();
	        JOptionPane.showMessageDialog(parent,"This screen will be enabled only on Invoice Generation Day, that is, Wednesday.");
	        
	        Home frame = new Home(args);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setVisible(true);
			dispose();
		}

	}

}
