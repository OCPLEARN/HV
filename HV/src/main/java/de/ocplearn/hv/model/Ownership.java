package de.ocplearn.hv.model;

import java.util.List;

public class Ownership {
	
	private final Unit unit;
	
	private final BuildingOwner buildingOwner;
	
	private Double buildingShare;
	
	// TODO business logik Test: ergeben die einzelnen BuildingOwner - shares pro building 100% ?

	public Ownership(Unit unit, BuildingOwner buildingOwner, Double buildingShare) {
		super();
		this.unit = unit;
		this.buildingOwner = buildingOwner;
		this.buildingShare = buildingShare;
	}

	
	public static List getAllOwnersWithShares() { return null; }


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
	
	
	
	

}
