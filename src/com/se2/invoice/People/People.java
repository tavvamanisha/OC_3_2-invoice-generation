package com.se2.invoice.People;
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

public class People extends JFrame {

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
					People frame = new People(args);
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
	public People(String[] args) throws IOException, SQLException {
		
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
		setBounds(100, 100, 1350, 772);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		//private JTable table;
		
		JScrollPane scrollPane = new JScrollPane();
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
					new People_Add(argNew).setVisible(true);
					
				} catch (Exception  ex) {
					ex.printStackTrace();
				}
			}
		});
		
		
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
						argNew[1]= "update";
						argNew[2]=(String)table.getModel().getValueAt(row, 0);
						argNew[3]=(String)table.getModel().getValueAt(row, 1);
						argNew[4]=(String)table.getModel().getValueAt(row, 2);
						argNew[5]=(String)table.getModel().getValueAt(row, 3);
						argNew[6]=(String)table.getModel().getValueAt(row, 4);
						argNew[7]=(String)table.getModel().getValueAt(row, 5);
						
						new People_Add(argNew).setVisible(true);
						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
			
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new Home(args).setVisible(true);
				dispose();
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(491)
							.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnUpdate, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnBack, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(21)
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 1293, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnAdd)
						.addComponent(btnUpdate)
						.addComponent(btnBack))
					.addPreferredGap(ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 646, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
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
		
		String[] header = new String[6];
		header[0] = "Person Name";
		header[1] = "Person Title";
		header[2] = "Bill Rate";
		header[3] = "Role";
		header[4] = "Password";
		header[5] = "Deactivated";
		
		PreparedStatement pst = conn.prepareStatement("Select * from People");
        ResultSet rs = pst.executeQuery();
        
        String[][] obj1 = new String[10000][10];
        
        int i = 0;
        while(rs.next()){
        	obj1[i][0]=rs.getString("Name");
        	obj1[i][1]=rs.getString("Title");
        	obj1[i][2]=rs.getString("BillRate");
        	obj1[i][3]=rs.getString("Role");
        	obj1[i][4]=rs.getString("password");
        	obj1[i][5]=rs.getString("deactivated");
        	
        	i++;
        }
        
        String[][] obj = Arrays.copyOfRange(obj1, 0, i);
        
		
		table.setModel(new DefaultTableModel(obj,header));
		//table.setModel(model);
		table.getColumnModel().getColumn(3).setResizable(false);
		scrollPane.setViewportView(table);
		
		contentPane.setLayout(gl_contentPane);
		
	}

}
