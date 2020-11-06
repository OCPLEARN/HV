package de.ocplearn.hv.test;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Provide random parts for model entities
 */
public class PartsBox {
	
	private static final PartsBox INSTANCE = new PartsBox();
	
	private PartsBox() {
	}
	
	private Random random = new Random();
	
	private  String[] firstNames = { 	"Peter","Lisa","Steve","Tony","Monica",
			"Bruce","Eva","Bill","Rachel","Clark",
			"Frank","Helena","Rudy","Joey","Homer",
			"Bart","Cindy","Rita","Bert","Chandler",
			"Greta", "Lara", "Hilbert", "Martin", "Alan"};	
	
	private String[] apartmentParts = { 
			"room", "apartment", "place", "area", "place", "floor", "id", "box"
		};
	
	private String[] cityParts = { 
			"Berlin", "Frankfurt", "München", "Hamburg", "Dortmund", "Stuttgart",
			"Nürnberg", "Heidelberg", "Bruchsal", "Leipzig", "Dresden", "Halle"
		};	
	
	private String[] provincesDE = {
			"Berlin", "Baden Würtenberg", "Sachsen-Anhalt", "Bayern", "Hamburg"
			
	};
	
	private String[] streetsDE = {
			"Goethestrasse", "Ahornweg", "Zeppelinstrasse", "Feldweg", "Waldstrasse",
			"Schumannallee", "Bethovenplatz", "Siemensstrasse"
	};	
	
	private String[] countries = {
			"DE", "NL"
	};	
	
	/**
	 * Return the singleton instance of PartsBox
	 * @return PartsBox instance
	 * */
	public static PartsBox getInstance() {
		return INSTANCE;
	}	
	
	/**
	 * Returns a random apartment String
	 * 
	 * @return apartment
	 * */
	public Supplier<String> firstNameSupplier = () -> {
		return (this.firstNames[ random.nextInt(this.firstNames.length) ] );
	};		
	
	/**
	 * Returns a random apartment String
	 * 
	 * @return apartment
	 * */
	public Supplier<String> apartmentsSupplier = () -> {
		return (this.apartmentParts[ random.nextInt(this.apartmentParts.length) ] + " " + random.nextInt(99));
	};	
	
	/**
	 * Returns a random Street
	 * 
	 * @return street
	 * */
	public Supplier<String> streetSupplier = () -> {
		return streetsDE[ random.nextInt(streetsDE.length) ];
	};
	
	/**
	 * Returns random zip code
	 * 
	 * @return zip code
	 * */
	public Supplier<String> zipSupplier = () -> {
		return "" + random.nextInt(9999);
	};

	/**
	 * Returns a random city 
	 * 
	 * @return city
	 * */
	public Supplier<String> citySupplier = () -> {
		return this.cityParts[ random.nextInt(this.cityParts.length) ];
	};		
	
	/**
	 * Returns a random province
	 * 
	 * @return province
	 * */
	public Supplier<String> provinceSupplier = () -> {
		return this.provincesDE[ random.nextInt(this.provincesDE.length) ];
	};
	
	/**
	 * Returns a random country 2 letter code 
	 * 
	 * @return country code
	 * */
	public Supplier<String> countrySupplier = () -> {
		return this.countries[ random.nextInt(this.countries.length) ];
	};	
	
	/**
	 * Returns a boolean value randomly
	 * 
	 * @return true or false
	 * */
	public BooleanSupplier boolSupplier = random::nextBoolean; // () -> { return randaom.nextBoolean(); }
	
	/**
	 * Supplier for a Latitude 
	 * 
	 * https://www.maptools.com/tutorials/lat_lon/formats
	 * DDD.DDDDD°
	 * 32.30642° N 122.61458° W
	 * or +32.30642, -122.61458
	 * 
	 * @return latitude
	 * */
	public Supplier<Double> latitudeSupplier = () -> {
			int deg = random.nextInt(90);
			// flip the sign randomly
			deg = this.boolSupplier.getAsBoolean() ? deg : -deg;
			int degFraction = random.nextInt(9999);
			
			return Double.parseDouble( ("" + deg + "." + degFraction) );
	};
	
	/**
	 * Supplier for a Longitude 
	 * 
	 * https://www.maptools.com/tutorials/lat_lon/formats
	 * DDD.DDDDD°
	 * 32.30642° N 122.61458° W
	 * or +32.30642, -122.61458
	 * 
	 * @return longitude
	 * */
	public Supplier<Double> longitudeSupplier = () -> {
		int deg = random.nextInt(180);
		// flip the sign randomly
		deg = this.boolSupplier.getAsBoolean() ? deg : -deg;
		int degFraction = random.nextInt(9999);
		
		return Double.parseDouble( ("" + deg + "." + degFraction) );
	};	
	
	
	
}
