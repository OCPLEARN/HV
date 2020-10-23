package de.ocplearn.hv.controller.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import de.ocplearn.hv.model.PaymentType;



public class PropertyManagementRegistrationFormCommand {
	
	// Password Patterns: see doc folder linklist file
	
	 @NotNull
	 @Size(min=6, message="{username.tooshort}")
	 @Size(max=50, message="{username.toolong}")
	 private String loginUserName;
	
	 
	// Regex Patterns: see doc folder linklist file !!!
	@NotNull
	@Size(min=8, max=128, message="{register.password.validation.size}")		
	@Pattern(message= "{register.password.validation.regex}" , regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&§():;<>,.?/~_+\\-=|'´`]).{1,}$")
	private String initialPassword;
	
	@NotNull
	private String repeatedPassword;
	
	//@NotNull
	private PaymentType paymentType;
	
	
	// if new user is NOT a company, then PrimaryContact data is used for CompanyContact
	
	// CompanyContact Object
	
	private String companyName;
	
	private String companyPhone;
	
	private String companyMobilePhone;
	
	private String companyFax;
	
	private String companyWebsite;
	
	private String companyEmail;
	
	private boolean company = true;
	
	
	// CompanyAddress
	
	
	





	private String companyStreet;
	
	private String companyHouseNumber;
	
	private String companyApartment;
	
	private String companyCity;
	
	private String companyProvince;
	
	private String companyCountry;
	
	private String companyZipCode;
	
	
	// PrimaryContact Object
	
	public String getCompanyZipCode() {
		return companyZipCode;
	}





	public void setCompanyZipCode(String companyZipCode) {
		this.companyZipCode = companyZipCode;
	}





	private String primaryLastName;
	
	private String primaryFirstName;
	
	private String primaryPhone;
	
	private String primaryMobilePhone;
	
	private String primaryFax;
	
	private String primaryWebsite;
	
	private String primaryEmail;
	
	
	// PrimaryAddress
//	
//	
//	private String primaryStreet;
//	
//	private String primaryHouseNumber;
//	
//	private String primaryApartment;
//	
//	private String primaryCity;
//	
//	private String primaryProvince;
//	
//	private String primaryCountry;	
	
	
	

	
	public PropertyManagementRegistrationFormCommand(){
		
	}



	public PropertyManagementRegistrationFormCommand(
			@NotNull @Size(min = 6, message = "{username.tooshort}") @Size(max = 50, message = "{username.toolong}") String loginUserName,
			@NotNull @Size(min = 8, max = 128, message = "{register.password.validation.size}") @Pattern(message = "{register.password.validation.regex}", regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&§():;<>,.?/~_+\\-=|'´`]).{1,}$") String initialPassword,
			@NotNull String repeatedPassword, PaymentType paymentType, String companyName, String companyPhone,
			String companyMobilePhone, String companyFax, String companyWebsite, String companyEmail, boolean company,
			String companyStreet, String companyHouseNumber, String companyApartment, String companyCity,
			String companyProvince, String companyCountry, String companyZipCode, String primaryLastName,
			String primaryFirstName, String primaryPhone, String primaryMobilePhone, String primaryFax,
			String primaryWebsite, String primaryEmail) {
		super();
		this.loginUserName = loginUserName;
		this.initialPassword = initialPassword;
		this.repeatedPassword = repeatedPassword;
		this.paymentType = paymentType;
		this.companyName = companyName;
		this.companyPhone = companyPhone;
		this.companyMobilePhone = companyMobilePhone;
		this.companyFax = companyFax;
		this.companyWebsite = companyWebsite;
		this.companyEmail = companyEmail;
		this.company = company;
		this.companyStreet = companyStreet;
		this.companyHouseNumber = companyHouseNumber;
		this.companyApartment = companyApartment;
		this.companyCity = companyCity;
		this.companyProvince = companyProvince;
		this.companyCountry = companyCountry;
		this.companyZipCode = companyZipCode;
		this.primaryLastName = primaryLastName;
		this.primaryFirstName = primaryFirstName;
		this.primaryPhone = primaryPhone;
		this.primaryMobilePhone = primaryMobilePhone;
		this.primaryFax = primaryFax;
		this.primaryWebsite = primaryWebsite;
		this.primaryEmail = primaryEmail;
	}





	public String getLoginUserName() {
		return loginUserName;
	}





	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}





	public String getInitialPassword() {
		return initialPassword;
	}





	public void setInitialPassword(String initialPassword) {
		this.initialPassword = initialPassword;
	}





	public String getRepeatedPassword() {
		return repeatedPassword;
	}





	public void setRepeatedPassword(String repeatedPassword) {
		this.repeatedPassword = repeatedPassword;
	}





	public PaymentType getPaymentType() {
		return paymentType;
	}





	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}





