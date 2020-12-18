package de.ocplearn.hv.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ocplearn.hv.dao.BuildingDao;
import de.ocplearn.hv.dao.BuildingOwnerDao;
import de.ocplearn.hv.dao.PropertyManagementDao;
import de.ocplearn.hv.dao.RenterDao;
import de.ocplearn.hv.dao.UnitDao;
import de.ocplearn.hv.dto.BuildingDto;
import de.ocplearn.hv.dto.BuildingOwnerDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.OwnershipDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.dto.RenterDto;
import de.ocplearn.hv.dto.UnitDto;
import de.ocplearn.hv.mapper.BuildingMapper;
import de.ocplearn.hv.mapper.BuildingOwnerMapper;
import de.ocplearn.hv.mapper.ContactMapper;
import de.ocplearn.hv.mapper.CycleAvoidingMappingContext;
import de.ocplearn.hv.mapper.LoginUserMapper;
import de.ocplearn.hv.mapper.OwnershipMapper;
import de.ocplearn.hv.mapper.PropertyManagementMapper;
import de.ocplearn.hv.mapper.RenterMapper;
import de.ocplearn.hv.mapper.UnitMapper;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.BuildingOwner;
import de.ocplearn.hv.model.Ownership;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.model.Unit;
import de.ocplearn.hv.model.UnitType;


@Service
public class PropertyManagementServiceImpl implements PropertyManagementService {

	private PropertyManagementDao propertyManagementDao;
	
	private PropertyManagementMapper propertyManagementMapper;
	
	private LoginUserMapper loginUserMapper;
	
	private UserService userService;
	
	private ContactService contactService; 
	
	private ContactMapper contactMapper;
	
	private OwnershipMapper ownershipMapper;
	
	@Autowired
	private BuildingDao buildingDao;
	
	private BuildingOwnerDao buildingOwnerDao;
	
	private BuildingMapper buildingMapper;
	
	private BuildingOwnerMapper buildingOwnerMapper;
	
	@Autowired
	private UnitDao unitDao;
	
	private UnitMapper unitMapper;
	
	private RenterDao renterDao;
	
	private RenterMapper renterMapper;
	
