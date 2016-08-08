package com.se2.invoice.timesheet;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXDatePicker;

import com.se2.invoice.forms.Home;
import com.se2.invoice.forms.dataConnectionObject;

import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class SubmitTimesheet extends JFrame {

	private JPanel contentPane;
	private JTextField txtWeekEndDate;
	private BufferedImage backgroundImage;
	private JXDatePicker dateWeekEnd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SubmitTimesheet frame = new SubmitTimesheet(args);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public SubmitTimesheet(String[] args) throws IOException, SQLException {
		
		dataConnectionObject dco = new dataConnectionObject();
		Connection conn = dco.getConnection();
		
		try {
		    backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setTitle("People Information");
		setIconImage(backgroundImage);
		
		setTitle("Submit timesheet");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1183, 704);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTable table = new JTable();
		DefaultTableModel model = new DefaultTableModel();
		
		JLabel label = new JLabel("Welcome,");
		label.setBounds(63, 23, 74, 14);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel(args[1]);
		label_1.setBounds(147, 23, 239, 14);
		contentPane.add(label_1);
		
		JLabel lblStartDate = new JLabel("Week Start Date");
		lblStartDate.setBounds(63, 64, 136, 14);
		contentPane.add(lblStartDate);
		
		JXDatePicker dateWeekStart = new JXDatePicker();
		Calendar instance = Calendar.getInstance();
	    Date date = instance.getTime();
	    //dateWeekStart.setDate(date);
	    dateWeekStart.getEditor().setText("Choose Date");
		dateWeekStart.setBounds(209, 60, 161, 22);
		dateWeekStart.setDate(date);
		//int day = dateWeekStart.getDate().getDay();
		dateWeekStart.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				Date d = dateWeekStart.getDate();
				Calendar c = Calendar.getInstance();
				c.setTime(d);
				
				if(d.getDay()!=1){
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"Week shall start from Monday. Please select a Monday date.");
				} else {
					Calendar cal = Calendar.getInstance();
					cal.setTime(dateWeekStart.getDate());
					cal.add(cal.DATE, 6);
					dateWeekEnd.setDate(cal.getTime());
				}
				
			}
		});
		contentPane.add(dateWeekStart);
		
		
		JLabel lblEndDate = new JLabel("Week End Date");
		lblEndDate.setBounds(63, 108, 135, 14);
		contentPane.add(lblEndDate);
		
		/*txtWeekEndDate = new JTextField();
		txtWeekEndDate.setEnabled(false);
		txtWeekEndDate.setEditable(false);
		txtWeekEndDate.setBounds(208, 105, 162, 20);
		contentPane.add(txtWeekEndDate);
		txtWeekEndDate.setColumns(10);*/
		dateWeekEnd = new JXDatePicker();
		dateWeekEnd.getEditor().setText("Choose Date");
		dateWeekEnd.setBounds(208, 105, 162, 20);
		//dateWeekEnd.setEnabled(false);
		dateWeekEnd.setEditable(false);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateWeekStart.getDate());
		cal.add(cal.DATE, 7);
		dateWeekEnd.setDate(cal.getTime());
		contentPane.add(dateWeekEnd);
		

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(63, 150, 1056, 461);
		scrollPane.setForeground(Color.GRAY);
		scrollPane.setBackground(Color.GRAY);
		scrollPane.setAutoscrolls(true);
		scrollPane.setToolTipText("");
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setForeground(Color.BLACK);
		table.setFillsViewportHeight(true);
		table.setBackground(SystemColor.control);
		table.setName("table");
		table.getGridColor();
		table.getTableHeader().getBackground().getBlue();
		table.setAutoCreateRowSorter(true);
		
		String[] header = new String[9];
		header[0] = "Project Number";
		header[1] = "Project Name";
		header[2] = "Monday";
		header[3] = "Tuesday";
		header[4] = "Wednesday";
		header[5] = "Thursday";
		header[6] = "Friday";
		header[7] = "Saturday";
		header[8] = "Sunday";
		
		PreparedStatement pst = conn.prepareStatement("SELECT A.ProjectNumber, B.ProjectName FROM project_person A,"
				+ " project B WHERE A.ProjectNumber=B.ProjectNumber AND A.deactivated='0' AND B.deactivated='0' AND "
				+ "A.PersonName=?");
		pst.setString(1,args[1]);
		
        ResultSet rs = pst.executeQuery();
        
        String[][] obj1 = new String[10000][9];
        
        int i = 0;
        while(rs.next()){
        	obj1[i][0]=rs.getString("ProjectNumber");
        	obj1[i][1]=rs.getString("ProjectName");
        	i++;
        }
        
        String[][] obj = Arrays.copyOfRange(obj1, 0, i);
        
		table.setModel(new DefaultTableModel(obj,header));
		
		
		//table.setModel(model);
		//table.getColumnModel().getColumn(3).setResizable(false);
		scrollPane.setViewportView(table);
		contentPane.setLayout(null);
		//contentPane.add(lblSelectProject);
		//contentPane.add(cmbProject);
		contentPane.add(scrollPane);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					table.getRowCount();
					/*ArrayList<String> projectNum = new ArrayList<String>();
					ArrayList<String> projectName = new ArrayList<String>();
					ArrayList<String> mon = new ArrayList<String>();
					ArrayList<String> tue = new ArrayList<String>();
					ArrayList<String> wed = new ArrayList<String>();
					ArrayList<String> thu = new ArrayList<String>();
					ArrayList<String> fri = new ArrayList<String>();
					ArrayList<String> sat = new ArrayList<String>();
					ArrayList<String> sun = new ArrayList<String>();*/
					for(int count = 0; count < table.getRowCount(); count++){
						/*System.out.println(count);
						System.out.println(table.getModel().getValueAt(count, 7));
						projectNum.add(table.getModel().getValueAt(count, 0).toString());
						projectName.add(table.getModel().getValueAt(count, 1).toString());*/
						
						String mon, tue, wed, thu, fri, sat, sun="0";
						if(table.getModel().getValueAt(count, 2)!=null){
							mon=(table.getModel().getValueAt(count, 2).toString());
						}else{
							mon=("0");
						}
						if(table.getModel().getValueAt(count, 3)!=null){
							tue=(table.getModel().getValueAt(count, 3).toString());
						}else{
							tue=("0");
						}
						if(table.getModel().getValueAt(count, 4)!=null){
							wed=(table.getModel().getValueAt(count, 4).toString());
						}else{
							wed=("0");
						}
						if(table.getModel().getValueAt(count, 5)!=null){
							thu=(table.getModel().getValueAt(count, 5).toString());
						}else{
							thu=("0");
						}
						if(table.getModel().getValueAt(count, 6)!=null){
							fri=(table.getModel().getValueAt(count, 6).toString());
						}else{
							fri=("0");
						}
						if(table.getModel().getValueAt(count, 7)!=null){
							sat=(table.getModel().getValueAt(count, 7).toString());
						}else{
							sat=("0");
						}
						if(table.getModel().getValueAt(count, 8)!=null){
							sun=(table.getModel().getValueAt(count, 8).toString());
						}else{
							sun=("0");
						}
						
						PreparedStatement pst = conn.prepareStatement("INSERT INTO people_timesheet "
								+ "(`ProjectNumber`, `PersonName`, `Week_start_date`, "
								+ "`mon`, `tue`, `wed`, `thu`, `fri`, `sat`, `sun`, `People_ts_status`) VALUES "
								+ "(?,?,?,?,?,?,?,?,?,?,?)");
						pst.setString(1, table.getModel().getValueAt(count, 0).toString());
						pst.setString(2, args[1]);
						java.sql.Date sqlStartDate = new java.sql.Date(dateWeekStart.getDate().getTime());
						pst.setDate(3, sqlStartDate);
						pst.setInt(4, Integer.parseInt(mon));
						pst.setInt(5, Integer.parseInt(tue));
						pst.setInt(6, Integer.parseInt(wed));
						pst.setInt(7, Integer.parseInt(thu));
						pst.setInt(8, Integer.parseInt(fri));
						pst.setInt(9, Integer.parseInt(sat));
						pst.setInt(10, Integer.parseInt(sun));
						pst.setString(11, "Submitted");
						
						pst.executeUpdate();
						
					}
					
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"Timesheet submitted successfully.");
			        
			        Home frame;
					frame = new Home(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
					
					/*PreparedStatement pst = conn.prepareStatement("INSERT INTO people_timesheet "
							+ "(`ProjectNumber`, `PersonName`, `Week_start_date`, "
							+ "`mon`, `tue`, `wed`, `thu`, `fri`, `sat`, `sun`, `People_ts_status`) VALUES "
							+ "(?,?,?,?,?,?,?,?,?,?,?)");*/
					
					
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		});
		btnSave.setBounds(626, 619, 89, 23);
		contentPane.add(btnSave);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				DisplayProject frame;
				try {
					frame = new DisplayProject(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				}
				
			}
		});
		btnBack.setBounds(471, 619, 89, 23);
		contentPane.add(btnBack);
		
	}
}
