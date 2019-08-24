package com.hybrid.cloud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hybrid.cloud.dao.UserDao;
import com.hybrid.cloud.models.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Override
	public void save(User userForm) {
		userDao.save(userForm);
	}

	@Override
	public Boolean validate(User userForm) {
		return userDao.validate(userForm);
	}

}
