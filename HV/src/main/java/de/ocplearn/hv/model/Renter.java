package de.ocplearn.hv.model;

import java.util.List;

public class Renter implements Comparable<Renter> {
	
	private int id;
	
	private Contact contact;
	
	private List<Unit> rentedUnits;
	// for Lists rather use a method in service than a field in class
	
	private LoginUser loginUser;
	
	private PropertyManagement propertyManagement;
	
	/**
	 * Constructs a Renter
	 * */
	public Renter () {
		
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the contact
	 */
	public Contact getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(Contact contact) {
		this.contact = contact;
	}

	/**
	 * @return the loginUser
	 */
	public LoginUser getLoginUser() {
		return loginUser;
	}

	/**
	 * @param loginUser the loginUser to set
	 */
	public void setLoginUser(LoginUser loginUser) {
		this.loginUser = loginUser;
	}

	/**
	 * @return the propertyManagement
	 */
	public PropertyManagement getPropertyManagement() {
		return propertyManagement;
	}

	/**
	 * @param propertyManagement the propertyManagement to set
	 */
	public void setPropertyManagement(PropertyManagement propertyManagement) {
		this.propertyManagement = propertyManagement;
	}

	/**
	 * 
	 * @param unit
	 * @return true, on success
	 * */
	public boolean addUnit( Unit unit ) {
		return this.rentedUnits.add(unit);
	}
	
	/**
	 * 
	 * @param unit
	 * @return true, on success
	 * */
	public boolean removeUnit( Unit unit ) {
		return this.rentedUnits.remove(unit);
	}	
	
	@Override
	public boolean equals( Object o ) {
		if ( this == o )return true;
		
		if(! (o instanceof Renter) )
			return false;
		
		Renter other = (Renter)o;
		
		if ( this.getId() != other.getId() ) 
			return false;
		
		return true;
	}	
	
	@Override
	public int hashCode() {
		return 31 * this.getId();
	}
	
	@Override
	public int compareTo(Renter o) {
		
		return this.getContact().compareTo(o.getContact());
	}	
	
	@Override
	public String toString() {
		return "Renter [id=" + id + ", contact=" + contact + ", loginUser=" + loginUser + "]";
	}	
	
}
