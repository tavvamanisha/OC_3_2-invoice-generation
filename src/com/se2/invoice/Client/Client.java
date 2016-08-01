package com.se2.invoice.Client;
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
import java.text.ParseException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.se2.invoice.forms.Home;
import com.se2.invoice.forms.dataConnectionObject;

import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Client extends JFrame {

	private JPanel contentPane;
	private BufferedImage backgroundImage;
	
	JTable table = new JTable();
	DefaultTableModel model = new DefaultTableModel();
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Client frame = new Client(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
	public Client(String[] args) throws IOException, SQLException {
		
		try {
		    backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setTitle("Client Information");
		setIconImage(backgroundImage);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1350, 772);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		//private JTable table;
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(36, 58, 1266, 664);
		scrollPane.setForeground(Color.GRAY);
		scrollPane.setBackground(Color.GRAY);
		scrollPane.setAutoscrolls(true);
		scrollPane.setToolTipText("");
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				try {
					String[] argNew = Arrays.copyOf(args, 2);
					argNew[1]= "add";
					new Client_Add(argNew).setVisible(true);
					
				} catch (Exception  ex) {
					ex.printStackTrace();
				}
				
			}
		});
		btnAdd.setBounds(304, 16, 76, 23);
		
		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
				if(row== -1){
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"Please select a row.");
				}else{
					dispose();
					try {
						String[] argNew = Arrays.copyOf(args, 15);
						//argNew[0] = args[0];
						argNew[1]= "update";
						argNew[2]=(String)table.getModel().getValueAt(row, 0);
						argNew[3]=(String)table.getModel().getValueAt(row, 1);
						argNew[4]=(String)table.getModel().getValueAt(row, 2);
						argNew[5]=(String)table.getModel().getValueAt(row, 3);
						argNew[6]=(String)table.getModel().getValueAt(row, 4);
						argNew[7]=(String)table.getModel().getValueAt(row, 5);
						argNew[8]=(String)table.getModel().getValueAt(row, 6);
						argNew[9]=(String)table.getModel().getValueAt(row, 7);
						argNew[10]=(String)table.getModel().getValueAt(row, 8);
						argNew[11]=(String)table.getModel().getValueAt(row, 9);
						argNew[12]=(String)table.getModel().getValueAt(row, 10);
						argNew[13]=(String)table.getModel().getValueAt(row, 11);
						argNew[14]=(String)table.getModel().getValueAt(row, 12);
						new Client_Add(argNew).setVisible(true);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		});
		btnUpdate.setBounds(398, 16, 75, 23);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Home(args).setVisible(true);
				dispose();
			}
		});
		btnBack.setBounds(491, 16, 67, 23);
		
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setForeground(Color.BLACK);
		table.setFillsViewportHeight(true);
		table.setBackground(SystemColor.control);
		table.setName("table");
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.getGridColor();
		table.getTableHeader().getBackground().getBlue();
		table.setAutoCreateRowSorter(true);
		
		String[] header = new String[13];
		
		
		header[0] = "Client Number";
		header[1] = "Client Name";
		header[2] = "Address 1";
		header[3] = "Address 2";
		header[4] = "City";
		header[5] = "State";
		header[6] = "ZIP";
		header[7] = "E-mail";
		header[8] = "Contact";
		header[9] = "Invoice Frequency";
		header[10] = "Billing Terms";
		header[11] = "Invoice Grouping";
		header[12] = "Active Status";
		
		//String[][] obj = new String[12][12];
		
		dataConnectionObject dco = new dataConnectionObject();
        Connection conn = dco.getConnection();
        PreparedStatement pst = conn.prepareStatement("Select * from client");
        ResultSet rs = pst.executeQuery();
        
        String[][] obj1 = new String[10000][13];
        
        int i = 0;
        while(rs.next()){
        	obj1[i][0]=rs.getString("Client_Number");
        	obj1[i][1]=rs.getString("Name");
        	obj1[i][2]=rs.getString("Address1");
        	obj1[i][3]=rs.getString("Address2");
        	obj1[i][4]=rs.getString("City");
        	obj1[i][5]=rs.getString("State");
        	obj1[i][6]=rs.getString("ZIP");
        	obj1[i][7]=rs.getString("Email");
        	obj1[i][8]=rs.getString("Contact");
        	obj1[i][9]=rs.getString("Invoice_Frequency");
        	obj1[i][10]=rs.getString("Billing_terms");
        	obj1[i][11]=rs.getString("Invoice_Grouping");
        	obj1[i][12]=rs.getString("deactivated");
        	
        	/*boolean deactive = rs.getBoolean("deactivated");
        	if(deactive)obj1[i][12]="Deactive";
        	else obj1[i][12]="Active";*/
        	
        	i++;
        }
        
        String[][] obj = Arrays.copyOfRange(obj1, 0, i);
        
		table.setModel(new DefaultTableModel(obj,header));
		table.getColumnModel().getColumn(3).setResizable(false);
		scrollPane.setViewportView(table);

		table.setModel(new DefaultTableModel(obj,header));
		//table.setModel(model);
		table.getColumnModel().getColumn(3).setResizable(false);
		contentPane.setLayout(null);
		scrollPane.setViewportView(table);
		contentPane.add(scrollPane);
		contentPane.add(btnAdd);
		contentPane.add(btnUpdate);
		contentPane.add(btnBack);
	
		
	}

}
