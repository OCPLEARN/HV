package de.ocplearn.hv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.BuildingDto;
import de.ocplearn.hv.model.Building;




@Mapper(componentModel= "spring")
public interface BuildingMapper {

	BuildingMapper INSTANCE = Mappers.getMapper(BuildingMapper.class);
	
	BuildingDto buildingToBuildingDto(Building building);
	
	Building buildingDtoToBuilding(BuildingDto buildingDto);
}
