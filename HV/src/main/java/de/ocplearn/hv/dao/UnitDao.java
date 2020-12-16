package de.ocplearn.hv.dao;

import java.util.Optional;
import java.util.Set;

import de.ocplearn.hv.dto.RenterDto;
import de.ocplearn.hv.dto.UnitDto;
import de.ocplearn.hv.model.BuildingOwner;
import de.ocplearn.hv.model.Ownership;
import de.ocplearn.hv.model.Renter;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.util.TablePageViewData;

public interface UnitDao {
	
	/**
	 * SQL : specifies which row to start from retrieving data
	 */ 
	int OFFSET = 0;
	
	/**
	 * SQL : limits numbers of rows to return 
	 */ 
	int LIMIT = 15;
	
	/**
	 * SQL : sort direction 
	 */
	String SORT_DIRECTION = "ASC";
	
	/**
	 * SQL : sort field
	 */
	String SORT_FIELD = "buildingId";
	
	/**
	 * TablePageViewData with default values
	 */
	TablePageViewData tablePageViewData = new TablePageViewData(OFFSET,LIMIT,SORT_FIELD,SORT_DIRECTION);
	
	//CRUD
	
	boolean save(Unit unit);
	
	boolean delete(Unit unit);
	
	Optional<Unit> findUnitByIdFull(int id);
	
	
	Unit getBuildingUnitFull(int buildingId);

	Optional<Unit> findUnitByIdPartial(int id);
	
	/**
	 * finds all units of given building id
	 * @param building id
	 * @return Set of Units
	 * */
	Set<Unit> findUnitsByBuildingIdFull (int buildingId);
	
	/**
	 * finds all units that are assigned to one renter id
	 * @param building id
	 * @return Set of Units
	 * */
	Set<Unit> findUnitsByRenterId (int renterId);
	
	
	/**
	 * Assigns a Renter to a unit. If unit is of type BUILDING_UNIT, the whole building is rented.
	 * @param Renter
	 * @param Unit
	 * @return true on success
	 * 
	 * */
	boolean assignRenterToUnit (Renter renter, Unit unit);
	
	
	boolean removeRenterFromUnit(Renter renter, Unit unit);
	
	/**
	 * Returns Ownership entry for given owner and unit
	 * @param Unit of type BUILDING_UNIT
	 * @return Ownership
	 * */
	Optional<Ownership> getOwnership( Unit unit, BuildingOwner buildingOwner );
	
	boolean saveOwnership( Ownership ownership );
	
}
