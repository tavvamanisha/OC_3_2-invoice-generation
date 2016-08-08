package com.se2.invoice.forms;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
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
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class AddDeveloper extends JFrame {

	private JPanel contentPane;
	private JTextField txtTitle;
	private JTextField txtRole;
	private BufferedImage backgroundImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddDeveloper frame = new AddDeveloper(args);
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
	public AddDeveloper(String[] args) throws SQLException, IOException {
		
		dataConnectionObject dco = new dataConnectionObject();
		Connection conn = dco.getConnection();
		
		try {
		    backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setTitle("People Information");
		setIconImage(backgroundImage);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblProject = new JLabel("Project:");
		lblProject.setBounds(59, 59, 75, 14);
		contentPane.add(lblProject);
		
		JLabel lblProjectname = new JLabel("ProjectName");
		lblProjectname.setBounds(154, 59, 173, 14);
		contentPane.add(lblProjectname);
		lblProjectname.setText(args[2]+" - "+args[3]);
		
		JLabel lblDeveloper = new JLabel("Developer");
		lblDeveloper.setBounds(59, 101, 85, 14);
		contentPane.add(lblDeveloper);
		
		JComboBox cmbDeveloper = new JComboBox();
		cmbDeveloper.setBounds(154, 98, 150, 20);
		contentPane.add(cmbDeveloper);
		
		PreparedStatement pst2 = conn.prepareStatement("SELECT Name, Title, BillRate from people "
				+ "WHERE Role='Developer'");
		
		ResultSet rs2 = pst2.executeQuery();
		String[] developerOptions = new String[1000];
		HashMap<String, String> developerNameTitleMap = new HashMap();
		HashMap<String, String> developerNameBillRateMap = new HashMap();
		
		int j=0;
		while(rs2.next()){
			developerOptions[j] = rs2.getString("Name");
			developerNameTitleMap.put(rs2.getString("Name"), rs2.getString("Title"));
			developerNameBillRateMap.put(rs2.getString("Name"), rs2.getString("BillRate"));
			
			j++;
		}
		cmbDeveloper.setModel(new DefaultComboBoxModel(Arrays.copyOf(developerOptions, j)));
        
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setBounds(59, 144, 46, 14);
		contentPane.add(lblTitle);
		
		JLabel lblRole = new JLabel("Role");
		lblRole.setBounds(59, 185, 46, 14);
		contentPane.add(lblRole);
		
		txtTitle = new JTextField();
		txtTitle.setBounds(154, 141, 150, 20);
		contentPane.add(txtTitle);
		txtTitle.setColumns(10);
		//txtTitle.setEnabled(false);
		txtTitle.setEditable(false);
		txtTitle.setText(developerNameTitleMap.get(cmbDeveloper.getSelectedItem()));
		
		txtRole = new JTextField();
		txtRole.setBounds(154, 182, 150, 20);
		contentPane.add(txtRole);
		txtRole.setColumns(10);
		txtRole.setEditable(false);
		txtRole.setText(developerNameBillRateMap.get(cmbDeveloper.getSelectedItem()));
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ProjectPeople frame;
				try {
					frame = new ProjectPeople(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
			}
		});
		btnBack.setBounds(10, 11, 66, 23);
		contentPane.add(btnBack);
		
		JButton btnSave = new JButton("Add Developer");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					PreparedStatement pst = conn.prepareStatement("SELECT deactivated from project_person "
							+ "WHERE ProjectNumber=? AND PersonName=?");
					pst.setString(1, args[2]);
					pst.setString(2, (String)cmbDeveloper.getSelectedItem());
					
					ResultSet rs = pst.executeQuery();
					
					if(rs.next()){
						PreparedStatement pst2 = conn.prepareStatement("UPDATE project_person SET "
								+ "deactivated=? WHERE ProjectNumber=? AND PersonName=?");
						pst2.setBoolean(1, false);
						pst2.setString(2, args[2]);
						pst2.setString(3, (String)cmbDeveloper.getSelectedItem());
						
						pst2.executeUpdate();
						
					} else {
						PreparedStatement pst2 = conn.prepareStatement("INSERT INTO project_person (`ProjectNumber`, "
								+ "`PersonName`, `deactivated`) VALUES (?,?,?)");
						pst2.setString(1, args[2]);
						pst2.setString(2, (String)cmbDeveloper.getSelectedItem());
						pst2.setBoolean(3, false);
						
						pst2.executeUpdate();
					}
					
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"Developer "+(String)cmbDeveloper.getSelectedItem()
			        +" has been successfuy added to the project "+args[2]);
			        
			        ProjectPeople frame;
					try {
						frame = new ProjectPeople(args);
						frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
						frame.setVisible(true);
						dispose();
					} catch (SQLException | IOException e) {
						e.printStackTrace();
					}
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnSave.setBounds(249, 227, 131, 23);
		contentPane.add(btnSave);
		
		cmbDeveloper.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				txtTitle.setText(developerNameTitleMap.get(cmbDeveloper.getSelectedItem()));
				txtRole.setText(developerNameBillRateMap.get(cmbDeveloper.getSelectedItem()));
			}
		});
		
	}

}
