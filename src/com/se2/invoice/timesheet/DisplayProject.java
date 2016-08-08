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
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.se2.invoice.forms.Home;
import com.se2.invoice.forms.dataConnectionObject;

import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class DisplayProject extends JFrame {

	private JPanel contentPane;
	private BufferedImage backgroundImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DisplayProject frame = new DisplayProject(args);
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
	public DisplayProject(String[] args) throws IOException, SQLException {
		
		dataConnectionObject dco = new dataConnectionObject();
		Connection conn = dco.getConnection();
		
		try {
		    backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setTitle("Project People Information");
		setIconImage(backgroundImage);
		
		setTitle("Display Timesheet");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1205, 590);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JTable table = new JTable();
		DefaultTableModel model = new DefaultTableModel();
		
		JLabel lblWelcome = new JLabel("Welcome,");
		lblWelcome.setBounds(56, 29, 89, 14);
		contentPane.add(lblWelcome);
		
		JLabel lblUsername = new JLabel(args[1]);
		lblUsername.setBounds(166, 29, 257, 14);
		contentPane.add(lblUsername);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(64, 67, 1070, 439);
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
		
		String[] header = new String[4];
		header[0] = "Project Number";
		header[1] = "Project Name";
		header[2] = "Developer";
		header[3] = "Project Manager";
		
		PreparedStatement pst = conn.prepareStatement("SELECT A.ProjectNumber, A.PersonName, B.ProjectName, "
				+ "B.ProjectManager FROM project_person A, project B WHERE A.ProjectNumber=B.ProjectNumber "
				+ "AND A.deactivated='0' AND B.deactivated='0'");
		//pst.setString(1,(String)cmbProject.getSelectedItem());
		
        ResultSet rs = pst.executeQuery();
        
        String[][] obj1 = new String[10000][10];
        
        int i = 0;
        while(rs.next()){
        	obj1[i][0]=rs.getString("ProjectNumber");
        	obj1[i][1]=rs.getString("ProjectName");
        	obj1[i][2]=rs.getString("PersonName");
        	obj1[i][3]=rs.getString("ProjectManager");
        	i++;
        }
        
        String[][] obj = Arrays.copyOfRange(obj1, 0, i);
        
		table.setModel(new DefaultTableModel(obj,header));
		
		
		//table.setModel(model);
		//table.getColumnModel().getColumn(3).setResizable(false);
		scrollPane.setViewportView(table);
		contentPane.setLayout(null);
	//	contentPane.add(lblSelectProject);
		//contentPane.add(cmbProject);
		contentPane.add(scrollPane);
		
		JButton btnProceed = new JButton("Proceed");
		btnProceed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SubmitTimesheet frame;
				try {
					frame = new SubmitTimesheet(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				}
				
			}
		});
		btnProceed.setBounds(654, 517, 89, 23);
		contentPane.add(btnProceed);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Home frame = new Home(args);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setVisible(true);
				dispose();
			}
		});
		btnBack.setBounds(450, 517, 89, 23);
		contentPane.add(btnBack);
		
	}
}
