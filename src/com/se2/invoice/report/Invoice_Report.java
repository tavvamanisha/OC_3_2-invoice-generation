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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.se2.invoice.forms.Home;
import com.se2.invoice.forms.dataConnectionObject;
import com.se2.invoice.generate.GenerateInvoice;

public class Invoice_Report extends JFrame {

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
					Invoice_Report frame = new Invoice_Report(args);
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
	public Invoice_Report(String[] args) {
		try {
			backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		dataConnectionObject dco = new dataConnectionObject();
		Connection conn;
		String[][] obj1 = new String[10000][8];
		int i = 0, projectno = 0;
		
		try {
			conn = dco.getConnection();
			PreparedStatement pst = conn.prepareStatement("SELECT I.*, P.ProjectName, C.Name "
					+ "FROM invoice_details I, client C, project P "
					+ "WHERE I.Client_id=C.Client_Number AND I.Project_id=P.ProjectNumber");

			ResultSet rs = pst.executeQuery();
			
			while (rs.next()) {
				
				obj1[i][0] = rs.getString("Invoice_id");
				obj1[i][1] = rs.getString("Name");
				obj1[i][2] = rs.getString("Project_id");
				obj1[i][3] = rs.getString("ProjectName");
				String sent = rs.getString("Invoice_sent");
				if("1".equals(sent)){
					obj1[i][4] = "Sent";
				}else{
					obj1[i][4] = "Not sent";
				}
				obj1[i][5] = rs.getString("Invoice_freq");
				obj1[i][6] = rs.getString("Invoice_date");
				obj1[i][7] = rs.getString("Total_amount_due");
				i++;
				
			}
			
		} catch (IOException | SQLException e2) {
			e2.printStackTrace();
		}

		setTitle("Invoice Report");
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
		
		String[] header = new String[8];

		header[0] = "Invoice Number";
		header[1] = "Client";
		header[2] = "Project Number";
		header[3] = "Project Name";
		header[4] = "Invoice Sent?";
		header[5] = "Invoice Frequency";
		header[6] = "Invoice Date";
		header[7] = "Amount";
		
		String[][] obj = Arrays.copyOfRange(obj1, 0, i);

		table.setModel(new DefaultTableModel(obj, header));
		scrollPane.setViewportView(table);

		contentPane.setLayout(null);
		scrollPane.setViewportView(table);
		contentPane.add(scrollPane);
		contentPane.add(btnBack);
		
	}
}
