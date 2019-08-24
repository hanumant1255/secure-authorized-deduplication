package com.hybrid.cloud.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hybrid.cloud.models.User;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void save(User userForm) {
		String query = "INSERT INTO USER (USERNAME, PASSWORD,EMAIL,ROLE) VALUES (:USERNAME,:PASSWORD,:EMAIL,'USER')";
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("USERNAME", userForm.getUsername());
		namedParameters.put("PASSWORD", userForm.getPassword());
		namedParameters.put("EMAIL", userForm.getEmail());
		namedParameterJdbcTemplate.update(query, namedParameters);
	}

	@Override
	public Boolean validate(User userForm) {
		String query = "SELECT ID  FROM USER WHERE USERNAME=:USERNAME AND PASSWORD=:PASSWORD ";
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("USERNAME", userForm.getUsername());
		namedParameters.put("PASSWORD", userForm.getPassword());
		User user = null;
		try {
			user = namedParameterJdbcTemplate.queryForObject(query, namedParameters, new RowMapper<User>() {
				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User c = new User();
					c.setId((long) rs.getInt(1));
					return c;
				}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		if (user != null) {
			return true;

		}
		return false;

	}

}
