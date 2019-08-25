package com.hybrid.cloud.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hybrid.cloud.models.FileMetadata;
import com.hybrid.cloud.models.User;

@Repository
public class UserDaoImpl implements UserDao {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void save(User userForm) throws Exception {
		try {
		String query = "INSERT INTO USER (USERNAME, PASSWORD,EMAIL,ROLE) VALUES (:USERNAME,:PASSWORD,:EMAIL,'USER')";
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("USERNAME", userForm.getUsername());
		namedParameters.put("PASSWORD", userForm.getPassword());
		namedParameters.put("EMAIL", userForm.getEmail());
		namedParameterJdbcTemplate.update(query, namedParameters);
		}catch(Exception e) {
			throw e;
		}
	}

	@Override
	public User validate(User userForm) throws Exception {
		String query = "SELECT ID,USERNAME,EMAIL,ROLE  FROM USER WHERE USERNAME=:USERNAME AND PASSWORD=:PASSWORD";
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("USERNAME", userForm.getUsername());
		namedParameters.put("PASSWORD", userForm.getPassword());

		User user = null;
		try {
			user = namedParameterJdbcTemplate.queryForObject(query, namedParameters, new RowMapper<User>() {
				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User c = new User();
					c.setId(rs.getInt(1));
					c.setUsername(rs.getString(2));
					c.setEmail(rs.getString(3));
					c.setRole(rs.getString(4));
					return c;
				}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}catch(Exception e) {
			throw e;
		}
		return user;
	}
	
	@Override
	public User getUserDetails(int userId) throws Exception {
		String query = "SELECT USERNAME,EMAIL,ROLE  FROM USER WHERE ID=:ID";
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("ID", userId);
		User user = null;
		try {
			user = namedParameterJdbcTemplate.queryForObject(query, namedParameters, new RowMapper<User>() {
				@Override
				public User mapRow(ResultSet rs, int rowNum) throws SQLException {
					User c = new User();
					c.setUsername(rs.getString(1));
					c.setEmail(rs.getString(2));
					c.setRole(rs.getString(3));
					return c;
				}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}catch(Exception e) {
			throw e;
		}
		return user;
	}

	@Override
	public List<FileMetadata> getFiles(User user) throws Exception {
		String query = "SELECT ID,NAME,TAG,URL FROM FILE WHERE ";
		Map<String, Object> namedParameters = new HashMap<String, Object>();

		if(user.getRole().equalsIgnoreCase("ADMIN")) {
			namedParameters.put("ROLE", user.getRole());
			query=query.concat("ROLE =:ROLE");
		}else {
			namedParameters.put("ID", user.getId());
			query=query.concat("USER_ID =:ID");
		}
		List<FileMetadata> files = null;
		try {
			files = namedParameterJdbcTemplate.query(query, namedParameters, new RowMapper<FileMetadata>() {
				@Override
				public FileMetadata mapRow(ResultSet rs, int rowNum) throws SQLException {
					FileMetadata c = new FileMetadata();
					c.setFileId(rs.getInt(1));
					c.setName(rs.getString(2));
					c.setTag(rs.getString(3));
					c.setUrl(rs.getString(4));
					return c;
				}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}catch(Exception e) {
			throw e;
		}
		return files;
	}

}
