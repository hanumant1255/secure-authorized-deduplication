package com.hybrid.cloud.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hybrid.cloud.models.FileMetadata;
import com.hybrid.cloud.models.User;

@Service
public interface UserService {

	void save(User userForm)throws Exception;

	User validate(User userForm)throws Exception;

	List<FileMetadata> getFiles(User user)throws Exception;
}
