package de.ocplearn.hv.service;

import java.util.List;

import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.model.Contact;

public interface ContactService {
	
	Contact findContactById(int id);
	List<ContactDto> findContactsByLastName(String lastName);
	List<ContactDto> findContactsIsCompany(boolean isCompany);
	List<ContactDto> findContactsByCompanyName(String companyName);
	List<ContactDto> getAllContacts();
	boolean createContact(ContactDto contactDto);
	boolean updateContact(ContactDto contactDto);
	boolean deleteContactById(int id);

	
	
	
	
	

}
