package de.ocplearn.hv.dto;

import java.util.List;

import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Unit;

public class RenterDto {
	
	private int id;
	
	private ContactDto contactDto;
	
	private List<UnitDto> rentedUnitsDto;
	
	private LoginUserDto loginUserDto;

}
