package com.se2.invoice.People;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="people")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 	

	private String name;
	private String title;
	private int billRate;
	private String role;
	//private String username;
	private String password;

	public Employee(String name, String title, int billRate, 
			String role, String username, String password) {
		super();
		this.name = name;
		this.title = title;
		this.billRate = billRate;
		this.role = role;
		//this.username = username;
		this.password = password;
	}

	public Employee( ) {
		super();
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getBillRate() {
		return billRate;
	}

	public void setBillRate(int billRate) {
		this.billRate = billRate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	/*public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}*/

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Employee [name=" + name + ", title=" + title + ", billRate=" + billRate + ", role=" + role + "]";
	}

}
