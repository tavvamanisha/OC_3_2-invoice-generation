package com.se2.invoice.Company;

public class CompanyBean {

	private String Name;
	private String Address1;
	private String Address2;
	private String City;
	private String State;
	private String ZIP;
	
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getAddress1() {
		return Address1;
	}
	public void setAddress1(String address1) {
		Address1 = address1;
	}
	public String getAddress2() {
		return Address2;
	}
	public void setAddress2(String address2) {
		Address2 = address2;
	}
	public String getCity() {
		return City;
	}
	public void setCity(String city) {
		City = city;
	}
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	public String getZIP() {
		return ZIP;
	}
	public void setZIP(String zIP) {
		ZIP = zIP;
	}
	
	public CompanyBean( String Name, String Address1,String Address2,
			String City,String State, String ZIP){
		
		super();
		
		this.Name=Name;
		this.Address1=Address1;
		this.Address2=Address2;
		this.City=City;
		this.State=State;
		this.ZIP=ZIP;
		
	}
	
	
	
}
