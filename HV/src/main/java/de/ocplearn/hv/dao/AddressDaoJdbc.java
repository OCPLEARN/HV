/**
 * 
 */
package de.ocplearn.hv.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.util.TablePageViewData;

/**
 * AddressDao for jdbc (MySQL)
 *
 */
@Component("AddressDaoJdbc")
public class AddressDaoJdbc implements AddressDao {

	@Override
	public boolean save(Address address) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Address address) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Address findById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Address> findByContactId(int contactId, TablePageViewData tablePageViewData) {
		// TODO Auto-generated method stub
		return null;
	}

}
