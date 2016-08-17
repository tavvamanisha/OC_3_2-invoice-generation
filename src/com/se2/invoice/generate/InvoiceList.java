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
import java.util.HashMap;

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
					obj1[i][0] = String.valueOf(rs.getInt("P.ProjectNumber"));
					obj1[i][1] = rs.getString("P.ProjectName");
					obj1[i][2] = String.valueOf(timelineCompletion);
					obj1[i][3] = "0";
				} else {
					int date = calendar.get(Calendar.DAY_OF_MONTH);
					if((date>= 4 && date <=10) && (day==Calendar.WEDNESDAY) && ("Monthly-Cal".equalsIgnoreCase(rs.getString("Invoice_Frequency")))
							|| (rs.getInt("InvoiceDateDiff") == 14 && "BiWeekly".equalsIgnoreCase(rs.getString("Invoice_Frequency")))
							|| (rs.getInt("InvoiceDateDiff") == 28 && "Monthly".equalsIgnoreCase(rs.getString("Invoice_Frequency")))){
						PreparedStatement pst1 = conn.prepareStatement("SELECT Total_amount_due from invoice_details WHERE Project_id=?");
						pst1.setInt(1, projectno);
						ResultSet rs1 = pst1.executeQuery();
						int amount=0;
						while(rs1.next()){
							amount = amount+rs1.getInt("Total_amount_due");
						}
						obj1[i][0] = String.valueOf(rs.getInt("P.ProjectNumber"));
						obj1[i][1] = rs.getString("P.ProjectName");
						obj1[i][2] = String.valueOf(timelineCompletion);
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
						String projectNum = (String)table.getModel().getValueAt(row, 0);
						
						try {
							PreparedStatement pst = conn.prepareStatement("SELECT P.ProjectNumber, C.Invoice_Frequency, "
									+ "MAX(T.Week_start_date), CURDATE()-T.Week_start_date AS DATEDIFF, T.InvoiceGenerated "
									+ "FROM people_timesheet T, project P, client C WHERE T.ProjectNumber=P.ProjectNumber "
									+ "AND P.Client=C.Client_Number AND P.ProjectNumber=? AND T.People_ts_status=?");
							pst.setString(1, projectNum);
							pst.setString(2, "Approved");
							ResultSet rs = pst.executeQuery();
							
							if(rs.next()){
								
								if(("Weekly".equalsIgnoreCase(rs.getString("Invoice_Frequency")) && (rs.getInt("DATEDIFF")>11 || rs.getBoolean("InvoiceGenerated")==false))
										||("BiWeekly".equalsIgnoreCase(rs.getString("Invoice_Frequency")) && (rs.getInt("DATEDIFF")>18 || rs.getBoolean("InvoiceGenerated")==false))
										||("Monthly".equalsIgnoreCase(rs.getString("Invoice_Frequency")) && (rs.getInt("DATEDIFF")>32 || rs.getBoolean("InvoiceGenerated")==false))
										||("Monthly-Cal".equalsIgnoreCase(rs.getString("Invoice_Frequency")) && (rs.getInt("DATEDIFF")>42 || rs.getBoolean("InvoiceGenerated")==false))){
									
									String[] ar = new String[14];
									ar[0]=projectNum;
									ar[2]=hashProjectClientAdd1.get(projectNum);
									ar[3]=hashProjectClientAdd2.get(projectNum);
									ar[4]=hashProjectClientCity.get(projectNum);
									ar[11]=hashProjectClientEmail.get(projectNum);
									ar[10]=hashProjectClientInvoiceGrouping.get(projectNum);
									ar[1]=hashProjectClientName.get(projectNum);
									ar[7]=hashProjectClientNumber.get(projectNum);
									ar[5]=hashProjectClientState.get(projectNum);
									ar[6]=hashProjectClientZip.get(projectNum);
									ar[9]=hashProjectClientInvoiceFrequency.get(projectNum);
									ar[8]=hashProjectClientPaymentTerms.get(projectNum);
									ar[12]=(String)table.getModel().getValueAt(row, 1);
									ar[13]=new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
									
									String argument[]=new String[16];
									argument[0]=args[0];
									argument[1]=args[1];
									for(int i=2;i<16;i++){
										argument[i]=ar[i-2];
									}
									
									GenerateInvoice.main(argument);
									
									JFrame parent = new JFrame();
							        JOptionPane.showMessageDialog(parent,"Invoice for project "+projectNum+" has been generated and saved successfully.");
							        
							        dispose();
									
								} else {
									JFrame parent = new JFrame();
							        JOptionPane.showMessageDialog(parent,"Invoice for project "+projectNum+" has already been generated.");
								}
								
								
							}else{
								String[] ar = new String[14];
								ar[0]=projectNum;
								ar[2]=hashProjectClientAdd1.get(projectNum);
								ar[3]=hashProjectClientAdd2.get(projectNum);
								ar[4]=hashProjectClientCity.get(projectNum);
								ar[11]=hashProjectClientEmail.get(projectNum);
								ar[10]=hashProjectClientInvoiceGrouping.get(projectNum);
								ar[1]=hashProjectClientName.get(projectNum);
								ar[7]=hashProjectClientNumber.get(projectNum);
								ar[5]=hashProjectClientState.get(projectNum);
								ar[6]=hashProjectClientZip.get(projectNum);
								ar[9]=hashProjectClientInvoiceFrequency.get(projectNum);
								ar[8]=hashProjectClientPaymentTerms.get(projectNum);
								ar[12]=(String)table.getModel().getValueAt(row, 1);
								ar[13]=new SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
								
								String argument[]=new String[16];
								argument[0]=args[0];
								argument[1]=args[1];
								for(int i=2;i<16;i++){
									argument[i]=ar[i-2];
								}
								
								GenerateInvoice.main(argument);
								
								JFrame parent = new JFrame();
						        JOptionPane.showMessageDialog(parent,"Invoice for project "+projectNum+" has been generated and saved successfully.");
						        
						        dispose();
							}
							
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
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
