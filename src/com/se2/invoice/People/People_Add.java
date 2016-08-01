package com.se2.invoice.People;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.se2.invoice.forms.dataConnectionObject;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;

public class People_Add extends JFrame {

	private JPanel contentPane;
	private JTextField txtPassword;
	private JTextField txtTitle;
	private JTextField txtName;
	private JTextField txtBillRate;
	private BufferedImage backgroundImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					People_Add frame = new People_Add(args);
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
	 */
	public People_Add(String[] args) throws IOException {
		
		try {
		    //backgroundImage = ImageIO.read(new File("C:\\Users\\Honey\\workspace\\Invoice\\eagle1.jpg"));
			backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setIconImage(backgroundImage);
		
		dataConnectionObject dco = new dataConnectionObject();
		Connection conn = dco.getConnection();
        
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 682, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(34, 45, 78, 14);
		contentPane.add(lblName);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(125, 129, 151, 20);
		contentPane.add(txtPassword);
		txtPassword.setColumns(10);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setBounds(338, 45, 46, 14);
		contentPane.add(lblTitle);
		
		txtTitle = new JTextField();
		txtTitle.setBounds(415, 42, 158, 20);
		contentPane.add(txtTitle);
		txtTitle.setColumns(10);
		
		JLabel lblBillrate = new JLabel("BillRate");
		lblBillrate.setBounds(34, 90, 46, 14);
		contentPane.add(lblBillrate);
		
		txtName = new JTextField();
		txtName.setColumns(10);
		txtName.setBounds(125, 42, 151, 20);
		contentPane.add(txtName);
		
		JLabel lblRole = new JLabel("Role");
		lblRole.setBounds(338, 90, 46, 14);
		contentPane.add(lblRole);
		
		JComboBox cmbRole = new JComboBox();
		cmbRole.setModel(new DefaultComboBoxModel(new String[] {"Developer", "Project Manager"}));
		cmbRole.setBounds(415, 87, 158, 20);
		contentPane.add(cmbRole);
		
		JCheckBox chckbxDeactivated = new JCheckBox("Deactivated");
		chckbxDeactivated.setBounds(338, 128, 97, 23);
		contentPane.add(chckbxDeactivated);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(34, 132, 46, 14);
		contentPane.add(lblPassword);
		
		txtBillRate = new JTextField();
		txtBillRate.setColumns(10);
		txtBillRate.setBounds(125, 87, 151, 20);
		contentPane.add(txtBillRate);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
        			PreparedStatement pst = conn.prepareStatement("INSERT INTO `people`(`Name`, `Title`, "
        					+ "`BillRate`, `Role`, `password`, `deactivated`) VALUES (?,?,?,?,?,?)");
        			
        			pst.setString(1, txtName.getText());
        			pst.setString(2, txtTitle.getText());
        			pst.setString(3, txtBillRate.getText()); 
        			pst.setString(4, (String)cmbRole.getSelectedItem());
        			pst.setString(5, txtPassword.getText());;
        			pst.setBoolean(6, chckbxDeactivated.isSelected());
        			
					pst.executeUpdate();
					
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"People addedd successfully.");
					
					People frame = new People(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
					
					
        		} catch(SQLException | IOException io){
        			io.printStackTrace();
        		}
			}
		});
		btnSave.setBounds(277, 192, 89, 23);
		contentPane.add(btnSave);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try{
        			PreparedStatement pst = conn.prepareStatement("UPDATE `people` SET"
        					+ "`Title`=?,`BillRate`=?,`Role`=?,`password`=?,`deactivated`=? WHERE Name=?");
        			
        			pst.setString(1, txtTitle.getText());
        			pst.setString(2, txtBillRate.getText()); 
        			pst.setString(3, (String)cmbRole.getSelectedItem());
        			pst.setString(4, txtPassword.getText());
        			pst.setBoolean(5, chckbxDeactivated.isSelected());
					pst.setString(6, txtName.getText());
					
					pst.executeUpdate();
					
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"People updated successfully.");
					
					People frame = new People(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
					
					
        		} catch(SQLException | IOException io){
        			io.printStackTrace();
        		}
				
			}
		});
		btnUpdate.setBounds(277, 216, 89, 23);
		contentPane.add(btnUpdate);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				People frame;
				try {
					frame = new People(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnBack.setBounds(10, 11, 66, 23);
		contentPane.add(btnBack);
		
		if("update".equals(args[1])){
			txtName.setEnabled(false);
			btnSave.setVisible(false);
			btnUpdate.setVisible(true);
			setTitle("Update People Details");
        	txtName.setText(args[2]);
        	txtTitle.setText(args[3]);
        	txtBillRate.setText(args[4]);
        	cmbRole.setSelectedItem(args[5]);
        	txtPassword.setText(args[6]);
        	if("1".equals(args[7]))chckbxDeactivated.setSelected(true);
        	else chckbxDeactivated.setSelected(false);
        	
        } else {
        	setTitle("Add New People Details");
        	btnUpdate.setVisible(false);
        	btnSave.setVisible(true);
        }
		
	}
}
