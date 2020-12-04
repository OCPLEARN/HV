package de.ocplearn.hv.dto;

import java.util.List;

import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.model.Renter;
import de.ocplearn.hv.model.Unit;

public class RenterDto implements Comparable<RenterDto> {
	
private int id;
	
	private ContactDto contact;
	
	private List<UnitDto> rentedUnits;
	// for Lists rather use a method in service than a field in class
	
	private LoginUserDto loginUser;
	
	private PropertyManagementDto propertyManagement;
	
	/**
	 * Constructs a Renter
	 * */
	public RenterDto () {
		
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
	public ContactDto getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(ContactDto contact) {
		this.contact = contact;
	}

	/**
	 * @return the loginUser
	 */
	public LoginUserDto getLoginUser() {
		return loginUser;
	}

	/**
	 * @param loginUser the loginUser to set
	 */
	public void setLoginUser(LoginUserDto loginUser) {
		this.loginUser = loginUser;
	}

	/**
	 * @return the propertyManagement
	 */
	public PropertyManagementDto getPropertyManagement() {
		return propertyManagement;
	}

	/**
	 * @param propertyManagement the propertyManagement to set
	 */
	public void setPropertyManagement(PropertyManagementDto propertyManagement) {
		this.propertyManagement = propertyManagement;
	}

	/**
	 * 
	 * @param unit
	 * @return true, on success
	 * */
	public boolean addUnit( UnitDto unit ) {
		return this.rentedUnits.add(unit);
	}
	
	/**
	 * 
	 * @param unit
	 * @return true, on success
	 * */
	public boolean removeUnit( UnitDto unit ) {
		return this.rentedUnits.remove(unit);
	}	
	
	@Override
	public boolean equals( Object o ) {
		if ( this == o )return true;
		
		if(! (o instanceof RenterDto) )
			return false;
		
		RenterDto other = (RenterDto)o;
		
		if ( this.getId() != other.getId() ) 
			return false;
		
		return true;
	}	
	
	@Override
	public int hashCode() {
		return 31 * this.getId();
	}
	
	@Override
	public int compareTo(RenterDto o) {
		
		return this.getContact().compareTo(o.getContact());
	}	
	
	@Override
	public String toString() {
		return "RenterDto [id=" + id + ", contact=" + contact + ", loginUser=" + loginUser + "]";
	}	

}
