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
	
	// BUILDING finder
	
	public List<BuildingDto> findBuildingsByPropertyManagement (int propertyManagementId);
	
	// BUILDINGOWNERS CRUD
	
	/**
	 * Creates a BuildingOwner
	 * @param buildingOwnerDto
	 * @return true, on success
	 * */
	public boolean createBuildingOwner(BuildingOwnerDto buildingOwnerDto);
	
	/**
	 * Removes BuildingOwner by id
	 * @param int
	 * @return true, on success 
	 * */
	public boolean deleteBuildingOwnerById(int buildingOwnerDtoId);
	
	/**
	 * Removes BuildingOwner 
	 * @param buildingOwnerDto
	 * @return true, on success 
	 * */	
	public boolean deleteBuildingOwner(BuildingOwnerDto buildingOwnerDto);
	
	/**
	 * Saves BuildingOwner 
	 * @param buildingOwnerDto
	 * @return true, on success 
	 * */	
	public boolean updateBuildingOwner(BuildingOwnerDto buildingOwnerDto);
	
	/**
	 * Finds BuildingOwner by id
	 * @param int
	 * @return BuildingOwnerDto
	 * */
	public BuildingOwnerDto findBuildingOwnerById (int buildingOwnerId);
	
	public List <BuildingOwnerDto> findBuildingOwnersByBuildingId(int buildingId);
	
	
	// ASSIGN BUILDINGOWNERS TO BUILDING
	
	public boolean assignBuildingOwnerToBuilding(BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto);
	
	public boolean removeBuildingOwnerFromBuilding(BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto);
	
	public boolean setOwnership( BuildingOwnerDto buildingOwnerDto, BuildingDto buildingDto, UnitDto unitDto, double buildingShare );
	
	public boolean removeOwnership( BuildingOwnerDto buildingOwnerDto, UnitDto unitDto );

	
	/**
	 * when creating a new building - without existing owners in either units or building - this method can be used to assign all 
	 * units of this building to the BuildingOwner given as Parameter in this method. If any owners are already assigned to either a unit 
	 * or the building they will be deleted.
	 * 
	 * @param buildingOwnerDto
	 * @param buildingDto
	 * @return
	 */
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
	
	public boolean assignRenterToUnit (RenterDto renterDto, UnitDto unitDto);
	
	public boolean removeRenterFromUnit (RenterDto renterDto, UnitDto unitDto);
	
	/**
	 * creates or updates given renter
	 * @param RenterDto
	 * @return true on success
	 * */
	boolean saveRenter( RenterDto renterDto );
	
	
	
	
	
}
