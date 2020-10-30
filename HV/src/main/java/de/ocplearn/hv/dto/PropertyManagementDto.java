package de.ocplearn.hv.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.PropertyManagement;

public class PropertyManagementDto implements Comparable<PropertyManagement>{

	private int id;
	
	@NotNull
	private LoginUserDto primaryLoginUser;
	
	@NotNull
	private ContactDto primaryContact;
	
	@NotNull
	private PaymentType paymentType;
	
	private List<LoginUserDto> loginUsers;
	
	@NotNull
	private ContactDto companyContact; //HQ
	
	
	public PropertyManagementDto() {
		super();
	}
	
	
	@Override
	public String toString() {
		return "PropertyManagementDto [id=" + id + ", primaryLoginUser=" + primaryLoginUser + ", primaryContact="
				+ primaryContact + ", paymentType=" + paymentType + ", loginUsers=" + loginUsers + ", companyContact="
				+ companyContact + "]";
	}


	public PropertyManagementDto(@NotNull LoginUserDto primaryLoginUser, @NotNull ContactDto primaryContact,
			@NotNull PaymentType paymentType, List<LoginUserDto> loginUsers, @NotNull ContactDto companyContact) {
		super();
		this.primaryLoginUser = primaryLoginUser;
		this.primaryContact = primaryContact;
		this.paymentType = paymentType;
		this.loginUsers = loginUsers;
		this.companyContact = companyContact;
	}

	// Getters & Setters

	public ContactDto getCompanyContact() {
		return companyContact;
	}

	public void setCompanyContact(ContactDto companyContact) {
		this.companyContact = companyContact;
	}

	public LoginUserDto getPrimaryLoginUser() {
		return primaryLoginUser;
	}

	public void setPrimaryLoginUser(LoginUserDto primaryLoginUser) {
		this.primaryLoginUser = primaryLoginUser;
	}

	public ContactDto getPrimaryContact() {
		return primaryContact;
	}

	public void setPrimaryContact(ContactDto primaryContact) {
		this.primaryContact = primaryContact;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public List<LoginUserDto> getLoginUsers() {
		return loginUsers;
	}

	public void setLoginUsers(List<LoginUserDto> loginUsers) {
		this.loginUsers = loginUsers;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
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
