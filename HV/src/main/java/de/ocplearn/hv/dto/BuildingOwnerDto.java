package de.ocplearn.hv.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents an owner of an unit or building
 */
public class BuildingOwnerDto implements Comparable<BuildingOwnerDto> {
	
	private int id;
	
	private ContactDto contact;
	
	private LoginUserDto loginUser;
	
	private List<BuildingDto> buildings;
	
	/**
	 * default constructor
	 */
	public BuildingOwnerDto() {}

	/**
	 * copy constructor
	 * @param  buildingOwner
	 */
	public BuildingOwnerDto(BuildingOwnerDto buildingOwner) {
		this( buildingOwner.getContact(), buildingOwner.getLoginUser(), buildingOwner.getBuildings() );
	}
	
	/**
	 * @param contact
	 * @param loginUser
	 */
	public BuildingOwnerDto(ContactDto contact, LoginUserDto loginUser, List<BuildingDto> buildings) {
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
	 * @return the buildings (as unmodifiable list)
	 */
	public List<BuildingDto> getBuildings() {
		return Collections.unmodifiableList(buildings) ;
	}

	/**
	 * @param buildings the buildings to set
	 */
	public void setBuildings(List<BuildingDto> buildings) {
		this.buildings = buildings;
	}

	/**
	 * @param building to add
	 */
	public void addBuilding( BuildingDto building ) {
		this.buildings.add(building);
	}
	
	/**
	 *  @param building to remove
	 */
	public void removeBuilding( BuildingDto building ) {
		this.buildings.remove(building);
	}
	
	@Override
	public boolean equals( Object o ) {
		if ( this == o )return true;
		
		if(! (o instanceof BuildingOwnerDto) )
			return false;
		
		BuildingOwnerDto other = (BuildingOwnerDto)o;
		
		if ( this.getId() != other.getId() ) 
			return false;
		
		return true;
	}

	@Override
	public int hashCode() {
		return 31 * this.getId();
	}
	
	@Override
	public int compareTo(BuildingOwnerDto o) {
		
		return this.getContact().compareTo(o.getContact());
	}
	
}
