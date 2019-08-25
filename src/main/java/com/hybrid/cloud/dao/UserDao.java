package com.hybrid.cloud.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hybrid.cloud.models.FileMetadata;
import com.hybrid.cloud.models.User;

@Repository
public interface UserDao {

	void save(User userForm) throws Exception;

	User validate(User userForm) throws Exception;
	
	User getUserDetails(int userId)throws Exception;

	List<FileMetadata> getFiles(User user) throws Exception;
}
