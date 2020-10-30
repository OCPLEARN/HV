package de.ocplearn.hv.service;

import java.util.List;
import java.util.Optional;

import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.model.PropertyManagement;

/**
 * Create, update, delete, read and finds PropertyManagements
 */

public interface PropertyManagementService {
	
	
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
	public PropertyManagementDto findPropertyManagementbyPrimaryContact( PropertyManagementDto propertyManagementDto );
	
	public List<Integer> getLoginUserIdsFromPropertyManagement(PropertyManagementDto propertyManagementDto);
	
	public boolean addLoginUserToPropertyManagement(LoginUserDto loginUserDto, PropertyManagementDto propertyManagementDto);
	
	public boolean removeLoginUserFromPropertyManagement(LoginUserDto loginUserDto,PropertyManagementDto propertyManagementDto);
	
	
}
