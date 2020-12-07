package de.ocplearn.hv.service;

import java.util.List;

import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.util.TablePageViewData;

/**
 * Access to contacts and addresses
 * as part of 
 * a) PropertyManagement
 * 
 * */
public interface ContactService {
	
	/**
	 * Finds by id
	 * 
	 * @param id 
	 * @return ContactDto
	 */
	ContactDto findContactById(int id);
	
	AddressDto findAddressById(int id);
	
	
	/**
	 * Finds by last name
	 * @param last name 
	 * @param TablePageViewData instance to select and sort the result
	 * @return List<'ContactDto'>  
	 */
	List<ContactDto> findContactsByLastName( String lastName, TablePageViewData tablePageViewData );
	
	/**
	 * Finds by unit
	 * @param Unit
	 * @param TablePageViewData instance to select and sort the result
	 * @return List<'ContactDto'>  
	 */	
	List<ContactDto> findRenterContactsOfUnit( Unit unit, TablePageViewData tablePageViewData );
	
	/**
	 * Finds by unit
	 * @param Unit
	 * @param TablePageViewData instance to select and sort the result
	 * @return List<'ContactDto'>  
	 */	
	List<ContactDto> findOwnerContactsOfUnit( Unit unit, TablePageViewData tablePageViewData );
	/**
	 * Finds all contacts, which are companies or persons
	 * @param true will find all company contacts
	 * @param TablePageViewData instance to select and sort the result
	 * @return List<'ContactDto'>  
	 */		
	List<ContactDto> findContactsIsCompany( boolean isCompany, TablePageViewData tablePageViewData );
	
	/**
	 * Finds by company name
	 * @param name of company
	 * @param TablePageViewData instance to select and sort the result
	 * @return List<'ContactDto'>  
	 */		
	List<ContactDto> findContactsByCompanyName( String companyName, TablePageViewData tablePageViewData );

	/**
	 * Finds all
	 * @param TablePageViewData instance to select and sort the result
	 * @return List<'ContactDto'>  
	 */			
	List<ContactDto> getAllContacts( TablePageViewData tablePageViewData );
	
	/**
	 * Finds contact addresses 
	 * @param id of contact
	 * @param TablePageViewData instance to select and sort the result
	 * @return List<'AddressDto'>  
	 */			
	List<AddressDto> findAddressesByContactId( int id, TablePageViewData tablePageViewData );

	/**
	 * Create a contact 
	 * 
	 * @param ContactDto to save
	 * @return true on success  
	 */			
	boolean createContact( ContactDto contactDto );
	
	/**
	 * update a contact 
	 * 
	 * @param ContactDto 
	 * @return true on success  
	 */				
	boolean updateContact( ContactDto contactDto );
	
	/**
	 * delete a contact 
	 * 
	 * @param id 
	 * @return true on success  
	 */					
	boolean deleteContactById( int id );
	
	/**
	 * delete a contact 
	 * 
	 * @param ContactDto 
	 * @return true on success  
	 */					
	boolean deleteContact( ContactDto contactDto );
	
	/**
	 * update an address
	 * 
	 * @param AddressDto 
	 * @return true on success  
	 */	
	boolean updateAddress( AddressDto addressDto );

	/**
	 * delete an address
	 * 
	 * @param AddressDto 
	 * @return true on success  
	 */		
	boolean deleteAddress( AddressDto addressDto ); 
	
	/**
	 * create an address
	 * 
	 * @param AddressDto 
	 * @return true on success  
	 */		
	boolean createAddress ( AddressDto addressDto);
	
	/**
	 * Only assigns give address to contact
	 * 
	 * @param contactId
	 * @param  address to assign, must already exist 
	 * @return true on success
	 */
	boolean addAddressToContact ( int contactId, AddressDto addressDto );

	boolean removeAddressFromContact ( int contactId, AddressDto addressDto );
	
	
	
	


}
