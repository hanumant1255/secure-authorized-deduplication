package com.hybrid.cloud.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import com.hybrid.cloud.AESEncrypterDecrypter;
import com.hybrid.cloud.dao.AmazonS3ClientDao;
import com.hybrid.cloud.models.FileMetadata;

@Service
public class AmazonS3ClientServiceImpl implements AmazonS3ClientService {
	private String awsS3AudioBucket;
	private AmazonS3 amazonS3;
	private static final Logger logger = LoggerFactory.getLogger(AmazonS3ClientServiceImpl.class);
	String password = "PASSWORD";
	Charset charset = StandardCharsets.UTF_8;

	@Autowired
	AmazonS3ClientDao amazonS3ClientDao;

	@Autowired
	public AmazonS3ClientServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider,
			String awsS3AudioBucket) {
		this.amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider)
				.withRegion(awsRegion.getName()).build();
		this.awsS3AudioBucket = awsS3AudioBucket;

	}

	public URL uploadFileToS3Bucket(int userId, MultipartFile multipartFile, boolean enablePublicReadAccess) {
		String fileName = multipartFile.getOriginalFilename();
		URL url = null;
		try {
			ObjectMetadata objectMetadata = new ObjectMetadata();
			objectMetadata.setContentType(multipartFile.getContentType());
			objectMetadata.setContentLength(multipartFile.getSize());
			try {
				String checksum = DigestUtils.sha256Hex(multipartFile.getInputStream());
				if (!amazonS3ClientDao.isExists(checksum)) {
					AESEncrypterDecrypter encrypter = new AESEncrypterDecrypter(checksum);
					String encrypted = encrypter.encrypt(IOUtils.toString(multipartFile.getInputStream()));
					amazonS3.putObject(this.awsS3AudioBucket, fileName, encrypted);
					url = amazonS3.getUrl(this.awsS3AudioBucket, fileName);
					FileMetadata file = new FileMetadata();

					file.setTag(checksum);
					file.setName(fileName);
					file.setUrl(url.toString());
					file.setRole("ADMIN");
					file.setUserId(userId);
					amazonS3ClientDao.insertFileMetadata(file);
				} else {
					throw new Exception("File already present");
				}
			} catch (Exception exception) {
				throw new RuntimeException("Error while uploading file.");
			}

		} catch (AmazonServiceException ex) {
			logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
		}
		return url;
	}

	@Override
	public void deleteFileFromS3Bucket(int userId, String fileName) {
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(awsS3AudioBucket, fileName));
		} catch (AmazonServiceException ex) {
			logger.error("error [" + ex.getMessage() + "] occurred while removing [" + fileName + "] ");
		}
	}

	@Override
	public ByteArrayOutputStream downloadFile(int userId, int fileId, String fileName) {
		try {
			S3Object s3object = amazonS3.getObject(new GetObjectRequest(awsS3AudioBucket, fileName));

			InputStream in = s3object.getObjectContent();

			FileMetadata file = amazonS3ClientDao.getFileMetadata(fileId);
			AESEncrypterDecrypter encrypter = new AESEncrypterDecrypter(file.getTag());
			String decrypted = encrypter.decrypt(IOUtils.toString(in));

			InputStream stream = new ByteArrayInputStream(decrypted.getBytes(StandardCharsets.UTF_8));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			int len;
			byte[] buffer = new byte[4096];
			while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, len);
			}
			return baos;
		} catch (Exception ioe) {
			logger.error("IOException: " + ioe.getMessage());
		}

		return null;
	}

}