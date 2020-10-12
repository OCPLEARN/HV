package de.ocplearn.hv.model;


public class Address implements Comparable<Address> {
	
	private int id;
	private String street;
	private int houseNumber;
	private String apartment;
	private String city;
	private String zipCode;
	private String province;
	private String country; //Country.Json in static/countrycodes from: https://unstats.un.org/unsd/amaapi/api/Country
	
	//latitude longitude double mit 6 Nachkommastellen
	//1.Latitude (Norden +, Süden -) 2. Longitude (Osten +, Westen -), Datumsgrenze 180 bzw. -180
	private double latitude;
	private double longitude;

	public Address() {
		super();
	}
	public Address(String street, int houseNumber, String apartment, String city, String province, String country,
			double latitude, double longitude) {
		super();
		this.street = street;
		this.houseNumber = houseNumber;
		this.apartment = apartment;
		this.city = city;
		this.province = province;
		this.country = country;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
		
	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}
	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	/**
	 * @return the houseNumber
	 */
	public int getHouseNumber() {
		return houseNumber;
	}
	/**
	 * @param houseNumber the houseNumber to set
	 */
	public void setHouseNumber(int houseNumber) {
		this.houseNumber = houseNumber;
	}
	/**
	 * @return the apartment
	 */
	public String getApartment() {
		return apartment;
	}
	/**
	 * @param apartment the apartment to set
	 */
	public void setApartment(String apartment) {
		this.apartment = apartment;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}


	@Override
	public String toString() {
		return "Address [street=" + street + ", houseNumber=" + houseNumber + ", apartment=" + apartment + ", city="
				+ city + ", province=" + province + ", country=" + country + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + houseNumber;
		result = prime * result + id;
		result = prime * result + ((street == null) ? 0 : street.hashCode());
		result = prime * result + ((apartment == null) ? 0 : apartment.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (houseNumber != other.houseNumber)
			return false;
		if (id != other.id)
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		return true;
	}
	

	@Override
	public int compareTo(Address o) {
		if (this.getCountry().equalsIgnoreCase(o.getCountry())) {
			if(this.getCity().equalsIgnoreCase(o.getCity())) {
				if (this.getStreet().equalsIgnoreCase(o.getStreet())) {
					if(this.getHouseNumber()==o.getHouseNumber()) {
						if(this.getApartment()!=null&&o.getApartment()!=null) {
							return (this.getApartment().compareTo(o.getApartment()));
						}else {
							return (this.getId()-o.getId());
						}
					}else {
						return (this.getHouseNumber()-o.getHouseNumber());
					}
				}else {
					return (this.getStreet().compareToIgnoreCase(o.getStreet()));
				}
			}else {
				return (this.getCity().compareToIgnoreCase(o.getCity()));
			}
		}else {
			return (this.getCountry().compareToIgnoreCase(o.getCountry()));
		}
	}
	
}
