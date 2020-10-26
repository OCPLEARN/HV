package de.ocplearn.hv.model;

public enum AddressType {
	
	PRIMARY_BUSINESS_ADDRESS 	( "Main business Address of primaryContact" ),
	SECONDARY_BUSINESS_ADDRESS 	( "Additional Address of primaryContact" ),
	PRIMARY_PRIVATE_ADDRESS 	( "Main private Address of primaryContact" ),
	SECONDARY_PRIVATE_ADDRESS 	( "Additional private Address of primaryContact" );
	
	private String description;
	
	private AddressType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
