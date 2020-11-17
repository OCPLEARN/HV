package de.ocplearn.hv.test.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import de.ocplearn.hv.service.UserService;
import de.ocplearn.hv.test.TestObjectSupplier;
import de.ocplearn.hv.test.dao.AddressDaoTest;
import de.ocplearn.hv.util.StaticHelpers;

/**
 * Tests for PropertyManagementService
 *
 */
@SpringBootTest
//Ordering TestMethods to use a created PropertyManagementDto from Test (1) to delete in Test (2)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)		
public class PropertyManagementServiceTest {

	@Autowired
	private TestObjectSupplier instance;
	
	private PropertyManagementService propertyManagementService;
	
	private UserService userService;
	
	private static PropertyManagementDto propertyManagementDto;
	
	public static HashMap<String, byte[]> hashMap = StaticHelpers.createHash("Pa$$w0rd", null);
	
	public static Supplier<List<AddressDto>> listAddressDtoSupplierWithData = () -> {
		return new ArrayList<AddressDto> (Arrays.asList( AddressDaoTest.testAddressDtoSupplier.get() )) ;};
		
	public static Supplier<LoginUserDto> loginUserDtoEmployeeSupplier = () -> {return new LoginUserDto("EmployeeTest" + System.currentTimeMillis(), Role.EMPLOYEE, hashMap.get("hash") , hashMap.get("salt"), Locale.GERMANY);};
	
	// needs to be static to hold the id of employee which is returned on saving / creating the dto
	public static LoginUserDto employee;// = loginUserDtoEmployeeSupplier.get();
	private static LoginUserDto employee2;// = loginUserDtoEmployeeSupplier.get();
	static {
		employee = loginUserDtoEmployeeSupplier.get();
		try {Thread.sleep(250);}
		catch( InterruptedException e ) {
			//
		}
		employee2 = loginUserDtoEmployeeSupplier.get();
	}
	
	public static Supplier<LoginUserDto> loginUserDtoSupplier = () -> {return new LoginUserDto("logUName" + System.currentTimeMillis(), Role.PROPERTY_MANAGER, hashMap.get("hash") , hashMap.get("salt"), Locale.GERMANY);};
		
	public static Supplier<ContactDto>  primaryContactDtoSupplier = () -> {return new ContactDto.ContactBuilder()
			.setcompanyName("ABC")
			.setCompany(true)
			.setEmail("email@email.de")
			.setFirstName("Armin")
			.setLastName("Rohde")
			.setSex("M")
			.setAddressList(listAddressDtoSupplierWithData.get())
			.build();};

	
	public static Supplier<ContactDto> companyContactDtoSupplier = primaryContactDtoSupplier;
	
	public static Supplier<List<LoginUserDto>> loginUserListDtoSupplier = ArrayList::new;
	
	
	@Autowired
	public PropertyManagementServiceTest( PropertyManagementService propertyManagementService,UserService userService ){
		this.propertyManagementService = propertyManagementService;
		this.userService=userService;
	}
	
		
		
		
		
		
	
	
	@Test
	@Order(1)
	public void testCreatePropertyManagement_givenDto_boolean() {
		
		PaymentType paymentType = PaymentType.STARTER;
		
		
		
		
			
		// AddressDto addressDto = AddressDaoTest.testAddressDtoSupplier.get();
		// Zwei Aufruf von Supplier<ContactDto> ergibt zwei  ContactDto - Objekte, 
		// die aber auf dasselbe Objekt auf dem HEap referenzieren (bezüglich der AdressList)
		// Die Folge: in der DB wird zweimal dasselbe Objekt mit derselben addressId gespeichert
		// Die Lösung: zwei einzelne Aufrufe des AdressList-Suppliers
		
		
		
				
		
		
		propertyManagementDto = new PropertyManagementDto(  loginUserDtoSupplier.get(), 
				  primaryContactDtoSupplier.get(),  paymentType, loginUserListDtoSupplier.get(),  companyContactDtoSupplier.get());
	
				
		
	//	Test Order of TestMethods	
	//	System.out.println("1 : create prop mgmt" + propertyManagementDto);

		
		Assertions.assertTrue(propertyManagementService.createPropertyManagement(propertyManagementDto));
		
		
				
	}
	
	
	// public boolean deletePropertyManagement( PropertyManagementDto propertyManagementDto );
	

	@Test
	@Order(2)
	public void testDeletePropertyManagement_givenPropertyManagementDto_boolean () {
		
		//  TDD Ansatz zum Erinnern für HR
		//	Test Order of TestMethods	
		//  System.out.println("2");
		
		// adds List of LoginUsers to
		
		
		// Deletes propertyManagementDto created in Test (1)
		Assertions.assertTrue( propertyManagementService.deletePropertyManagement( propertyManagementDto ) );
		System.out.println("TESTDELETE: "+propertyManagementDto);
		
	}
	
