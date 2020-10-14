package de.ocplearn.hv.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.exceptions.CountryListisNotAvailableException;


//JSON Data Sheet Structure
//{"countryCode":4,"countryName":"Afghanistan","currencyName":"Afghani","isCountry":true}


@Component("CountrylistJSON")
public class CountryList {
	
	private ResourceLoader resourceLoader;
	private Set<String> countryNames;
	
	public CountryList(ResourceLoader resourceLoader){
		
		this.resourceLoader = resourceLoader;
		loadCountryListJSON();
	}

	
	
	
	
//		Variante Countrylist nicht aus Json Datei sindern aus Locales
//	Locale[] locales = Locale.getAvailableLocales();
//	for (int i = 0; i < locales.length; i++) {
//		System.out.println(locales[i].getDisplayCountry());
//	}
	
	
	
	
	
	
	public void loadCountryListJSON() {
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
						
		}catch( IOException | ParseException e ) {
			throw new CountryListisNotAvailableException("countryListIsNotAvailable");
		}
	}
	
	

	public Set<String> getCountryNames() {
		return countryNames;
	}
	
	
	
		
}
	

