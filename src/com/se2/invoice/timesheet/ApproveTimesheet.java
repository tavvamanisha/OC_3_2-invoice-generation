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

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXDatePicker;

import com.se2.invoice.forms.Home;
import com.se2.invoice.forms.dataConnectionObject;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ApproveTimesheet extends JFrame {

	private JPanel contentPane;
	private BufferedImage backgroundImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApproveTimesheet frame = new ApproveTimesheet(args);
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
	public ApproveTimesheet(String[] args) throws IOException, SQLException {
		
		dataConnectionObject dco = new dataConnectionObject();
		Connection conn = dco.getConnection();
		
		try {
		    backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setTitle("Approve Timesheet");
		setIconImage(backgroundImage);
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1191, 705);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTable table = new JTable();
		DefaultTableModel model = new DefaultTableModel();
		
		
		JLabel label = new JLabel("Welcome,");
		label.setBounds(63, 23, 85, 14);
		contentPane.add(label);
		
		JLabel lblUsername = new JLabel(args[1]);
		lblUsername.setBounds(158, 23, 385, 14);
		contentPane.add(lblUsername);
		
		/*JLabel lblStartDate = new JLabel("Project");
		lblStartDate.setBounds(63, 64, 72, 14);
		contentPane.add(lblStartDate);*/
		

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(63, 73, 1074, 537);
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
		
		String[] header = new String[13];
		header[0] = "Project Number";
		header[1] = "Project Name";
		header[2] = "Week Start Date";
		header[3] = "Employee";
		header[4] = "Monday";
		header[5] = "Tuesday";
		header[6] = "Wednesday";
		header[7] = "Thursday";
		header[8] = "Friday";
		header[9] = "Saturday";
		header[10] = "Sunday";
		header[11] = "Total Hours";
		header[12] = "Unique ID";
		
		PreparedStatement pst = conn.prepareStatement("SELECT A.*, B.ProjectName FROM people_timesheet A, project B WHERE "
				+ "A.ProjectNumber=B.ProjectNumber AND B.ProjectManager=? AND A.People_ts_status='Submitted'");
		pst.setString(1,args[1]);
		
        ResultSet rs = pst.executeQuery();
        
        String[][] obj1 = new String[10000][13];
        
        int i = 0;
        while(rs.next()){
        	obj1[i][0]=rs.getString("ProjectNumber");
        	obj1[i][1]=rs.getString("ProjectName");
        	obj1[i][2]=rs.getString("Week_start_date");
        	obj1[i][3]=rs.getString("PersonName");
        	obj1[i][4]=rs.getString("mon");
        	obj1[i][5]=rs.getString("tue");
        	obj1[i][6]=rs.getString("wed");
        	obj1[i][7]=rs.getString("thu");
        	obj1[i][8]=rs.getString("fri");
        	obj1[i][9]=rs.getString("sat");
        	obj1[i][10]=rs.getString("sun");
        	obj1[i][11]=String.valueOf(rs.getInt("mon")+rs.getInt("tue")+rs.getInt("wed")+rs.getInt("thu")+rs.getInt("fri")+rs.getInt("sat")+rs.getInt("sun"));
        	obj1[i][12]=rs.getString("People_ts_id");
        	i++;
        }
        
        String[][] obj = Arrays.copyOfRange(obj1, 0, i);
        
		table.setModel(new DefaultTableModel(obj,header));
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(12));
		
		
		//table.setModel(model);
		//table.getColumnModel().getColumn(3).setResizable(false);
		scrollPane.setViewportView(table);
		contentPane.setLayout(null);
		//contentPane.add(lblSelectProject);
		//contentPane.add(cmbProject);
		contentPane.add(scrollPane);
		
		JButton btnSave = new JButton("Approve");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(table.getSelectedRow()==-1){
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"Please select a row to approve.");
				} else {
					try {
						PreparedStatement pst = conn.prepareStatement("UPDATE `people_timesheet` SET "
								+ "`People_ts_status`=? WHERE `People_ts_id`=?");
						pst.setString(1, "Approved");
						pst.setInt(2, Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), 12).toString()));
						
						pst.executeUpdate();
						
						JFrame parent = new JFrame();
				        JOptionPane.showMessageDialog(parent,"Selected row approved successfullly.");
				        
				        ApproveTimesheet frame;
						frame = new ApproveTimesheet(args);
						frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
						frame.setVisible(true);
						dispose();
						
						
					} catch (SQLException | IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		
		btnSave.setBounds(697, 632, 89, 23);
		contentPane.add(btnSave);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Home frame;
				frame = new Home(args);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setVisible(true);
				dispose();
				
			}
		});
		btnBack.setBounds(424, 632, 89, 23);
		contentPane.add(btnBack);
		
		JButton btnDiscard = new JButton("Discard");
		btnDiscard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(table.getSelectedRow()==-1){
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"Please select a row to discard.");
				} else {
					try {
						PreparedStatement pst = conn.prepareStatement("UPDATE `people_timesheet` SET "
								+ "`People_ts_status`=? WHERE `People_ts_id`=?");
						pst.setString(1, "Discarded");
						pst.setInt(2, Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(), 12).toString()));
						
						pst.executeUpdate();
						
						JFrame parent = new JFrame();
				        JOptionPane.showMessageDialog(parent,"Selected row discarded successfullly.");
				        
				        ApproveTimesheet frame;
						frame = new ApproveTimesheet(args);
						frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
						frame.setVisible(true);
						dispose();
						
						
					} catch (SQLException | IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		btnDiscard.setBounds(558, 632, 89, 23);
		contentPane.add(btnDiscard);
		
		/*JComboBox cmbProject = new JComboBox();
		cmbProject.setBounds(174, 61, 162, 20);
		contentPane.add(cmbProject);*/
	}
}
