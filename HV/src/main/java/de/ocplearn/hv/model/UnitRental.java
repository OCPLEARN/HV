package de.ocplearn.hv.model;

import java.time.LocalDate;

public class UnitRental {
	
	private int id;
	
	private Unit unit;
	
	private Renter renter;
	
	private LocalDate moveIn;
	
	private LocalDate moveOut;

	
	public UnitRental() {
		super();
	}

	public UnitRental(Unit unit, Renter renter, LocalDate moveIn, LocalDate moveOut) {
		super();
		this.unit = unit;
		this.renter = renter;
		this.moveIn = moveIn;
		this.moveOut = moveOut;
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
	public Unit getUnit() {
		return unit;
	}

	/**
	 * @param unit the unit to set
	 */
	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	/**
	 * @return the renter
	 */
	public Renter getRenter() {
		return renter;
	}

	/**
	 * @param renter the renter to set
	 */
	public void setRenter(Renter renter) {
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
		UnitRental other = (UnitRental) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UnitRental [id=" + id + ", unit=" + unit + ", renter=" + renter + ", moveIn=" + moveIn + ", moveOut="
				+ moveOut + "]";
	}
	
	
	

}
