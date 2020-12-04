package de.ocplearn.hv.dto;

import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.UnitType;

public class UnitDto implements Comparable<UnitDto>{
	
	private int id;
	
	private BuildingDto building;
	
	private String unitName;
	
	private AddressDto address;
	
	private double usableFloorSpace;
	
	private int constructionYear;
	
	private String note;
	
	private UnitType unitType;

	
	
	/**
	 * @return the building
	 */
	public BuildingDto getBuilding() {
		return building;
	}

	/**
	 * @param building the building to set
	 */
	public void setBuilding(BuildingDto building) {
		this.building = building;
	}

	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}

	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	/**
	 * @return the address
	 */
	public AddressDto getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(AddressDto address) {
		this.address= address;
	}

	/**
	 * @return the usableFloorSpace
	 */
	public double getUsableFloorSpace() {
		return usableFloorSpace;
	}

	/**
	 * @param usableFloorSpace the usableFloorSpace to set
	 */
	public void setUsableFloorSpace(double usableFloorSpace) {
		this.usableFloorSpace = usableFloorSpace;
	}

	/**
	 * @return the constructionYear
	 */
	public int getConstructionYear() {
		return constructionYear;
	}

	/**
	 * @param constructionYear the constructionYear to set
	 */
	public void setConstructionYear(int constructionYear) {
		this.constructionYear = constructionYear;
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
	 * @return the unitType
	 */
	public UnitType getUnitType() {
		return unitType;
	}

	/**
	 * @param unitType the unitType to set
	 */
	public void setUnitType(UnitType unitType) {
		this.unitType = unitType;
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



	public UnitDto(BuildingDto buildingDto, String unitName, AddressDto addressDto, double usableFloorSpace, int constructionYear,
			String note, UnitType unitType) {
		super();
		this.building = buildingDto;
		this.unitName = unitName;
		this.address = addressDto;
		this.usableFloorSpace = usableFloorSpace;
		this.constructionYear = constructionYear;
		this.note = note;
		this.unitType = unitType;
	}

	public UnitDto() {
		super();
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((building == null) ? 0 : building.hashCode());
		result = prime * result + constructionYear;
		result = prime * result + id;
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((unitName == null) ? 0 : unitName.hashCode());
		result = prime * result + ((unitType == null) ? 0 : unitType.hashCode());
		long temp;
		temp = Double.doubleToLongBits(usableFloorSpace);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		UnitDto other = (UnitDto) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (building == null) {
			if (other.building != null)
				return false;
		} else if (!building.equals(other.building))
			return false;
		if (constructionYear != other.constructionYear)
			return false;
		if (id != other.id)
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (unitName == null) {
			if (other.unitName != null)
				return false;
		} else if (!unitName.equals(other.unitName))
			return false;
		if (unitType != other.unitType)
			return false;
		if (Double.doubleToLongBits(usableFloorSpace) != Double.doubleToLongBits(other.usableFloorSpace))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UnitDto [id=" + id + ", buildingDto=" + building + ", unitName=" + unitName + ", addressDto="
				+ address + ", usableFloorSpace=" + usableFloorSpace + ", constructionYear=" + constructionYear
				+ ", note=" + note + ", unitType=" + unitType + "]";
	}
	
	@Override
	public int compareTo(UnitDto o) {
		if(this.getBuilding().getId()==o.getBuilding().getId()) {
			if(this.getUnitName().equals(o.getUnitName())) {
				if(this.getAddress().getId()==o.getAddress().getId()) {
					if(this.getConstructionYear()==o.getConstructionYear()) {
						return this.getConstructionYear()-o.getConstructionYear();
					}else {
						return this.getId()-o.getId();
					}
				}else {
					return this.getAddress().getId()-o.getAddress().getId();
				}
			}else {
				return this.getUnitName().compareTo(o.getUnitName());
			}
		}else {
			return this.getBuilding().getId()-o.getBuilding().getId();
		}
	}

}
