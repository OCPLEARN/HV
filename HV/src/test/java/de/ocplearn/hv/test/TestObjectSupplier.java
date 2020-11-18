package de.ocplearn.hv.test;

import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

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
import de.ocplearn.hv.model.BuildingType;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.model.UnitType;
import de.ocplearn.hv.service.ContactService;
import de.ocplearn.hv.service.PropertyManagementService;
import de.ocplearn.hv.service.UserService;
import de.ocplearn.hv.test.dao.AddressDaoTest;
import de.ocplearn.hv.test.dao.LoginUserDaoTest;
import de.ocplearn.hv.util.StaticHelpers;

@SpringBootTest
@Component
public class TestObjectSupplier {
	
	// list of PM test models
	private static final Map<String, String> PM_MODELS;
	
	static {
		PM_MODELS = new HashMap<>();
		PM_MODELS.put("Model1", "userModel1");
		PM_MODELS.put("Model2", "userModel2");
		PM_MODELS.put("Model3", "userModel3");
	}
	
	private static TestObjectSupplier INSTANCE;	
		
	//static { INSTANCE = new TestObjectSupplier();}
	
	public static TestObjectSupplier getInstance() { return INSTANCE;};
	
	private UserService userService;
	
	private PropertyManagementService propertyManagementService;
	
	private LoginUserDto loginUserDto;
	
	private ContactDto contactDto;
	
	private AddressDto addressDto;
	
	private PropertyManagementDto propertyManagementDto;
	
	private BuildingDto buildingDto;
	
	private BuildingOwnerDto buildingOwnerDto;
	
	private LoginUserDao loginUserDao;

	PartsBox partsBox = PartsBox.getInstance();
	
	@Autowired
	private TestObjectSupplier(
			UserService userService,
			@Qualifier("LoginUserDaoJdbc") LoginUserDao loginUserDao,
			PropertyManagementService propertyManagementService
			) {
		this.userService = userService;
		this.loginUserDao = loginUserDao;
		this.propertyManagementService = propertyManagementService;
		
		PM_MODELS.forEach( (k, v) -> {
			// k = model name
			// v = userModel1 loginUserName 
			LoginUserDto loginUserDto = this.userService.findUserByLoginUserName(v);

			if (loginUserDto == null) {
				
				// #1 loginUser
				loginUserDto = this.createLoginUserDto(Role.PROPERTY_MANAGER, v);
				// #2 PM
				PropertyManagementDto pmModel = this.createPropertyManagementDto(loginUserDto);
				this.propertyManagementService.createPropertyManagement(propertyManagementDto);	// pm saved, lu saved
				// #3 Building + Address
				AddressDto buildingAddress = this.createAddressDto(AddressType.BUILDING_ADDRESS);
				AddressDto unitAddress = this.createAddressDto(AddressType.PRIMARY_PRIVATE_ADDRESS);
				BuildingDto buildingDto = this.createBuildingDto( pmModel );
				buildingDto.setName("House " + k);
				buildingDto.setAddress(buildingAddress);
				boolean bo1 = this.propertyManagementService.createBuilding(buildingDto);	// building saved
				//System.out.println("bo1 : " + bo1);

				// #4 BuildingOwner and assign 
				BuildingOwnerDto sister1 = this.createBuildingOwnerDto();
				this.propertyManagementService.createBuildingOwner(sister1);	//sis 1 saved
				this.propertyManagementService.assignBuildingOwnerToBuilding(sister1, buildingDto);
				BuildingOwnerDto sister2 = this.createBuildingOwnerDto();
				this.propertyManagementService.createBuildingOwner(sister2);	// sis2 saved
				this.propertyManagementService.assignBuildingOwnerToBuilding(sister2, buildingDto);				
				
				// units
				switch(v) {
					case "userModel3" : 
						// L
					case "userModel2" : 
						// M				
					case "userModel1" : {
						// S
						// unit 1 BUILDING
						UnitDto unitDto_BUILDING = new UnitDto( buildingDto, "BUILDING_UNIT name", buildingAddress, 1000.00, 1962, "note", UnitType.BUILDING_UNIT );
						this.propertyManagementService.createUnit(unitDto_BUILDING);
						// unit 2 EG left
						UnitDto unitDto_unit2 = new UnitDto( buildingDto, "EG left", unitAddress, 122.25, 1962, "note", UnitType.APARTMENT_UNIT );
						this.propertyManagementService.createUnit(unitDto_unit2);
						
						UnitDto unitDto_unit3 = new UnitDto( buildingDto, "EG right", unitAddress, 122.25, 1962, "note", UnitType.APARTMENT_UNIT );
						this.propertyManagementService.createUnit(unitDto_unit3);
						
						UnitDto unitDto_unit4 = new UnitDto( buildingDto, "OG left", unitAddress, 112.75, 1964, "note", UnitType.APARTMENT_UNIT );
						this.propertyManagementService.createUnit(unitDto_unit4);
						
						UnitDto unitDto_unit5 = new UnitDto( buildingDto, "OG right", unitAddress, 112.75, 1964, "note", UnitType.APARTMENT_UNIT );
						this.propertyManagementService.createUnit(unitDto_unit5);
						break;
					}		
				}

			}
		} );
		
		INSTANCE = this;
	}
	
