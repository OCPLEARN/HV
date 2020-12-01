package de.ocplearn.hv.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import de.ocplearn.hv.exceptions.CountryListisNotAvailableException;


//JSON Data Sheet Structure
//{"countryCode":4,"countryName":"Afghanistan","currencyName":"Afghani","isCountry":true}


@Component("CountrylistJSON")
public class CountryList {
	
	//private ResourceLoader resourceLoader;
	private Set<String> countryNames;
	
	// @Autowired ResourceLoader resourceLoader
	public CountryList(){
		//this.resourceLoader = resourceLoader;
		loadCountryListJSON();
	}
	
//		Variante Countrylist nicht aus Json Datei sindern aus Locales
//	Locale[] locales = Locale.getAvailableLocales();
//	for (int i = 0; i < locales.length; i++) {
//		System.out.println(locales[i].getDisplayCountry());
//	}
	
	public void loadCountryListJSON() {
		
		// https://stackoverflow.com/questions/25869428/classpath-resource-not-found-when-running-as-jar
		
		// resource.getFile() expects the resource itself to be available on the file system,
		// i.e. it can't be nested inside a jar file. 
		
		
		ClassPathResource classPathResource = new ClassPathResource("static/countrycodes/Country.json");
		
		//Resource resource = resourceLoader.getResource("classpath:static/countrycodes/Country.json");
		// File file = null;
		try ( BufferedReader reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()));) {
			
//			file = resource.getFile();
//			System.err.println("loadCountryListJSON() " + file.getAbsolutePath());
//			if ( file == null ) {
//				System.err.println("loadCountryListJSON() is null ");
//			}
			//file = ResourceUtils.getFile("classpath:static/countrycodes/Country.json");
			
			Set<String> countryNames = new TreeSet<>();
			
			JSONParser parser = new JSONParser();
			
			org.json.simple.JSONArray jsonArray = (org.json.simple.JSONArray) parser.parse(reader);
			
			Stream<org.json.simple.JSONObject> stream = null;
			
			stream = jsonArray.stream();
			
			countryNames =
			
					stream	.filter(t -> (boolean)t.get("isCountry"))
							.flatMap( jsonObject -> {
								String value = (String)jsonObject.get("countryName"); // [{"countryCode":4,"countryName":"Afghanistan","currencyName":"Afghani","isCountry":true},
								return Stream.of(value);
								} )
							.collect( Collectors.toSet() );			
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			throw new CountryListisNotAvailableException("countryListIsNotAvailable");
		}

	}
	
	

	public Set<String> getCountryNames() {
		return countryNames;
	}
	
	
	
		
}
	

