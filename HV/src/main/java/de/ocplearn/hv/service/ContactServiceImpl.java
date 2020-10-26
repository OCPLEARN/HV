package de.ocplearn.hv.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ocplearn.hv.dao.AddressDao;
import de.ocplearn.hv.dao.ContactDao;
import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.mapper.AddressMapper;
import de.ocplearn.hv.mapper.ContactMapper;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.util.TablePageViewData;

@Service
public class ContactServiceImpl implements ContactService{

	private ContactDao contactDao;
	
	private ContactMapper contactMapper;
	
	private AddressMapper addressMapper;
	
	private AddressDao addressDao; 
	
	@Autowired
	public ContactServiceImpl(ContactDao contactDao, ContactMapper contactMapper, AddressMapper addressMapper, AddressDao addressDao) {
		super();
		this.contactDao = contactDao;
		this.contactMapper=contactMapper;
		this.addressMapper = addressMapper;
		this.addressDao = addressDao;
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
		
		if(result) {
			contactDto.setId(contact.getId());
			List<AddressDto> addressesDto = contactDto.getAddresses();
			
			for(int i = 0; i < addressesDto.size(); i++) {
				
				Address address = addressMapper.addressDtoToAddress(addressesDto.get(i));
				address = addressDao.save(address);
				if (address.getId() != 0) {
					contactDao.addAddress(contact, address);
					addressesDto.get(i).setId(address.getId());
				}
				
				
			}
		}
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
