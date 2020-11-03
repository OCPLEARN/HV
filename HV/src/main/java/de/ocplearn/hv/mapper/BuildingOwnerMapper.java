package de.ocplearn.hv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import de.ocplearn.hv.dto.BuildingDto;
import de.ocplearn.hv.dto.BuildingOwnerDto;
import de.ocplearn.hv.model.Building;
import de.ocplearn.hv.model.BuildingOwner;

@Mapper(componentModel= "spring")
public interface BuildingOwnerMapper {

	BuildingOwnerMapper INSTANCE = Mappers.getMapper(BuildingOwnerMapper.class);
	
	BuildingOwnerDto buildingOwnerToBuildingOwnerDto(BuildingOwner buildingOwner);
	
	BuildingOwner buildingOwnerDtoToBuildingOwner(BuildingOwnerDto buildingOwnerDto);
}
