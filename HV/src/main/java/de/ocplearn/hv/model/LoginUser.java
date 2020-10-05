package de.ocplearn.hv.model;

import java.util.Locale;
import java.util.Objects;




/**
 * A LoginUser can log into the application with a specific role
 * 
 * */
public class LoginUser implements Comparable<LoginUser> {

    private int id;
    private String loginUserName;    
    private Role role;
    private byte [] passwHash;
    private byte [] salt;
    private Locale locale;	
	
    //attributes for Spring security - User details interface
    //private boolean enabled;
    
    public LoginUser(){}
    
    public LoginUser(String loginUserName, Role role, byte[] passwHash, byte[] salt, Locale locale) {
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
        final LoginUser other = (LoginUser) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.loginUserName, other.loginUserName)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(LoginUser o) {
        return this.getLoginUserName().compareTo(o.getLoginUserName());
    }    
}
