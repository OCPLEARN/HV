package de.ocplearn.hv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import de.ocplearn.hv.dto.AddressDto;
import de.ocplearn.hv.dto.OwnershipDto;
import de.ocplearn.hv.model.Address;
import de.ocplearn.hv.model.Ownership;

@Mapper(componentModel= "spring")
public interface OwnershipMapper {
	
	OwnershipMapper INSTANCE = Mappers.getMapper(OwnershipMapper.class);
	
	OwnershipDto ownershipToOwnershipDto (Ownership ownership);
	
	Ownership ownershipDtoToOwnership(OwnershipDto ownershipDto);
	
	

}
