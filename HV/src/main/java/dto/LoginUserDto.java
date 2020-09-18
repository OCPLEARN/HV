package dto;

import java.util.Locale;

import de.ocplearn.hv.model.Role;

@lombok.Data
public class LoginUserDto {
	
    private int id;
    private String loginUserName;
    private Role role;
    private byte [] passwHash;
    private byte [] salt;
    private Locale locale;

}
