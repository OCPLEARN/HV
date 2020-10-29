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
