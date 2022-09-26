package com.example.demo.model;

public class User {
	private String empId;
	private String namePrefix;
	private String firstName;
	private String lastName;
	
	public User()
	{
		
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getNamePrefix() {
		return namePrefix;
	}
	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public User(String empId, String namePrefix, String firstName, String lastName) {
		
		this.empId = empId;
		this.namePrefix = namePrefix;
		this.firstName = firstName;
		this.lastName = lastName;
	}


}
