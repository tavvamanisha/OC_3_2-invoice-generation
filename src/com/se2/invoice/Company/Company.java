package com.se2.invoice.Company;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.se2.invoice.forms.dataConnectionObject;

import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;

public class Company extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtCity;
	private JTextField txtState;
	private JTextField txtZip;
	private JTextArea txtAddress1;
	private JTextArea txtAddress2;
	private BufferedImage backgroundImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Company frame = new Company();
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
	public Company(String[] args) throws IOException, SQLException {
		
		dataConnectionObject dco = new dataConnectionObject();
		Connection conn = dco.getConnection();
		
		try {
		    backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setTitle("Update Company Information");
		setIconImage(backgroundImage);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 605, 408);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(28, 46, 46, 14);
		contentPane.add(lblName);
		
		JLabel lblAddress_2 = new JLabel("Address2");
		lblAddress_2.setBounds(311, 94, 72, 14);
		contentPane.add(lblAddress_2);
		
		JLabel lblAddress = new JLabel("Address1");
		lblAddress.setBounds(311, 94, 46, 14);
		contentPane.add(lblAddress);
		
		JLabel lblAddress_1 = new JLabel("Address1");
		lblAddress_1.setBounds(28, 94, 46, 14);
		contentPane.add(lblAddress_1);
		
		txtAddress1 = new JTextArea();
		txtAddress1.setBounds(101, 89, 177, 71);
		contentPane.add(txtAddress1);
		
		txtName = new JTextField();
		txtName.setBounds(101, 43, 256, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		txtAddress2 = new JTextArea();
		txtAddress2.setBounds(381, 94, 168, 65);
		contentPane.add(txtAddress2);
		
		JLabel lblCity = new JLabel("City");
		lblCity.setBounds(28, 215, 46, 14);
		contentPane.add(lblCity);
		
		JLabel lblState = new JLabel("State");
		lblState.setBounds(311, 215, 46, 14);
		contentPane.add(lblState);
		
		JLabel lblZip = new JLabel("ZIP");
		lblZip.setBounds(28, 265, 46, 14);
		contentPane.add(lblZip);
		
		txtCity = new JTextField();
		txtCity.setBounds(101, 212, 177, 20);
		contentPane.add(txtCity);
		txtCity.setColumns(10);
		
		txtState = new JTextField();
		txtState.setColumns(10);
		txtState.setBounds(381, 212, 173, 20);
		contentPane.add(txtState);
		
		txtZip = new JTextField();
		txtZip.setBounds(101, 262, 177, 20);
		contentPane.add(txtZip);
		txtZip.setColumns(10);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					PreparedStatement pst1 = conn.prepareStatement("UPDATE `company` SET `Name`=?,`Address1`=?,"
							+ "`Address2`=?,`City`=?,`State`=?,`ZIP`=?");
					
					pst1.setString(1, txtName.getText());
					pst1.setString(2, txtAddress1.getText());
					pst1.setString(3, txtAddress2.getText());
					pst1.setString(4, txtCity.getText());
					pst1.setString(5, txtState.getText());
					pst1.setString(6, txtZip.getText());
					
					pst1.executeUpdate();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		btnUpdate.setBounds(284, 308, 89, 23);
		contentPane.add(btnUpdate);
		
		PreparedStatement pst = conn.prepareStatement("Select * from company");
		ResultSet rs = pst.executeQuery();
		
		if(rs.next()){
			
			txtName.setText(rs.getString("Name"));
			txtAddress1.setText(rs.getString("Address1"));
			txtAddress2.setText(rs.getString("Address2"));
			txtCity.setText(rs.getString("City"));
			txtState.setText(rs.getString("State"));
			txtZip.setText(rs.getString("ZIP"));
			
		}
	}
}
