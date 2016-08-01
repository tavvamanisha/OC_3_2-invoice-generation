package com.se2.invoice.Project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table (name="project")

public class Projectbean {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 	

	private int Client;
	
	private int ProjectNumber;
	private String ProjectName;
	private String StartDate;
	private String EndDate;
	private String Status;
	private String ProjectManager;
	private String ClientContact;
	private int Budget;
	
	
	public Projectbean(int clientno, int projectno, String projectname, 
			String startdate,String enddate, String status,
			String projectmanager, String clientcntct, int budget) {
		
		super();
		this.Client = clientno;
		this.ProjectNumber = projectno;
		this.ProjectName = projectname;
		this.StartDate = startdate;
		this.EndDate = enddate;
		this.Status = status;
		this.ProjectManager=projectmanager;
		this.ClientContact=clientcntct;
		this.Budget=budget;
		
		
	}
	
	public Projectbean() {
		super();
	}

	
	
	public int getClient() {
		return Client;
	}

	public void setClient(int client) {
		Client = client;
	}

	public int getProjectNumber() {
		return ProjectNumber;
	}

	public void setProjectNumber(int projectNumber) {
		ProjectNumber = projectNumber;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String getEndDate() {
		return EndDate;
	}

	public void setEndDate(String endDate) {
		EndDate = endDate;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getProjectMangager() {
		return ProjectManager;
	}

	public void setProjectMangager(String projectMangager) {
		ProjectManager = projectMangager;
	}

	public String getClientContact() {
		return ClientContact;
	}

	public void setClientContact(String clientContact) {
		ClientContact = clientContact;
	}

	public int getBudget() {
		return Budget;
	}

	public void setBudget(int budget) {
		Budget = budget;
	}

	@Override
	public String toString() {
		return "Project [projectNumber=" + ProjectNumber + ", projectName=" + ProjectName;
	}

}
