package de.ocplearn.hv.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import de.ocplearn.hv.model.LoginUser;
import de.ocplearn.hv.model.Role;
import de.ocplearn.hv.util.StaticHelpers;

/**
 * LoginUserDao implemented with the spring JdbcTemplate
 * 
 * */
@Component("LoginUserDaoJdbcTemplate")
public class LoginUserDaoJdbcTemplate implements LoginUserDao {

    @Qualifier("datasource1")
    @Autowired
	private DataSource datasource;		
	
	private JdbcTemplate jdbc = new JdbcTemplate(datasource);
	
	@Override
	public boolean save(LoginUser loginUser) {
		if (loginUser.getId()>0) {
			return insert( loginUser );
		} else{
			return update ( loginUser );
		}
	}

	private boolean insert( LoginUser loginUser ) {
		
		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
				"INSERT INTO loginUser (id,loginUserName,passwHash,salt,loginUserRole,locale) VALUE (null,?,?,?,?,?);",
				Types.INTEGER,Types.VARCHAR,Types.VARBINARY,Types.BINARY,Types.VARCHAR,Types.VARCHAR
			).newPreparedStatementCreator(
					Arrays.asList(
							loginUser.getLoginUserName(),
							loginUser.getPasswHash(),
							loginUser.getSalt(),
							loginUser.getRole().name(),
							loginUser.getLocale().toString()
							)
					);		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int rowCount = jdbc.update(psc,keyHolder);
		loginUser.setId(keyHolder.getKey().intValue());
		return rowCount == 1 ? true : false;
	}

	private boolean update( LoginUser loginUser ) {

		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
				"UPDATE loginUser SET loginUserName = ?, passwHash = ?, salt = ?, loginUserRole = ?, locale = ? WHERE id = ?;",
				Types.VARCHAR,Types.VARBINARY,Types.BINARY,Types.VARCHAR,Types.VARCHAR,Types.INTEGER
			).newPreparedStatementCreator(
					Arrays.asList(
							loginUser.getLoginUserName(),
							loginUser.getPasswHash(),
							loginUser.getSalt(),
							loginUser.getRole().name(),
							loginUser.getLocale().toString(),
							loginUser.getId()
							)
					);		
		return jdbc.update(psc) == 1 ? true: false;
	}	
	
	@Override
	public boolean delete(LoginUser loginUser) {

		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
				"DELETE FROM loginUser WHERE id = '?';",
				Types.INTEGER
			).newPreparedStatementCreator(
					Arrays.asList(
							loginUser.getId()
							)
					);		
		return jdbc.update(psc) == 1 ? true : false;
	}

	@Override
	public boolean delete(String loginUserName) {
		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
				"DELETE FROM loginUser WHERE loginUserName = '?';",
				Types.VARCHAR
			).newPreparedStatementCreator(
					Arrays.asList(
							loginUserName
							)
					);		
		return jdbc.update(psc) == 1 ? true : false;
	}

	@Override
	public Optional<LoginUser> findUserById(int id) {
		
		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
				"SELECT * FROM loginUser WHERE id = '?';",
				Types.INTEGER
			).newPreparedStatementCreator(
					Arrays.asList(id)
					);
		// jdbc.query(psc, this::mapRowToLoginUser);
		List<LoginUser> list = jdbc.query(psc, this::mapRowToLoginUser);
		return Optional.of(list.get(0));
	}

	@Override
	public Optional<LoginUser> findUserByLoginUserName(String loginUserName) {
		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
				"SELECT * FROM loginUser WHERE loginUserName = '?';",
				Types.INTEGER
			).newPreparedStatementCreator(
					Arrays.asList(loginUserName)
					);
		// jdbc.query(psc, this::mapRowToLoginUser);
		List<LoginUser> list = jdbc.query(psc, this::mapRowToLoginUser);
		return Optional.of(list.get(0));
	}

	@Override
	public List<LoginUser> findAllByRole(Role role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean userAlreadyExists(String loginUserName) {
		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
				"SELECT * FROM loginUser WHERE loginUserName = ?;",
				Types.VARCHAR
			).newPreparedStatementCreator(
					Arrays.asList(loginUserName)
					);
		// jdbc.query(psc, this::mapRowToLoginUser);
		List<LoginUser> list = jdbc.query(psc, this::mapRowToLoginUser);
		return list.size() > 0 ? true : false;
	}

	@Override
	public boolean validateUser(String loginUserName, String password) {
        
		HashMap<String,byte[]> userMap = null;
        byte[] hash = null;
        byte[] salt = null;

		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
				"SELECT * FROM loginUser WHERE loginUserName = ?;",
				Types.VARCHAR
			).newPreparedStatementCreator(
					Arrays.asList(loginUserName)
					);
		// jdbc.query(psc, this::mapRowToLoginUser);
		List<LoginUser> list = jdbc.query(psc, this::mapRowToLoginUser);        

		if ( list.size() > 0 ) {
        	LoginUser user = list.get(0);
        	hash = user.getPasswHash();
        	salt = user.getSalt();
        	userMap = StaticHelpers.createHash( password, salt );
        	return Arrays.equals(userMap.get("hash"), hash );
        }else {
        	return false;
        }

	}

	private LoginUser mapRowToLoginUser( ResultSet rs, int rowNum ) throws SQLException {
		LoginUser loginUser = new LoginUser();
		loginUser.setId(rs.getInt("id"));
		loginUser.setLoginUserName(rs.getString("loginUserName"));
		loginUser.setPasswHash(rs.getBytes("passwHash"));
		loginUser.setSalt(rs.getBytes("salt"));
		loginUser.setRole( Role.valueOf( rs.getString("loginUserRole") ) );
		loginUser.setLocale(new Locale( rs.getString("locale") ));
		return loginUser;
	}
}
