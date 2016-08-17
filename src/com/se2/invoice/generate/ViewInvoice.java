package com.se2.invoice.generate;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.se2.invoice.forms.Home;
import com.se2.invoice.forms.dataConnectionObject;
import com.se2.invoice.timesheet.DisplayProject;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowserWindow;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ViewInvoice extends JFrame {

	private JPanel contentPane;
	
	private BufferedImage backgroundImage;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewInvoice frame = new ViewInvoice(args);
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
	public ViewInvoice(String[] args) {
		
		try {
		    backgroundImage = ImageIO.read(new File("eagle.png"));
		} catch (IOException e) {
		    e.printStackTrace();
		}
		setTitle("View & Send Invoice");
		setIconImage(backgroundImage);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1207, 542);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Back");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				InvoiceList frame;
				try {
					frame = new InvoiceList(args);
					frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
					frame.setVisible(true);
					dispose();
				} catch (IOException | SQLException e) {
					e.printStackTrace();
				}
				
			}
		});
		btnNewButton.setBounds(308, 29, 120, 23);
		contentPane.add(btnNewButton);
		
		JButton btnViewInvoice = new JButton("View Invoice");
		btnViewInvoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String today = new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());
				System.out.println(args[2]);
				String path = today+"_"+args[2]+".pdf";
				File myFile = new File(path);
                try {
					Desktop.getDesktop().open(myFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnViewInvoice.setBounds(470, 29, 120, 23);
		contentPane.add(btnViewInvoice);
		
		JButton btnSendInvoice = new JButton("Send Invoice");
		btnSendInvoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String today = new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());
				String path = today+"_"+args[2]+".pdf";
				
				String[] to = new String[1];
				to[0]=args[13];
				String subject = "Invoice from Eagle consulting.";
				String emailBody = "Hi "+args[2]+",<br><br>Please find the latest invoice for the project '"+args[14]+"' attached.<br><br><br><br>";
				
				generateEmail(to, subject, emailBody, path);
				
				dataConnectionObject dco = new dataConnectionObject();
				try {
					Connection conn = dco.getConnection();
					PreparedStatement pst = conn.prepareStatement("UPDATE `invoice_details` SET `Invoice_sent`=? WHERE `Invoice_id`=?");
					pst.setBoolean(1, true);
					pst.setInt(2, Integer.parseInt(args[16]));
					pst.executeUpdate();
					
				} catch (IOException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
				JFrame parent = new JFrame();
		        JOptionPane.showMessageDialog(parent,"Email has been sent to '"+args[3]+"' successfully on email address '"
				+args[13]+"' for the project '"+args[14]+"'.");
		        
		        Home frame;
				frame = new Home(args);
				frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
				frame.setVisible(true);
				dispose();
			}
		});
		btnSendInvoice.setBounds(633, 29, 120, 23);
		contentPane.add(btnSendInvoice);
	}
	
	private static void generateEmail(String[] emailAddresses, String emailSubject, String emailBody, String filename) {
        
        if(emailAddresses.length>0){
        	
        	String host="smtp.gmail.com";
        	final String user="eagleconsultinginvoice@gmail.com";//change accordingly
            final String password="eagleinvoice";//change accordingly

            //String to=emailId;//change accordingly
            //Get the session object
            Properties props = new Properties();
            props.put("mail.smtp.host",host);
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class",
            "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user,password);
                }
            });

            //Compose the message
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(user));
                
                for (int i=0;i<emailAddresses.length;i++){
                    message.addRecipient(Message.RecipientType.TO,new InternetAddress(emailAddresses[i]));
                }
                message.setSubject(emailSubject);
                //message.setContent(emailBody,"text/html");
                
                BodyPart messageBodyPart = new MimeBodyPart();
                //messageBodyPart.setText(emailBody);
                messageBodyPart.setContent(emailBody, "text/html");
                
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);
                
                message.setContent(multipart);
                //send the message

                Transport.send(message);

                System.out.println("message sent successfully...");
        	

            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
       
    }
}
