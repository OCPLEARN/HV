package de.ocplearn.hv.dao;

import java.util.List;
import java.util.Optional;

import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.PropertyManagement;

/**
 * DAO for PropertyManagement
 *
 */
public interface PropertyManagementDao {
	
	
	/** 
	 * Saves PropertyManagement and returns true when successful
	 * 
	 * @param propertyManagement
	 * @return boolean
	 */
	boolean save(PropertyManagement propertyManagement);
	
	
	/**
	 * Finds and deletes PropertyManagement by id and returns true when successful
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean delete(int id);

	
	/**
	 * Finds PropertyManagement by primary LoginUser
	 * 
	 * @param primaryLoginUser
	 * @return Optional<PropertyManagement>
	 */
	Optional<PropertyManagement> findPropertyManagementByPrimaryLoginUser( LoginUser primaryLoginUser );
	
	
	/**
	 * Finds PropertyManagement by primary Contact
	 * 
	 * @param primaryContact
	 * @return Optional<PropertyManagement>
	 */
	PropertyManagement findPropertyManagementByPrimaryContact( LoginUser primaryContact );
	
	
	/**
	 * Finds PropertyManagement by LoginUser
	 * 
	 * @param LoginUser
	 * @return Optional<PropertyManagement>
	 */
	Optional<PropertyManagement> findPropertyManagementByLoginUser( LoginUser loginUser );
	
	
	
	/**
	 * Finds all PropertyManagement of specified PaymentType
	 * @param paymentType
	 * @return List<PropertyManagement>
	 */
	List<PropertyManagement> findPropertyManagementByPaymentType( PaymentType paymentType );
	
	
	
	
	
}
