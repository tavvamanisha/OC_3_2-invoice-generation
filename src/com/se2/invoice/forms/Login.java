package com.se2.invoice.forms;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.se2.invoice.People.Employee;

import javax.swing.JLabel;
import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private JPanel contentPane;
	//private JTextField textField;
	private JPasswordField passwordField;
	private BufferedImage backgroundImage;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("InvoiceSE2");
	    EntityManager entitymanager = emfactory.createEntityManager();
	    
		try {
		    //backgroundImage = ImageIO.read(new File("C:\\Users\\Honey\\workspace\\Invoice\\eagle1.jpg"));
			backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setTitle("Login");
		setIconImage(backgroundImage);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 885, 481);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setFont(new Font("Serif", Font.BOLD | Font.ITALIC, 20));
		lblUsername.setBounds(262, 141, 105, 27);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(262, 179, 105, 27);
		Font myFont = new Font("Serif", Font.ITALIC | Font.BOLD, 5);
	    Font newFont = myFont.deriveFont(20F);
	    lblPassword.setFont(newFont);
		contentPane.add(lblPassword);
		
		/*textField = new JTextField();
		contentPane.add(textField);
		textField.setColumns(10);*/
		
		passwordField = new JPasswordField();
		passwordField.setBounds(426, 186, 116, 20);
		contentPane.add(passwordField);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Employee employee = entitymanager.find(Employee.class, textField.getText());
				
				if(employee != null){
					if(employee.getPassword().equalsIgnoreCase(passwordField.getText().toString())){
						
						String[] str = new String[1];
						str[0] = employee.getRole();
						new Home(str).setVisible(true);
						new Home(str);
						setVisible(false);
						
					} else System.out.println("Password not correct");
				} else {
					System.out.println("Username not correct");
				}
				
			}
		});
		btnLogin.setBounds(339, 275, 89, 23);
		contentPane.add(btnLogin);
		
		textField = new JTextField();
		textField.setBounds(426, 148, 116, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
	}
}
