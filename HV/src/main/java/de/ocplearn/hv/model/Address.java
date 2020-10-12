package de.ocplearn.hv.model;

import java.util.Locale;

public class Address {
	
	private String street;
	private int houseNumber;
	private String apartment;
	private String city;
	private String province;
	//Country.Json in static/countrycodes from 
	//https://unstats.un.org/unsd/amaapi/api/Country
	private String country;
	
	//latitude longitude
	//double mit 6 Nachkommastellen
	//1.Latitude 2. Longitude, 
	//Norden +, Süden -
	//Osten +, Westen -
	//Datumsgrenze 180
	
	private double latitude;
	private double longitude;

	
}
