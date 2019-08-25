package com.hybrid.cloud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hybrid.cloud.dao.UserDao;
import com.hybrid.cloud.models.FileMetadata;
import com.hybrid.cloud.models.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDao userDao;

	@Override
	public void save(User userForm) throws Exception {
			userDao.save(userForm);
	}

	@Override
	public User validate(User userForm) throws Exception{
		return userDao.validate(userForm);
	}

	@Override
	public List<FileMetadata> getFiles(User user)throws Exception {
		return userDao.getFiles(user);
	}

}
