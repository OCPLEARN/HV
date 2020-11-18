package de.ocplearn.hv.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.BuildingDto;
import de.ocplearn.hv.model.Building;




@Mapper(componentModel= "spring")
public interface BuildingMapper {

	BuildingMapper INSTANCE = Mappers.getMapper(BuildingMapper.class);
	
	//@Mapping( target = "owners", ignore = true)
	BuildingDto buildingToBuildingDto(Building building, @Context CycleAvoidingMappingContext context);
	
	//@Mapping( target = "owners", ignore = true)
	Building buildingDtoToBuilding(BuildingDto buildingDto, @Context CycleAvoidingMappingContext context);
}
