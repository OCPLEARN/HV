package de.ocplearn.hv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "Spring")
public class PropertyManagementMapper {
	
	PropertyManagementMapper INSTANCE = Mappers.getMapper(PropertyManagementMapper.class);
	
	PropertyManagementMapper propertyManagementMapperDtoToPropertyManagementMapper( PropertyManagementMapper propertyManagementMapper );

}


// CarDto carDto = CarMapper.INSTANCE.carToCarDto( car );