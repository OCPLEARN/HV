package de.ocplearn.hv.model;

public enum BuildingType {
	
	SINGLE_FAMILY_HOUSE 	("Einfamilienhaus"),
	APARTMENT_BUILDING 		("Mehrfamilienhaus"),
	COMMERCIAL_PROPERTY 	("Gewerbeobjekt"),
	MIXED_PROPERTY 			("Gemischtes Objekt"),
	HALL 					("Halle"),
	SHOP 					("Ladengeschäft"),
	OFFICE 					("Büro, Kanzlei, Praxis"),
	APARTMENT 				("Wohnung"),
	ESTATE 					("Grundstück"),
	GARAGE 					("Garage"),
	PARKING_POSITION 		("Stellplatz"),
	OTHER 					("anderes Objekt");
	
private String description;
	
	private BuildingType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
	
	

}
