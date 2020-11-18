package de.ocplearn.hv.test.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dao.BuildingDao;
import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.BuildingDto;
import de.ocplearn.hv.dto.BuildingOwnerDto;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.mapper.BuildingMapper;
import de.ocplearn.hv.mapper.CycleAvoidingMappingContext;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.BuildingType;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.service.ContactService;
import de.ocplearn.hv.service.PropertyManagementService;
import de.ocplearn.hv.service.UserService;
import de.ocplearn.hv.test.dao.AddressDaoTest;
import de.ocplearn.hv.util.StaticHelpers;

@SpringBootTest
public class BuildingServiceTest {
	
	ContactService contactService;
	
	private PropertyManagementService propertyManagementService;
	
	private UserService userService;
	
	private BuildingMapper buildingMapper;
	
	private BuildingDao buildingDao; 
	
	public static HashMap<String, byte[]> hashMap = StaticHelpers.createHash("Pa$$w0rd", null);
	
	public static Supplier<LoginUserDto> loginUserDtoEmployeeSupplier = () -> {return new LoginUserDto("BuildingTest" + System.currentTimeMillis(), Role.EMPLOYEE, hashMap.get("hash") , hashMap.get("salt"), Locale.GERMANY);};
	
	
	@Autowired
	public BuildingServiceTest(BuildingDao buildingDao, PropertyManagementService propertyManagementService, UserService userService,BuildingMapper buildingMapper, ContactService contactService) {
		super();
		this.buildingMapper = buildingMapper;
		this.contactService = contactService;
		this.userService = userService;
		this.propertyManagementService = propertyManagementService;
		this.buildingDao = buildingDao;
	}
	
	@Test
	public void testCreateBuilding() {
	
		BuildingDto buildingDto = new BuildingDto();
		AddressDto address = AddressDaoTest.testAddressDtoSupplier.get();
		Assertions.assertTrue(contactService.createAddress(address));
		PaymentType paymentType = PaymentType.STARTER;
		List<LoginUserDto> loginUsers = PropertyManagementServiceTest.loginUserListDtoSupplier.get();
		LoginUserDto employee = loginUserDtoEmployeeSupplier.get();
		Assertions.assertTrue(userService.createUser(employee));
		loginUsers.add(employee);
		PropertyManagementDto propertyManagementDto = new PropertyManagementDto(  
				PropertyManagementServiceTest.loginUserDtoSupplier.get(), 
				PropertyManagementServiceTest.primaryContactDtoSupplier.get(),  
				paymentType, 
				loginUsers,  
				PropertyManagementServiceTest.companyContactDtoSupplier.get());
		Assertions.assertTrue(propertyManagementService.createPropertyManagement(propertyManagementDto));
		buildingDto.setAddress(address);
		buildingDto.setPropertyManagement(propertyManagementDto);
		buildingDto.setBuildingType(BuildingType.APARTMENT);
		buildingDto.setName("TEST-OFFICE");
		buildingDto.setNote("this is an office building");
		buildingDto.setOwners(new ArrayList<BuildingOwnerDto>());
		
		System.out.println("testCreateBuilding" + buildingDto);
		Building building = buildingMapper.buildingDtoToBuilding(buildingDto, new CycleAvoidingMappingContext());
		System.out.println("testCreateBuilding2" + building);
		Assertions.assertTrue(buildingDao.save(building));
		
		
		}
	
}
