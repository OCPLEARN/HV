package de.ocplearn.hv.model;

import java.util.List;

import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.dto.LoginUserDto;

public class PropertyManagement implements Comparable<PropertyManagement> {
    
	private int id;
	private LoginUser primaryLoginUser;
	private Contact primaryContact; //Prokura
	private Contact companyContact; //HQ
	private PaymentType paymentType;
	private List<LoginUser> loginUsers;
	
	
	
	@Override
	public String toString() {
		return "PropertyManagement [id=" + id + ", primaryLoginUser=" + primaryLoginUser + ", primaryContact="
				+ primaryContact + ", companyContact=" + companyContact + ", paymentType=" + paymentType
				+ ", loginUsers=" + loginUsers + "]";
	}

	public PropertyManagement() {
	
	}

	public PropertyManagement(LoginUser primaryLoginUser, Contact primaryContact, Contact companyContact, PaymentType paymentType,
			List<LoginUser> loginUsers) {
		super();
		this.primaryLoginUser = primaryLoginUser;
		this.primaryContact = primaryContact;
		this.companyContact = companyContact;
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

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
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
		this.id = id;
	}
	public Contact getCompanyContact() {
		return companyContact;
	}

	public void setCompanyContact(Contact companyContact) {
		this.companyContact = companyContact;
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

	@Override
	public int compareTo(PropertyManagement o) {
		// TODO Auto-generated method stub
		return 0;
	} 
	
	
	
	
	
	
}
