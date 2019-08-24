package com.hybrid.cloud.service;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AmazonS3ClientService {
	URL uploadFileToS3Bucket(int userId, MultipartFile multipartFile, boolean enablePublicReadAccess);

	void deleteFileFromS3Bucket(int userId, String fileName);

	ByteArrayOutputStream downloadFile(int userId, int fileId, String fileName);
}
