package com.hybrid.cloud.dao;

import org.springframework.stereotype.Repository;

import com.hybrid.cloud.models.FileMetadata;
import com.hybrid.cloud.models.User;

@Repository
public interface AwsClientDao {

	void insertFileMetadata(FileMetadata file);

	FileMetadata getFileMetadata(int fileId);
	
	boolean isExists(String checksum);

	void deleteFileMetadata(int fileId);

	
}


