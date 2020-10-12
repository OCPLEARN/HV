package de.ocplearn.hv.test.contact;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.mapper.ContactMapper;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.Contact.ContactBuilder;

import org.json.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
@SpringBootTest
public class ContactTest {
	

	//wenn ContactMapper das ComponentModel "spring" hat, kann Autogewired werden
	//ContactMapper contactMapper = ContactMapper.INSTANCE;
	
	@Autowired
	ContactMapper contactMapper; 	
	
	//Interface Resourceloader lädt Resource aus Verzeichnis mit relativem Pfad
	
	@Autowired
	ResourceLoader resourceLoader;

	@Test
	public void test_createContact_contactCreated() {

		Contact contact = new ContactBuilder().setEmail("test@gmail.com").setFirstName("Julian").setLastName("Test")
				.build();

		Assertions.assertTrue(contact.getFirstName().equals("Julian"));

	}

	@Test
	public void test_sortContact_contactsSorted() {
		Contact contact1 = new ContactBuilder().setEmail("apfel@gmail.com").setFirstName("Julian").setLastName("Test")
				.build();

		Contact contact2 = new ContactBuilder().setEmail("test@gmail.com").setFirstName("Julian").setLastName("Apfel")
				.build();

		List<Contact> contactList = new ArrayList<Contact>();
		contactList.add(contact1);
		contactList.add(contact2);
		contactList.sort(Comparator.naturalOrder());

		Assertions.assertTrue(contactList.get(0).getLastName().equals("Apfel"));

		contactList.sort(Comparator.reverseOrder());

		Assertions.assertTrue(contactList.get(0).getLastName().equals("Test"));
	}
	
	@Test
	public void test_mappedObject_equalValues() {
		
		//given
		Contact contact1 = new ContactBuilder().setEmail("apfel@gmail.com").setFirstName("Julian").setLastName("Test")
				.build();
		
		//when
		
	
		ContactDto contactDto = contactMapper.contactToContactDto(contact1);
		
		
		//then
		
		Assertions.assertTrue(contactDto.getFirstName().equals(contact1.getFirstName()));
		
	}
	
	

	
	@Test
	public void test_getResourceFromClasspath_Resource() throws IOException{
	
		Resource resource = resourceLoader.getResource("classpath:static/countrycodes/Country.json");
		File file = resource.getFile();
				
		Assertions.assertTrue(file.exists());
	}
	
	
	@Test
	public void test_loadCountryList_countryListJson() {
				
		Resource resource = resourceLoader.getResource("classpath:static/countrycodes/Country.json");
		File file;
		try {
			file = resource.getFile();
			
			try (FileReader fileReader = new FileReader(file);){
				JSONParser parser = new JSONParser();
				JSONArray jsonArray = (JSONArray) parser.parse(fileReader);
				System.out.println(jsonArray);
				Assertions.assertFalse(jsonArray.isEmpty());
				
			} catch(IOException | ParseException e) {
				e.getStackTrace();
			} 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		

	}
	
		
	
	

}
