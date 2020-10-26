package de.ocplearn.hv.dao;

import java.util.List;
import java.util.Optional;

import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.util.TablePageViewData;

public interface ContactDao {

	boolean save(Contact contact);
	boolean delete (Contact contact);
	boolean deleteContactById(int id);
	boolean update (Contact contact);
	boolean addAddress (Contact contact, Address address);
	
	Optional<Contact> findContactById(int id);
	
	List<Contact> findContactsByLastName(String lastName, TablePageViewData tablePageViewData);
	List<Contact> findContactsOfUnit(Unit unit, TablePageViewData tablePageViewData);
	List<Contact> findContactsIsCompany(boolean isCompany, TablePageViewData tablePageViewData);
	List<Contact> findContactsByCompanyName(String companyName, TablePageViewData tablePageViewData);
	List<Contact> getAllContacts( TablePageViewData tablePageViewData);
	List<Contact> findAddressesByContactId(int id, TablePageViewData tablePageViewData);
	
	
	
	
}
