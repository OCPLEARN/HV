package de.ocplearn.hv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.model.Address;

@Mapper(componentModel= "spring")
public interface AddressMapper {
	
	AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);
	
	AddressDto addressToAddressDto(Address address);
	
	Address addressDtoToAddress(AddressDto addressDto);
	
	

}
