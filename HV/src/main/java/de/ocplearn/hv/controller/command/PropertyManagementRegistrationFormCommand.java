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
	
	@NotNull
	@Size(min=8)
	@Size(max=128)
	//@Pattern(message= "{register.password.validation.regex}" , regexp = "^(?=.*[0-9]{1})(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&§():;<>,.?/~_+\\-=|'´`]).{8,128}$")
	private String initialPassword;
	
	@Size(min=8)
	@Size(max=128)
	@NotNull
	//@Pattern(message="{register.password.validation.regex}", regexp = "^(?=.*[0-9]{1})(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&§():;<>,.?/~_+\\-=|'´`]).{8,128}$")
	private String repeatedPassword;

	
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
