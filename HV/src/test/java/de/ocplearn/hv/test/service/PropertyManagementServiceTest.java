package de.ocplearn.hv.test.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.omg.CORBA.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.service.PropertyManagementService;
import de.ocplearn.hv.util.StaticHelpers;

/**
 * Tests for PropertyManagementService
 *
 */
@SpringBootTest
//Ordering TestMethods to use a created PropertyManagementDto from Test (1) to delete in Test (2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)		
public class PropertyManagementServiceTest {

	private PropertyManagementService propertyManagementService;
	
	private PropertyManagementDto propertyManagementDto;
		
	
	@Autowired
	public PropertyManagementServiceTest( PropertyManagementService propertyManagementService ){
		this.propertyManagementService = propertyManagementService;
	}
	
	
	
	@Test
	@Order(1)
	public void testCreatePropertyManagement_givenDto_boolean() {
		
		PaymentType paymentType = PaymentType.STARTER;
		
		HashMap<String, byte[]> hashMap = StaticHelpers.createHash("Pa$$w0rd", null);
		
		Supplier<LoginUserDto> loginUserDtoSupplier = () -> {return new LoginUserDto("logUName" + System.currentTimeMillis(), Role.PROPERTY_MANAGER, hashMap.get("hash") , hashMap.get("salt"), Locale.GERMANY);};
	
		Supplier<List<AddressDto>> listAddressDtoSupplier = ArrayList::new;
		
		Supplier<ContactDto>  primaryContactDtoSupplier = () -> {return new ContactDto.ContactBuilder()
																						.setcompanyName("ABC")
																						.setCompany(true)
																						.setEmail("email@email.de")
																						.setFirstName("Armin")
																						.setLastName("Rohde")
																						.setSex("M")
																						.setAddressList(listAddressDtoSupplier.get())
																						.build();};
			
		Supplier<ContactDto> companyContactDtoSupplier = primaryContactDtoSupplier;
		
		Supplier<List<LoginUserDto>> loginUserListDto = ArrayList::new;
		
		propertyManagementDto = new PropertyManagementDto(  loginUserDtoSupplier.get(), 
				  primaryContactDtoSupplier.get(),  paymentType, loginUserListDto.get(),  companyContactDtoSupplier.get());
	
	//	Test Order of TestMethods	
	//	System.out.println("1");

		
		Assertions.assertTrue(propertyManagementService.createPropertyManagement(propertyManagementDto));
		
				
	}
	
	
	// public boolean deletePropertyManagement( PropertyManagementDto propertyManagementDto );
	

	@Test
	@Order(2)
	public void testDeletePropertyManagement_givenPropertyManagementDto_boolean () {
		
		//  TDD Ansatz zum Erinnern für HR
		//	Test Order of TestMethods	
		//  System.out.println("2");
		
		// Deletes propertyManagementDto created in Test (1)
		Assertions.assertTrue( propertyManagementService.deletePropertyManagement( propertyManagementDto ) );
		
	}
	
	
	
	
}