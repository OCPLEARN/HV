package de.ocplearn.hv.model;

public enum AddressType {
	
	PRIMARY_BUSINESS_ADDRESS 	( "Main business Address of Contact" ),
	SECONDARY_BUSINESS_ADDRESS 	( "Additional Address of Contact" ),
	PRIMARY_PRIVATE_ADDRESS 	( "Main private Address of Contact" ),
	SECONDARY_PRIVATE_ADDRESS 	( "Additional private Address of Contact" ),
	BUILDING_ADDRESS 			( "Address of a building" );
	
	private String description;
	
	private AddressType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
