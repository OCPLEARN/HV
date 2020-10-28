package de.ocplearn.hv.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ocplearn.hv.dao.AddressDao;
import de.ocplearn.hv.dao.ContactDao;
import de.ocplearn.hv.dao.LoginUserDao;
import de.ocplearn.hv.dao.PropertyManagementDao;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.mapper.PropertyManagementMapper;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.PropertyManagement;


@Service
public class PropertyManagementServiceImpl implements PropertyManagementService {

	private PropertyManagementDao propertyManagementDao;
	
	private PropertyManagementMapper propertyManagementMapper;
	
	private UserService userService;
	
	private ContactService contactService; 
		
	@Autowired
	public PropertyManagementServiceImpl ( 	PropertyManagementDao propertyManagementDao,
											PropertyManagementMapper propertyManagementMapper,
											UserService userService,
											ContactService contactService
										) {
		
		this.propertyManagementDao = propertyManagementDao;
		this.propertyManagementMapper = propertyManagementMapper;
		this.userService = userService;
		this.contactService = contactService;
		}
	
	
	@Override
	public boolean createPropertyManagement(PropertyManagementDto propertyManagementDto) {
		
		userService.createUser(propertyManagementDto.getPrimaryLoginUser());
		contactService.createContact(propertyManagementDto.getPrimaryContact());
		contactService.createContact(propertyManagementDto.getCompanyContact());
		
		PropertyManagement propertyManagement = propertyManagementMapper.propertyManagementDtoToPropertyManagement(propertyManagementDto);
		
		if(	propertyManagementDao.save(propertyManagement)) {
			System.out.println("======");
			System.out.println(propertyManagement.getPrimaryLoginUser());
		propertyManagementDto.setId(propertyManagement.getId());
		return true;
	}else
		return false;
	}

	@Override
	public boolean deletePropertyManagement(PropertyManagementDto propertyManagementDto) {

		System.out.println("=====");
		System.out.println(propertyManagementDto.getPrimaryLoginUser().getId());
		System.out.println(propertyManagementDto.getPrimaryContact().toString());
		
		PropertyManagement propertyManagement = propertyManagementMapper.propertyManagementDtoToPropertyManagement(propertyManagementDto);
		
		boolean deleteOk = true;
		
		if ( ! contactService.deleteContactById(propertyManagement.getPrimaryContact().getId())) deleteOk = false;		
		if ( ! contactService.deleteContactById(propertyManagement.getCompanyContact().getId())) deleteOk = false;
		if ( ! userService.deleteUser(propertyManagementDto.getPrimaryLoginUser()) ) deleteOk = false;
	
		//TODO Delete List of LoginUsers
		
		if ( ! propertyManagementDao.delete(propertyManagement)) deleteOk = false;
		
		return deleteOk;
	}

	@Override
	public boolean updatePropertyManagement(PropertyManagementDto propertyManagementDto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<PropertyManagementDto> findPropertyManagementbyId(PropertyManagementDto propertyManagementDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<PropertyManagementDto> findPropertyManagementbyPrimaryContact(
			PropertyManagementDto propertyManagementDto) {
		// TODO Auto-generated method stub
		return null;
	}

}
