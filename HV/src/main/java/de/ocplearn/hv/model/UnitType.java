package de.ocplearn.hv.model;

public enum UnitType {
	
	
	
	APARTMENT_UNIT 		("Apartments and private housing") {
		@Override
		public String detailedDescription() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	COMMERCIAL_UNIT 	("Stores and offices") {
		@Override
		public String detailedDescription() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	PARKING_UNIT 		("Parking lots and garages") {
		@Override
		public String detailedDescription() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	BUILDING_UNIT 		("Unit representing the building") {
		@Override
		public String detailedDescription() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	REAL_ESTATE_UNIT 	("plain ground") {
		@Override
		public String detailedDescription() {
			// TODO Auto-generated method stub
			return null;
		}
	},
	OTHER_UNIT 			("other Units") {
		@Override
		public String detailedDescription() {
			return "Tents on Mars";
		}
	};
	

	private String description;
	
	private UnitType (String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	// OCP SPECIALITIES
	
	// THIS field variable cannot be called from outside, before enum class isn´t called at least once 
	// enums build instances before static members
	
	private static String field;
	
	// THIS ABSTRACT METHOD forces UntitType to implement for all instances of UnitType
	
	public abstract String detailedDescription();
	

}
