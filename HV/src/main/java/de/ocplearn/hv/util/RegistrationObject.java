package de.ocplearn.hv.util;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegistrationObject {
	
	// Password Patterns: see doc folder linklist file
	
	 @NotNull
	 @Size(min=6, message="{username.tooshort}")
	 @Size(max=50, message="{username.toolong}")
	 private String loginUserName;
	
	@NotNull
	@Size(min=8)
	@Size(max=128)
	@Pattern(message= "{register.password.validation.regex}" , regexp = "^(?=.*[0-9]{1})(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&§():;<>,.?/~_+-=|'´`]).{8,128}$)")
	private String intialPassword;
	
	@Size(min=8)
	@Size(max=128)
	@NotNull
	@Pattern(message="{register.password.validation.regex}", regexp = "^(?=.*[0-9]{1})(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&§():;<>,.?/~_+-=|'´`]).{8,128}$)")
	private String repeatedPassword;
	
	public RegistrationObject() {}

	public RegistrationObject(String intialPassword, String repeatedPassword) {
		super();
		this.intialPassword = intialPassword.trim();
		this.repeatedPassword = repeatedPassword.trim();
	}

	// Getters and Setters
	public String getIntialPassword() {
		return intialPassword;
	}

	public String getRepeatedPassword() {
		return repeatedPassword;
	}

	public void setIntialPassword(String intialPassword) {
		this.intialPassword = intialPassword.trim();
	}

	public void setRepeatedPassword(String repeatedPassword) {
		this.repeatedPassword = repeatedPassword.trim();
	}

	public String getLoginUserName() {
		return loginUserName;
	}

	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}
	
	
}