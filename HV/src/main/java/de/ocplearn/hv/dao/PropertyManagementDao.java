package de.ocplearn.hv.dao;

import java.util.Optional;

import de.ocplearn.hv.model.PropertyManagement;

public interface PropertyManagementDao {
	
	public boolean save( PropertyManagement propertyManagement);
	
	public boolean delete( PropertyManagement propertyManagement );
	
	public Optional<PropertyManagement> findById( int id );
	
	public Optional<PropertyManagement> findByPrimaryContact( PropertyManagement propertyManagement );

}
