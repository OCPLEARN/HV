package de.ocplearn.hv.service;

import java.util.List;

import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.util.TablePageViewData;

public class ContactServiceImpl implements ContactService{

	@Override
	public Contact findContactById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContactDto> findContactsByLastName(String lastName, TablePageViewData tablePageViewData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContactDto> findContactsOfUnit(Unit unit, TablePageViewData tablePageViewData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContactDto> findContactsIsCompany(boolean isCompany, TablePageViewData tablePageViewData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContactDto> findContactsByCompanyName(String companyName, TablePageViewData tablePageViewData) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ContactDto> getAllContacts(TablePageViewData tablePageViewData) {
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
