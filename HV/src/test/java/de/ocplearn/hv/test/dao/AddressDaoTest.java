/**
 * 
 */
package de.ocplearn.hv.test.dao;

import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dao.AddressDao;
import de.ocplearn.hv.dao.AddressDaoJdbc;
import de.ocplearn.hv.dao.LoginUserDao;
import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.mapper.AddressMapper;
import de.ocplearn.hv.mapper.LoginUserMapper;
import de.ocplearn.hv.model.Address;

/**
 * Test for the address dao interface
 *
 */
@SpringBootTest
public class AddressDaoTest {

	// id of test address
	private static int addr1Id;
	
	private Supplier<AddressDto> testAddressDtoSupplier = () -> {
		return new AddressDto("Poststraße", " 3", "Hauptbahnhof",
				"Frankfurt am Main","60329","Hessen","DE", 50.106825, 8.663707);
	};
	
	@Autowired
	@Qualifier("AddressDaoJdbc")	// LoginUserDaoJdbcTemplate LoginUserDaoJdbc LoginUserDaoInMemory
	private AddressDao addressDao;	
	
	@Autowired
	public AddressMapper addressMapper;		
	
    @org.junit.jupiter.api.BeforeAll
    public static void setUpClass() throws Exception {
    }
    
	@Test
	/**
	 * Test for boolean save( LoginUser loginUser );
	 * */
	public void testSaveAndUpdate_Address_ValidCreatedAndUpdated() {
		
		// prepare address
		// INSERT INTO address (id,street,houseNumber,adrline1,adrline2,city,zip,province,country,coordinate)
		// String street, int houseNumber, String apartment, String city,String zipCode, String province, String country,
		// double latitude, double longitude
//		AddressDto addr1 = new AddressDto("Poststraße", 3, "Hauptbahnhof",
//				"Frankfurt am Main","60329","Hessen","DE", 50.106825, 8.663707);
		
		AddressDto addr1 = this.testAddressDtoSupplier.get();
		
		// save 
		Address addr1Saved = addressDao.save( addressMapper.addressDtoToAddress(addr1) );	
		// assert is saved
		Assertions.assertTrue( addr1Saved.getId() > 0 );
		AddressDaoTest.addr1Id =  addr1Saved.getId();
		// System.out.println("new address id = " + addr1Id);
		
		// change values
		addr1Saved.setStreet("Europaplatz");
		addr1Saved.setHouseNumber("1");
		addr1Saved.setApartment("Hauptbahnhof");
		addr1Saved.setCity("Berlin");
		addr1Saved.setZipCode("10551");
		addr1Saved.setProvince("Berlin");
		addr1Saved.setLatitude(52.524700);
		addr1Saved.setLongitude(13.369231);
		
		// save changed address
		addr1Saved = addressDao.save( addr1Saved );	// no mapping here, using returned model type
		// assert same address changed
		Assertions.assertTrue( addr1Saved.getId() == AddressDaoTest.addr1Id );
		
		// get data row
		Optional<Address> opt = addressDao.findById(AddressDaoTest.addr1Id);
		Assertions.assertTrue(opt.isPresent());
		
		Address addressFound = opt.get();
		Assertions.assertEquals(52.524700, addressFound.getLatitude());
		Assertions.assertEquals(13.369231, addressFound.getLongitude());
		
	}    
	
	@Test
	/**
	 * Test for boolean save( LoginUser loginUser );
	 * */
	public void testDelete_Address_ValidDeleted() {
		
		if (AddressDaoTest.addr1Id == 0) {
			System.out.println("No address from previous test found");
			AddressDto addressDto = this.testAddressDtoSupplier.get();
			Address addressSaved = this.addressDao.save(this.addressMapper.addressDtoToAddress(addressDto));
			AddressDaoTest.addr1Id = addressSaved.getId();
		}
		
		Optional<Address> opt = this.addressDao.findById(AddressDaoTest.addr1Id);
		Assertions.assertTrue(opt.isPresent());
		Assertions.assertTrue( this.addressDao.delete( opt.get() ) );
		
	}
	
}
