package de.ocplearn.hv.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


//JSON Data Sheet Structure
//{"countryCode":4,"countryName":"Afghanistan","currencyName":"Afghani","isCountry":true}


@Component("CountrylistJSON")
public class CountryList {
	
	private ResourceLoader resourceLoader;
	private static int loadCount;
	
	public CountryList(ResourceLoader resourceLoader){
		
		this.resourceLoader = resourceLoader;
		loadCountryListJSON();
	}
	//@Bean(name = "loadCountryListJSON")
	public void loadCountryListJSON() {
		Resource resource = this.resourceLoader.getResource("classpath:static/countrycodes/Country.json");
		File file;
		try {
			file = resource.getFile();
			
			try (FileReader fileReader = new FileReader(file);){
				JSONParser parser = new JSONParser();
				JSONArray jsonArray = (JSONArray) parser.parse(fileReader);
		//		jsonArray.   stream().   .flatMap(t -> t.);	
				
		//	    String name = (String) jsonObject.get("name");
		
			//	String[]<JSONObject> 

				
//				Locale[] locales = Locale.getAvailableLocales();
//				for (int i = 0; i < locales.length; i++) {
//					System.out.println(locales[i].getDisplayCountry());
//				}
				
								
			} catch(IOException | ParseException e) {
				e.getStackTrace();
			} 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}	
		loadCount += 1;
	}
	
	public int getLoadCount() {
		return loadCount;
	}
	
	
		
}
	

