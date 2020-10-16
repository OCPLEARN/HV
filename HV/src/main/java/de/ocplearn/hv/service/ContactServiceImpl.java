package de.ocplearn.hv.service;

import java.util.List;

import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Contact;

public class ContactServiceImpl implements ContactService{

	@Override
	public Contact findContactById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContactDto> findContactsByLastName(String lastName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContactDto> findContactsIsCompany(boolean isCompany) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContactDto> findContactsByCompanyName(String companyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContactDto> getAllContacts() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean createContact(ContactDto contactDto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateContact(ContactDto contactDto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteContactById(int id) {
		// TODO Auto-generated method stub
		return false;
	}


}
