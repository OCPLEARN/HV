package de.ocplearn.hv.service;

import java.util.List;
import java.util.Optional;

import de.ocplearn.hv.dto.BuildingDto;
import de.ocplearn.hv.dto.BuildingOwnerDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.dto.RenterDto;
import de.ocplearn.hv.dto.UnitDto;
import de.ocplearn.hv.model.PropertyManagement;


/**
 * Create, update, delete, read and finds PropertyManagements
 * and manages BuildingOwners and Units
 */
public interface PropertyManagementService {
	
	// PROPERTYMANAGEMENTS
	
	
	/**
	 * Creates a PropertyManagement Object and saves it to the DB
	 * 
	 * @param propertyManagementDto
	 * @return boolean true when successful
	 */
	public boolean createPropertyManagement( PropertyManagementDto propertyManagementDto );

	/** 
	 * Deletes a PropertyManagement Object from the DB
	 * 
	 * @param propertyManagementDto
	 * @return boolean true when successful
	 */
	public boolean deletePropertyManagement( PropertyManagementDto propertyManagementDto );
	
	/** 
	 * Updates a PropertyManagement Object and saves it to the DB
	 * 
	 * @param propertyManagementDto
	 * @return boolean true when successful
	 */
	public boolean updatePropertyManagement( PropertyManagementDto propertyManagementDto );
	
	/**
	 * Finds a PropertyManagement by Id and returns it
	 * 
	 * @param propertyManagementDto
	 * @return PropertyManagementDto if PropertyManagement exists in DB
	 */
	public PropertyManagementDto findPropertyManagementbyId( int id );
	
	/**
	 *  Finds a PropertyManagement by PrimaryContact and returns it
	 * 
	 * @param propertyManagementDto
	 * @return PropertyManagementDto if PropertyManagement exists in DB
	 */
	public PropertyManagementDto findPropertyManagementbyPrimaryContact( ContactDto contactDto );
	
	/**
	 * Find propertymnagent by primary login users name
	 * @param login user name
	 * @return PropertyManagementDto
	 * */
	public PropertyManagementDto findPropertyManagementbyPrimaryLoginUserName(String PrimaryLoginUserName);
	
	public List<Integer> getLoginUserIdsFromPropertyManagement(PropertyManagementDto propertyManagementDto);
	
	public List<PropertyManagementDto> findPropertyManagementbyCompanyName( String companyName );
	
	public boolean addLoginUserToPropertyManagement(LoginUserDto loginUserDto, PropertyManagementDto propertyManagementDto);
	
	public boolean removeLoginUserFromPropertyManagement(LoginUserDto loginUserDto,PropertyManagementDto propertyManagementDto);
	
	
	// BUILDINGS CRUD
	
	public boolean createBuilding(BuildingDto buildingDto);
	
	public boolean deleteBuildingById(int buildingDtoId);
	
	public boolean deleteBuilding(BuildingDto buildingDto);
	
	public boolean updateBuilding(BuildingDto buildingDto);
	
	public BuildingDto findBuildingById (int buildingId);
	
	// BUILDINGOWNERS CRUD
	
	public boolean createBuildingOwner(BuildingOwnerDto buildingOwnerDto);
	
	public boolean deleteBuildingOwnerById(int buildingOwnerDtoId);
	
	public boolean deleteBuildingOwner(BuildingOwnerDto buildingOwnerDto);
	
	public boolean updateBuildingOwner(BuildingOwnerDto buildingOwnerDto);
	
	public BuildingOwnerDto findBuildingOwnerById (int buildingOwnerId);
	
	
	
	// ASSIGN BUILDINGOWNERS TO BUILDING
	
	public boolean assignBuildingOwnerToBuilding(BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto);
	
	public boolean removeBuildingOwnerFromBuilding(BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto);
	
	public boolean assignAllUnitsToOneOwner(BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto);
	
	
	// UNITS CRUD
	
	public boolean createUnit(UnitDto unitDto);
	
	public boolean deleteUnit(UnitDto unitDto);
	
	public boolean updateUnit(UnitDto unitDto);
	
	public UnitDto findUnitById(int unitId);
	
	
	//  ASSIGN BUILDINGOWNERS TO UNIT

	public boolean assignUnitOwnerToUnit (BuildingOwnerDto buildingOwnerDto, UnitDto unitDto);
	
	public boolean removeUnitOwnerFromUnit (BuildingOwnerDto buildingOwnerDto, UnitDto unitDto);
	
	
	//  ASSIGN RENTER TO UNIT
	
	public boolean assignUnitRenterToUnit (RenterDto renterDto, UnitDto unitDto);
	
	public boolean removeRenterFromUnit (RenterDto renterDto, UnitDto unitDto);
	

	
	
	
	
	
}
