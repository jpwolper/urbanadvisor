package com.techelevator.citymap.model;

import javax.sql.DataSource;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import com.techelevator.citymap.security.PasswordHasher;
import com.techelevator.citymap.model.User;

@Component
public class JDBCUserDAO implements UserDAO {

	private JdbcTemplate jdbcTemplate;
	private PasswordHasher passwordHasher;

	@Autowired
	public JDBCUserDAO(DataSource dataSource, PasswordHasher passwordHasher) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.passwordHasher = passwordHasher;
	}
	
	@Override
	public boolean saveUser(String userName, String password, String firstName, String lastName) {
		String sqlSearchForUsername = "SELECT user_name "+
			      "FROM app_user "+
			      "WHERE UPPER(user_name) = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchForUsername, userName.toUpperCase());
		if(!results.next()){
			byte[] salt = passwordHasher.generateRandomSalt();
			String hashedPassword = passwordHasher.computeHash(password, salt);
			String saltString = new String(Base64.encode(salt));
			jdbcTemplate.update("INSERT INTO app_user(user_name, password, salt, first_name, last_name) VALUES (?, ?, ?, ?, ?)", userName.toUpperCase(), hashedPassword, saltString, firstName, lastName);
			return true;
		}
		else{
			return false;
		}
	}

	/*@Override
	public boolean validatePassword(String userName, String password) {
		String sqlSearchForUser = "SELECT * "+
							      "FROM app_user "+
							      "WHERE UPPER(user_name) = ?";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchForUser, userName.toUpperCase());
		if(results.next()) {
			String storedSalt = results.getString("salt");
			String storedPassword = results.getString("password");
			String hashedPassword = passwordHasher.computeHash(password, Base64.decode(storedSalt));
			return storedPassword.equals(hashedPassword);
		} else {
			return false;
		}
	}*/
	
	@Override
	public boolean validateUserName(String userName) {
		boolean isValidUser = false;
		String sqlGetUser = "Select * FROM app_user WHERE UPPER(user_name) = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetUser, userName.toUpperCase());
		if (results.next()) {
			isValidUser = true;
		}
		return isValidUser;
	}

	@Override
	public void updatePassword(String userName, String password) {
		byte[] salt = passwordHasher.generateRandomSalt();
		String hashedPassword = passwordHasher.computeHash(password, salt);
		String saltString = new String(Base64.encode(salt));
		jdbcTemplate.update("UPDATE app_user SET password = ?, salt = ? WHERE user_name = ?", hashedPassword, saltString, userName.toUpperCase());
	}
	
	@Override
	public User getUser(String userName) {
		User newUser = new User();
		String sqlGetUser = "Select * FROM app_user WHERE user_name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetUser, userName.toUpperCase());
		if (results.next()) {
			newUser = mapRowToUser(results);
		}
		return newUser;
	}
	
	private User mapRowToUser(SqlRowSet results) {
		User u = new User();
		u.setUserName(results.getString("user_name"));
		u.setPassword(results.getString("password"));
		u.setFirstName(results.getString("first_name"));
		u.setLastName(results.getString("last_name"));
		u.setSalt(results.getString("salt"));
		u.setAdmin(results.getBoolean("admin"));
		return u;
	}

}