	@Test
	@Order(3)
	public void testUpdatePropertyManagament_givenUpdatedPropertyManagementDto_boolean() {
		System.out.println("Test 3");
		PaymentType paymentType = PaymentType.STARTER;
		
		HashMap<String, byte[]> hashMap = StaticHelpers.createHash("Pa$$w0rd", null);
		
		Supplier<LoginUserDto> loginUserDtoSupplier = () -> {return new LoginUserDto("UpdatePropMGMTTest" + System.currentTimeMillis(), Role.PROPERTY_MANAGER, hashMap.get("hash") , hashMap.get("salt"), Locale.GERMANY);};
				
		
		
		Supplier<ContactDto>  primaryContactDtoSupplier = () -> {return new ContactDto.ContactBuilder()
																						.setcompanyName("UpdatePropMGMTTest")
																						.setCompany(true)
																						.setEmail("email@email.de")
																						.setFirstName("Armin")
																						.setLastName("Rohde")
																						.setSex("M")
																						.setAddressList(listAddressDtoSupplierWithData.get())
																						.build();};
			
		Supplier<ContactDto> companyContactDtoSupplier = primaryContactDtoSupplier;
				
		Supplier<List<LoginUserDto>> loginUserListDto = ArrayList::new;
		
		propertyManagementDto = new PropertyManagementDto(  loginUserDtoSupplier.get(), 
				  primaryContactDtoSupplier.get(),  paymentType, loginUserListDto.get(),  companyContactDtoSupplier.get());
		
		Assertions.assertTrue(propertyManagementService.createPropertyManagement(propertyManagementDto));
		
		
		propertyManagementDto.setPaymentType(PaymentType.SUPER_PRO);
		Assertions.assertTrue(propertyManagementService.updatePropertyManagement(propertyManagementDto));
		Assertions.assertTrue(propertyManagementDto.getPaymentType().toString().equals(PaymentType.SUPER_PRO.toString()));
		System.out.println("TESTUPDATE: "+propertyManagementDto);
	}
	
	@Test
	@Order(4)
	public void testFindPropertyManagament_givenPropertyManagementDtoId_boolean(){
		System.out.println("Test 4");
		PropertyManagementDto findPropertyManagement = propertyManagementService.findPropertyManagementbyId(propertyManagementDto.getId());
		Assertions.assertTrue(findPropertyManagement.getId()!=0);
		System.out.println(findPropertyManagement);
		
	}
	
	@Test
	@Order(5)
	public void testAddLoginUserToPropertyMgmt_givenLoginUser_boolean() {
		System.out.println("Test 5");
		
		Assertions.assertTrue(userService.createUser(employee) );
		Assertions.assertTrue(userService.createUser(employee2) );		
			
		Assertions.assertTrue(propertyManagementService.addLoginUserToPropertyManagement(employee, propertyManagementDto));
		Assertions.assertTrue(propertyManagementService.addLoginUserToPropertyManagement(employee2, propertyManagementDto));
		
	}
	
	
	
	@Test
	@Order(6)
	public void testLoginUserListAvailable_givenPropertyMGMT_boolean() {
		System.out.println("Test 6");
		System.out.println("testLoginUserListAvailable_givenPropertyMGMT_boolean" + propertyManagementDto.getLoginUsers());
		
		Assertions.assertTrue(propertyManagementDto.getLoginUsers().size()>0);
		
	}
	
	@Test
	@Order(7)
	public void testRemoveLoginUserFromPropertyMgmt_givenLoginUser_boolean() {
		System.out.println("Test 7");
		 
		Assertions.assertTrue(propertyManagementService.removeLoginUserFromPropertyManagement(employee, propertyManagementDto));
		Assertions.assertTrue(propertyManagementService.removeLoginUserFromPropertyManagement(employee2, propertyManagementDto));
		
	}
	
	@Test
	@Order(8)	
	public void testGetModel_givenModelKey_PropertyMgmt() {
		//TestObjectSupplier instance = TestObjectSupplier.getInstance();
		PropertyManagementDto model1 =  instance.getModel("Model1");
		//System.out.println( "testGetModel_givenModelKey_PropertyMgmt()" );
		//System.out.println("\t " + model1.getPrimaryLoginUser());
		Assertions.assertTrue(model1.getPrimaryLoginUser().getLoginUserName().equals("userModel1"));
		
		
		
	}
	
	
}










