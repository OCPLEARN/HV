package de.ocplearn.hv.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.ocplearn.hv.dao.PropertyManagementDao;
import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.model.PropertyManagement;


@Service
public class PropertyManagementServiceImpl implements PropertyManagementService {

	private PropertyManagementDao propertyManagementDao;
	
	private
	
	@Autowired
	public PropertyManagementServiceImpl ( PropertyManagementDao propertyManagementDao ) {
		this.propertyManagementDao = propertyManagementDao;
	}
	
	
	@Override
	public boolean createPropertyManagement(PropertyManagementDto propertyManagementDto) {
		
		PropertyManagement propertyManagement = propertyManagement
		propertyManagementDao.save(propertyManagement)
		return false;
	}

	@Override
	public boolean deletePropertyManagement(PropertyManagementDto propertyManagementDto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updatePropertyManagement(PropertyManagementDto propertyManagementDto) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Optional<PropertyManagementDto> findPropertyManagementbyId(PropertyManagementDto propertyManagementDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<PropertyManagementDto> findPropertyManagementbyPrimaryContact(
			PropertyManagementDto propertyManagementDto) {
		// TODO Auto-generated method stub
		return null;
	}

}
