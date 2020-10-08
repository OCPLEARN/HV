package de.ocplearn.hv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import de.ocplearn.hv.dto.ContactDto;
import de.ocplearn.hv.model.Contact;

@Mapper(componentModel = "spring")
public interface ContactMapper {

	ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);
	
	ContactDto contactToContactDto(Contact contact);
	
	Contact contactDtoToContact(ContactDto contactDto);
	
}
