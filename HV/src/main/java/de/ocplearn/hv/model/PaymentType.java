package de.ocplearn.hv.model;

public enum PaymentType {
	
	FREE ("administration of a single unit"),
	STARTER ("administration of up to ten units or one building"),
	PRO ("administration unlimited for one PROPERTY_MANAGER"),
	SUPER_PRO ("administration unlimited for unlimited PROPERTY_MANAGERs");

	private String description;

	public String getDescription() {
		return description;
	}
	
	private PaymentType(String description) {
		this.description = description;
	}
	
}
