/**
 * 
 */
package de.ocplearn.hv.dao;

import java.util.List;

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
	Address save(Address address);	
	
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
	Address findById(int id);		
	
	/**
	 * find an all addresses by a contact id
	 * 
	 * @param contact id
	 * @param TablePageViewData (optional, contains offset,rowCount,orderBy,orderByDirection)
	 * @return List<Address>
	 */
	List<Address> findByContactId(int contactId, TablePageViewData tablePageViewData);			
	
	
	
	
	
}
