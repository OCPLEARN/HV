package de.ocplearn.hv.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import de.ocplearn.hv.dto.BuildingDto;
import de.ocplearn.hv.dto.BuildingOwnerDto;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.BuildingOwner;

@Mapper(componentModel= "spring")
public interface BuildingOwnerMapper {

	BuildingOwnerMapper INSTANCE = Mappers.getMapper(BuildingOwnerMapper.class);
	
	//@Mapping( target = "buildings", ignore = true)
	BuildingOwnerDto buildingOwnerToBuildingOwnerDto(BuildingOwner buildingOwner, @Context CycleAvoidingMappingContext context);
	
	//@Mapping( target = "buildings", ignore = true)
	BuildingOwner buildingOwnerDtoToBuildingOwner(BuildingOwnerDto buildingOwnerDto, @Context CycleAvoidingMappingContext context);
}
