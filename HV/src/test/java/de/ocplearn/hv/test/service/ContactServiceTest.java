package de.ocplearn.hv.test.service;

import java.util.List;

import javax.swing.text.TabExpander;
import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.AfterAll;
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
import de.ocplearn.hv.test.dao.LoginUserDaoTest;
import de.ocplearn.hv.util.TablePageViewData;

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
		//Assertions.assertTrue(contactService.createContact(testContact));
		
		LoginUserDto loginUserDto = new LoginUserDaoTest().getLoginUser();
		
		PropertyManagementDto propertyManagementDto= new PropertyManagementDto();
		propertyManagementDto.setPaymentType(PaymentType.FREE);
		propertyManagementDto.setPrimaryLoginUser(loginUserDto);
		propertyManagementDto.setPrimaryContact(testContact);
		System.out.println("testContact.getId() " + testContact.getId());
		propertyManagementDto.setCompanyContact(testContact);
		System.out.println("propertyManagementDto.getCompanyContact().getId() :" + propertyManagementDto.getCompanyContact().getId());

		Assertions.assertTrue(propertyManagementService.createPropertyManagement(propertyManagementDto));
			
	}
	
	@Test
	public void testUpdateContact() {
		ContactDto testContact = new ContactDto();
		testContact.setFirstName("UPDATE");
		testContact.setLastName("TEST");
		testContact.setSex("SEXTEST");
		testContact.setCompany(false);
		testContact.setEmail("UPDATETest@Testing.com");
		testContact.setWebsite("UPDATETest.com");
		testContact.setPhone("+49123456789");
		testContact.setMobilePhone("49123456789");
		testContact.setFax("49123456789");
		contactService.createContact(testContact);
		
		testContact.setFirstName("SUCCESSFUL");
		testContact.setLastName("UPDATETEST");
		contactService.updateContact(testContact);
		ContactDto updateTest = contactService.findContactById(testContact.getId());
		Assertions.assertEquals(testContact.getLastName(), updateTest.getLastName());
	}
	
	@Test
	public void testFindContactById() {
		ContactDto testContact = new ContactDto();
		testContact.setFirstName("FIND");
		testContact.setLastName("TEST");
		testContact.setSex("SEXTEST");
		testContact.setCompany(false);
		testContact.setEmail("FIND@Testing.com");
		testContact.setWebsite("UPDATETest.com");
		testContact.setPhone("+49123456789");
		testContact.setMobilePhone("49123456789");
		testContact.setFax("49123456789");
		contactService.createContact(testContact);
		int id = testContact.getId();
		ContactDto contactDto = contactService.findContactById(id);
		Assertions.assertTrue(contactDto.getFirstName().equals("FIND"));
	}
	
	@Test
	public void testDeleteContact() {
		
		ContactDto testContact = new ContactDto();
		testContact.setFirstName("UPDATE");
		testContact.setLastName("TEST");
		testContact.setSex("SEXTEST");
		testContact.setCompany(false);
		testContact.setEmail("UPDATETest@Testing.com");
		testContact.setWebsite("UPDATETest.com");
		testContact.setPhone("+49123456789");
		testContact.setMobilePhone("49123456789");
		testContact.setFax("49123456789");
		contactService.createContact(testContact);
		int id=testContact.getId();
		
		Assertions.assertTrue(contactService.deleteContactById(id));
		
		
	}
	
	@Test
	//@AfterAll
	public void testFindAllContacts() {
		TablePageViewData tablePageViewData = new TablePageViewData();
		tablePageViewData.setOffset(1);
		tablePageViewData.setOrderBy("lastName");
		tablePageViewData.setOrderByDirection("ASC");
		tablePageViewData.setRowCount(10);
		List <ContactDto> contactList = contactService.getAllContacts(tablePageViewData);
		System.out.println(contactList);
		System.out.println("HOW MANY: "+contactList.size());
		Assertions.assertTrue(contactList!= null);
		
	}
	
}
