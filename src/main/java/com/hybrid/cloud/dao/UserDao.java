package com.hybrid.cloud.dao;

import org.springframework.stereotype.Repository;

import com.hybrid.cloud.models.User;

@Repository
public interface UserDao {

	void save(User userForm);

	Boolean validate(User userForm);
	
	User getUserDetails(int userId);
}
