package com.hybrid.cloud.dao;

import org.springframework.stereotype.Repository;

import com.hybrid.cloud.models.FileMetadata;

@Repository
public interface AmazonS3ClientDao {

	void insertFileMetadata(FileMetadata file);

	FileMetadata getFileMetadata(int fileId);
	
	boolean isExists(String checksum);
	
}
