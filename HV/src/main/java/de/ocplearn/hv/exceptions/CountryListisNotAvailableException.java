package de.ocplearn.hv.exceptions;

public class CountryListisNotAvailableException extends RuntimeException{

	public CountryListisNotAvailableException() {
		super();
	}
	
	public CountryListisNotAvailableException(String msg) {
		super(msg);
	}
}
