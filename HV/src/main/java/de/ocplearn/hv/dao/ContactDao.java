package de.ocplearn.hv.dao;

import java.util.List;
import java.util.Optional;

import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.util.TablePageViewData;

/**
 * A contact with addresses
 * */
public interface ContactDao {

	/**
	 * Saves a contact
	 * 
	 * @return true if, successfull
	 * */
	boolean save(Contact contact);
	
	/**
	 * Deletes a contact
	 * 
	 * @param contact to delete
	 * @return true, if successfully deleted
	 * */
	boolean delete (Contact contact);
	
	/**
	 * Deletes by given id
	 * 
	 * @param contact id
	 * @return true if successful
	 * */
	boolean deleteContactById(int id);
	
	/**
	 * Saves contact
	 * 
	 * @param contact to update
	 * @return true, if successfully updated
	 * */
	boolean update (Contact contact);
	
	/**
	 * Assigns an address 
	 * 
	 * @param contact 
	 * @param address
	 * @return true on success
	 * */
	boolean assignAddress (Contact contact, Address address);
	
	/**
	 * Assigns an address 
	 * 
	 * @param id of contact 
	 * @param address
	 * @return true on success 
	 * */	
	boolean assignAddress (int contactId, Address address);
	
	/**
	 * Deletes an address from a contact, an individual address can only 
	 * be assigned to one contact
	 * 
	 * @param id of address
	 * @return true on success
	 * */
	boolean deleteAddressFromContact (int addressId);
	
	/**
	 * find contact by id
	 * 
	 * @param id
	 * @return Optional\<Contact\> 
	 * */
	Optional<Contact> findContactById(int id);
	
	/**
	 * finds by last name
	 * 
	 * @param last name
	 * @param TablePageViewData instance to select and sort the result
	 * @return List<'Contact'>
	 * */
	List<Contact> findContactsByLastName(String lastName, TablePageViewData tablePageViewData);
	
	/**
	 * finds contacts assigned to units
	 * 
	 * @param unit
	 * @param TablePageViewData instance to select and sort the result
	 * @return List<'Contact'>
	 * */	
	List<Contact> findContactsOfUnit(Unit unit, TablePageViewData tablePageViewData);
	
	/**
	 * finds contacts representing only companies or persons
	 * 
	 * @param isCompany = true, if only companies
	 * @param TablePageViewData instance to select and sort the result
	 * @return List<'Contact'>
	 */		
	List<Contact> findContactsIsCompany(boolean isCompany, TablePageViewData tablePageViewData);
	
	/**
	 * finds contacts by company name
	 * 
	 * @param name of company
	 * @param TablePageViewData instance to select and sort the result
	 * @return List<'Contact'>
	 */	
	List<Contact> findContactsByCompanyName(String companyName, TablePageViewData tablePageViewData);

	/**
	 * finds all contacts 
	 * 
	 * @return List<'Contact'>
	 */		
	List<Contact> getAllContacts( TablePageViewData tablePageViewData);

	/**
	 * finds all addresses for a given contact id
	 * 
	 * @param id of contact 
	 * @param TablePageViewData instance to select and sort the result 
	 * @return List<'Contact'>
	 */	
	List<Address> findAddressesByContactId(int id, TablePageViewData tablePageViewData);
	
}
