package de.ocplearn.hv.controller.command;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



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
	
	// CompanyContact Object
	
	private String companyName;
	
	private String companyPhone;
	
	private String companyMobilePhone;
	
	private String companyFax;
	
	private String companyWebsite;
	
	private String companyEmail;
	
	
	// CompanyAddress
	
	
	private String companyStreet;
	
	private String companyHouseNumber;
	
	private String companyApartment;
	
	private String companyCity;
	
	private String companyProvince;
	
	private String companyCountry;
	
	
	
	// PrimaryContact Object
	
	private String primaryName;
	
	private String primaryPhone;
	
	private String primaryMobilePhone;
	
	private String primaryFax;
	
	private String primaryWebsite;
	
	private String primaryEmail;
	
	
	// PrimaryAddress
	
	
	private String primaryStreet;
	
	private String primaryHouseNumber;
	
	private String primaryApartment;
	
	private String primaryCity;
	
	private String primaryProvince;
	
	private String primaryCountry;	
	
	
	

	
	public PropertyManagementRegistrationFormCommand(){
		
	}
	
	
	
	

	// Getters and Setters
	
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
	
	
	
	
}
