package com.se2.invoice.ProjectPeople;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;

public class AddDeveloper extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AddDeveloper frame = new AddDeveloper();
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
	public AddDeveloper() {
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
		
		JLabel lblDeveloper = new JLabel("Developer");
		lblDeveloper.setBounds(59, 101, 85, 14);
		contentPane.add(lblDeveloper);
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(154, 98, 150, 20);
		contentPane.add(comboBox);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setBounds(59, 144, 46, 14);
		contentPane.add(lblTitle);
		
		JLabel lblRole = new JLabel("Role");
		lblRole.setBounds(59, 185, 46, 14);
		contentPane.add(lblRole);
		
		textField = new JTextField();
		textField.setBounds(154, 141, 150, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBounds(154, 182, 150, 20);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(10, 11, 66, 23);
		contentPane.add(btnBack);
		
		JButton btnSave = new JButton("Save");
		btnSave.setBounds(249, 227, 89, 23);
		contentPane.add(btnSave);
	}

}
