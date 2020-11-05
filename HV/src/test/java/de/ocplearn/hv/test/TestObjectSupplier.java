package de.ocplearn.hv.test;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.service.ContactService;
import de.ocplearn.hv.service.PropertyManagementService;
import de.ocplearn.hv.service.UserService;

@SpringBootTest
public class TestObjectSupplier {
	
	private static final TestObjectSupplier INSTANCE;	
		
	static { INSTANCE = new TestObjectSupplier();}
	
	public static TestObjectSupplier getInstance() { return INSTANCE;};
	
	
	private LoginUserDto loginUserDto;
	
	private ContactDto contactDto;
	
	private AddressDto addressDto;
	
	private PropertyManagementDto propertyManagementDto;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ContactService contactService;
	
	@Autowired
	private PropertyManagementService propertyManagementService;


	private TestObjectSupplier( LoginUserDto loginUserDto, ContactDto contactDto, AddressDto addressDto, PropertyManagementDto propertyManagementDto ) {	
		
	}
	
	
	
	
	


	
	
	
	
	
	
	
	
	
	
	

}
