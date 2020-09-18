package de.ocplearn.hv.config;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.service.UserService;
import dto.LoginUserDto;

public class CustomAuthenticationProvider implements AuthenticationProvider {

	
	@Autowired
	UserService userService;
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String loginUserName = authentication.getName();
		Object credentials = authentication.getCredentials();
		
		 System.out.println("credentials class: " + credentials.getClass());
	        if (!(credentials instanceof String)) {
	            return null;
	        }
	        String password = credentials.toString();

	        Boolean isValidated = userService.validateUserPassword(loginUserName,password);
	        
	        if (!isValidated) {
	            throw new BadCredentialsException("Authentication failed for " + loginUserName);
	        }
	        
	        LoginUserDto loginUserDto
	        
	        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
	        grantedAuthorities.add(new SimpleGrantedAuthority(userOptional.get().role));
	        
		  Authentication auth = new UsernamePasswordAuthenticationToken(name, password, grantedAuthorities);
	        return auth;
		
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return false;
	}

}
