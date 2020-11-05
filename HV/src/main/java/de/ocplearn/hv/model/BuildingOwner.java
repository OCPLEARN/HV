package de.ocplearn.hv.model;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents an owner of an unit or building
 */
public class BuildingOwner implements Comparable<BuildingOwner> {
	
	private int id;
	
	private Contact contact;
	
	private LoginUser loginUser;
	
	private List<Building> buildings;
	
	/**
	 * default constructor
	 */
	public BuildingOwner() {}

	/**
	 * copy constructor
	 * @param  buildingOwner
	 */
	public BuildingOwner(BuildingOwner buildingOwner) {
		this( buildingOwner.getContact(), buildingOwner.getLoginUser(), buildingOwner.getBuildings() );
	}
	
	/**
	 * @param contact
	 * @param loginUser
	 */
	public BuildingOwner(Contact contact, LoginUser loginUser, List<Building> buildings) {
		super();
		this.contact = Objects.requireNonNull(contact, "Building needs Contact");
		this.loginUser = Objects.requireNonNull(loginUser, "Building needs Contact");
		this.buildings = buildings;
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
	 * @return the buildings (as unmodifiable list)
	 */
	public List<Building> getBuildings() {
		return Collections.unmodifiableList(buildings) ;
	}

	/**
	 * @param buildings the buildings to set
	 */
	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}

	/**
	 * @param building to add
	 */
	public void addBuilding( Building building ) {
		this.buildings.add(building);
	}
	
	/**
	 *  @param building to remove
	 */
	public void removeBuilding( Building building ) {
		this.buildings.remove(building);
	}
	
	@Override
	public boolean equals( Object o ) {
		if ( this == o )return true;
		
		if(! (o instanceof BuildingOwner) )
			return false;
		
		BuildingOwner other = (BuildingOwner)o;
		
		if ( this.getId() != other.getId() ) 
			return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		return 31 * this.getId();
	}
	
	@Override
	public int compareTo(BuildingOwner o) {
		
		return this.getContact().compareTo(o.getContact());
	}

	@Override
	public String toString() {
		return "BuildingOwner [id=" + id + ", contact=" + contact + ", loginUser=" + loginUser + "]";
	}
	
	
	
}
