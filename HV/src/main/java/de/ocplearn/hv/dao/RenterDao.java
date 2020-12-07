/**
 * 
 */
package de.ocplearn.hv.dao;

import java.util.Optional;

import de.ocplearn.hv.model.BuildingOwner;
import de.ocplearn.hv.model.Renter;
import de.ocplearn.hv.util.TablePageViewData;

/**
 * Dao for renter
 *
 */
public interface RenterDao {

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
	String SORT_FIELD = "id";
	
	/**
	 * TablePageViewData with default values
	 */
	TablePageViewData tablePageViewData = new TablePageViewData(OFFSET,LIMIT,SORT_FIELD,SORT_DIRECTION);	
		
	/**
	 * Store a renter
	 * 
	 *  @param Renter
	 *  @return true on success
	 * 
	 */
	public boolean save( Renter renter );
	
	/**
	 * Removal of Renter
	 * 
	 * @param Renter
	 * @return true on success
	 */
	public boolean delete( Renter renter );

	/**
	 * Removal of Renter
	 * 
	 * @param id 
	 * @return true on success
	 */
	public boolean delete( int renterId );	
	
	/**
	 * Find by id, internal references to other entities are only filled with id
	 * @param id
	 * @return Optional<Renter'>
	 */
	public Optional<Renter> findByIdPartial( int id );
	
	/**
	 * Find by id
	 * @param id
	 * @return Optional<'Renter'>
	 */
	public Optional<Renter> findByIdFull( int id );		
	
}
