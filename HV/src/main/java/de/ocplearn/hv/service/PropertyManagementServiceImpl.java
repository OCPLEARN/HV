package de.ocplearn.hv.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ocplearn.hv.dao.AddressDao;
import de.ocplearn.hv.dao.ContactDao;
import de.ocplearn.hv.dao.LoginUserDao;
import de.ocplearn.hv.dao.PropertyManagementDao;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.mapper.LoginUserMapper;
import de.ocplearn.hv.mapper.PropertyManagementMapper;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PropertyManagement;


@Service
public class PropertyManagementServiceImpl implements PropertyManagementService {

	private PropertyManagementDao propertyManagementDao;
	
	private PropertyManagementMapper propertyManagementMapper;
	
	private LoginUserMapper loginUserMapper;
	
	private UserService userService;
	
	private ContactService contactService; 
		
	@Autowired
	public PropertyManagementServiceImpl ( 	PropertyManagementDao propertyManagementDao,
											PropertyManagementMapper propertyManagementMapper,
											UserService userService,
											ContactService contactService,
											LoginUserMapper loginUserMapper
										) {
		
		this.propertyManagementDao = propertyManagementDao;
		this.propertyManagementMapper = propertyManagementMapper;
		this.userService = userService;
		this.contactService = contactService;
		this.loginUserMapper=loginUserMapper;
		}
	
	
	@Override
	public boolean createPropertyManagement(PropertyManagementDto propertyManagementDto) {
		
		if ( ! userService.createUser(propertyManagementDto.getPrimaryLoginUser()) ) System.out.println("!!!  false 1");
		if ( ! contactService.createContact(propertyManagementDto.getPrimaryContact()) ) System.out.println("!!!  false 2");
		if ( ! contactService.createContact(propertyManagementDto.getCompanyContact()) ) System.out.println("!!!  false 3");
		
		
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
		
		boolean deleteOk = true;
		
		PropertyManagement propertyManagement = propertyManagementMapper.propertyManagementDtoToPropertyManagement(propertyManagementDto);
			
		
		// delete all loginUsers from PropertyManagement
		for(LoginUserDto loginUserDto : propertyManagementDto.getLoginUsers()) {
			if( ! this.removeLoginUserFromPropertyManagement(loginUserDto, propertyManagementDto)) {
				deleteOk = false;
			} else {
				if ( ! userService.deleteUser(loginUserDto)) deleteOk = false;
			}
		}
		
		if ( ! propertyManagementDao.delete(propertyManagement)) deleteOk = false;
		 System.out.println("delete 2 ok : " + deleteOk);
		
		if ( ! userService.deleteUser(propertyManagementDto.getPrimaryLoginUser()) ) deleteOk = false;
		 System.out.println("delete 1 ok : " + deleteOk);
		
		if ( ! contactService.deleteContactById(propertyManagement.getPrimaryContact().getId())) deleteOk = false;
		 System.out.println("PrimaryContact gelöscht " + deleteOk);
		 
		if ( ! contactService.deleteContactById(propertyManagement.getCompanyContact().getId())) deleteOk = false;
		
		System.out.println("CompanyContact gelöscht " + deleteOk);

		// TODO success notification to deleting person
		return deleteOk;
	}

	@Override
	public boolean updatePropertyManagement(PropertyManagementDto propertyManagementDto) {
		return propertyManagementDao.save(propertyManagementMapper.propertyManagementDtoToPropertyManagement(propertyManagementDto));
	}

	@Override
	public PropertyManagementDto findPropertyManagementbyId(int id) {
		Optional<PropertyManagement> optionalPropertyManagement =  propertyManagementDao.findById(id);
		if (optionalPropertyManagement.isPresent()) {
			PropertyManagementDto propertyManagementDto = propertyManagementMapper.propertyManagementToPropertyManagementDto(optionalPropertyManagement.get());
			propertyManagementDto.setCompanyContact(contactService.findContactById(propertyManagementDto.getCompanyContact().getId()));
			propertyManagementDto.setPrimaryContact(contactService.findContactById(propertyManagementDto.getPrimaryContact().getId()));
			propertyManagementDto.setPrimaryLoginUser(userService.findUserById(propertyManagementDto.getPrimaryLoginUser().getId()));
			
			List <LoginUserDto> loginuserDtos = new ArrayList<LoginUserDto>();
			List <Integer> userIds = getLoginUserIdsFromPropertyManagement(propertyManagementDto);
			System.out.println("userIds SERVICEIMPL" + userIds);
			for (int i:userIds) {
				loginuserDtos.add(userService.findUserById(i));
			}
			System.out.println("findPropertyManagementbyId SERVICEIMPL" + loginuserDtos);
			propertyManagementDto.setLoginUsers(loginuserDtos);
			return propertyManagementDto;
		}else {
				return null;
			}
	}

	@Override
	public PropertyManagementDto findPropertyManagementbyPrimaryContact(
			PropertyManagementDto propertyManagementDto) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean addLoginUserToPropertyManagement(LoginUserDto loginUserDto,  PropertyManagementDto propertyManagementDto) {
		
		if (!
		propertyManagementDao.addLoginUserToPropertyManagement(
				loginUserMapper.loginUserDtoToLoginUser(loginUserDto), 
				propertyManagementMapper.propertyManagementDtoToPropertyManagement(propertyManagementDto)
			)
		)return false;
		
		propertyManagementDto.getLoginUsers().add(loginUserDto);		
		
		return true;
	}
	@Override
	public boolean removeLoginUserFromPropertyManagement(LoginUserDto loginUserDto,  PropertyManagementDto propertyManagementDto) {
		return propertyManagementDao.removeLoginUserFromPropertyManagement(loginUserMapper.loginUserDtoToLoginUser(loginUserDto), 
				propertyManagementMapper.propertyManagementDtoToPropertyManagement(propertyManagementDto));
	}
	@Override
	public List<Integer> getLoginUserIdsFromPropertyManagement(PropertyManagementDto propertyManagementDto) {

		return propertyManagementDao.getLoginUsersByPropertyManagement(propertyManagementMapper.propertyManagementDtoToPropertyManagement(propertyManagementDto));
				
	}
	
	@Override
	public List<PropertyManagementDto> findPropertyManagementbyCompanyName(PropertyManagementDto propertyManagementDto) {
		
		List<PropertyManagementDto> propertyManagements = new ArrayList<PropertyManagementDto>();
		
		propertyManagements.add()
		
		return ;
	}
}
