package de.ocplearn.hv.dao;

import java.util.List;
import java.util.Optional;

import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PropertyManagement;

public interface PropertyManagementDao {
	
	public boolean save( PropertyManagement propertyManagement);
	
	public boolean delete( PropertyManagement propertyManagement );
	
	public Optional<PropertyManagement> findById( int id );
	
	public Optional<PropertyManagement> findByPrimaryContact( PropertyManagement propertyManagement );
		
	public boolean addLoginUserToPropertyManagement(LoginUser loginUser,PropertyManagement propertyManagement);
	
	public boolean removeLoginUserFromPropertyManagement(LoginUser loginUser,PropertyManagement propertyManagement);
	
	public List<Integer> getLoginUsersByPropertyManagement(PropertyManagement propertyManagement);

	public List<PropertyManagement> findPropertyManagementByComanyName (String name);
}
