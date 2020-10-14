package de.ocplearn.hv.test.contact;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ResourceUtils;

import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.mapper.ContactMapper;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.Contact.ContactBuilder;
import de.ocplearn.hv.util.CountryList;

import org.json.JSONException;
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

	@Autowired
	ApplicationContext applicationContext;
	
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
				Assertions.assertFalse(jsonArray.isEmpty());
				
			} catch(IOException | ParseException e) {
				e.getStackTrace();
			} 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	
	
	@Test
	public void test_loadCountryList_countryListJson2() throws ParseException {
		Resource resource = resourceLoader.getResource("classpath:static/countrycodes/Country.json");
		File file = null;
		try {
			file = resource.getFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Set<String> countryNames = new TreeSet<>();
	
		try( BufferedReader bufferedReader = new BufferedReader( new FileReader(file) ); ){
			JSONParser parser = new JSONParser();
			org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) parser.parse(bufferedReader);
			
			Stream<org.json.simple.JSONObject> stream = null;
			
			stream = jsonArray.stream();
			
			countryNames =
			
					stream	.filter(t -> (boolean)t.get("isCountry"))
							.flatMap( jsonObject -> {
								String value = (String)jsonObject.get("countryName"); // [{"countryCode":4,"countryName":"Afghanistan","currencyName":"Afghani","isCountry":true},
								return Stream.of(value);
								} )
							.collect( Collectors.toSet() );
			
			//System.out.println( "count countries" + countryNames.size() );
			
		}catch( IOException e ) {
			e.printStackTrace();
		}
		Assertions.assertFalse( countryNames.contains("Europe") );
	}	
	
	
	//Zweiter Test mit org.json.JSONArray statt org.json.simple
//	@Test
//	public void test_loadCountryList_countryListJson3() {
//				
//		Resource resource = resourceLoader.getResource("classpath:static/countrycodes/Country.json");
//		File file = null;
//		try {
//			file = resource.getFile();
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
//		
//		// [{"countryCode":4,"countryName":"Afghanistan","currencyName":"Afghani","isCountry":true},
//		
//		Set<String> countryNames = new TreeSet<>(); 
//		
//		try( BufferedReader bufferedReader = new BufferedReader( new FileReader(file) ); ){
//			
//			StringBuilder sb = new StringBuilder();
//			String str = "";
//			while( ( str = bufferedReader.readLine() ) != null ) {
//				sb.append(str); 
//				System.out.println(str);
//			}
//			//sb.toString - hat das die Zeilentrenner noch?
//			System.out.println(sb.toString());
//			
//			//org.json.JSONArray ist NICHT org.json.simple
//			org.json.JSONArray jsonArray = null;
//			try {
//				jsonArray = new org.json.JSONArray( sb.toString() );
//			} catch (JSONException e1) {
//				e1.printStackTrace();
//			}
//			
//			int length = jsonArray.length();
//			for ( int i = 0; i < length; i++ ) {
//				try {
//					org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
//					countryNames.add(jsonObject.getString("countryName"));
//				} catch (JSONException e2) {
//					e2.printStackTrace();
//				}
//
//			}
//			
//		}catch( IOException e ) {
//			e.printStackTrace();
//		}
//			
//		System.out.println(countryNames);
//		
//
//	}	
	

}
