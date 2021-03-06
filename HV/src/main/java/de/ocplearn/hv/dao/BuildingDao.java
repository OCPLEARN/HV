package de.ocplearn.hv.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.BuildingOwner;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.util.TablePageViewData;

public interface BuildingDao {
	

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
	String SORT_FIELD = "name";
	
	/**
	 * TablePageViewData with default values
	 */
	TablePageViewData tablePageViewData = new TablePageViewData(OFFSET,LIMIT,SORT_FIELD,SORT_DIRECTION);
	
	 boolean save(Building building);
	 	 
	 boolean deleteById(int buildingId);
	 
	 /**
	  * * finds Building by id and returns an optional of a Building 
	  *   that has id, name, Address, BuildingType, PropertyManagement, note
	  *   AND a FILLED OUT List of Owners and FILLED OUT sets of Units and Transactions
	  * 
	  * @param id
	  * @return Optional<Building>
	  */
	 Optional<Building> findByIdFull(int id);
	 
	 /**
	  * * finds Building by id and returns an optional of a Building 
	  *   that has id, name, Address, BuildingType, PropertyManagement, note
	  *   AND an EMPTY List of Owners and EMPTY sets of Units and Transactions
	  * 
	  * @param id
	  * @return Optional<Building>
	  */
	 Optional<Building> findByIdPartial(int id);
	 
	 List<Building> findBuildingsByPropertyManagement(int propertyManagementId);
	 	 
	 List <Integer> findBuildingOwnerIdsByBuildingId(int buildingId, TablePageViewData tablePageViewData);
	 
	 List <Building> getAllBuildingsOfPropertyManagementById(int propertyManangementId);
	 
	 
	 boolean removeBuildingOwnerFromBuilding(BuildingOwner buildingOwner, Building building);

	 boolean addOwnerToUnit(BuildingOwner buildingOwner, Unit unit, double buildingShare, LocalDate startShare);
	 
	 boolean removeOwnerFromUnit(BuildingOwner buildingOwner, Unit unit);

	 boolean updateOwnership( BuildingOwner buildingOwner, Unit unit, double buildingShare );
	
	

}
