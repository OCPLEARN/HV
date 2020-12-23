package de.ocplearn.hv.model;

import java.time.LocalDate;
import java.util.List;

public class Ownership {
	
	private int id;
	
	private Unit unit;
	
	private BuildingOwner buildingOwner;
	
	private Double buildingShare;
	
	private LocalDate shareStart;
	
	private LocalDate shareEnd;
	
	
	// TODO business logik Test: ergeben die einzelnen BuildingOwner - shares pro building 100% ?

	

	
	public static List getAllOwnersWithShares() { return null; }

	
	public Ownership() {
		super();
	}


	public Ownership(Unit unit, BuildingOwner buildingOwner, Double buildingShare, LocalDate shareStart,
			LocalDate shareEnd) {
		super();
		this.unit = unit;
		this.buildingOwner = buildingOwner;
		this.buildingShare = buildingShare;
		this.shareStart = shareStart;
		this.shareEnd = shareEnd;
	}


	/**
	 * @return the unit
	 */
	public Unit getUnit() {
		return unit;
	}


	/**
	 * @return the buildingOwner
	 */
	public BuildingOwner getBuildingOwner() {
		return buildingOwner;
	}


	/**
	 * @return the buildingShare
	 */
	public Double getBuildingShare() {
		return buildingShare;
	}


	public LocalDate getShareStart() {
		return shareStart;
	}


	public void setShareStart(LocalDate shareStart) {
		this.shareStart = shareStart;
	}


	public LocalDate getShareEnd() {
		return shareEnd;
	}


	public void setShareEnd(LocalDate shareEnd) {
		this.shareEnd = shareEnd;
	}


	public void setBuildingShare(Double buildingShare) {
		this.buildingShare = buildingShare;
	}


	public int getId() {
		return id;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	/**
	 * @param buildingOwner the buildingOwner to set
	 */
	public void setBuildingOwner(BuildingOwner buildingOwner) {
		this.buildingOwner = buildingOwner;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Ownership other = (Ownership) obj;
		if (id != other.id)
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Ownership [id=" + id + ", unit=" + unit.getId() + ", buildingOwner=" + buildingOwner.getId() + ", buildingShare="
				+ buildingShare + ", shareStart=" + shareStart + ", shareEnd=" + shareEnd + "]";
	}

}
