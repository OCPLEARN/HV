package de.ocplearn.hv.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PropertyManagement;

public class PropertyManagementDto implements Comparable<PropertyManagement>{

	private int id;
	
	@NotNull
	private LoginUser primaryLoginUser;
	
	@NotNull
	private Contact primaryContact;
	
	@NotNull
	private String paymentType;
	
	private List<LoginUser> loginUsers;
	
	
	
	
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

	// hashCode & equals
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((loginUsers == null) ? 0 : loginUsers.hashCode());
		result = prime * result + ((paymentType == null) ? 0 : paymentType.hashCode());
		result = prime * result + ((primaryContact == null) ? 0 : primaryContact.hashCode());
		result = prime * result + ((primaryLoginUser == null) ? 0 : primaryLoginUser.hashCode());
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
		PropertyManagementDto other = (PropertyManagementDto) obj;
		if (id != other.id)
			return false;
		if (loginUsers == null) {
			if (other.loginUsers != null)
				return false;
		} else if (!loginUsers.equals(other.loginUsers))
			return false;
		if (paymentType == null) {
			if (other.paymentType != null)
				return false;
		} else if (!paymentType.equals(other.paymentType))
			return false;
		if (primaryContact == null) {
			if (other.primaryContact != null)
				return false;
		} else if (!primaryContact.equals(other.primaryContact))
			return false;
		if (primaryLoginUser == null) {
			if (other.primaryLoginUser != null)
				return false;
		} else if (!primaryLoginUser.equals(other.primaryLoginUser))
			return false;
		return true;
	}

	// Compare
	
	@Override
	public int compareTo(PropertyManagement o) {
		
		return this.getPrimaryContact().compareTo(getPrimaryContact());
	}

	
	

	
	
	
}
