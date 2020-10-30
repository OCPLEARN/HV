/**
 * 
 */
package de.ocplearn.hv.dao;

import java.util.List;
import java.util.Optional;

import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.util.TablePageViewData;

/**
 * Data access object for an address 
 *
 */
public interface AddressDao {

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
	String SORT_FIELD = "country";
	
	/**
	 * TablePageViewData with default values
	 */
	TablePageViewData tablePageViewData = new TablePageViewData(OFFSET,LIMIT,SORT_FIELD,SORT_DIRECTION);
	
	/**
	 * store or update an address 
	 * 
	 * @param Address
	 * @return boolean true, if successful
	 */
	boolean save(Address address);	
	
	/**
	 * deletes an address 
	 * 
	 * @param Address
	 * @return boolean true, if successful
	 */
	boolean delete(Address address);		

	/**
	 * find an address by id
	 * 
	 * @param id
	 * @return Address
	 */
	Optional<Address> findById(int id);			
	
	
	
	
	
}
