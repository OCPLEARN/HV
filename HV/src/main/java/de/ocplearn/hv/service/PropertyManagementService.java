package de.ocplearn.hv.service;

import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;


/**
 * CRUD and Finder methods for PropertyManagement
 *
 */
public interface PropertyManagementService {
	
	// CRUD
	
	/** Creates a PropertyManagement and returns boolean true when successful
	 *  
	 * @param primaryLoginUser
	 * @param primaryContact
	 * @param paymentType
	 * @return boolean
	 */
	public boolean createPropertyManagement( LoginUser primaryLoginUser, Contact primaryContact, String paymentType );
	
	
	/** Updates an existing PropertyManagement and returns true when successful
	 * 
	 * @param propertyManagementDto
	 * @return boolean
	 */
	public boolean updatePropertyManagement( PropertyManagementDto propertyManagementDto );
	
	
	/** Deletes a PropertyManagement by parameter id and returns true when successful
	 * 
	 * @param id
	 * @return boolean
	 */
	public boolean deletePropertyManagement( int id );
	
	
	// Finder methods
	
	/** Finds PropertyManagement by PrimaryLoginUser
	 * 
	 * @param primaryLoginUser
	 * @return PropertyManagementDto when successful or null when not successful
	 */
	PropertyManagementDto findPropertyManagement( LoginUser primaryLoginUser );
	
	
	/** Finds PropertyManagement by PrimaryContact
	 * 
	 * @param primaryLoginUser
	 * @return PropertyManagementDto when successful or null when not successful
	 */
	PropertyManagementDto findPropertyManagement( Contact primaryContact );

	
	/** Finds PropertyManagement by PaymentType
	 * 
	 * @param primaryLoginUser
	 * @return PropertyManagementDto when successful or null when not successful
	 */
	PropertyManagementDto findPropertyManagement( String paymentType );
	
	

	
	
	
}
