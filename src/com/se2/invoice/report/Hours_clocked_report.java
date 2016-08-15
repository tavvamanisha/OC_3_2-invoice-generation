package com.se2.invoice.report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXDatePicker;

import com.se2.invoice.forms.Home;
import com.se2.invoice.forms.dataConnectionObject;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;

public class Hours_clocked_report extends JFrame {

	private JPanel contentPane;
	
	JTable table = new JTable();
	DefaultTableModel model = new DefaultTableModel();
	JXDatePicker datePicker;
	BufferedImage backgroundImage;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Hours_clocked_report frame = new Hours_clocked_report(args);
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
	public Hours_clocked_report(String[] args) {
		
		try {
		    backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setTitle("Hours Clocked Report");
		setIconImage(backgroundImage);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1258, 683);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSelectWeekStart = new JLabel("Select Week Start Date");
		lblSelectWeekStart.setBounds(58, 54, 183, 14);
		contentPane.add(lblSelectWeekStart);
		
		datePicker = new JXDatePicker();
		datePicker.setBounds(210, 50, 183, 22);
		contentPane.add(datePicker);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(417, 50, 89, 23);
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(datePicker.getDate()==null){
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"Please select a week start date.");
				} else {
					
					Date d = datePicker.getDate();
					Calendar c = Calendar.getInstance();
					c.setTime(d);
					
					if(d.getDay()!=1){
						JFrame parent = new JFrame();
				        JOptionPane.showMessageDialog(parent,"Please select a Monday date.");
					} else {
						dataConnectionObject dco = new dataConnectionObject();
				        Connection conn;
						try {
							conn = dco.getConnection();
							PreparedStatement pst = conn.prepareStatement("SELECT P.ProjectNumber, P.ProjectName, "
									+ "P.ProjectManager, T.PersonName, T.People_ts_status FROM people_timesheet T, project P "
									+ "WHERE T.ProjectNumber=P.ProjectNumber AND T.Week_start_date=? ORDER BY "
									+ "T.People_ts_status, P.ProjectNumber, T.PersonName");
							pst.setDate(1, new java.sql.Date(datePicker.getDate().getTime()));
							ResultSet rs = pst.executeQuery();
							
							String[][] obj1 = new String[10000][5];
					        
					        int i = 0;
					        while(rs.next()){
					        	obj1[i][0]=rs.getString("ProjectNumber");
					        	obj1[i][1]=rs.getString("ProjectName");
					        	obj1[i][2]=rs.getString("ProjectManager");
					        	obj1[i][3]=rs.getString("PersonName");
					        	obj1[i][4]=rs.getString("People_ts_status");
					        	
					        	i++;
					        }
					        
					        String[][] obj = Arrays.copyOfRange(obj1, 0, i);
					        
					        String[] header = new String[5];
							header[0] = "Project Number";
							header[1] = "Project Name";
							header[2] = "Project Manager";
							header[3] = "Employee Name";
							header[4] = "Timesheet Status";
					        
					        table.setModel(new DefaultTableModel(obj,header));
							
						} catch (IOException | SQLException e1) {
							e1.printStackTrace();
						};
					}
				}
			}
		});
		contentPane.add(btnSearch);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(38, 99, 1169, 519);
		scrollPane.setForeground(Color.GRAY);
		scrollPane.setBackground(Color.GRAY);
		scrollPane.setAutoscrolls(true);
		scrollPane.setToolTipText("");
		table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setForeground(Color.BLACK);
		table.setFillsViewportHeight(true);
		table.setBackground(SystemColor.control);
		table.setName("table");
		table.getGridColor();
		table.getTableHeader().getBackground().getBlue();
		table.setAutoCreateRowSorter(true);
		
		//table.setModel(model);
		//table.getColumnModel().getColumn(3).setResizable(false);
		scrollPane.setViewportView(table);
		contentPane.setLayout(null);
		contentPane.add(lblSelectWeekStart);
		contentPane.add(datePicker);
		contentPane.add(btnSearch);
		contentPane.add(scrollPane);
		
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(10, 11, 89, 23);
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Home frame = new Home(args);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setVisible(true);
				dispose();
			}
		});
		contentPane.add(btnBack);
		
	}
}
