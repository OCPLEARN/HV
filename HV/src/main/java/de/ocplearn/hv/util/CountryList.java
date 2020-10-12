package de.ocplearn.hv.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;


@Component
public class CountryList {
	
	
	public CountryList(ResourceLoader resourceLoader){
		
		Resource resource = resourceLoader.getResource("classpath:static/countrycodes/Country.json");
		File file;
		try {
			file = resource.getFile();
			
			try (FileReader fileReader = new FileReader(file);){
				JSONParser parser = new JSONParser();
				JSONArray jsonArray = (JSONArray) parser.parse(fileReader);
				System.out.println(jsonArray);
				
				
				
			} catch(IOException | ParseException e) {
				e.getStackTrace();
			} 
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		
		
	}
		
	}
	

