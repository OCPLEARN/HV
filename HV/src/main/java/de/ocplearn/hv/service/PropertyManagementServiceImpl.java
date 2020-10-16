package de.ocplearn.hv.service;

import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.model.Contact;
import de.ocplearn.hv.model.LoginUser;

public class PropertyManagementServiceImpl implements PropertyManagementService {

	@Override
	public boolean createPropertyManagement(LoginUser primaryLoginUser, Contact primaryContact, String paymentType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updatePropertyManagement(PropertyManagementDto propertyManagementDto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deletePropertyManagement(int id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public PropertyManagementDto findPropertyManagement(LoginUser primaryLoginUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyManagementDto findPropertyManagement(Contact primaryContact) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyManagementDto findPropertyManagement(String paymentType) {
		// TODO Auto-generated method stub
		return null;
	}

}
