package de.ocplearn.hv.dao;

import java.util.List;
import java.util.Optional;

import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PropertyManagement;

public interface PropertyManagementDao {
	
	public boolean save( PropertyManagement propertyManagement);
	
	public boolean delete( PropertyManagement propertyManagement );
	
	public Optional<PropertyManagement> findById( int id );
	
	public Optional<PropertyManagement> findByPrimaryContact( Contact primaryContact );
		
	public Optional<PropertyManagement> findByPrimaryLoginUserId( int id );
	
	public boolean addLoginUserToPropertyManagement(LoginUser loginUser,PropertyManagement propertyManagement);
	
	public boolean removeLoginUserFromPropertyManagement(LoginUser loginUser,PropertyManagement propertyManagement);
	
	public List<Integer> getLoginUsersByPropertyManagement(PropertyManagement propertyManagement);

	public List<PropertyManagement> findPropertyManagementByComanyName (String companyName);
	
	
}
