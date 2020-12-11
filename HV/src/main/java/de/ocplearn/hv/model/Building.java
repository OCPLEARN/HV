package de.ocplearn.hv.model;


import java.util.Collections;
import java.util.List;
import java.util.Set;

public class Building implements Comparable<Building>{
	
	private int id;
	
	private String name;
	
	private Address address;
	
	private BuildingType buildingType;
	
	private List<BuildingOwner> owners;
	
	private List<Ownership> ownerships;
		
	private boolean wegType;
	
	// statt  des Feldes private List<BuildingOwner> owners;
	// kann man eine Methode der folgenden Art einbauen
	// private List<BuildingOwner> getBuildingOwners(){return new ArrayList();}
	// Vorteil: man muss nicht bei jedem Objektaufruf von Building alle Owner und deren Contact(e) und Address(en) aufrufen 
	// Top Level Objekte klein halten
	
	private Set<Unit> units;
	
	private Set<Transaction> transactions;
	
	private PropertyManagement propertyManagement;
	
	private String note;
	

	public Building() {
		super();
	}
	
	public Building(String name, Address address, BuildingType buildingType,
			List<BuildingOwner> owners, Set<Unit> units, Set<Transaction> transactions, PropertyManagement propertyManagement,
			String note, boolean wegType,  List<Ownership> ownerships ) { 
		super();
		
		this.name = name;
		this.address = address;
		this.buildingType = buildingType;
		this.owners = owners;
		this.units = units;
		this.transactions = transactions;
		this.propertyManagement = propertyManagement;
		this.note = note;
		this.wegType = wegType;
		this.ownerships = ownerships;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}

	/**
	 * @return the buidlinType
	 */
	public BuildingType getBuildingType() {
		return buildingType;
	}

	/**
	 * @param buidlinType the buidlinType to set
	 */
	public void setBuildingType(BuildingType buildingType) {
		this.buildingType = buildingType;
	}

	/**
	 * @return the owners
	 */
	public List<BuildingOwner> getOwners() {
		return Collections.unmodifiableList(owners);
	}

	/**
	 * @param owners the owners to set
	 */
	public void setOwners(List<BuildingOwner> owners) {
		this.owners = owners;
	}
	
	public void addOwners(BuildingOwner buildingOwner) {
		owners.add(buildingOwner);
	}

	/**
	 * @return the units
	 */
	public Set<Unit> getUnits() {
		return units;
	}

	/**
	 * @param units the units to set
	 */
	public void setUnits(Set<Unit> units) {
		this.units = units;
	}

	/**
	 * @return the transactions
	 */
	public Set<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions the transactions to set
	 */
	public void setTransactions(Set<Transaction> transactions) {
		this.transactions = transactions;
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
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the ownerships
	 */
	public List<Ownership> getOwnerships() {
		return ownerships;
	}

	/**
	 * @param ownerships the ownerships to set
	 */
	public void setOwnerships(List<Ownership> ownerships) {
		this.ownerships = ownerships;
	}

	/**
	 * @return the wegType
	 */
	public boolean isWegType() {
		return wegType;
	}

	/**
	 * @param wegType the wegType to set
	 */
	public void setWegType(boolean wegType) {
		this.wegType = wegType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((buildingType == null) ? 0 : buildingType.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Building other = (Building) obj;
		if (buildingType != other.buildingType)
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "Building [id=" + id + ", name=" + name + ", address=" + address + ", buildingType=" + buildingType
				+ ", owners=" + owners + ", units=" + units + ", transactions=" + transactions + ", propertyManagement="
				+ propertyManagement + ", note=" + note + "]";
	}

	@Override
	public int compareTo(Building o) {
		 return	this.getAddress().compareTo(o.getAddress());
	}








}
	
	
	