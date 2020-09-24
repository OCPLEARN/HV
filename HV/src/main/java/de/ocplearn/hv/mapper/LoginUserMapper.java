package de.ocplearn.hv.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.model.LoginUser;

@Mapper(componentModel = "spring")
public interface LoginUserMapper {

	LoginUserMapper INSTANCE = Mappers.getMapper(LoginUserMapper.class);
	
	LoginUserDto loginUserToLoginUserDto( LoginUser loginUser );
	
	LoginUser loginUserDtoToLoginUser( LoginUserDto loginUserDto );
}
