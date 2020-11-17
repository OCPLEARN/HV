package de.ocplearn.hv.dao;

import java.util.Optional;

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
	
	
	Unit getBuildingUnit(int buildingId);

	Optional<Unit> findUnitByIdPartial(int id);
}
