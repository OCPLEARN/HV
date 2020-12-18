package de.ocplearn.hv.test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.logging.Logger;

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
import de.ocplearn.hv.dto.OwnershipDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.dto.TransactionDto;
import de.ocplearn.hv.dto.UnitDto;
import de.ocplearn.hv.model.AddressType;
import de.ocplearn.hv.model.BuildingType;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.model.UnitType;
import de.ocplearn.hv.service.PropertyManagementService;
import de.ocplearn.hv.service.UserService;
import de.ocplearn.hv.test.dao.AddressDaoTest;
import de.ocplearn.hv.test.dao.LoginUserDaoTest;
import de.ocplearn.hv.util.LoggerBuilder;
import de.ocplearn.hv.util.StaticHelpers;

@SpringBootTest
@Component
public class TestObjectSupplier {
	
	private Logger logger;
	
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
	
	
	
	private org.slf4j.Logger logger2 = org.slf4j.LoggerFactory.getLogger("de.ocplearn.hv");
	
	
	@Autowired
	private TestObjectSupplier(
			UserService userService,
			@Qualifier("LoginUserDaoJdbc") LoginUserDao loginUserDao,
			PropertyManagementService propertyManagementService,
			@Autowired LoggerBuilder loggerBuilder
			) {
		
		this.logger = loggerBuilder.build("de.ocplearn.hv");
		this.userService = userService;
		this.loginUserDao = loginUserDao;
		this.propertyManagementService = propertyManagementService;
		
		//this.logger.log(Level.INFO,"check for models ...");
		
		logger2.info("Ich pribier mal aus"); // Test SLF4J logger
		
		PM_MODELS.forEach( (k, v) -> {
			// k = model name
			// v = userModel1 loginUserName 
			LoginUserDto loginUserDto = this.userService.findUserByLoginUserName(v);

			if (loginUserDto == null) {
				
				// #1 loginUser
				loginUserDto = this.createLoginUserDto(Role.PROPERTY_MANAGER, v);
				// #2 PM
				PropertyManagementDto pmModel = this.createPropertyManagementDto(loginUserDto);
				this.propertyManagementService.createPropertyManagement(pmModel);	// pm saved, lu saved
				// #3 Building + Address + Building Unit
				AddressDto buildingAddress = this.createAddressDto(AddressType.BUILDING_ADDRESS);
				AddressDto unitAddress = this.createAddressDto(AddressType.PRIMARY_PRIVATE_ADDRESS);
				BuildingDto buildingDto = this.createBuildingDto( pmModel );
				buildingDto.setName("House " + k);
				buildingDto.setAddress(buildingAddress);
				this.propertyManagementService.createBuilding(buildingDto);	// building saved
				// unit 1 BUILDING
				UnitDto unitDto_BUILDING = new UnitDto( buildingDto, "BUILDING_UNIT name", buildingAddress, 1000.00, 1962, "note", UnitType.BUILDING_UNIT );
				this.propertyManagementService.createUnit(unitDto_BUILDING);
				System.out.println("######### buiding unit id =   " + unitDto_BUILDING.getId() );
				//System.out.println("bo1 : " + bo1);
				
				
				// #4 BuildingOwner and assign 
				BuildingOwnerDto sister1 = this.createBuildingOwnerDto( pmModel );
				this.propertyManagementService.createBuildingOwner(sister1);	//sis 1 saved
				
				OwnershipDto ownership1 = new OwnershipDto(unitDto_BUILDING, sister1, 0.5, LocalDate.of(1980, 4, 5), null  );
				this.propertyManagementService.setOwnership(ownership1, buildingDto);
				
				BuildingOwnerDto sister2 = this.createBuildingOwnerDto( pmModel );
				this.propertyManagementService.createBuildingOwner(sister2);	// sis2 saved
				OwnershipDto ownership2 = new OwnershipDto(unitDto_BUILDING, sister2, 0.5, LocalDate.of(1980, 4, 5), null  );
				this.propertyManagementService.setOwnership(ownership2, buildingDto);			
				
				// units
				switch(v) {
					case "userModel3" : {
						// L
						BuildingDto buildingDto3 = this.createBuildingDto( pmModel );
						buildingDto3.setName("House 3 " + k);
						AddressDto addressDto = new AddressDto(partsBox.streetSupplier.get(), 
								"49", 
								partsBox.apartmentsSupplier.get(), 
								partsBox.citySupplier.get(), 
								partsBox.zipSupplier.get(), 
								partsBox.provinceSupplier.get(), 
								partsBox.countrySupplier.get(), 
								partsBox.latitudeSupplier.get(), 
								partsBox.longitudeSupplier.get(),
								AddressType.BUILDING_ADDRESS);
						buildingDto3.setAddress(addressDto);;
						this.propertyManagementService.createBuilding(buildingDto3);	// building saved
						//  BUILDING unit for building 3
						UnitDto unitDto_BUILDING3 = new UnitDto( buildingDto3, "BUILDING_UNIT building 2", addressDto, 45000.00, 2000, "Skyscraper 12", UnitType.BUILDING_UNIT );
						this.propertyManagementService.createUnit(unitDto_BUILDING3);
						System.out.println("######### buiding unit id =   " + unitDto_BUILDING3.getId() );
						//assign buildingowner to building
						OwnershipDto ownership3 = new OwnershipDto(unitDto_BUILDING3, sister1, 1.0, LocalDate.of(1990, 1, 1), null  );
						this.propertyManagementService.setOwnership(ownership3, buildingDto3);
						
						//units
						String[] unitNames =  {"EG left", "EG right", "OG1 left", "OG1 right", "OG2 left", "OG2, right", "OG3 left", "OG3, right"};
						for (String s: unitNames) {
							UnitDto unitDto_unit = new UnitDto( buildingDto3, s, addressDto, 80.25, 1977, "note", UnitType.APARTMENT_UNIT );
							this.propertyManagementService.createUnit(unitDto_unit);
							
						}
						UnitDto unitDto_unit = new UnitDto( buildingDto3, "garage", addressDto, 175.00, 1985, "parking", UnitType.PARKING_UNIT );
						this.propertyManagementService.createUnit(unitDto_unit);
						
						
						
						
						
						
					}
					case "userModel2" :{ 
						// M				
						
						BuildingDto buildingDto2 = this.createBuildingDto( pmModel );
						buildingDto2.setName("House 2 " + k);
						buildingDto2.setBuildingType(BuildingType.HALL);
						AddressDto addressDto = new AddressDto(partsBox.streetSupplier.get(), 
								"42", 
								partsBox.apartmentsSupplier.get(), 
								partsBox.citySupplier.get(), 
								partsBox.zipSupplier.get(), 
								partsBox.provinceSupplier.get(), 
								partsBox.countrySupplier.get(), 
								partsBox.latitudeSupplier.get(), 
								partsBox.longitudeSupplier.get(),
								AddressType.BUILDING_ADDRESS);
						buildingDto2.setAddress(addressDto);;
						this.propertyManagementService.createBuilding(buildingDto2);	// building saved
						//  BUILDING unit for building 2
						UnitDto unitDto_BUILDING2 = new UnitDto( buildingDto2, "BUILDING_UNIT building 2", addressDto, 50000.00, 1980, "warehouse 13", UnitType.BUILDING_UNIT );
						this.propertyManagementService.createUnit(unitDto_BUILDING2);
						System.out.println("######### buiding unit id =   " + unitDto_BUILDING2.getId() );
						//assign buildingowner to building	
						OwnershipDto ownership4 = new OwnershipDto(unitDto_BUILDING2, sister2, 1.0, LocalDate.of(2000, 1, 1), null  );
						this.propertyManagementService.setOwnership(ownership4, buildingDto2);
						
						
						
						
						
					}
					case "userModel1" : {
						// S
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
		buildingDto.setWegType(false);
		return buildingDto;		
	}
	
	public BuildingDto createBuildingDto() {
		return this.createBuildingDto(null);
	}
	
	public BuildingOwnerDto createBuildingOwnerDto () {
		
		buildingOwnerDto = new BuildingOwnerDto();
		buildingOwnerDto.setContact(createContactDto(true, this.partsBox.firstNameSupplier.get(), partsBox.lastNameSupplier.get(),partsBox.companyNameSupplier.get()));
		buildingOwnerDto.setLoginUser(createLoginUserDto(Role.OWNER));
		buildingOwnerDto.setBuildings(new ArrayList<BuildingDto>());
		buildingOwnerDto.setPropertyManagement(createPropertyManagementDto());
		
		return buildingOwnerDto;
		
	}
	
	public BuildingOwnerDto createBuildingOwnerDto ( PropertyManagementDto propertyManagementDto ) {
		
		buildingOwnerDto = createBuildingOwnerDto();
		buildingOwnerDto.setPropertyManagement( propertyManagementDto) ;
		
		return buildingOwnerDto;

	}



	
	
	
	
	
	
	
	
	
	
	

}
