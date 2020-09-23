package de.ocplearn.hv.model2;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.model2.LoginUser;

@Mapper(componentModel = "spring")
public interface LoginUserMapper2 {

	LoginUserMapper2 INSTANCE = Mappers.getMapper(LoginUserMapper2.class);
	
	LoginUserDto loginUserToLoginUserDto( LoginUser loginUser );
	
	LoginUser loginUserDtoToLoginUser( LoginUserDto loginUserDto );
}
