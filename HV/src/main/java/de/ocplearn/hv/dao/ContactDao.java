package de.ocplearn.hv.dao;

import java.util.List;
import java.util.Optional;

import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.model.Contact;

public interface ContactDao {

	boolean save(Contact contact);
	boolean delete (Contact contact);
	boolean deleteContactById(int id);
	boolean update (Contact contact);
	
	Optional<Contact> findContactById(int id);
	
	List<Contact> findContactsByLastName(String lastName);
	List<Contact> findContactsIsCompany(boolean isCompany);
	List<Contact> findContactsByCompanyName(String companyName);
	List<Contact> getAllContacts();
	
	
	
	
}
