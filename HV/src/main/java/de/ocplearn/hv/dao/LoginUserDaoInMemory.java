package de.ocplearn.hv.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.ocplearn.hv.dto.LoginUserDto;
import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.util.StaticHelpers;

/**
 * LoginUserDao implementation for an in memory Set
 * 
 * */
@Component("LoginUserDaoInMemory")
public class LoginUserDaoInMemory implements LoginUserDao {
	
	private static int idSrc;
	
	private static Set<LoginUser> data = new HashSet<>();
	
	static {
		String loginUserName = "admin";
		LoginUser adminUser = new LoginUser();
        adminUser.setLoginUserName(loginUserName);
        adminUser.setRole(Role.ADMIN);
        adminUser.setLocale( Locale.GERMANY );		
		adminUser.setId(idSrc++);

        HashMap<String, byte[]> hm = StaticHelpers.createHash("Pa$$w0rd", null);
        adminUser.setPasswHash( hm.get("hash") );
        adminUser.setSalt(hm.get("salt") );		
        data.add(adminUser);
	}
	
	@Override
	public boolean save(LoginUser loginUser) {
		
		if (loginUser.getId() == 0) {
			loginUser.setId(++idSrc);
			data.add(loginUser);
		}else {
			LoginUser tmp = loginUser;
			data.remove(loginUser);
			data.add(tmp);
		}
		
		return true;
	}

	@Override
	public boolean delete(LoginUser loginUser) {
		data.remove(loginUser);
		return false;
	}

	@Override
	public boolean delete(String loginUserName) {
		Optional<LoginUser> opt =
		data.stream()
			.filter(loginUser -> loginUser.getLoginUserName().equals(loginUserName) )
			.findFirst();

		if ( opt.isPresent() ) {
			data.remove(opt.get());
			return true;
		}else {
			return false;
		}

	}

	@Override
	public Optional<LoginUser> findUserById(int id) {
		return 
		data.stream()
			.filter(loginUser -> loginUser.getId() == id )
			.findFirst();
	}

	@Override
	public Optional<LoginUser> findUserByLoginUserName(String loginUserName) {
		return 
		data.stream()
		.filter(loginUser -> loginUser.getLoginUserName().equals(loginUserName) )
			.findFirst();
	}

	@Override
	public List<LoginUser> findAllByRole(Role role) {
		return new ArrayList<>(
					data.stream()
						. filter( loginUser -> loginUser.getRole().equals(role) )
						.collect(Collectors.toList())
				);
	}

	@Override
	public boolean userAlreadyExists(String loginUserName) {
		return 
				data.stream()
					.anyMatch(loginUser -> loginUser.getLoginUserName().equals(loginUserName));
	}

	@Override
	public boolean validateUser(String loginUserName, String password) {

		Optional<LoginUser> opt =
				data.stream()
					.filter(loginUser -> loginUser.getLoginUserName().equals(loginUserName))
					.findFirst();
		if ( ! opt.isPresent() ) {
			return false;
		}
		
		LoginUser loginUser = opt.get();
		
        HashMap<String,byte[]> userMap = null;
        byte[] hash = loginUser.getPasswHash();
        byte[] salt = loginUser.getSalt();		
		
        userMap = StaticHelpers.createHash( password, salt );
        
        return  Arrays.equals(userMap.get("hash"), hash );
	}

}
