package de.ocplearn.hv.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.service.UserService;


@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserService userService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String loginUserName = authentication.getName();
		Object credentials = authentication.getCredentials();
		
		System.out.println("credentials class: " + credentials.getClass());
	    if (!(credentials instanceof String)) {
	            return null;
	    }
	    
	    String password = credentials.toString();

	    System.out.println("+++ authenticate, loginUserName = " + loginUserName + ", password = " + password);
	    
	    Optional<LoginUserDto> opt = userService.validateUserPassword(loginUserName,password);
	        
        if (!opt.isPresent()) {
            throw new BadCredentialsException("Authentication failed for " + loginUserName);
        }
	        
	    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
	        
	    String role = opt.get().getRole().toString();
	    
	    grantedAuthorities.add(new SimpleGrantedAuthority( role ) );
	        
		Authentication auth = new UsernamePasswordAuthenticationToken(opt.get().getLoginUserName(), password, grantedAuthorities);
	        
		//auth.setAuthenticated(true);	// Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead
		
		System.out.println("+++ auth.setAuthenticated(true)");
		
		return auth;
		
	}

	   @Override
	    public boolean supports(Class<?> authentication) {
	        return authentication.equals(UsernamePasswordAuthenticationToken.class);
	    }

}
