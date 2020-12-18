package de.ocplearn.hv.dto;

import java.time.LocalDate;



public class OwnershipDto {

	private int id;
	
	private UnitDto unit;
	
	private BuildingOwnerDto buildingOwner;
	
	private Double buildingShare;
	
	private LocalDate shareStart;
	
	private LocalDate shareEnd;

	public OwnershipDto() {
		super();
	}

	public OwnershipDto(UnitDto unit, BuildingOwnerDto buildingOwner, Double buildingShare, LocalDate shareStart,
			LocalDate shareEnd) {
		super();
		this.unit = unit;
		this.buildingOwner = buildingOwner;
		this.buildingShare = buildingShare;
		this.shareStart = shareStart;
		this.shareEnd = shareEnd;
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
	 * @return the unit
	 */
	public UnitDto getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(UnitDto unit) {
		this.unit = unit;
	}

	/**
	 * @return the buildingOwner
	 */
	public BuildingOwnerDto getBuildingOwner() {
		return buildingOwner;
	}

	/**
	 * @param buildingOwner the buildingOwner to set
	 */
	public void setBuildingOwner(BuildingOwnerDto buildingOwner) {
		this.buildingOwner = buildingOwner;
	}

	/**
	 * @return the buildingShare
	 */
	public Double getBuildingShare() {
		return buildingShare;
	}

	/**
	 * @param buildingShare the buildingShare to set
	 */
	public void setBuildingShare(Double buildingShare) {
		this.buildingShare = buildingShare;
	}

	/**
	 * @return the shareStart
	 */
	public LocalDate getShareStart() {
		return shareStart;
	}

	/**
	 * @param shareStart the shareStart to set
	 */
	public void setShareStart(LocalDate shareStart) {
		this.shareStart = shareStart;
	}

	/**
	 * @return the shareEnd
	 */
	public LocalDate getShareEnd() {
		return shareEnd;
	}

	/**
	 * @param shareEnd the shareEnd to set
	 */
	public void setShareEnd(LocalDate shareEnd) {
		this.shareEnd = shareEnd;
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
		OwnershipDto other = (OwnershipDto) obj;
		if (id != other.id)
			return false;
		return true;
	}
	

	@Override
	public String toString() {
		return "OwnershipDto [id=" + id + ", unit=" + unit + ", buildingOwner=" + buildingOwner + ", buildingShare="
				+ buildingShare + ", shareStart=" + shareStart + ", shareEnd=" + shareEnd + "]";
	}	
	
}
