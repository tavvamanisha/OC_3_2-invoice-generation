package com.se2.invoice.generate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.se2.invoice.forms.dataConnectionObject;

public class GenerateInvoice {

	private BaseFont bfBold;
	private BaseFont bf;
	private int pageNumber = 0;
	private static HashMap<String, Integer> hashPersonHours = new HashMap<String, Integer>();
	private static HashMap<String, Integer> hashPersonOvertimeHours = new HashMap<String, Integer>();
	private static HashMap<String, Integer> hashPersonBillRate = new HashMap<String, Integer>();
	private static double totalAmount = 0;
	private static String[] arg;

	public static void main(String[] args) {
		GenerateInvoice generateInvoice = new GenerateInvoice();
		if (args.length < 1) {
			System.err.println("Usage: java " + generateInvoice.getClass().getName() + " PDF_Filename");
			System.exit(1);
		}
		generateInvoice.createPDF(args);
		
		updateDB(args);
		
		ViewInvoice frame;
		frame = new ViewInvoice(arg);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

	

	private void createPDF(String[] args) {
		
		Document doc = new Document();
		PdfWriter docWriter = null;
		
		try {
			dataConnectionObject dco = new dataConnectionObject();
			Connection conn = dco.getConnection();
			
			PreparedStatement pst = conn.prepareStatement("SELECT DISTINCT(PersonName) AS DEVELOPER FROM people_timesheet "
					+ "WHERE ProjectNumber=? AND InvoiceGenerated=? AND People_ts_status=?");
			pst.setInt(1, Integer.parseInt(args[2]));
			pst.setBoolean(2, false);
			pst.setString(3, "Approved");
			ResultSet rs = pst.executeQuery();
			
			int numberOfDevelopers=0;
			String[] developers1 = new String[1000];
			
			while(rs.next()){
				developers1[numberOfDevelopers]=rs.getString("DEVELOPER");
				numberOfDevelopers++;
			}
			
			String[] developers = Arrays.copyOfRange(developers1, 0, numberOfDevelopers);
			
			initializeFonts();
			
			String today = new SimpleDateFormat("yyyy.MM.dd").format(new java.util.Date());
			String path = today+"_"+args[2]+".pdf";
			docWriter = PdfWriter.getInstance(doc, new FileOutputStream(path));
			doc.addAuthor("Eagle Consulting");
			doc.addCreationDate();
			doc.addProducer();
			doc.addCreator("Eagle Consulting");
			doc.addTitle("Eagle Consulting Invoice");
			doc.setPageSize(PageSize.LETTER);

			doc.open();
			PdfContentByte cb = docWriter.getDirectContent();

			boolean beginPage = true;
			int y = 0;

			for (int i = 0; i < numberOfDevelopers; i++) {
				if (beginPage) {
					beginPage = false;
					generateLayout(doc, cb);
					generateHeader(doc, cb, args, conn);
					y = 615;
				}
				generateDetail(doc, cb, i, y, args, developers[i], "hours");
				y = y - 15;
				generateDetail(doc, cb, i, y, args, developers[i], "overtime");
				y = y - 15;
				if (y < 50) {
					printPageNumber(cb);
					doc.newPage();
					beginPage = true;
				}
			}
			printPageNumber(cb);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException dex) {
			dex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		
		finally {
			if (doc != null) {
				doc.close();
			}
			if (docWriter != null) {
				docWriter.close();
			}
		}
	}

	private void generateLayout(Document doc, PdfContentByte cb) {
		try {
			cb.setLineWidth(1f);

			// Invoice Header box Text Headings
			createHeadings(cb, 422, 743, "Invoice Number: ");
			createHeadings(cb, 422, 723, "Invoice Date: ");
			createHeadings(cb, 422, 703, "Payment Terms: ");
			createHeadings(cb, 422, 683, "Billing Frequency: ");
			createHeadings(cb, 422, 663, "Total Amount Due: ");

			 //Invoice Detail box layout
			cb.rectangle(20, 50, 550, 600);
			cb.rectangle(40,400 , 550, 600);
			cb.moveTo(20, 630);
			cb.lineTo(570, 630);
			cb.moveTo(50, 50);
			cb.lineTo(50, 650);
			cb.moveTo(250, 50);
			cb.lineTo(250, 650);
			cb.moveTo(430, 50);
			cb.lineTo(430, 650);
			cb.moveTo(500, 50);
			cb.lineTo(500, 650);
			cb.stroke();
								
			// Invoice Detail box Text Headings
			createHeadings(cb, 22, 633, "Date");
			createHeadings(cb, 52, 633, "Description");
			createHeadings(cb, 252, 633, "Rate");
			createHeadings(cb, 432, 633, "Hours");
			createHeadings(cb, 502, 633, "Amount");

			// add the images
			Image companyLogo = Image.getInstance("eagle1.png");
			companyLogo.setAbsolutePosition(25, 700);
			companyLogo.scalePercent(25);
			doc.add(companyLogo);
			
		}

		catch (DocumentException dex) {
			dex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void generateHeader(Document doc, PdfContentByte cb, String[] args, Connection conn){

		try {
			
			PreparedStatement pst1 = conn.prepareStatement("SELECT MAX(Invoice_id) AS INV FROM invoice_details");
			ResultSet rs1 = pst1.executeQuery();
			
			PreparedStatement pst = conn.prepareStatement("SELECT T.*, P.BillRate FROM people_timesheet T, people P "
					+ "WHERE P.Name=T.PersonName AND ProjectNumber=? AND InvoiceGenerated=? AND P.deactivated=? AND T.People_ts_status=?");
			pst.setInt(1, Integer.parseInt(args[2]));
			pst.setBoolean(2, false);
			pst.setBoolean(3, false);
			pst.setString(4, "Approved");
			ResultSet rs = pst.executeQuery();
			
			while(rs.next()){
				String personName=rs.getString("PersonName");
				int hours = rs.getInt("mon")+rs.getInt("tue")+rs.getInt("wed")+rs.getInt("thu")+rs.getInt("fri")+rs.getInt("sat")+rs.getInt("sun");
				if(hours>40){
					int normalHours=0, overtimeHours=0;
					if(hashPersonHours.get(personName)!=null){
						normalHours = hashPersonHours.get(personName)+40;
					}else{
						normalHours = 40;
					}
					if(hashPersonOvertimeHours.get(personName)!=null){
						overtimeHours = hashPersonOvertimeHours.get(personName)+hours-40;
					}else{
						overtimeHours = hours-40;
					}
					hashPersonHours.put(personName, normalHours);
					hashPersonOvertimeHours.put(personName, overtimeHours);
				}else{
					int normalHours=0;
					if(hashPersonHours.get(personName)!=null){
						normalHours=hashPersonHours.get(personName)+hours;
					}else{
						normalHours=hours;
					}
					hashPersonHours.put(personName, normalHours);
				}
				hashPersonBillRate.put(personName, rs.getInt("BillRate"));
			}
			
			Set SOK = hashPersonBillRate.keySet();
			Iterator iterator = SOK.iterator();
			while (iterator.hasNext()){
				String personName = (String) iterator.next();
				int billRate = hashPersonBillRate.get(personName);
				int hours=0;
				if(hashPersonHours.get(personName)!=null){
					hours = hashPersonHours.get(personName);
				}else {
					hours=0;
				}
				
				int overtime = 0;
				if(hashPersonOvertimeHours.get(personName)!=null){
					overtime=hashPersonOvertimeHours.get(personName);
				}
				totalAmount=(hours*billRate)+(1.5*overtime*billRate);
			}
			
			
			createHeadings(cb, 200, 750, "To:\n");
			createHeadings(cb, 200, 735, "\t"+args[3]);
			createHeadings(cb, 200, 720, "\t"+args[4]);
			createHeadings(cb, 200, 705, "\t"+args[5]);
			createHeadings(cb, 200, 690, "\t"+args[6]+", "+args[7]+" "+args[8]);
			createHeadings(cb, 200, 670, "Client ID: "+args[9]);
			createHeadings(cb, 200, 650, "Project: "+args[14]);
			
			if(rs1.next()){
				createHeadings(cb, 482, 743, String.valueOf(rs1.getInt("INV")+1));
				arg = new String[17];
				
				for(int i=0;i<16;i++){
					arg[i]=args[i];
				}
				arg[16]=String.valueOf(rs1.getInt("INV")+1);
			} else {
				createHeadings(cb, 482, 743, "1");
			}
			
			createHeadings(cb, 482, 723, args[15]);
			createHeadings(cb, 482, 703, args[10]);
			createHeadings(cb, 482, 683, args[11]);
			createHeadings(cb, 482, 663, String.valueOf(totalAmount));

		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void generateDetail(Document doc, PdfContentByte cb, int index, int y, String[] args, String developer, String hours) {
		DecimalFormat df = new DecimalFormat("0.00");

		try {
			
			Calendar calendar = Calendar.getInstance();
			String startDate=null, endDate = null;
			if("Weekly".equalsIgnoreCase(args[11])){
				calendar.add(Calendar.DAY_OF_MONTH, -9);
				startDate = new SimpleDateFormat("MM/dd").format(calendar.getTime());
				calendar.add(Calendar.DAY_OF_MONTH, 6);
				endDate = new SimpleDateFormat("MM/dd").format(calendar.getTime());
			} else if ("BiWeekly".equalsIgnoreCase(args[11])){
				calendar.add(Calendar.DAY_OF_MONTH, -16);
				startDate = new SimpleDateFormat("MM/dd").format(calendar.getTime());
				calendar.add(Calendar.DAY_OF_MONTH, 13);
				endDate = new SimpleDateFormat("MM/dd").format(calendar.getTime());
			} else if ("Monthly".equalsIgnoreCase(args[11])){
				calendar.add(Calendar.DAY_OF_MONTH, -30);
				startDate = new SimpleDateFormat("MM/dd").format(calendar.getTime());
				calendar.add(Calendar.DAY_OF_MONTH, 27);
				endDate = new SimpleDateFormat("MM/dd").format(calendar.getTime());
			} else if ("Monthly-Cal".equalsIgnoreCase(args[11])){
				calendar.add(Calendar.MONTH, -1);
				calendar.set(Calendar.DATE, 1);
				
				switch (calendar.DAY_OF_WEEK){
					case Calendar.MONDAY:
						calendar.add(Calendar.DAY_OF_MONTH, 7);
						break; 
					case Calendar.TUESDAY:
						calendar.add(Calendar.DAY_OF_MONTH, 6);
						break;
					case Calendar.WEDNESDAY:
						calendar.add(Calendar.DAY_OF_MONTH, 5);
						break;
					case Calendar.THURSDAY:
						calendar.add(Calendar.DAY_OF_MONTH, 4);
						break;
					case Calendar.FRIDAY:
						calendar.add(Calendar.DAY_OF_MONTH, 3);
						break;
					case Calendar.SATURDAY:
						calendar.add(Calendar.DAY_OF_MONTH, 2);
						break;
					case Calendar.SUNDAY:
						calendar.add(Calendar.DAY_OF_MONTH, 1);
						break;
				}
				
				startDate = new SimpleDateFormat("MM/dd").format(calendar.getTime());
				
				Calendar calendar2 = Calendar.getInstance();
				calendar2.add(Calendar.DAY_OF_MONTH, -3);
				endDate = new SimpleDateFormat("MM/dd").format(calendar2.getTime());
			}
						
			if("hours".equalsIgnoreCase(hours)){
				
				if(hashPersonHours.get(developer)!=null){
					createContent(cb, 48, y, startDate+"-"+endDate, PdfContentByte.ALIGN_CENTER);
					createContent(cb, 52, y, developer, PdfContentByte.ALIGN_LEFT);
					createContent(cb, 152, y, String.valueOf(hashPersonBillRate.get(developer)),PdfContentByte.ALIGN_RIGHT);
					createContent(cb, 498, y, String.valueOf(hashPersonHours.get(developer)),PdfContentByte.ALIGN_RIGHT);
					createContent(cb, 568, y, String.valueOf(hashPersonHours.get(developer)*hashPersonBillRate.get(developer)),PdfContentByte.ALIGN_RIGHT);
				}else{
					//createContent(cb, 498, y, "0",PdfContentByte.ALIGN_RIGHT);
					//createContent(cb, 568, y, "0",PdfContentByte.ALIGN_RIGHT);
				}
				
			} else if("overtime".equalsIgnoreCase(hours)) {
				
				if(hashPersonOvertimeHours.get(developer)!=null){
					createContent(cb, 48, y, startDate+"-"+endDate, PdfContentByte.ALIGN_CENTER);
					createContent(cb, 52, y, developer, PdfContentByte.ALIGN_LEFT);
					createContent(cb, 152, y, String.valueOf(hashPersonBillRate.get(developer)*1.5),PdfContentByte.ALIGN_RIGHT);
					createContent(cb, 498, y, String.valueOf(hashPersonOvertimeHours.get(developer)),PdfContentByte.ALIGN_RIGHT);
					createContent(cb, 568, y, String.valueOf(1.5*hashPersonOvertimeHours.get(developer)*hashPersonBillRate.get(developer)),PdfContentByte.ALIGN_RIGHT);
				}else{
					//createContent(cb, 498, y, "0",PdfContentByte.ALIGN_RIGHT);
					//createContent(cb, 568, y, "0",PdfContentByte.ALIGN_RIGHT);
				}
				
			}

		}

		catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private void createHeadings(PdfContentByte cb, float x, float y, String text) {

		cb.beginText();
		cb.setFontAndSize(bfBold, 8);
		cb.setTextMatrix(x, y);
		if(text!=null)cb.showText(text.trim());
		cb.endText();

	}

	private void printPageNumber(PdfContentByte cb) {

		cb.beginText();
		cb.setFontAndSize(bfBold, 8);
		cb.showTextAligned(PdfContentByte.ALIGN_RIGHT, "Page No. " + (pageNumber + 1), 570, 25, 0);
		cb.endText();

		pageNumber++;

	}

	private void createContent(PdfContentByte cb, float x, float y, String text, int align) {

		cb.beginText();
		cb.setFontAndSize(bf, 8);
		cb.showTextAligned(align, text.trim(), x, y, 0);
		cb.endText();

	}

	private void initializeFonts() {

		try {
			bfBold = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);

		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static void updateDB(String[] args) {
		
		dataConnectionObject dco = new dataConnectionObject();
		try {
			Connection conn = dco.getConnection();
			
			PreparedStatement pst1 = conn.prepareStatement("SELECT MAX(Invoice_id) AS INV FROM invoice_details");
			ResultSet rs1 = pst1.executeQuery();
			int invoiceNumber=0;
			if(rs1.next()){
				invoiceNumber=rs1.getInt("INV")+1;
			}else{
				invoiceNumber=1;
			}
			
			PreparedStatement pst = conn.prepareStatement("INSERT INTO `invoice_details`"
					+ "(`Invoice_id`, `Client_id`, `Project_id`, `Invoice_date`, `Invoice_bill_terms`, "
					+ "`Invoice_freq`, `Total_amount_due`, `Invoice_sent`) "
					+ "VALUES (?,?,?,?,?,?,?,?)");
			
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy"); 
		    Date startDate = new Date();
		    try {
		        startDate = df.parse(args[15]);
		        String newDateString = df.format(startDate);
		        System.out.println(newDateString);
		    } catch (ParseException e) {
		        e.printStackTrace();
		    }
			
			pst.setInt(1, invoiceNumber);
			pst.setString(2, args[9]);
			pst.setString(3, args[2]);
			pst.setDate(4, new java.sql.Date(startDate.getTime()));
			pst.setString(5, args[10]);
			pst.setString(6, args[11]);
			pst.setInt(7, (int)totalAmount);
			pst.setBoolean(8, false);
			
			pst.executeUpdate();
			
			Set SOK = hashPersonBillRate.keySet();
			Iterator iterator = SOK.iterator();
			while (iterator.hasNext()){
				String personName = (String) iterator.next();
				int billRate = hashPersonBillRate.get(personName);
				int hours=0;
				if(hashPersonHours.get(personName)!=null){
					hours = hashPersonHours.get(personName);
				}else {
					hours=0;
				}
				
				int overtime = 0;
				if(hashPersonOvertimeHours.get(personName)!=null){
					overtime=hashPersonOvertimeHours.get(personName);
				}
				
				PreparedStatement pst2 = conn.prepareStatement("INSERT INTO `invoice_people_mapping`"
						+ "(`Invoice_id`, `Project_people_id`, `People_name`, `Mapping_rate`, `Mapping_hours`, `Mapping_date`) "
						+ "VALUES (?,?,?,?,?,?)");
				pst2.setInt(1, invoiceNumber);
				pst2.setString(2, args[2]);
				pst2.setString(3, personName);
				pst2.setInt(4, hashPersonBillRate.get(personName));
				pst2.setInt(5, hours);
				pst2.setDate(6, new java.sql.Date(startDate.getTime()));
				pst2.executeUpdate();
				
				PreparedStatement pst3 = conn.prepareStatement("INSERT INTO `invoice_people_mapping`"
						+ "(`Invoice_id`, `Project_people_id`, `People_name`, `Mapping_rate`, `Mapping_hours`, `Mapping_date`) "
						+ "VALUES (?,?,?,?,?,?)");
				pst3.setInt(1, invoiceNumber);
				pst3.setString(2, args[2]);
				pst3.setString(3, personName);
				pst3.setInt(4, (int)(hashPersonBillRate.get(personName)*1.5));
				pst3.setInt(5, overtime);
				pst3.setDate(6, new java.sql.Date(startDate.getTime()));
				pst3.executeUpdate();
				
				PreparedStatement pst4 = conn.prepareStatement("UPDATE `people_timesheet` SET `InvoiceGenerated`=? WHERE "
						+ "ProjectNumber=? AND PersonName=? AND People_ts_status=? AND InvoiceGenerated=?");
				pst4.setBoolean(1, true);
				pst4.setString(2, args[2]);
				pst4.setString(3, personName);
				pst4.setString(4, "Approved");
				pst4.setBoolean(5, false);
				pst4.executeUpdate();
				
			}
			
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
}