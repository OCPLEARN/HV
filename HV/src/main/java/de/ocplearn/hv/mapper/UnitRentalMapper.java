package de.ocplearn.hv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.UnitRentalDto;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.UnitRental;

@Mapper(componentModel= "spring")
public interface UnitRentalMapper {

	
	UnitRentalMapper INSTANCE = Mappers.getMapper(UnitRentalMapper.class);
	
	UnitRentalDto unitRentalToUnitRentalDto(UnitRental unitRental);
	
	UnitRental unitRentalDtoToUnitRental(UnitRentalDto unitRentalDto);
}
