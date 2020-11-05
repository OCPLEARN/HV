package de.ocplearn.hv.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.service.PropertyManagementService;

@SpringBootTest
public class TestObjectSupplier {
	
	private static final TestObjectSupplier INSTANCE;	
	
	private static final String COMPANY_NAME = "BU bizarre unique ltd";
	
	static { INSTANCE = new TestObjectSupplier();}
	
	
	public static TestObjectSupplier getInstance() { return INSTANCE;};
	
	private LoginUser loginUser;
	
	private Contact contact;
	
	private Address address;
	
	private PropertyManagement propertyManagement;
	
	@Autowired
	private PropertyManagementService propertyManagementService;


	private TestObjectSupplier() {		
	}
	
	public PropertyManagement getPropertyManagement() {
		return this.propertyManagement;
	}
	
	
	
	
	
	
	

}
