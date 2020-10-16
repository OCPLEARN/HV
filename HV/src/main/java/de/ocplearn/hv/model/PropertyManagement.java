package de.ocplearn.hv.model;

import java.util.List;

public class PropertyManagement {
    
	private int id;
	private LoginUser primaryLoginUser;
	private Contact primaryContact;
	private String paymentType;
	private List<LoginUser> loginUsers;
	
	
	public PropertyManagement() {
	}

	public PropertyManagement(LoginUser primaryLoginUser, Contact primaryContact, String paymentType,
			List<LoginUser> loginUsers) {
		this.primaryLoginUser = primaryLoginUser;
		this.primaryContact = primaryContact;
		this.paymentType = paymentType;
		this.loginUsers = loginUsers;
	}

	
	// Getters & Setters

	public LoginUser getPrimaryLoginUser() {
		return primaryLoginUser;
	}

	public void setPrimaryLoginUser(LoginUser primaryLoginUser) {
		this.primaryLoginUser = primaryLoginUser;
	}

	public Contact getPrimaryContact() {
		return primaryContact;
	}

	public void setPrimaryContact(Contact primaryContact) {
		this.primaryContact = primaryContact;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public List<LoginUser> getLoginUsers() {
		return loginUsers;
	}

	public void setLoginUsers(List<LoginUser> loginUsers) {
		this.loginUsers = loginUsers;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		 this.id=id;
	}

	
	// hashCode & equals
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PropertyManagement other = (PropertyManagement) obj;
		if (id != other.id)
			return false;
		return true;
	} 
	
	
	
}
