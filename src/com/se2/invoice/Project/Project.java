package com.se2.invoice.Project;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.se2.invoice.People.Employee;
import com.se2.invoice.forms.Home;
import com.se2.invoice.forms.dataConnectionObject;

import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Project extends JFrame {

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
					Project frame = new Project(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					//frame.setUndecorated(true);
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
	public Project(String[] args) throws IOException, SQLException {
		
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("InvoiceSE2");
		EntityManager entitymanager = emfactory.createEntityManager();
	    
		try {
			backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setTitle("Project Information");
		setIconImage(backgroundImage);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1350, 772);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		//private JTable table;
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(36, 58, 1277, 670);
		scrollPane.setForeground(Color.GRAY);
		scrollPane.setBackground(Color.GRAY);
		scrollPane.setAutoscrolls(true);
		scrollPane.setToolTipText("");
		table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		
		
		JPanel panel = new JPanel();
		panel.setBounds(202, 16, 543, 36);
		panel.setLayout(null);
		
		JButton button_1 = new JButton("Add");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				dispose();
				try {
					String[] argNew = Arrays.copyOf(args, 2);
					//argNew[0] = args[0];
					argNew[1]= "add";
					new Project_Add(argNew).setVisible(true);
					
				} catch (IOException | SQLException | ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		button_1.setBounds(78, 5, 80, 23);
		panel.add(button_1);
		
		JButton button = new JButton("Update");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int row = table.getSelectedRow();
				if(row== -1){
					JFrame parent = new JFrame();
			        JOptionPane.showMessageDialog(parent,"Please select a row.");
				}else{
					dispose();
					try {
						String[] argNew = Arrays.copyOf(args, 12);
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
						new Project_Add(argNew).setVisible(true);
						
					} catch (IOException | SQLException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
		});
		button.setBounds(234, 5, 80, 23);
		panel.add(button);
		
		JButton button_2 = new JButton("Back");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Home(args).setVisible(true);
				dispose();
			}
		});
		button_2.setBounds(379, 5, 80, 23);
		panel.add(button_2);
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
		
		String[] header = new String[10];
		
		header[0] = "Project Number";
		header[1] = "Project Name";
		header[2] = "Client";
		header[3] = "Start Date";
		header[4] = "End Date";
		header[5] = "Status";
		header[6] = "Project Manager";
		header[7] = "Client Contact";
		header[8] = "Budget";
		header[9] = "Active Status";
		
		dataConnectionObject dco = new dataConnectionObject();
        Connection conn = dco.getConnection();
        PreparedStatement pst = conn.prepareStatement("Select ProjectNumber, ProjectName, "
        		+ "Client, CASE WHEN `StartDate`!='0000-00-00' THEN `StartDate` END AS StartDate, "
        		+ "CASE WHEN `EndDate`!='0000-00-00' THEN `EndDate` END AS EndDate, Status, "
        		+ "ProjectManager, ClientContact, Budget, deactivated from Project");
        ResultSet rs = pst.executeQuery();
        
        String[][] obj1 = new String[10000][10];
        
        int i = 0;
        while(rs.next()){
        	obj1[i][0]=rs.getString("ProjectNumber");
        	obj1[i][1]=rs.getString("ProjectName");
        	obj1[i][2]=rs.getString("Client");
        	obj1[i][3]=rs.getString("StartDate");
        	obj1[i][4]=rs.getString("EndDate");
        	obj1[i][5]=rs.getString("Status");
        	obj1[i][6]=rs.getString("ProjectManager");
        	obj1[i][7]=rs.getString("ClientContact");
        	obj1[i][8]=rs.getString("Budget");
        	boolean deactive = rs.getBoolean("deactivated");
        	if(deactive)obj1[i][9]="Deactive";
        	else obj1[i][9]="Active";
        	
        	i++;
        }
        
        String[][] obj = Arrays.copyOfRange(obj1, 0, i);
        
		table.setModel(new DefaultTableModel(obj,header));
		table.getColumnModel().getColumn(3).setResizable(false);
		scrollPane.setViewportView(table);
		contentPane.setLayout(null);
		contentPane.add(panel);
		contentPane.add(scrollPane);
				
	}
}