	public String getCompanyName() {
		return companyName;
	}





	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}





	public String getCompanyPhone() {
		return companyPhone;
	}





	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}





	public String getCompanyMobilePhone() {
		return companyMobilePhone;
	}





	public void setCompanyMobilePhone(String companyMobilePhone) {
		this.companyMobilePhone = companyMobilePhone;
	}





	public String getCompanyFax() {
		return companyFax;
	}





	public void setCompanyFax(String companyFax) {
		this.companyFax = companyFax;
	}





	public String getCompanyWebsite() {
		return companyWebsite;
	}





	public void setCompanyWebsite(String companyWebsite) {
		this.companyWebsite = companyWebsite;
	}





	public String getCompanyEmail() {
		return companyEmail;
	}





	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}





	public String getCompanyStreet() {
		return companyStreet;
	}





	public void setCompanyStreet(String companyStreet) {
		this.companyStreet = companyStreet;
	}





	public String getCompanyHouseNumber() {
		return companyHouseNumber;
	}





	public void setCompanyHouseNumber(String companyHouseNumber) {
		this.companyHouseNumber = companyHouseNumber;
	}





	public String getCompanyApartment() {
		return companyApartment;
	}





	public void setCompanyApartment(String companyApartment) {
		this.companyApartment = companyApartment;
	}





	public String getCompanyCity() {
		return companyCity;
	}





	public void setCompanyCity(String companyCity) {
		this.companyCity = companyCity;
	}





	public String getCompanyProvince() {
		return companyProvince;
	}





	public void setCompanyProvince(String companyProvince) {
		this.companyProvince = companyProvince;
	}





	public String getCompanyCountry() {
		return companyCountry;
	}





	public void setCompanyCountry(String companyCountry) {
		this.companyCountry = companyCountry;
	}





	public String getPrimaryLastName() {
		return primaryLastName;
	}





	public void setPrimaryLastName(String primaryLastName) {
		this.primaryLastName = primaryLastName;
	}





	public String getPrimaryFirstName() {
		return primaryFirstName;
	}





	public void setPrimaryFirstName(String primaryFirstName) {
		this.primaryFirstName = primaryFirstName;
	}





	public String getPrimaryPhone() {
		return primaryPhone;
	}





	public void setPrimaryPhone(String primaryPhone) {
		this.primaryPhone = primaryPhone;
	}





	public String getPrimaryMobilePhone() {
		return primaryMobilePhone;
	}





	public void setPrimaryMobilePhone(String primaryMobilePhone) {
		this.primaryMobilePhone = primaryMobilePhone;
	}





	public String getPrimaryFax() {
		return primaryFax;
	}





	public void setPrimaryFax(String primaryFax) {
		this.primaryFax = primaryFax;
	}





	public String getPrimaryWebsite() {
		return primaryWebsite;
	}





	public void setPrimaryWebsite(String primaryWebsite) {
		this.primaryWebsite = primaryWebsite;
	}





	public String getPrimaryEmail() {
		return primaryEmail;
	}





	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}



	public boolean isCompany() {
		return company;
	}





	public void setCompany(boolean company) {
		this.company = company;
	}

	




	@Override
	public String toString() {
		return "PropertyManagementRegistrationFormCommand [loginUserName=" + loginUserName + ", initialPassword="
				+ initialPassword + ", repeatedPassword=" + repeatedPassword + ", paymentType=" + paymentType
				+ ", companyName=" + companyName + ", companyPhone=" + companyPhone + ", companyMobilePhone="
				+ companyMobilePhone + ", companyFax=" + companyFax + ", companyWebsite=" + companyWebsite
				+ ", companyEmail=" + companyEmail + ", companyStreet=" + companyStreet + ", companyHouseNumber="
				+ companyHouseNumber + ", companyApartment=" + companyApartment + ", companyCity=" + companyCity
				+ ", companyProvince=" + companyProvince + ", companyCountry=" + companyCountry + ", companyZipCode="
				+ companyZipCode + ", primaryLastName=" + primaryLastName + ", primaryFirstName=" + primaryFirstName
				+ ", primaryPhone=" + primaryPhone + ", primaryMobilePhone=" + primaryMobilePhone + ", primaryFax="
				+ primaryFax + ", primaryWebsite=" + primaryWebsite + ", primaryEmail=" + primaryEmail + "]";
	}



	
	
	
	
	
	
}
