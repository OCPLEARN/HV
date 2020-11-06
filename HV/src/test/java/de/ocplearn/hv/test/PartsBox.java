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
	
	/**
	 * Return the singleton instance of PartsBox
	 * @return PartsBox instance
	 * */
	public static PartsBox getInstance() {
		return INSTANCE;
	}
	
	private String[] streetsDE = {
			"Goethestrasse", "Ahornweg", "Zeppelinstrasse", "Feldweg", "Waldstrasse",
			"Schumannallee", "Bethovenplatz", "Siemensstrasse"
	};	
	
	private String[] countries = {
			"DE", "NL"
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
