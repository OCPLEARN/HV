package de.ocplearn.hv.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;

import org.mapstruct.factory.Mappers;

import de.ocplearn.hv.dto.BuildingOwnerDto;
import de.ocplearn.hv.dto.UnitDto;
import de.ocplearn.hv.model.BuildingOwner;
import de.ocplearn.hv.model.Unit;

@Mapper(componentModel = "spring")
public interface UnitMapper {

	UnitMapper INSTANCE = Mappers.getMapper(UnitMapper.class);
	
	UnitDto unitToUnitDto(Unit unit, @Context CycleAvoidingMappingContext context);
	
	Unit unitDtoToUnit(UnitDto unitDto, @Context CycleAvoidingMappingContext context);
}