	@Autowired
	public PropertyManagementServiceImpl ( 	PropertyManagementDao propertyManagementDao,
											PropertyManagementMapper propertyManagementMapper,
											UserService userService,
											ContactService contactService,
											LoginUserMapper loginUserMapper,
											ContactMapper contactMapper,		
											BuildingMapper buildingMapper,
											BuildingOwnerMapper buildingOwnerMapper,
											OwnershipMapper ownershipMapper,
											BuildingOwnerDao buildingOwnerDao,
											UnitMapper unitMapper,
											RenterMapper renterMapper,
											RenterDao renterDao
											
										) {
		
		this.propertyManagementDao = propertyManagementDao;
		this.propertyManagementMapper = propertyManagementMapper;
		this.userService = userService;
		this.contactService = contactService;
		this.loginUserMapper=loginUserMapper;
		this.contactMapper = contactMapper;
		//this.buildingDao = buildingDao;
		this.buildingMapper = buildingMapper;
		this.buildingOwnerMapper = buildingOwnerMapper;
		this.buildingOwnerDao = buildingOwnerDao;
		//this.unitDao = unitDao;
		this.unitMapper = unitMapper;
		this.renterMapper = renterMapper;
		this.renterDao = renterDao;
		this.ownershipMapper = ownershipMapper;
		}
	
	
	@Override
	public boolean createPropertyManagement(PropertyManagementDto propertyManagementDto) {
		
		if ( ! userService.createUser(propertyManagementDto.getPrimaryLoginUser()) ) System.out.println("!!!  false 1");
		if ( ! contactService.createContact(propertyManagementDto.getPrimaryContact()) ) System.out.println("!!!  false 2");
		if ( ! contactService.createContact(propertyManagementDto.getCompanyContact()) ) System.out.println("!!!  false 3");
		
		
		PropertyManagement propertyManagement = propertyManagementMapper.propertyManagementDtoToPropertyManagement(propertyManagementDto);
		System.out.println("CREATE: "+propertyManagement);
		if(	propertyManagementDao.save(propertyManagement)) {
			System.out.println("======");
			System.out.println(propertyManagement.getPrimaryLoginUser().getId());
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
			 ContactDto contactDto) {
		Optional<PropertyManagement> optPropertyManagement =propertyManagementDao.findByPrimaryContact(contactMapper.contactDtoToContact(contactDto));
		if(optPropertyManagement.isPresent()) {
			return propertyManagementMapper.propertyManagementToPropertyManagementDto(optPropertyManagement.get());
		}else {
			return null;
		}
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
	public List<PropertyManagementDto> findPropertyManagementbyCompanyName( String companyName ) {
		
		List<PropertyManagementDto> propertyManagementDtoList = new ArrayList<PropertyManagementDto>();
		
		List<PropertyManagement> propertyManagementList = propertyManagementDao.findPropertyManagementByComanyName(companyName);
		for (PropertyManagement propertyManagement : propertyManagementList) {
			propertyManagementDtoList.add(propertyManagementMapper.propertyManagementToPropertyManagementDto(propertyManagement));
		}
		
		return propertyManagementDtoList;
	}


	@Override
	public boolean createBuilding(BuildingDto buildingDto) {
		// #1 Address
		if( ! this.contactService.createAddress(buildingDto.getAddress()) ) return false;
		// #2 Owner
		// TODO should createBuilding() also create its owners?
//		for ( BuildingOwnerDto bo : buildingDto.getOwners() ) {
//			if ( bo.getId() == 0 ) { // create owner, not exiting before
//				if ( ! this.buildingOwnerDao.save( this.buildingOwnerMapper.buildingOwnerDtoToBuildingOwner(bo) ) )
//				return false;
//			}
//		}
		Building building = buildingMapper.buildingDtoToBuilding(buildingDto, new CycleAvoidingMappingContext() );
		if( buildingDao.save(building)) {
			buildingDto.setId(building.getId());
			return true;
		}else {
		return false;
		}
	}


	@Override
	public boolean deleteBuildingById(int buildingDtoId) {
		 return deleteBuilding( this.buildingMapper.buildingToBuildingDto(buildingDao.findByIdFull(buildingDtoId).get(),new CycleAvoidingMappingContext())  );
	}

	@Override
	public boolean deleteBuilding(BuildingDto buildingDto) {
		//TODO: check if building/unit is currently in use in any active rental contracts
		if (findBuildingById(buildingDto.getId())!=null) {
				if(contactService.deleteAddress(buildingDto.getAddress())) {
					for (UnitDto unit : buildingDto.getUnits()) {
						if (!deleteUnit(unit)) return false;
					}
				return buildingDao.deleteById(buildingDto.getId());
				}else {
					return false;
				}
			}else {
				return false;
			}
	}

	@Override
	public boolean updateBuilding(BuildingDto buildingDto) {
		return buildingDao.save(buildingMapper.buildingDtoToBuilding(buildingDto, new CycleAvoidingMappingContext()));
	}


//	@Override
//	public boolean assignBuildingOwnerToBuilding(BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto) {
//		// TODO anpassen Ownership1
//		Unit unit = this.unitDao.getBuildingUnitFull(buildingDto.getId());
//		if(unit!=null) {
//			
//			UnitDto buildingUnit = unitMapper.unitToUnitDto(unit,new CycleAvoidingMappingContext());
//			if(buildingUnit.getUnitType()!=UnitType.BUILDING_UNIT) {
//				throw new IllegalStateException("not a BUILDING_UNIT id: " + buildingUnit.getId());
//			}
//			if(this.assignUnitOwnerToUnit(buildingOwnerDto, buildingUnit)) {
//				buildingOwnerDto.addBuilding(buildingDto);
//				buildingDto.addOwners(buildingOwnerDto);
//				return true;
//			}else {
//				return false;
//			}
//		}else {
//			System.out.println("assignBuildingOwnerToBuilding unit is null");
//			return false;
//			
//		}
//		
//	}


	@Override
	public boolean removeBuildingOwnerFromBuilding(BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto) {
		//TODO: check if owner still assigned to a unit, if so, delete that first
		//TODO: need method to remove owner from units as well to be able to remove from WEG true/false
		return buildingDao.removeBuildingOwnerFromBuilding(buildingOwnerMapper.buildingOwnerDtoToBuildingOwner(buildingOwnerDto, new CycleAvoidingMappingContext()), buildingMapper.buildingDtoToBuilding(buildingDto, new CycleAvoidingMappingContext()));
	}

//
//	@Override
//	public boolean assignAllUnitsToOneOwner(BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto) {
//		
//		//if any buildingOwners exist for this Building, remove all existing buildingOwners from this building
//		for(BuildingOwnerDto buildingOwnerDto2: buildingDto.getOwners()) {
//		if (! buildingDao.removeBuildingOwnerFromBuilding(buildingOwnerMapper.buildingOwnerDtoToBuildingOwner
//				(buildingOwnerDto2, new CycleAvoidingMappingContext()), 
//				buildingMapper.buildingDtoToBuilding(buildingDto, new CycleAvoidingMappingContext()))) {
//			return false;
//		}
//		
//		}
//		//if any UnitOwners exist for Units of this Building, remove all existing UnitOwners from these Units
//		for (UnitDto unitDto:buildingDto.getUnits()) {
//			if(! buildingDao.removeOwnerFromUnit(buildingOwnerMapper.buildingOwnerDtoToBuildingOwner
//					(buildingOwnerDto, new CycleAvoidingMappingContext()),
//					unitMapper.unitDtoToUnit(unitDto, new CycleAvoidingMappingContext()))){
//				return false;
//			}
//		}
//		//assign new BuildingOwner to the unit representing the Buildingownership
//		if (!assignBuildingOwnerToBuilding(buildingOwnerDto, buildingDto))return false;
//		
//		//assign new Buildingowner to all Units in this Building
//		for (UnitDto unitDto:buildingDto.getUnits()) {
//			if (!assignUnitOwnerToUnit(buildingOwnerDto, unitDto)) return false;
//		}
//		return true;
//	}


	@Override
	public boolean createUnit(UnitDto unitDto) {
		if(unitDto.getAddress().getId()==0) {
			if(!contactService.createAddress(unitDto.getAddress())) return false;
		}
		System.out.println("public boolean createUnit(UnitDto unitDto)" + unitDto);
		Unit unit = unitMapper.unitDtoToUnit(unitDto, new CycleAvoidingMappingContext());
		System.out.println("this.unitMapper.UnitDtoToUnit(unitDto)" + unit);
		
	
		
		if( this.unitDao.save(unit)) {
			unitDto.setId(unit.getId());
			return true;
		}else {
			return false;
		}
	}


	@Override
	public boolean deleteUnit(UnitDto unitDto) {
		return this.unitDao.delete(this.unitMapper.unitDtoToUnit(unitDto, new CycleAvoidingMappingContext()));
	}


	@Override
	public boolean updateUnit(UnitDto unitDto) {
		return this.unitDao.save(this.unitMapper.unitDtoToUnit(unitDto, new CycleAvoidingMappingContext()));
	}


//	@Override
//	public boolean assignUnitOwnerToUnit(BuildingOwnerDto buildingOwnerDto, UnitDto unitDto) {
//		BuildingOwner buildingOwner = buildingOwnerMapper.buildingOwnerDtoToBuildingOwner(buildingOwnerDto, new CycleAvoidingMappingContext());
//		Unit unit = unitMapper.unitDtoToUnit(unitDto, new CycleAvoidingMappingContext());
//		if( buildingDao.addOwnerToUnit(buildingOwner,unit, 0.0)){ 	// buildingShare 0.0 will be replaced by method setOwnership() in PMService
//			return true;
//		}else {
//			return false;
//		}
//		//TODO: check if unitowner is already listed as buildingowner in the unit representing the building, if not, add it
//	}
//
//
//	@Override
//	public boolean removeUnitOwnerFromUnit(BuildingOwnerDto buildingOwnerDto, UnitDto unitDto) {
//		// TODO anpassen Ownership1
//		return this.buildingDao.removeOwnerFromUnit( 
//			this.buildingOwnerMapper.buildingOwnerDtoToBuildingOwner(buildingOwnerDto, new CycleAvoidingMappingContext()) ,
//			this.unitMapper.unitDtoToUnit(unitDto, new CycleAvoidingMappingContext())
//		);
//	}


	@Override
	public boolean assignRenterToUnit(RenterDto renterDto, UnitDto unitDto) {
		return this.unitDao.assignRenterToUnit(
				this.renterMapper.RenterDtoToRenter(renterDto),
				this.unitMapper.unitDtoToUnit(unitDto, new CycleAvoidingMappingContext())
				);
	}


	@Override
	public boolean removeRenterFromUnit(RenterDto renterDto, UnitDto unitDto) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public BuildingDto findBuildingById(int buildingId) {
		Optional<Building> opt = buildingDao.findByIdFull(buildingId);
		if (opt.isPresent()) {
		return buildingMapper.buildingToBuildingDto(opt.get(), new CycleAvoidingMappingContext());
		}else {
			return null;
		}
	}
	
	@Override
	public List<BuildingOwnerDto> findBuildingOwnersByBuildingId(int buildingId) {
		return buildingDao.findBuildingOwnerIdsByBuildingId(buildingId, BuildingDao.tablePageViewData)
				.stream()
				.map(id -> findBuildingOwnerById(id))
				.collect(Collectors.toList());
	}

	@Override
	public List<BuildingDto> findBuildingsByPropertyManagement(int propertyManagementId) {
		return this.buildingDao.findBuildingsByPropertyManagement(propertyManagementId)
				.stream()
				.map(building -> this.buildingMapper.buildingToBuildingDto(building, new CycleAvoidingMappingContext()))
				.collect(Collectors.toList());
	}	

	@Override
	public boolean createBuildingOwner(BuildingOwnerDto buildingOwnerDto) {
		
		if( buildingOwnerDto.getLoginUser() == null ) {
			buildingOwnerDto.setLoginUser( new LoginUserDto() );
		} else {
			userService.createUser( buildingOwnerDto.getLoginUser() );
			}
		
		if ( ! contactService.createContact(buildingOwnerDto.getContact()) ){
			
			return false; 
		}
		
		BuildingOwner buildingOwner = this.buildingOwnerMapper.buildingOwnerDtoToBuildingOwner(buildingOwnerDto,new CycleAvoidingMappingContext());
		
	
		
		if (this.buildingOwnerDao.save( buildingOwner )) {
			buildingOwnerDto.setId(buildingOwner.getId());
			return true;
		}else {
			return false;
		}
	}


	@Override
	public boolean deleteBuildingOwnerById(int buildingOwnerDtoId) {
		//TODO anpassen Ownership1
		return this.buildingOwnerDao.delete(buildingOwnerDtoId);
	}


	@Override
	public boolean deleteBuildingOwner(BuildingOwnerDto buildingOwnerDto) {
		//TODO anpassen Ownership1
		return this.buildingOwnerDao.delete( this.buildingOwnerMapper.buildingOwnerDtoToBuildingOwner(buildingOwnerDto,new CycleAvoidingMappingContext()) );
	}


	@Override
	public boolean updateBuildingOwner(BuildingOwnerDto buildingOwnerDto) {
		return this.buildingOwnerDao.save( this.buildingOwnerMapper.buildingOwnerDtoToBuildingOwner(buildingOwnerDto,new CycleAvoidingMappingContext()) );
	}


	@Override
	public BuildingOwnerDto findBuildingOwnerById(int buildingOwnerId) {
		Optional<BuildingOwner> opt = this.buildingOwnerDao.findByIdFull(buildingOwnerId);
		return opt.isPresent() ? this.buildingOwnerMapper.buildingOwnerToBuildingOwnerDto(opt.get(), new CycleAvoidingMappingContext()) : null;
	}


	@Override
	public UnitDto findUnitById(int unitId) {
		//TODO: replace with findUnitByIdFull once it is implemented
		Optional<Unit> opt = unitDao.findUnitByIdPartial(unitId);
		if (opt.isPresent()) {
		return unitMapper.unitToUnitDto(opt.get(),new CycleAvoidingMappingContext());
		}else {
			return null;
		}
	}


	@Override
	public PropertyManagementDto findPropertyManagementbyPrimaryLoginUserName(String PrimaryLoginUserName) {
		LoginUserDto loginUserDto = this.userService.findUserByLoginUserName(PrimaryLoginUserName);
		Optional<PropertyManagement> opt =  this.propertyManagementDao.findByPrimaryLoginUserId(loginUserDto.getId());
		if ( ! opt.isPresent() ) return null;
		return this.findPropertyManagementbyId( opt.get().getId() );
	}

	@Override
	public boolean saveRenter(RenterDto renterDto) {
		return this.renterDao.save( this.renterMapper.RenterDtoToRenter(renterDto) );
	}

	@Override
	public boolean setOwnership(OwnershipDto ownership, BuildingDto building) {
		return this.setOwnership(ownership.getBuildingOwner(), building, ownership.getUnit(), 
				ownership.getBuildingShare(), ownership.getShareStart());
	}
	
	@Override
	public boolean setOwnership( BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto, UnitDto unitDto,
			double buildingShare, LocalDate shareStart ) {
		
		// 1. set LocalDate - test Date from DB , wenn gleich dann nur KorrekturEintrag (keine Änderung des shareEnd Datums)
		//										, neuer Eintrag => new shareStart
		OwnershipDto currentOwnership = null;
		
		for( OwnershipDto ownership : buildingDto.getOwnerships() ) {
			if( ( ownership.getBuildingOwner().getId() == buildingOwnerDto.getId() ) && ( ownership.getShareEnd()  == null ) ) {			
				//	find active entry
				currentOwnership = ownership;
				// is this just a fix or a real change
				//if( ownership.getShareStart().isEqual(shareStart) ) {
//				if( shareStart.isAfter(ownership.getShareStart()) ) {
//					// is correction entry
//					currentOwnership.setBuildingShare(buildingShare);
//					currentOwnership.setShareStart(shareStart);
//					
//					return unitDao.saveOwnership(ownershipMapper.ownershipDtoToOwnership(currentOwnership, new CycleAvoidingMappingContext()));
//				}
				break ;	// can only be 1 entry for this owner
			}
		}
		
		// is change  ownership entry
		// (1) change current entry
		
		if(currentOwnership!=null) {
			currentOwnership.setShareEnd(shareStart.minusDays(1));
			unitDao.saveOwnership( ownershipMapper.ownershipDtoToOwnership(currentOwnership, new CycleAvoidingMappingContext()) );
		}
		
		// 2. WEG Flag? 	wenn ja:	- check unit != unitDto Building (IllegalState)
		// check of given unit
		if(buildingDto.isWegType() ) { 	
			// WEG set: unit must not be a building unit
			if( (unitDto.getUnitType() ==UnitType.BUILDING_UNIT)) throw new IllegalStateException("Wrong unit type supplied"); 
						
		} else { 
			// not WEG: unit must be building unit
			if( ! (unitDto.getUnitType() == UnitType.BUILDING_UNIT)) throw new IllegalStateException("Wrong unit type supplied");
		}
		
		// (2: )Neue currentOwnership aus dem übergebenen ObjectDto
		currentOwnership = new OwnershipDto(unitDto, buildingOwnerDto, buildingShare, shareStart, null);
	
		unitDao.saveOwnership( ownershipMapper.ownershipDtoToOwnership(currentOwnership, new CycleAvoidingMappingContext()) );
		
		// (3) add new ownership to buildings  list of ownerships
		buildingDto.getOwnerships().add(currentOwnership); // aktualisierter Zustand für den Caller
		
		//								- get Set<Unit>
		//								- get List<Ownerships>
		//								- set Owner.setOwnerships
		//								-  check Ownerships == 100%
		//								-  save with new Date		
		
		// 					wenn nein: 	- check unitDto == unitDto-Building? (IllegalState)
		// 								-  getList<Owner>
		//								-  get List<Ownership>
		//								-  set Owner.setOwnership
		//								-  check Ownerships == 100%
		//								-  save with new Date
		
		return true;
	}
//
//
//	@Override
//	public boolean assignBuildingOwnerToBuilding(BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto,
//			UnitDto unitDto) {
//		// TODO Auto-generated method stub
//		return false;
//	}




	
	 
}
