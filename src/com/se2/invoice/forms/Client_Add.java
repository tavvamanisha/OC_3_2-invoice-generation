package com.se2.invoice.forms;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.jdesktop.swingx.JXDatePicker;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Client_Add extends JFrame {

	private JPanel contentPane;
	private JTextField txtClientNumber;
	private JTextField txtCity;
	private JTextField txtClientName;
	private JTextField textField_3;
	private JTextField txtClientContact;
	private JTextField txtZIP;
	private JTextField txtBilling;
	private JTextField txtState;
	private JTextField txtEmail;
	private JTextArea txtAddress1;
	private JTextArea txtAddress2;
	private JComboBox cmbInvoicefreq;
	private JComboBox cmbInvoiceGroup;
	private JCheckBox chckbxDeactivate;
	private BufferedImage backgroundImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client_Add frame = new Client_Add(args);
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
	public Client_Add(String[] args) throws IOException {
		
		dataConnectionObject dco = new dataConnectionObject();
		Connection conn = dco.getConnection();
		
		try {
		    backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setTitle("Client Information");
		setIconImage(backgroundImage);
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 694, 496);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblProject = new JLabel("Address1");
		lblProject.setBounds(22, 96, 117, 14);
		contentPane.add(lblProject);
		
		JLabel lblClientName = new JLabel("Client Name");
		lblClientName.setBounds(353, 57, 96, 14);
		contentPane.add(lblClientName);
		
		JLabel lblStartDate = new JLabel("City");
		lblStartDate.setBounds(22, 174, 75, 14);
		contentPane.add(lblStartDate);
		
		JLabel lblClientContact = new JLabel("ZIP");
		lblClientContact.setBounds(22, 209, 82, 14);
		contentPane.add(lblClientContact);
		
		JLabel lblBudget = new JLabel("State");
		lblBudget.setBounds(353, 174, 82, 14);
		contentPane.add(lblBudget);
		
		txtClientNumber = new JTextField();
		txtClientNumber.setBounds(142, 54, 177, 20);
		contentPane.add(txtClientNumber);
		txtClientNumber.setColumns(10);
		
		txtCity = new JTextField();
		txtCity.setBounds(142, 171, 177, 20);
		contentPane.add(txtCity);
		txtCity.setColumns(10);
		
		txtClientName = new JTextField();
		txtClientName.setBounds(469, 54, 161, 20);
		contentPane.add(txtClientName);
		txtClientName.setColumns(10);
		
		JXDatePicker picker = new JXDatePicker();
		picker.setBounds(0, 0, 0, 0);
        picker.setDate(Calendar.getInstance().getTime());
        picker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));

        contentPane.add(picker);
        
        JButton btnSave = new JButton("Save");
        btnSave.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		
        		try{
        			PreparedStatement pst = conn.prepareStatement("INSERT INTO `client`"
	                		+ "(`Client_Number`, `Name`, `Address1`, `Address2`, "
	                		+ "`City`, `State`, `ZIP`, `Email`, "
	                		+ "`Contact`, `Invoice_Frequency`,`Billing_terms`, `Invoice_Grouping`,`deactivated`) VALUES "
	                		+ "(?,?,?,?,?,?,?,?,?,?,?,?,?)");
        			
        			pst.setString(1, txtClientNumber.getText());
        			pst.setString(2, txtClientName.getText());
        			pst.setString(3, txtAddress1.getText()); 
        			pst.setString(4, txtAddress2.getText());
        			pst.setString(5, txtCity.getText());
        			pst.setString(6, txtState.getText());
					pst.setString(7, txtZIP.getText());
					pst.setString(8, txtEmail.getText());
					pst.setString(9, txtClientContact.getText());
					pst.setString(10, (String)cmbInvoicefreq.getSelectedItem());
					pst.setString(11, txtBilling.getText());
					pst.setString(12, (String)cmbInvoiceGroup.getSelectedItem());
					pst.setBoolean(13, chckbxDeactivate.isSelected());
					
					pst.executeUpdate();
					
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"Client addedd successfully.");
					
					Client frame = new Client(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
					
					
        		} catch(SQLException | IOException io){
        			io.printStackTrace();
        		}
        		
        	}
        });
        btnSave.setBounds(298, 354, 89, 23);
        contentPane.add(btnSave);
        
        JLabel lblNumber = new JLabel("Client Number");
        lblNumber.setBounds(20, 57, 648, 14);
        contentPane.add(lblNumber);
        
        txtAddress1 = new JTextArea();
        txtAddress1.setBounds(142, 91, 177, 62);
        contentPane.add(txtAddress1);
        
        JLabel lblAddress = new JLabel("Address2");
        lblAddress.setBounds(353, 96, 117, 14);
        contentPane.add(lblAddress);
        
        txtAddress2 = new JTextArea();
        txtAddress2.setBounds(472, 91, 161, 62);
        contentPane.add(txtAddress2);
        
        txtClientContact = new JTextField();
        txtClientContact.setColumns(10);
        txtClientContact.setBounds(469, 241, 164, 20);
        contentPane.add(txtClientContact);
        
        txtZIP = new JTextField();
        txtZIP.setColumns(10);
        txtZIP.setBounds(142, 206, 177, 20);
        contentPane.add(txtZIP);
        
        JLabel lblInvoiceFrequency = new JLabel("Invoice Frequency");
        lblInvoiceFrequency.setBounds(22, 284, 117, 14);
        contentPane.add(lblInvoiceFrequency);
        
        cmbInvoicefreq = new JComboBox();
        cmbInvoicefreq.setModel(new DefaultComboBoxModel(new String[] {"Weekly", "Bi-Weekly", "Monthly", "Monthly-Cal"}));
        cmbInvoicefreq.setBounds(142, 281, 177, 20);
        contentPane.add(cmbInvoicefreq);
        
        JLabel lblBillingTerms = new JLabel("Billing Terms");
        lblBillingTerms.setBounds(22, 244, 82, 14);
        contentPane.add(lblBillingTerms);
        
        txtBilling = new JTextField();
        txtBilling.setColumns(10);
        txtBilling.setBounds(142, 241, 177, 20);
        contentPane.add(txtBilling);
        
        JLabel lblInvoiceGrouping = new JLabel("Invoice Grouping");
        lblInvoiceGrouping.setBounds(353, 284, 117, 14);
        contentPane.add(lblInvoiceGrouping);
        
        cmbInvoiceGroup = new JComboBox();
        cmbInvoiceGroup.setModel(new DefaultComboBoxModel(new String[] {"Client", "Project"}));
        cmbInvoiceGroup.setBounds(469, 284, 161, 20);
        contentPane.add(cmbInvoiceGroup);
        
        JButton btnUpdate = new JButton("Update");
        //btnUpdate.addActionListener(new ActionListener() {
    	btnUpdate.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent arg0) {
            		
            		try{
            			PreparedStatement pst = conn.prepareStatement("UPDATE `client` SET "
            					+ "`Name`=?,`Address1`=?,"
            					+ "`Address2`=?,`City`=?,`State`=?,"
            					+ "`ZIP`=?,`Email`=?,`Contact`=?,"
            					+ "`Invoice_Frequency`=?,`Billing_terms`=?,"
            					+ "`Invoice_Grouping`=?,`deactivated`=? "
            					+ "WHERE `Client_Number`=?");
            			
            			pst.setString(1, txtClientName.getText());
            			pst.setString(2, txtAddress1.getText()); 
            			pst.setString(3, txtAddress2.getText());
            			pst.setString(4, txtCity.getText());
            			pst.setString(5, txtState.getText());
    					pst.setString(6, txtZIP.getText());
    					pst.setString(7, txtEmail.getText());
    					pst.setString(8, txtClientContact.getText());
    					pst.setString(9, (String)cmbInvoicefreq.getSelectedItem());
    					pst.setString(10, txtBilling.getText());
    					pst.setString(11, (String)cmbInvoiceGroup.getSelectedItem());
    					pst.setBoolean(12, chckbxDeactivate.isSelected());
    					pst.setString(13, txtClientNumber.getText());
            			
    					pst.executeUpdate();
    					
    					JFrame parent = new JFrame();
    			        JOptionPane.showMessageDialog(parent,"Client updated successfully.");
    					
    					Client frame = new Client(args);
    					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    					frame.setVisible(true);
    					dispose();
    					
    					
            		} catch(SQLException | IOException io){
            			io.printStackTrace();
            		}
            		
            	}
            });
            btnUpdate.setBounds(298, 388, 89, 23);
        contentPane.add(btnUpdate);
        
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setBounds(353, 209, 46, 14);
        contentPane.add(lblEmail);
        
        txtState = new JTextField();
        txtState.setColumns(10);
        txtState.setBounds(469, 171, 164, 20);
        contentPane.add(txtState);
        
        JLabel lblContact = new JLabel("Contact");
        lblContact.setBounds(353, 246, 46, 14);
        contentPane.add(lblContact);
        
        txtEmail = new JTextField();
        txtEmail.setColumns(10);
        txtEmail.setBounds(469, 206, 164, 20);
        contentPane.add(txtEmail);
        
        chckbxDeactivate = new JCheckBox("Deactivate");
        chckbxDeactivate.setBounds(289, 325, 97, 23);
        contentPane.add(chckbxDeactivate);
        
        JButton btnBack = new JButton("Back");
        btnBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		Client frame;
				try {
					frame = new Client(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
				} catch (SQLException | IOException e) {
					e.printStackTrace();
				}
        	}
        });
        btnBack.setBounds(8, 11, 89, 23);
        contentPane.add(btnBack);
        
        if("update".equals(args[2])){
        	txtClientNumber.setEnabled(false);
        	btnSave.setVisible(false);
        	setTitle("Update Client Details");
        	
        	txtClientNumber.setText(args[3]);
        	txtClientName.setText(args[4]);
        	txtAddress1.setText(args[5]);
        	txtAddress2.setText(args[6]);
        	txtCity.setText(args[7]);
        	txtState.setText(args[8]);
        	txtZIP.setText(args[9]);
        	txtEmail.setText(args[10]);
        	txtClientContact.setText(args[11]);
        	cmbInvoicefreq.setSelectedItem(args[12]);
        	txtBilling.setText(args[13]);
        	cmbInvoiceGroup.setSelectedItem(args[14]);
        	if("0".equals(args[15]))chckbxDeactivate.setSelected(false);
        	else chckbxDeactivate.setSelected(true);
        	        	
        } else {
        	setTitle("Add New Client Details");
        	btnUpdate.setVisible(false);
        }
        
		
	}
}
