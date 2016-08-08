package com.se2.invoice.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

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
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

public class ProjectPeople extends JFrame {

	private JPanel contentPane;
	JTable table = new JTable();
	DefaultTableModel model = new DefaultTableModel();
	private BufferedImage backgroundImage;
	private HashMap<String, String> projectNumberNameMap;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ProjectPeople frame = new ProjectPeople(args);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public ProjectPeople(String[] args) throws SQLException, IOException {
		
		try {
		    backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setTitle("Project People Information");
		setIconImage(backgroundImage);
		
		
		dataConnectionObject dco = new dataConnectionObject();
		Connection conn = dco.getConnection();
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 780, 506);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSelectProject = new JLabel("Select Project");
		lblSelectProject.setBounds(172, 38, 140, 14);
		contentPane.add(lblSelectProject);
		
		JComboBox cmbProject = new JComboBox();
		cmbProject.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				PreparedStatement pst;
				try {
					pst = conn.prepareStatement("SELECT DISTINCT(A.PersonName) AS Name, B.Title As Title, "
							+ "B.Role, B.BillRate, C.ProjectName FROM project_person A, people B, project C "
							+ "WHERE B.Name=A.PersonName AND A.ProjectNumber = C.ProjectNumber "
							+ "AND A.deactivated = '0' AND C.ProjectNumber=?");
					pst.setString(1,(String)cmbProject.getSelectedItem());
					
			        ResultSet rs = pst.executeQuery();
			        
			        String[][] obj1 = new String[10000][10];
			        
			        int i = 0;
			        while(rs.next()){
			        	obj1[i][0]=rs.getString("Name");
			        	obj1[i][1]=rs.getString("Title");
			        	obj1[i][2]=rs.getString("BillRate");
			        	
			        	i++;
			        }
			        
			        String[] header = new String[3];
					header[0] = "Name";
					header[1] = "Title";
					header[2] = "Rate";
					
			        String[][] obj = Arrays.copyOfRange(obj1, 0, i);
			        
			        table.setModel(new DefaultTableModel(obj,header));
			        
				} catch (SQLException eq) {
					eq.printStackTrace();
				}
				
			}
		});
		cmbProject.setBounds(302, 35, 173, 20);
		contentPane.add(cmbProject);
		
		projectNumberNameMap = new HashMap();
		
		PreparedStatement pst2 = conn.prepareStatement("SELECT ProjectName as Name, ProjectNumber from project "
				+ "WHERE ProjectManager=?");
		pst2.setString(1, args[1]);
		ResultSet rs2 = pst2.executeQuery();
		String[] projectOptions = new String[1000];
		int j=0;
		while(rs2.next()){
			projectOptions[j] = rs2.getString("ProjectNumber");
			j++;
			projectNumberNameMap.put(rs2.getString("ProjectNumber"), rs2.getString("Name"));
		}
		cmbProject.setModel(new DefaultComboBoxModel(Arrays.copyOf(projectOptions, j)));
        
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(64, 67, 640, 266);
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
		
		String[] header = new String[3];
		header[0] = "Name";
		header[1] = "Title";
		header[2] = "Rate";
		
		PreparedStatement pst = conn.prepareStatement("SELECT DISTINCT(A.PersonName) AS Name, B.Title As Title, "
				+ "B.Role, B.BillRate, C.ProjectName FROM project_person A, people B, project C "
				+ "WHERE B.Name=A.PersonName AND A.ProjectNumber = C.ProjectNumber "
				+ "AND A.deactivated = '0' AND C.ProjectNumber=?");
		pst.setString(1,(String)cmbProject.getSelectedItem());
		
        ResultSet rs = pst.executeQuery();
        
        String[][] obj1 = new String[10000][10];
        
        int i = 0;
        while(rs.next()){
        	obj1[i][0]=rs.getString("Name");
        	obj1[i][1]=rs.getString("Title");
        	obj1[i][2]=rs.getString("BillRate");
        	
        	i++;
        }
        
        String[][] obj = Arrays.copyOfRange(obj1, 0, i);
        
		table.setModel(new DefaultTableModel(obj,header));
		
		
		//table.setModel(model);
		//table.getColumnModel().getColumn(3).setResizable(false);
		scrollPane.setViewportView(table);
		contentPane.setLayout(null);
		contentPane.add(lblSelectProject);
		contentPane.add(cmbProject);
		contentPane.add(scrollPane);
		
		JButton btnNewButton = new JButton("Add Developer");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String[] argNew = Arrays.copyOf(args, 4);
				argNew[2]= (String)cmbProject.getSelectedItem();
				argNew[3]= projectNumberNameMap.get((String)cmbProject.getSelectedItem());
				
				AddDeveloper frame;
				try {
					frame = new AddDeveloper(argNew);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
				} catch (SQLException | IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(314, 364, 132, 23);
		contentPane.add(btnNewButton);
		
		JButton button = new JButton("Delete Developer");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if(row== -1){
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"Please select a row.");
				} else{
					try {
						PreparedStatement pst = conn.prepareStatement("UPDATE `project_person` SET "
								+ "`deactivated`=? WHERE PersonName=? AND ProjectNumber=?");
						
						//System.out.println(projectNumberNameMap.get(cmbProject.getSelectedItem()));
						
						pst.setBoolean(1, true);
						pst.setString(2, (String)table.getModel().getValueAt(row, 0));
						pst.setString(3, (String)cmbProject.getSelectedItem());
						
						pst.executeUpdate();
						
						JFrame parent = new JFrame();
				        JOptionPane.showMessageDialog(parent,"Developer has been removed from Project successfully.");
				        dispose();
				        new ProjectPeople(args).setVisible(true);
						
					} catch (SQLException | IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		button.setBounds(496, 364, 132, 23);
		contentPane.add(button);
		
		JButton button_1 = new JButton("Back");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Home frame = new Home(args);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setVisible(true);
				dispose();
			}
		});
		button_1.setBounds(128, 364, 132, 23);
		contentPane.add(button_1);
	}
}
