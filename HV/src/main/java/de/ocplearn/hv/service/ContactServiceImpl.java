package de.ocplearn.hv.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import de.ocplearn.hv.dao.AddressDao;
import de.ocplearn.hv.dao.ContactDao;
import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.exceptions.DataAccessException;
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
	public ContactServiceImpl(ContactDao contactDao, ContactMapper contactMapper, AddressMapper addressMapper, @Qualifier ("AddressDaoJdbc")AddressDao addressDao) {
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
	public AddressDto findAddressById(int id) {
		Optional<Address> opt = addressDao.findById(id);
		if(opt.isPresent()) {
			return addressMapper.addressToAddressDto(opt.get());
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
		
		List<AddressDto> addressesDto = contactDto.getAddresses();
		if(addressesDto == null||addressesDto.size()<1) {
			throw new IllegalStateException("Address is required for signup");
		}else {
			Contact contact = contactMapper.contactDtoToContact(contactDto);
			boolean result = contactDao.save(contact);
			
			if(result) {
				contactDto.setId(contact.getId());
				
				for(int i = 0; i < addressesDto.size(); i++) {
					Address address = addressMapper.addressDtoToAddress(addressesDto.get(i));
					boolean addressSavedSuccessful = addressDao.save(address);
					if (addressSavedSuccessful) {
						contactDao.assignAddress(contact, address);
						addressesDto.get(i).setId(address.getId());
					}	else {
						return false;
					}
				}
			
			}
			return result;
		}
		
		
	}
	
	
	
	
	

	@Override
	public boolean updateContact(ContactDto contactDto) {
		return contactDao.save(contactMapper.contactDtoToContact(contactDto));
	}

	@Override
	public boolean deleteContactById( int id ) {
		//System.out.println("csimpl:  deleteContactById() " + id);
		//return contactDao.deleteContactById(id);
	//	 contactDao.findContactById(id)
		//Optional<Contact> contact = this.contactDao.findContactById(id);
		
		return deleteContact( this.contactMapper.contactToContactDto( (contactDao.findContactById(id).get()) ) );
	}

	@Override
	public boolean deleteContact( ContactDto contactDto ) {
		
		for(AddressDto addressDto : contactDto.getAddresses()) {
			if( ! contactDao.deleteAddressFromContact(addressDto.getId()) )  return false;			// deletes address id from link table
			System.out.println("Verknüpfung gelöscht");
			if( ! addressDao.delete(addressMapper.addressDtoToAddress(addressDto)) ) return false;	// deletes address from address table
			System.out.println("Adresse gelöscht");

		}
		if( ! contactDao.deleteContactById(contactMapper.contactDtoToContact(contactDto).getId())) return false;
		
		return true;
	}
	
	@Override
	public boolean updateAddress( AddressDto addressDto ) {
		Address address = addressMapper.addressDtoToAddress(addressDto);
		return addressDao.save(address);
	}

	@Override
	public boolean deleteAddress(AddressDto addressDto) {
		
		Address address = addressMapper.addressDtoToAddress(addressDto);
		return addressDao.delete(address);
		
	}

	@Override
	public boolean addAddressToContact(int contactId, AddressDto addressDto) {
		
		Address addressDtoToAddress = addressMapper.addressDtoToAddress(addressDto);
		
		return contactDao.assignAddress(contactId, addressDtoToAddress);
	}
	
	@Override
	public boolean createAddress(AddressDto addressDto) {
		
		Address address = addressMapper.addressDtoToAddress(addressDto);
		
		if (addressDao.save(address)) {
			addressDto.setId(address.getId());
			return true;
		}
		return false;
	}


		@Override
		public boolean removeAddressFromContact(int contactId, AddressDto addressDto) {
			return contactDao.deleteAddressFromContact(addressDto.getId());
			}




}
