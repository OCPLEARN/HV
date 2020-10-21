package de.ocplearn.hv.test.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.service.ContactService;
import de.ocplearn.hv.service.PropertyManagementService;
import de.ocplearn.hv.service.UserService;

@SpringBootTest
public class ContactServiceTest {
	
	private ContactService contactService;
	
	private UserService userService;
	
	private PropertyManagementService propertyManagementService;


	@Autowired
	public ContactServiceTest(ContactService contactService, UserService userService,
			PropertyManagementService propertyManagementService) {
		super();
		this.contactService = contactService;
		this.userService = userService;
		this.propertyManagementService = propertyManagementService;
	}

	@Test
	public void testCreateContact() {
		
//		LoginUserDto loginUserDto = userService.findUserById(1);
//		System.out.println(loginUserDto);
//		PropertyManagementDto propertyManagementDto= new PropertyManagementDto();
//		propertyManagementDto.setPaymentType(PaymentType.FREE);
//		propertyManagementDto.setPrimaryLoginUser(loginUserDto);
//		propertyManagementService.createPropertyManagement(propertyManagementDto);
//		System.out.println(propertyManagementDto);
		
		
		
		
		ContactDto testContact = new ContactDto();
		testContact.setFirstName("CREATE");
		testContact.setLastName("TEST");
		testContact.setSex("SEXTEST");
		testContact.setCompany(false);
		testContact.setEmail("createTest@Testing.com");
		testContact.setWebsite("createTest.com");
		testContact.setPhone("+49123456789");
		testContact.setMobilePhone("49123456789");
		testContact.setFax("49123456789");
		Assertions.assertTrue(contactService.createContact(testContact));
		System.out.println(testContact);
	
		
		
	}
	
}
