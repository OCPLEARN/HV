package de.ocplearn.hv.service;

import java.util.List;

import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.util.TablePageViewData;

public interface ContactService {
	
	ContactDto findContactById(int id);
	
	List<ContactDto> findContactsByLastName( String lastName, TablePageViewData tablePageViewData );
	List<ContactDto> findContactsOfUnit( Unit unit, TablePageViewData tablePageViewData );
	List<ContactDto> findContactsIsCompany( boolean isCompany, TablePageViewData tablePageViewData );
	List<ContactDto> findContactsByCompanyName( String companyName, TablePageViewData tablePageViewData );
	List<ContactDto> getAllContacts( TablePageViewData tablePageViewData );
	List<AddressDto> findAddressesByContactId( int id, TablePageViewData tablePageViewData );

	
	boolean createContact( ContactDto contactDto );
	boolean updateContact( ContactDto contactDto );
	boolean deleteContactById( int id );
	boolean updateAddress( AddressDto addressDto );
	boolean deleteAddress( AddressDto addressDto );
	
	
	
	
	

}
