package de.ocplearn.hv.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ocplearn.hv.dao.BuildingDao;
import de.ocplearn.hv.dao.BuildingOwnerDao;
import de.ocplearn.hv.dao.PropertyManagementDao;
import de.ocplearn.hv.dao.UnitDao;
import de.ocplearn.hv.dto.BuildingDto;
import de.ocplearn.hv.dto.BuildingOwnerDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.dto.RenterDto;
import de.ocplearn.hv.dto.UnitDto;
import de.ocplearn.hv.mapper.BuildingMapper;
import de.ocplearn.hv.mapper.BuildingOwnerMapper;
import de.ocplearn.hv.mapper.ContactMapper;
import de.ocplearn.hv.mapper.LoginUserMapper;
import de.ocplearn.hv.mapper.PropertyManagementMapper;
import de.ocplearn.hv.mapper.UnitMapper;
import de.ocplearn.hv.model.BuildingOwner;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.model.Unit;


@Service
public class PropertyManagementServiceImpl implements PropertyManagementService {

	private PropertyManagementDao propertyManagementDao;
	
	private PropertyManagementMapper propertyManagementMapper;
	
	private LoginUserMapper loginUserMapper;
	
	private UserService userService;
	
	private ContactService contactService; 
	
	private ContactMapper contactMapper;
	
	private BuildingDao buildingDao;
	
	private BuildingOwnerDao buildingOwnerDao;
	
	private BuildingMapper buildingMapper;
	
	private BuildingOwnerMapper buildingOwnerMapper;
	
	private UnitDao unitDao;
	
	private UnitMapper unitMapper;
		
	@Autowired
	public PropertyManagementServiceImpl ( 	PropertyManagementDao propertyManagementDao,
											PropertyManagementMapper propertyManagementMapper,
											UserService userService,
											ContactService contactService,
											LoginUserMapper loginUserMapper,
											ContactMapper contactMapper,
											BuildingDao buildingDao,
											BuildingMapper buildingMapper,
											BuildingOwnerMapper buildingOwnerMapper,
											BuildingOwnerDao buildingOwnerDao,
											UnitDao unitDao,
											UnitMapper unitMapper
											
										) {
		
		this.propertyManagementDao = propertyManagementDao;
		this.propertyManagementMapper = propertyManagementMapper;
		this.userService = userService;
		this.contactService = contactService;
		this.loginUserMapper=loginUserMapper;
		this.contactMapper = contactMapper;
		this.buildingDao = buildingDao;
		this.buildingMapper = buildingMapper;
		this.buildingOwnerMapper = buildingOwnerMapper;
		this.buildingOwnerDao = buildingOwnerDao;
		this.unitDao = unitDao;
		this.unitMapper = unitMapper;
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
		return buildingDao.save(buildingMapper.buildingDtoToBuilding(buildingDto));
	}


	@Override
	public boolean deleteBuildingById(int buildingDtoId) {
		 return deleteBuilding( this.buildingMapper.buildingToBuildingDto( (buildingDao.findByIdFull(buildingDtoId).get()) ) );
	}

	@Override
	public boolean deleteBuilding(BuildingDto buildingDto) {
		//TODO: check if building/unit is currently in use in any active rental contracts
		if (findBuildingById(buildingDto.getId())!=null) {
				if(contactService.deleteAddress(buildingDto.getAddress())) {
					//TODO: delete UNITS from this Building	deleteUnit()
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
		return buildingDao.save(buildingMapper.buildingDtoToBuilding(buildingDto));
	}


	@Override
	public boolean assignBuildingOwnerToBuilding(BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto) {
		return buildingDao.addBuildingOwnerToBuilding(buildingOwnerMapper.buildingOwnerDtoToBuildingOwner(buildingOwnerDto), buildingMapper.buildingDtoToBuilding(buildingDto));
	}


	@Override
	public boolean removeBuildingOwnerFromBuilding(BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto) {
		return buildingDao.removeBuildingOwnerFromBuilding(buildingOwnerMapper.buildingOwnerDtoToBuildingOwner(buildingOwnerDto), buildingMapper.buildingDtoToBuilding(buildingDto));
	}


	@Override
	public boolean assignAllUnitsToOneOwner(BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean createUnit(UnitDto unitDto) {
		System.out.println("public boolean createUnit(UnitDto unitDto)" + unitDto);
		Unit unit = unitMapper.unitDtoToUnit(unitDto);
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
		return this.unitDao.delete(this.unitMapper.unitDtoToUnit(unitDto));
	}


	@Override
	public boolean updateUnit(UnitDto unitDto) {
		return this.unitDao.save(this.unitMapper.unitDtoToUnit(unitDto));
	}


	@Override
	public boolean assignUnitOwnerToUnit(BuildingOwnerDto buildingOwnerDto, UnitDto unitDto) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean removeUnitOwnerFromUnit(BuildingOwnerDto buildingOwnerDto, UnitDto unitDto) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean assignUnitRenterToUnit(RenterDto renterDto, UnitDto unitDto) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean removeRenterFromUnit(RenterDto renterDto, UnitDto unitDto) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public BuildingDto findBuildingById(int buildingId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BuildingDto> findBuildingsByPropertyManagement(int propertyManagementId) {
		return this.buildingDao.findBuildingsByPropertyManagement(propertyManagementId)
				.stream()
				.map(building -> this.buildingMapper.buildingToBuildingDto(building))
				.collect(Collectors.toList());
	}	

	@Override
	public boolean createBuildingOwner(BuildingOwnerDto buildingOwnerDto) {
		return this.buildingOwnerDao.save( this.buildingOwnerMapper.buildingOwnerDtoToBuildingOwner(buildingOwnerDto) );
	}


	@Override
	public boolean deleteBuildingOwnerById(int buildingOwnerDtoId) {
		return this.buildingOwnerDao.delete(buildingOwnerDtoId);
	}


	@Override
	public boolean deleteBuildingOwner(BuildingOwnerDto buildingOwnerDto) {
		return this.buildingOwnerDao.delete( this.buildingOwnerMapper.buildingOwnerDtoToBuildingOwner(buildingOwnerDto) );
	}


	@Override
	public boolean updateBuildingOwner(BuildingOwnerDto buildingOwnerDto) {
		return this.buildingOwnerDao.save( this.buildingOwnerMapper.buildingOwnerDtoToBuildingOwner(buildingOwnerDto) );
	}


	@Override
	public BuildingOwnerDto findBuildingOwnerById(int buildingOwnerId) {
		Optional<BuildingOwner> opt = this.buildingOwnerDao.findByIdFull(buildingOwnerId);
		return opt.isPresent() ? this.buildingOwnerMapper.buildingOwnerToBuildingOwnerDto(opt.get()) : null;
	}


	@Override
	public UnitDto findUnitById(int unitId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public PropertyManagementDto findPropertyManagementbyPrimaryLoginUserName(String PrimaryLoginUserName) {
		LoginUserDto loginUserDto = this.userService.findUserByLoginUserName(PrimaryLoginUserName);
		Optional<PropertyManagement> opt =  this.propertyManagementDao.findByPrimaryLoginUserId(loginUserDto.getId());
		if ( ! opt.isPresent() ) return null;
		return this.findPropertyManagementbyId( opt.get().getId() );
	}



	
	 
}
