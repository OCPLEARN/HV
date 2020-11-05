package de.ocplearn.hv.dao;

import java.util.List;
import java.util.Optional;

import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.BuildingOwner;
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
	 boolean delete(Building building);
	 
	 Optional<Building> findById(int id);
	 List <Integer> findBuildingOwnerIdsByBuildingId(int buildingId, TablePageViewData tablePageViewData);
	 List <Building> getAllBuildings();
	 
	 boolean addBuildingOwnerToBuilding(BuildingOwner buildingOwner, Building building);
	 boolean removeBuildingOwnerFromBuilding(BuildingOwner buildingOwner, Building building);
	 
	
	
	
	

}
