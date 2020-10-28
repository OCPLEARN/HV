package de.ocplearn.hv.test.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
public class PropertyManagementServiceTest {

	private PropertyManagementService propertyManagementService;
	
	
		
	
	@Autowired
	public PropertyManagementServiceTest( PropertyManagementService propertyManagementService ){
		this.propertyManagementService = propertyManagementService;
	}
	
	
	@Test
	public void testCreatePropertyManagement_givenDto_boolean() {
		
		PaymentType paymentType = PaymentType.STARTER;
		
		HashMap<String, byte[]> hashMap = StaticHelpers.createHash("Pa$$w0rd", null);
		
		Supplier<LoginUserDto> loginUserDtoSupplier = () -> {return new LoginUserDto("logUName", Role.PROPERTY_MANAGER, hashMap.get("hash") , hashMap.get("salt"), Locale.GERMANY);};
	
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
		
		PropertyManagementDto propertyManagementDto = new PropertyManagementDto(  loginUserDtoSupplier.get(), 
				  primaryContactDtoSupplier.get(),  paymentType, loginUserListDto.get(),  companyContactDtoSupplier.get());
			
		
		
		Assertions.assertTrue(propertyManagementService.createPropertyManagement(propertyManagementDto));
		
		
				
				
	}
	
	
	
	
}