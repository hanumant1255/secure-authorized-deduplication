package com.hybrid.cloud.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.hybrid.cloud.models.FileMetadata;

@Repository
public class AmazonS3ClientDaoImpl implements AmazonS3ClientDao {

	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	@Override
	public void insertFileMetadata(FileMetadata file) {
		String query = "INSERT INTO FILE (NAME,TAG,URL,ROLE,USER_ID) VALUES (:NAME,:TAG,:URL,:ROLE,:USER_ID)";
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("NAME", file.getName());
		namedParameters.put("TAG", file.getTag());
		namedParameters.put("URL", file.getUrl());
		namedParameters.put("ROLE", file.getRole());
		namedParameters.put("USER_ID", file.getUserId());
		namedParameterJdbcTemplate.update(query, namedParameters);
	}

	@Override
	public FileMetadata getFileMetadata(int fileId) {
		String query = "SELECT TAG,URL FROM FILE WHERE ID=:ID";
		Map<String, Object> namedParameters = new HashMap<String, Object>();
		namedParameters.put("ID", fileId);
		FileMetadata file = null;
		try {
			file = namedParameterJdbcTemplate.queryForObject(query, namedParameters, new RowMapper<FileMetadata>() {
				@Override
				public FileMetadata mapRow(ResultSet rs, int rowNum) throws SQLException {
					FileMetadata c = new FileMetadata();
					c.setTag(rs.getString(1));
					c.setUrl(rs.getString(2));
					return c;
				}
			});
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		return file;
	}

	@Override
	public boolean isExists(String checksum) {
		String query = "SELECT TAG FROM FILE";
		List<String> tagList = new ArrayList<String>();
		try {
			tagList = namedParameterJdbcTemplate.query(query, new RowMapper<String>() {
				@Override
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString(1);

				}
			});

			return tagList.contains(checksum);

		} catch (Exception e) {
		}
		return false;
	}

}
