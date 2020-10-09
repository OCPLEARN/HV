package de.ocplearn.hv.test.contact;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.mapper.ContactMapper;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.Contact.ContactBuilder;


@SpringBootTest
public class ContactTest {
	
	@Autowired
	ContactMapper contactMapper; 
	
	//wenn ContactMapper das ComponentModel "spring" hat, kann Autogewired werden
	//ContactMapper contactMapper = ContactMapper.INSTANCE;

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

}
