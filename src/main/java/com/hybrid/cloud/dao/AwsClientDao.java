package com.hybrid.cloud.dao;

import org.springframework.stereotype.Repository;

import com.hybrid.cloud.models.FileMetadata;
import com.hybrid.cloud.models.User;

@Repository
public interface AwsClientDao {

	void insertFileMetadata(FileMetadata file)throws Exception;

	FileMetadata getFileMetadata(int fileId)throws Exception;
	
	boolean isExists(String checksum)throws Exception;

	void deleteFileMetadata(int fileId)throws Exception;

	
}


