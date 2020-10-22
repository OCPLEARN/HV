package de.ocplearn.hv.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ocplearn.hv.dao.ContactDao;
import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.mapper.ContactMapper;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.util.TablePageViewData;

@Service
public class ContactServiceImpl implements ContactService{

	private ContactDao contactDao;
	
	private ContactMapper contactMapper;
	
	@Autowired
	public ContactServiceImpl(ContactDao contactDao, ContactMapper contactMapper) {
		super();
		this.contactDao = contactDao;
		this.contactMapper=contactMapper;
	}

	@Override
	public ContactDto findContactById(int id) {
		Optional<Contact> contact= contactDao.findContactById(id);
		if (contact.isPresent()) {
			return contactMapper.contactToContactDto(contact.get());
		}else {
			return null;
		}
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
		return contactDao.getAllContacts(tablePageViewData).stream().map(contact -> contactMapper.contactToContactDto(contact)).collect(Collectors.toList());
	}

	@Override
	public List<AddressDto> findAddressesByContactId(int id, TablePageViewData tablePageViewData) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean createContact(ContactDto contactDto) {
		Contact contact = contactMapper.contactDtoToContact(contactDto);
		boolean result = contactDao.save(contact);
		contactDto.setId(contact.getId());
		return result;
	}

	@Override
	public boolean updateContact(ContactDto contactDto) {
		return contactDao.save(contactMapper.contactDtoToContact(contactDto));
	}

	@Override
	public boolean deleteContactById(int id) {
		return contactDao.deleteContactById(id);
	}



}
