package com.se2.invoice.forms;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.se2.invoice.Client.Client;
import com.se2.invoice.Company.Company;
import com.se2.invoice.People.People;
import com.se2.invoice.Project.Project;
import com.se2.invoice.ProjectPeople.ProjectPeople;

public class Home extends JFrame {

	private JPanel contentPane;
	private BufferedImage backgroundImage;

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home(args);
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
	
	public Home(final String[] args) {
		try {
		    backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setTitle("Home");
		setIconImage(backgroundImage);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 768, 480);
		
		JMenuBar mainmenubar = new JMenuBar();
		mainmenubar.setToolTipText("");
		setJMenuBar(mainmenubar);

		
		JMenu mmMaintainence = new JMenu("Maintainence");
		mainmenubar.add(mmMaintainence);
		if(!"Developer".equalsIgnoreCase(args[0])){
			mmMaintainence.setEnabled(true);
		} else mmMaintainence.setEnabled(false);
		
		JMenuItem mmtmCompany = new JMenuItem("Company");
		mmMaintainence.add(mmtmCompany);
		if("Accountant".equalsIgnoreCase(args[0])){
			mmtmCompany.setEnabled(true);
		} else mmtmCompany.setEnabled(false);
		mmtmCompany.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Company frame;
				try {
					frame = new Company(args);
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
		
		JMenuItem mntmClient = new JMenuItem("Client");
		mntmClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Client frame;
				try {
					frame = new Client(args);
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
		mmMaintainence.add(mntmClient);
		if("Accountant".equalsIgnoreCase(args[0])){
			mntmClient.setEnabled(true);
		} else mntmClient.setEnabled(false);
		
		
		JMenuItem mntmProject = new JMenuItem("Project");
		mntmProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					Project frame = new Project(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
				} catch (IOException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setVisible(false);
			}
		});
		mmMaintainence.add(mntmProject);
		if("Project Manager".equalsIgnoreCase(args[0])){
			mntmProject.setEnabled(true);
		} else mntmProject.setEnabled(false);
		
		JMenuItem mntmPeople = new JMenuItem("People");
		mntmPeople.addActionListener(new ActionListener() {
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
		mmMaintainence.add(mntmPeople);
		if("Accountant".equalsIgnoreCase(args[0])){
			mntmPeople.setEnabled(true);
		} else mntmPeople.setEnabled(false);
		
		JMenuItem mntmPrjtPPl = new JMenuItem("Project-People");
		mmMaintainence.add(mntmPrjtPPl);
		if("Project Manager".equalsIgnoreCase(args[0])){
			mntmPrjtPPl.setEnabled(true);
		} else mntmPrjtPPl.setEnabled(false);
		mntmPrjtPPl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProjectPeople frame;
				try {
					frame = new ProjectPeople(args);
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
		
		JMenu mnTimesheet = new JMenu("Timesheet");
		mainmenubar.add(mnTimesheet);
		if(!"Accountant".equalsIgnoreCase(args[0])){
			mnTimesheet.setEnabled(true);
		} else mnTimesheet.setEnabled(false);
		
		JMenuItem mntmSubmit = new JMenuItem("Submit");
		mnTimesheet.add(mntmSubmit);
		
		JMenuItem mntmApprove = new JMenuItem("Approve");
		mnTimesheet.add(mntmApprove);
		if(!"Developer".equalsIgnoreCase(args[0])){
			mntmApprove.setEnabled(true);
		} else mntmApprove.setEnabled(false);
		
		JMenu mnInvoice = new JMenu("Invoice");
		mainmenubar.add(mnInvoice);
		if("Accountant".equalsIgnoreCase(args[0])){
			mnInvoice.setEnabled(true);
		} else mnInvoice.setEnabled(false);
		
		JMenuItem mntmGenerate = new JMenuItem("Generate");
		mnInvoice.add(mntmGenerate);
		
		JMenu mnReports = new JMenu("Reports");
		mainmenubar.add(mnReports);
		
		JMenuItem mntmHoursReport = new JMenuItem("Hours Clocked");
		mnReports.add(mntmHoursReport);
		
		JMenuItem mntmProjectReport = new JMenuItem("Project");
		mnReports.add(mntmProjectReport);
		
		JMenuItem mntmInvoiceReport = new JMenuItem("Invoice");
		mnReports.add(mntmInvoiceReport);
		if("Accountant".equalsIgnoreCase(args[0])){
			mntmInvoiceReport.setEnabled(true);
		} else mntmInvoiceReport.setEnabled(false);
		
		JMenu mnSystem = new JMenu("System");
		mainmenubar.add(mnSystem);
		
		JMenuItem mntmChangePassword = new JMenuItem("Change Password");
		mnSystem.add(mntmChangePassword);
		
		/*JMenuItem mntmUpdateUsername = new JMenuItem("Add/Update Username");
		mnSystem.add(mntmUpdateUsername);*/
		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mnSystem.add(mntmLogout);
		mntmLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel lblUnivName = new JLabel("Welcome to Eagle Consulting Services");
		lblUnivName.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblUnivName.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblUnivName, BorderLayout.CENTER);
		
	}
	
	/*public Home() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}*/

}
