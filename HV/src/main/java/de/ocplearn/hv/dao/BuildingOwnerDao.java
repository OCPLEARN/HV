package de.ocplearn.hv.dao;

import java.util.List;
import java.util.Optional;

import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.BuildingOwner;
import de.ocplearn.hv.util.TablePageViewData;

/**
 * DAO for BuildingOwner
 */
public interface BuildingOwnerDao {

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
	 * lastName from Contact
	 */
	String SORT_FIELD = "lastName";
	
	/**
	 * TablePageViewData with default values
	 */
	TablePageViewData tablePageViewData = new TablePageViewData(OFFSET,LIMIT,SORT_FIELD,SORT_DIRECTION);	
	
	/**
	 * Storage of BuildingOwner
	 * 
	 *  @param BuildingOwner
	 *  @return true on success
	 * 
	 */
	public boolean save( BuildingOwner buildingOwner );
	
	/**
	 * Removal of BuildingOwner
	 * 
	 * @param BuildingOwner
	 * @return true on success
	 */
	public boolean delete( BuildingOwner buildingOwner );

	/**
	 * Removal of BuildingOwner
	 * 
	 * @param id 
	 * @return true on success
	 */
	public boolean delete( int buildingOwnerId );	
	
	/**
	 * Find by id, internal references to other entities are only filled with id
	 * @param id
	 * @return Optional<'BuildingOwner'>
	 */
	public Optional<BuildingOwner> findByIdPartial( int id );
	
	/**
	 * Find by id
	 * @param id
	 * @return Optional<'BuildingOwner'>
	 */
	public Optional<BuildingOwner> findByIdFull( int id );	
	
	/**
	 * Gets a List of building ids connected to this owner id
	 * internal references to other entities are only filled with id
	 * 
	 * @param id of BuildingOwner
	 * @param TablePageViewData
	 * @return List of all ids BuildingOwner owns
	 */
	public List<Integer> findAllBuildingIdsByOwnerId( int id, TablePageViewData tablePageViewData );
}
