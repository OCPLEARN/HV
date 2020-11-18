package de.ocplearn.hv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import de.ocplearn.hv.dto.PropertyManagementDto;
import de.ocplearn.hv.model.PropertyManagement;

@Mapper(componentModel = "spring")
public interface PropertyManagementMapper {
	
	PropertyManagementMapper INSTANCE = Mappers.getMapper(PropertyManagementMapper.class);
	
	PropertyManagement propertyManagementDtoToPropertyManagement( PropertyManagementDto propertyManagementDto );
	
	PropertyManagementDto propertyManagementToPropertyManagementDto (PropertyManagement propertyManagement);

}


// CarDto carDto = CarMapper.INSTANCE.carToCarDto( car );