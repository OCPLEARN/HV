package de.ocplearn.hv.test.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.service.ContactService;

//@SpringBootTest
public class ContactServiceTest {
	
	private ContactService contactService;
	
	//@Autowired
	public ContactServiceTest(ContactService contactService) {
		
	}

	//@Test
	public void testCreateContact() {
		ContactDto testContact = new ContactDto();
		testContact.setFirstName("Test");
	}
	
}
