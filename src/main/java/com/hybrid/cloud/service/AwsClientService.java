package com.hybrid.cloud.service;

import java.io.ByteArrayOutputStream;
import java.net.URL;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface AwsClientService {
	URL uploadFileToS3Bucket(int userId, MultipartFile multipartFile)throws Exception;

	void deleteFileFromS3Bucket(int userId, int fileId, String fileName)throws Exception;

	ByteArrayOutputStream downloadFile(int userId, int fileId, String fileName, String key)throws Exception;
}
