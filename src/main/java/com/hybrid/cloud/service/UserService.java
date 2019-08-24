package com.hybrid.cloud.service;

import org.springframework.stereotype.Service;

import com.hybrid.cloud.models.User;

@Service
public interface UserService {

	void save(User userForm);

	Boolean validate(User userForm);
}
