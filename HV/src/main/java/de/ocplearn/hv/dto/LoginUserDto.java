package de.ocplearn.hv.dto;

import java.util.Locale;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.NonNull;

import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Role;

public class LoginUserDto implements Comparable<LoginUserDto> {
	
	
	
    private int id;
    @NotNull
    @Size(min=6, message="{username.tooshort}")
    @Size(max=50, message="{username.toolong}")
    private String loginUserName;
    private Role role;
    @NotNull
    private byte [] passwHash;
    private byte [] salt;

	private Locale locale;

    public LoginUserDto(){}
    
    public LoginUserDto(String loginUserName, Role role, byte[] passwHash, byte[] salt, Locale locale) {
        this.loginUserName = loginUserName;
        this.role = role;
        this.passwHash = passwHash;
        this.salt = salt;
        this.locale = locale;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public byte[] getPasswHash() {
        return passwHash;
    }

    public void setPasswHash(byte[] passwHash) {
        this.passwHash = passwHash;
    }

    public byte[] getSalt() {
        return salt;
    }

    public void setSalt(byte[] salt) {
        this.salt = salt;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

 
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.id;
        hash = 97 * hash + Objects.hashCode(this.loginUserName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LoginUserDto other = (LoginUserDto) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.loginUserName, other.loginUserName)) {
            return false;
        }
        return true;
    }

    @Override
   	public String toString() {
   		return "LoginUserDto [id=" + id + ", loginUserName=" + loginUserName + ", role=" + role + ", locale=" + locale
   				+ "]";
   	}
    
    /**
     * Compares by loginUserName
     * */
    @Override
    public int compareTo(LoginUserDto o) {
        return this.getLoginUserName().compareTo(o.getLoginUserName());
    }
    

    
    
}
