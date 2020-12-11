package de.ocplearn.hv.model;

public class Ownership {
	
	private final Unit unit;
	
	private final BuildingOwner buildingOwner;
	
	private Double ownershipShare;
	
	// TODO business logik Test: ergeben die einzelnen BuildingOwner - shares pro building 100% ?

	public Ownership(Unit unit, BuildingOwner buildingOwner, Double ownershipShare) {
		super();
		this.unit = unit;
		this.buildingOwner = buildingOwner;
		this.ownershipShare = ownershipShare;
	}

	
	public static List getAllOwnersWithShares() {}
	
	
	
	

}
