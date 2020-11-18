package de.ocplearn.hv.dto;

import de.ocplearn.hv.model.AddressType;
import lombok.Builder;


public class AddressDto implements Comparable<AddressDto> {

	
			
		private int id;
		private String street;
		private String houseNumber;
		private String apartment;
		private String city;
		private String zipCode;
		private String province;
		private String country; //Country.Json in static/countrycodes from: https://unstats.un.org/unsd/amaapi/api/Country
		private AddressType addressType;
		//latitude longitude double mit 6 Nachkommastellen
		//1.Latitude (Norden +, Süden -) 2. Longitude (Osten +, Westen -), Datumsgrenze 180 bzw. -180
		private double latitude;
		private double longitude;

		public AddressDto() {
			super();
		}
		public AddressDto(String street, String houseNumber, String apartment, String city,String zipCode, String province, String country,
				double latitude, double longitude, AddressType addressType) {
			super();
			this.street = street;
			this.houseNumber = houseNumber;
			this.apartment = apartment;
			this.city = city;
			this.zipCode = zipCode;
			this.province = province;
			this.country = country;
			this.latitude = latitude;
			this.longitude = longitude;
			this.addressType = addressType;
		}
		
		// Copy Constructor === DANGER ===
		// May only be used for simple Objects without id references from DB
		// Only for first construction of object
		// when references in original object are altered - references of the newly created object are altered, too.
		public AddressDto(AddressDto addressDto) {
			this(addressDto.street, addressDto.houseNumber, addressDto.apartment,addressDto.city,  addressDto.zipCode, addressDto.province, addressDto.country, addressDto.latitude, addressDto.longitude, addressDto.addressType );			
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
		public String getHouseNumber() {
			return houseNumber;
		}
		/**
		 * @param houseNumber the houseNumber to set
		 */
		public void setHouseNumber(String houseNumber) {
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

		/**
		 * @return the zipCode
		 */
		public String getZipCode() {
			return zipCode;
		}
		/**
		 * @param zipCode 
		 */
		public void setZipCode(String zipCode) {
			this.zipCode = zipCode;
		}
		public AddressType getAddressType() {
			return addressType;
		}
		public void setAddressType(AddressType addressType) {
			this.addressType = addressType;
		}
		@Override
		public String toString() {
			return "Address [id ="+id+" street=" + street + ", houseNumber=" + houseNumber + ", apartment=" + apartment + ", city="
					+ city + ", province=" + province + ", country=" + country + ", latitude=" + latitude + ", longitude="
					+ longitude + ", AddressType=" + addressType +  "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((city == null) ? 0 : city.hashCode());
			result = prime * result + ((country == null) ? 0 : country.hashCode());
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
			AddressDto other = (AddressDto) obj;
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
			} else if (!street.equals(other.street)) {
				return false;
			} else if (!addressType.equals(other.addressType))
				return false;
			return true;
		}
		

		@Override
		public int compareTo(AddressDto o) {
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
							return (this.getHouseNumber().compareTo(o.getHouseNumber()));
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

	
	
	