	public PropertyManagementDto getModel( String key ) {
		String loginUserName = PM_MODELS.get(key);
		return this.propertyManagementService.findPropertyManagementbyPrimaryLoginUserName(loginUserName);
	}
	
	public LoginUserDto createLoginUserDto(Role role, String loginUserName) {
		 loginUserDto = new LoginUserDto();
			
			//String randomName = LoginUserDaoTest.getRandomName();
		 	String randomName = loginUserName;
		 
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
	
	public LoginUserDto createLoginUserDto(Role role) {
		return this.createLoginUserDto(role,LoginUserDaoTest.getRandomName() );
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
	
	public PropertyManagementDto createPropertyManagementDto(LoginUserDto loginUserDto) {
		
		propertyManagementDto = new PropertyManagementDto();
		propertyManagementDto.setPrimaryLoginUser(loginUserDto);
		propertyManagementDto.setPrimaryContact(createContactDto(false, partsBox.firstNameSupplier.get(), partsBox.lastNameSupplier.get()));
		propertyManagementDto.setPaymentType(PaymentType.PRO);
		propertyManagementDto.setLoginUsers(Arrays.asList(createLoginUserDto(Role.EMPLOYEE),createLoginUserDto(Role.EMPLOYEE)));
		propertyManagementDto.setCompanyContact(createContactDto(true, partsBox.firstNameSupplier.get(), partsBox.lastNameSupplier.get(), "Model Company" ));
		
		return propertyManagementDto;
	}	
	
	public PropertyManagementDto createPropertyManagementDto() {
		return this.createPropertyManagementDto( createLoginUserDto(Role.PROPERTY_MANAGER) );
	}

	public BuildingDto createBuildingDto(PropertyManagementDto propertyManagementDto)  { 
		buildingDto = new BuildingDto();
		buildingDto.setName("Manson-House");
		buildingDto.setAddress(createAddressDto(AddressType.BUILDING_ADDRESS));
		//TODO: implement createOwner, createUnit, createTransactions
		buildingDto.setOwners(new ArrayList<BuildingOwnerDto>());
		buildingDto.setUnits(new TreeSet<UnitDto>());
		buildingDto.setTransactions(new TreeSet<TransactionDto>());
		buildingDto.setPropertyManagement( propertyManagementDto );
		buildingDto.setNote("Do not enter");
		buildingDto.setBuildingType(BuildingType.APARTMENT_BUILDING);
		return buildingDto;		
	}
	
	public BuildingDto createBuildingDto() {
		return this.createBuildingDto(null);
	}
	
	public BuildingOwnerDto createBuildingOwnerDto () {
		
		buildingOwnerDto = new BuildingOwnerDto();
		buildingOwnerDto.setContact(createContactDto(true, this.partsBox.firstNameSupplier.get(), partsBox.lastNameSupplier.get()));
		buildingOwnerDto.setLoginUser(createLoginUserDto(Role.OWNER));
		buildingOwnerDto.setBuildings(new ArrayList<BuildingDto>());
		
		return buildingOwnerDto;
		
	}
	


	
	
	
	
	
	
	
	
	
	
	

}
