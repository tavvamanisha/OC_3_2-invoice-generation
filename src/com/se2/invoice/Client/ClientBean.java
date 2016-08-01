package com.se2.invoice.Client;

public class ClientBean {

	private int clientNumber;
	private String clientName;
	private String address1;
	private String address2;
	private String city,state,zip,email,contactPerson;
	private String billingTerms, invoiceGrouping, invoiceFrequency;
	private boolean deactivated;
	
	
	public ClientBean(int clientNumber, String clientName, String address1,String address2,
			String city,String state, String zip, String email, String contactPerson,
			String billingTerms, String invoiceGrouping,String invoiceFrequency, boolean deactivated){
		
		super();
		this.clientNumber=clientNumber;
		this.clientName=clientName;
		this.address1=address1;
		this.address2=address2;
		this.city=city;
		this.state=state;
		this.zip=zip;
		this.email=email;
		this.contactPerson=contactPerson;
		this.billingTerms=billingTerms;
		this.invoiceFrequency=invoiceFrequency;
		this.invoiceGrouping=invoiceGrouping;
		
	}
	
	
	
	
	
	
	
	
	public int getClientNumber() {
		return clientNumber;
	}
	public void setClientNumber(int clientNumber) {
		this.clientNumber = clientNumber;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	public String getBillingTerms() {
		return billingTerms;
	}
	public void setBillingTerms(String billingTerms) {
		this.billingTerms = billingTerms;
	}
	public String getInvoiceGrouping() {
		return invoiceGrouping;
	}
	public void setInvoiceGrouping(String invoiceGrouping) {
		this.invoiceGrouping = invoiceGrouping;
	}
	public String getInvoiceFrequency() {
		return invoiceFrequency;
	}
	public void setInvoiceFrequency(String invoiceFrequency) {
		this.invoiceFrequency = invoiceFrequency;
	}
	public boolean isDeactivated() {
		return deactivated;
	}
	public void setDeactivated(boolean deactivated) {
		this.deactivated = deactivated;
	}
	
	
	
	
	
	
	
	
}
