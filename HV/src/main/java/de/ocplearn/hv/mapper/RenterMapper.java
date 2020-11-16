package de.ocplearn.hv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import de.ocplearn.hv.dto.RenterDto;
import de.ocplearn.hv.model.Renter;

@Mapper(componentModel = "spring")
public interface RenterMapper {

	RenterMapper INSTANCE = Mappers.getMapper(RenterMapper.class);
	
	RenterDto RenterToRenterDto(Renter renter);
	
	Renter RenterDtoToRenter(RenterDto renterDto);
	
}
