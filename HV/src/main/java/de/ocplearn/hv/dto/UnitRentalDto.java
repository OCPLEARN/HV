package de.ocplearn.hv.dto;

import java.time.LocalDate;

public class UnitRentalDto {
	
	private int id;
	
	private UnitDto unit;
	
	private RenterDto renter;
	
	private LocalDate moveIn;
	
	private LocalDate moveOut;

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
	 * @return the renter
	 */
	public RenterDto getRenter() {
		return renter;
	}

	/**
	 * @param renter the renter to set
	 */
	public void setRenter(RenterDto renter) {
		this.renter = renter;
	}

	/**
	 * @return the moveIn
	 */
	public LocalDate getMoveIn() {
		return moveIn;
	}

	/**
	 * @param moveIn the moveIn to set
	 */
	public void setMoveIn(LocalDate moveIn) {
		this.moveIn = moveIn;
	}

	/**
	 * @return the moveOut
	 */
	public LocalDate getMoveOut() {
		return moveOut;
	}

	/**
	 * @param moveOut the moveOut to set
	 */
	public void setMoveOut(LocalDate moveOut) {
		this.moveOut = moveOut;
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
		UnitRentalDto other = (UnitRentalDto) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UnitRentalDto [id=" + id + ", unit=" + unit + ", renter=" + renter + ", moveIn=" + moveIn + ", moveOut="
				+ moveOut + "]";
	}
	
	

}
