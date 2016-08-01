package com.se2.invoice.Project;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import org.jdesktop.swingx.JXDatePicker;

import com.se2.invoice.forms.dataConnectionObject;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.awt.event.ItemEvent;

public class Project_Add extends JFrame {

	private JPanel contentPane;
	private JTextField textProjectName;
	private JTextField textClientContact;
	private JTextField textBudget;
	private JTextField textProjectNumber;
	private BufferedImage backgroundImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Project_Add frame = new Project_Add(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws ParseException 
	 */
	public Project_Add(String[] args) throws IOException, SQLException, ParseException{
		
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
		setBounds(100, 100, 840, 376);
		
		if("add".equals(args[1])){
			setTitle("Add New Project");
		} else {
			setTitle("Update Project");
		}
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblProject = new JLabel("Project Name");
		lblProject.setBounds(31, 62, 116, 14);
		contentPane.add(lblProject);
		
		
		JLabel lblClientName = new JLabel("Client Name");
		lblClientName.setBounds(436, 23, 96, 14);
		contentPane.add(lblClientName);
		
		JLabel lblStartDate = new JLabel("Start Date");
		lblStartDate.setBounds(31, 107, 75, 14);
		contentPane.add(lblStartDate);
		
		JLabel lblEndDate = new JLabel("End Date");
		lblEndDate.setBounds(436, 107, 82, 14);
		contentPane.add(lblEndDate);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(31, 152, 75, 14);
		contentPane.add(lblStatus);
		
		JLabel lblProjectManager = new JLabel("Project Manager");
		lblProjectManager.setBounds(436, 62, 111, 14);
		contentPane.add(lblProjectManager);
		
		JLabel lblClientContact = new JLabel("Client Contact");
		lblClientContact.setBounds(31, 196, 82, 14);
		contentPane.add(lblClientContact);
		
		JLabel lblBudget = new JLabel("Budget");
		lblBudget.setBounds(436, 152, 82, 14);
		contentPane.add(lblBudget);
		
		JCheckBox chckbxDeactivated = new JCheckBox("Deactivated");
		chckbxDeactivated.setBounds(436, 192, 97, 23);
		contentPane.add(chckbxDeactivated);
		
		textProjectName = new JTextField();
		textProjectName.setBounds(186, 59, 177, 20);
		contentPane.add(textProjectName);
		textProjectName.setColumns(10);
		
		textClientContact = new JTextField();
		textClientContact.setBounds(183, 193, 177, 20);
		contentPane.add(textClientContact);
		textClientContact.setColumns(10);
		
		
		
		textBudget = new JTextField();
		textBudget.setBounds(562, 149, 149, 20);
		contentPane.add(textBudget);
		textBudget.setColumns(10);
		
		JComboBox comboClientName = new JComboBox();
		comboClientName.setBounds(562, 20, 217, 20);
		contentPane.add(comboClientName);
		
		PreparedStatement pst1 = conn.prepareStatement("SELECT Client_Number, Name, Contact from client");
		ResultSet rs1 = pst1.executeQuery();
		String[] clientNameOptions = new String[1000];
		int i=0;
		HashMap<String, String> clientContactMap= new HashMap<>();
		HashMap<String, String> clientNumberNameMap= new HashMap<>();
		HashMap<String, String> clientNameNumberMap= new HashMap<>();
		while(rs1.next()){
			String str = rs1.getString("Client_Number");
			String str2 = rs1.getString("Name");
			clientContactMap.put(str2, rs1.getString("Contact"));
			clientNumberNameMap.put(str, str2);
			clientNameNumberMap.put(str2, str);
			
			clientNameOptions[i] = str2;
			i++;
		}
		comboClientName.setModel(new DefaultComboBoxModel(Arrays.copyOf(clientNameOptions, i)));
		
		
		JXDatePicker picker = new JXDatePicker();
        picker.setDate(Calendar.getInstance().getTime());
        picker.setFormats(new SimpleDateFormat("dd.MM.yyyy"));

        contentPane.add(picker);
        
        JXDatePicker startDate = new JXDatePicker();
        startDate.getEditor().setEditable(false);
        startDate.getEditor().setText("Choose Date");
        startDate.setBounds(186, 103, 177, 22);
        contentPane.add(startDate);
        
        JXDatePicker endDate = new JXDatePicker();
        endDate.getEditor().setText("Choose Date");
        endDate.setBounds(562, 103, 149, 22);
        contentPane.add(endDate);
        
        JComboBox comboStatus = new JComboBox();
        comboStatus.setModel(new DefaultComboBoxModel(new String[] {"In Progress", "Closed"}));
        comboStatus.setBounds(186, 149, 177, 20);
        contentPane.add(comboStatus);
        
        JComboBox comboProjectManager = new JComboBox();
        comboProjectManager.setBounds(562, 59, 217, 20);
        contentPane.add(comboProjectManager);
        
        PreparedStatement pst2 = conn.prepareStatement("SELECT Name from people where Role = 'Project Manager'");
		ResultSet rs2 = pst2.executeQuery();
		String[] projectManagerOptions = new String[1000];
		int j=0;
		while(rs2.next()){
			projectManagerOptions[j] = rs2.getString("Name");
			j++;;
		}
		comboProjectManager.setModel(new DefaultComboBoxModel(Arrays.copyOf(projectManagerOptions, j)));
        
        
        JButton btnSave = new JButton("Save");
        if("add".equalsIgnoreCase(args[1])){
        	btnSave.setVisible(true);
        } else {
        	btnSave.setVisible(false);
        }
        btnSave.addActionListener(new ActionListener() {
    	public void actionPerformed(ActionEvent e){
        		
        		try{
        			PreparedStatement pst = conn.prepareStatement("INSERT INTO `project`"
	                		+ "(`Client`, `ProjectNumber`, `ProjectName`, `StartDate`, "
	                		+ "`EndDate`, `Status`, `ProjectManager`, `ClientContact`, "
	                		+ "`Budget`, `deactivated`) VALUES "
	                		+ "(?,?,?,?,?,?,?,?,?,?)");
        			
        			pst.setString(1, clientNameNumberMap.get(comboClientName.getSelectedItem()));
        			pst.setString(2, textProjectNumber.getText());
        			pst.setString(3, textProjectName.getText());
					pst.setDate(4, new java.sql.Date(startDate.getDate().getTime()));
					pst.setDate(5, new java.sql.Date(endDate.getDate().getTime()));
					pst.setString(6, (String)comboStatus.getSelectedItem());
					pst.setString(7, (String)comboProjectManager.getSelectedItem());
					pst.setString(8, textClientContact.getText());
					pst.setString(9, textBudget.getText());
					pst.setBoolean(10, chckbxDeactivated.isSelected());
					
					pst.executeUpdate();
					
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"Client added successfully.");
					
					Project frame = new Project(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
					
					
        		} catch(SQLException | IOException io){
        			io.printStackTrace();
        		}
        		
				
        	}
        });
        btnSave.setBounds(290, 254, 89, 23);
        contentPane.add(btnSave);
        
        JButton btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					PreparedStatement pst = conn.prepareStatement("UPDATE `project` SET `Client`=?,"
							+ "`ProjectName`=?,`StartDate`=?,`EndDate`=?,"
							+ "`Status`=?,`ProjectManager`=?,`ClientContact`=?,`Budget`=?,"
							+ "`deactivated`=? WHERE `ProjectNumber`=?");
					
					pst.setString(1, clientNameNumberMap.get(comboClientName.getSelectedItem()));
					pst.setString(2, textProjectName.getText());
					pst.setDate(3, new java.sql.Date(startDate.getDate().getTime()));
					pst.setDate(4, new java.sql.Date(endDate.getDate().getTime()));
					pst.setString(5, (String)comboStatus.getSelectedItem());
					pst.setString(6, (String)comboProjectManager.getSelectedItem());
					pst.setString(7, textClientContact.getText());
					pst.setString(8, textBudget.getText());
					pst.setBoolean(9, chckbxDeactivated.isSelected());
					pst.setString(10, textProjectNumber.getText());
					
					pst.executeUpdate();
					
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"Project updated successfully.");
					
					Project frame = new Project(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
					
					
				} catch (SQLException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
        btnUpdate.setBounds(290, 280, 89, 23);
        contentPane.add(btnUpdate);
        if("update".equalsIgnoreCase(args[1])){
        	btnUpdate.setVisible(true);
        } else {
        	btnUpdate.setVisible(false);
        }
        
        JLabel lblProjectNumber = new JLabel("Project Number");
        lblProjectNumber.setBounds(31, 23, 116, 14);
        contentPane.add(lblProjectNumber);
        
        textProjectNumber = new JTextField();
        textProjectNumber.setBounds(186, 20, 177, 20);
        contentPane.add(textProjectNumber);
        textProjectNumber.setColumns(10);
        if("update".equals(args[1])){
        	textProjectNumber.setText(args[2]);
        	textProjectName.setText(args[3]);
        	comboClientName.setSelectedItem(clientNumberNameMap.get(args[4]));
        	DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        	startDate.setDate(format.parse(args[5]));
        	endDate.setDate(format.parse(args[6]));
        	comboStatus.setSelectedItem(args[7]);
        	comboProjectManager.setSelectedItem(args[8]);
        	textBudget.setText(args[10]);
        	
        	boolean deactiveStatus = false;
        	
        	if("Deactive".equals(args[11]))deactiveStatus=true;
        	chckbxDeactivated.setSelected(deactiveStatus);
        	
        	textProjectNumber.setEnabled(false);
        	
        }
        
        textClientContact.setText(clientContactMap.get(comboClientName.getSelectedItem()));
		
		comboClientName.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				textClientContact.setText(clientContactMap.get(comboClientName.getSelectedItem()));
			}
		});
        
	}
}
