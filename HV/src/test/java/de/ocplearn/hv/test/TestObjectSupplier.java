package de.ocplearn.hv.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dao.LoginUserDao;
import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.BuildingDto;
import de.ocplearn.hv.dto.BuildingOwnerDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.dto.TransactionDto;
import de.ocplearn.hv.dto.UnitDto;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.AddressType;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.service.ContactService;
import de.ocplearn.hv.service.PropertyManagementService;
import de.ocplearn.hv.service.UserService;
import de.ocplearn.hv.test.dao.AddressDaoTest;
import de.ocplearn.hv.test.dao.LoginUserDaoTest;
import de.ocplearn.hv.util.StaticHelpers;

@SpringBootTest
public class TestObjectSupplier {
	
	private static final TestObjectSupplier INSTANCE;	
		
	static { INSTANCE = new TestObjectSupplier();}
	
	public static TestObjectSupplier getInstance() { return INSTANCE;};
	
	
	private LoginUserDto loginUserDto;
	
	private ContactDto contactDto;
	
	private AddressDto addressDto;
	
	private PropertyManagementDto propertyManagementDto;
	
	private BuildingDto buildingDto;
	
	private BuildingOwnerDto buildingOwnerDto;
	
	@Autowired
	private LoginUserDao loginUserDao;

	
	private TestObjectSupplier() {	
		
	}
	
	public LoginUserDto createLoginUserDto(Role role) {
		 loginUserDto = new LoginUserDto();
		
		String randomName = LoginUserDaoTest.getRandomName();
		
		Optional<LoginUser> opt = loginUserDao.findUserByLoginUserName(randomName);
		if ( opt.isPresent() ) {
			System.out.println("### LoginUserName already exists!");
			throw new RuntimeException("### random LoginUserName already exists!");
		}
		loginUserDto.setLoginUserName(randomName);
		
		 HashMap<String, byte[]> hm = StaticHelpers.createHash("Pa$$w0rd", null);
	        loginUserDto.setPasswHash( hm.get("hash") );
	        loginUserDto.setSalt(hm.get("salt") );	
	        if (role==null) {
	        loginUserDto.setRole(Role.PROPERTY_MANAGER);
	        }else {
	        	loginUserDto.setRole(role);
	        }
	        loginUserDto.setLocale( Locale.GERMANY );
	        
	    return loginUserDto;
	}
	
	public ContactDto createContactDto (boolean isCompany, String firstName, String lastName, String...companyName ) {
				
		contactDto =  new ContactDto.ContactBuilder()
				.setCompany(isCompany)
				.setcompanyName(isCompany?companyName[0]:null)
				.setEmail("AB@ABB.de")
				.setFax("06221 12345678")
				.setFirstName((firstName==null)?"Hans":firstName)
				.setLastName((lastName==null)?"Wurst":lastName)
				.setMobilePhone("0172 12345678")
				.setAddressList(Arrays.asList(AddressDaoTest.testAddressDtoSupplier.get(), createAddressDto(null)))
				.build();
		
		return contactDto;
		}

	
	
	public AddressDto createAddressDto (AddressType addressType) {
		 addressDto = new AddressDto("Poststraße", " 3", "Hauptbahnhof",
				"Frankfurt am Main","60329","Hessen","DE", 
				50.106825, 8.663707, (addressType==null)?AddressType.PRIMARY_BUSINESS_ADDRESS: addressType);
		 
		return addressDto;
	}
	
	public PropertyManagementDto createPropertyManagementDto() {
		
		propertyManagementDto = new PropertyManagementDto();
		propertyManagementDto.setPrimaryLoginUser(createLoginUserDto(Role.PROPERTY_MANAGER));
		propertyManagementDto.setPrimaryContact(createContactDto(false, "Peter", "Pan"));
		propertyManagementDto.setPaymentType(PaymentType.PRO);
		propertyManagementDto.setLoginUsers(Arrays.asList(createLoginUserDto(Role.EMPLOYEE),createLoginUserDto(Role.EMPLOYEE)));
		propertyManagementDto.setCompanyContact(createContactDto(true, "Bruce", "Banner"));
		
		return propertyManagementDto;
	}
	
	public BuildingDto createBuildingDto() {
		buildingDto = new BuildingDto();
		buildingDto.setName("Manson-House");
		buildingDto.setAddress(createAddressDto(AddressType.BUILDING_ADDRESS));
		//TODO: implement createOwner, createUnit, createTransactions
		buildingDto.setOwners(new ArrayList<BuildingOwnerDto>());
		buildingDto.setUnits(new TreeSet<UnitDto>());
		buildingDto.setTransactions(new TreeSet<TransactionDto>());
		buildingDto.setPropertyManagement(createPropertyManagementDto());
		buildingDto.setNote("Do not enter");
		
		return buildingDto;
		
	}
	
	public BuildingOwnerDto createBuildingOwnerDto () {
		
		buildingOwnerDto = new BuildingOwnerDto();
		buildingOwnerDto.setContact(createContactDto(true, "Bruce", "Banner"));
		buildingOwnerDto.setLoginUser(createLoginUserDto(Role.OWNER));
		//TODO: complete building <--> buildingOwner create methods
		buildingOwnerDto.setBuildings(new ArrayList<BuildingDto>());
		
		return buildingOwnerDto;
		
	}
	


	
	
	
	
	
	
	
	
	
	
	

}
