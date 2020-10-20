package de.ocplearn.hv.test.service;

import org.springframework.beans.factory.annotation.Autowired;

import de.ocplearn.hv.model.PropertyManagement;

/**
 * Tests for PropertyManagementService
 *
 */
public class PropertyManagementServiceTest {

	private PropertyManagement propertyManagement;
	
	public PropertyManagementServiceTest() {}
	
	@Autowired
	public PropertyManagementServiceTest( PropertyManagement propertyManagement ){
		this.propertyManagement = propertyManagement;
	}
	

	
	
	
	
}