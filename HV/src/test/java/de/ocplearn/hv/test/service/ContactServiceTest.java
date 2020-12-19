package de.ocplearn.hv.test.service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.text.TabExpander;
import javax.validation.constraints.AssertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.model.AddressType;
import de.ocplearn.hv.model.PaymentType;
import de.ocplearn.hv.model.PropertyManagement;
import de.ocplearn.hv.service.ContactService;
import de.ocplearn.hv.service.PropertyManagementService;
import de.ocplearn.hv.service.UserService;
import de.ocplearn.hv.test.dao.AddressDaoTest;
import de.ocplearn.hv.test.dao.LoginUserDaoTest;
import de.ocplearn.hv.util.TablePageViewData;

@SpringBootTest
public class ContactServiceTest {
	
	private ContactService contactService;
	
	private UserService userService;
	
	
	private PropertyManagementService propertyManagementService;

	private Supplier<ContactDto> contactDtoSupplier = () ->{ 
		return new ContactDto.ContactBuilder()
				.setCompany(true)
				.setcompanyName("ABB")
				.setEmail("AB@ABB.de")
				.setFax("06221 12345678")
				.setFirstName("Anton")
				.setLastName("Berta")
				.setMobilePhone("0172 12345678")
				.setAddressList(Arrays.asList(AddressDaoTest.testAddressDtoSupplier.get()))
				.build();};

	private Supplier<AddressDto> addressDtoSupplier = () -> { return new AddressDto("HindenburgStrasse", "12", "a12","Malsch","75234","Baden Württemberg","GER", 48.891948, 8.332487, AddressType.PRIMARY_PRIVATE_ADDRESS );};			
				
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
		testContact.setAddresses(PropertyManagementServiceTest.listAddressDtoSupplierWithData.get());
		//Assertions.assertTrue(contactService.createContact(testContact));
		
		LoginUserDto loginUserDto = new LoginUserDaoTest().getLoginUser();
		
		PropertyManagementDto propertyManagementDto= new PropertyManagementDto();
		propertyManagementDto.setPaymentType(PaymentType.FREE);
		propertyManagementDto.setPrimaryLoginUser(loginUserDto);
		propertyManagementDto.setPrimaryContact(testContact);
		//System.out.println("testContact.getId() " + testContact.getId());
		propertyManagementDto.setCompanyContact(testContact);
		//System.out.println("propertyManagementDto.getCompanyContact().getId() :" + propertyManagementDto.getCompanyContact().getId());

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
		testContact.setAddresses(PropertyManagementServiceTest.listAddressDtoSupplierWithData.get());
		
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
		testContact.setAddresses(PropertyManagementServiceTest.listAddressDtoSupplierWithData.get());
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
		testContact.setAddresses(PropertyManagementServiceTest.listAddressDtoSupplierWithData.get());
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
		//System.out.println(contactList);
		//System.out.println("HOW MANY: "+contactList.size());
		Assertions.assertTrue(contactList!= null);
		
	}
	
	@Test
	public void testCreateContact_assignAddressesToContact_boolean () {
		
		// create new ContactDto and create and assign new PRIMARY_BUSINESS_ADDRESS
		ContactDto contactDto = contactDtoSupplier.get();
		Assertions.assertTrue(contactService.createContact(contactDto));
		// create new Address and save to DB
		AddressDto addressDto =  addressDtoSupplier.get();
		Assertions.assertTrue(contactService.createAddress(addressDto));
		// assign new address to ContactDto 
		Assertions.assertTrue(contactService.addAddressToContact(contactDto.getId(), addressDto));
		//next Step Test if two different Addressobjects exist and are from different AddressType
		
	}
	
	@Test
	public void testDeleteContact_givenContactWithAdresses_boolean () {
		
		ContactDto contactDto = contactDtoSupplier.get();
		Assertions.assertTrue(contactService.createContact(contactDto));
		
		Assertions.assertTrue( ! contactDto.getAddresses().isEmpty());
		
		Assertions.assertTrue( contactService.deleteContact(contactDto));
		
	}
	
	
	
	
	
	
	
	
}
