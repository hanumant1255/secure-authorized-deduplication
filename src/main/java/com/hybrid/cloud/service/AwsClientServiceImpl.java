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
import org.springframework.mail.javamail.JavaMailSender;
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
import com.hybrid.cloud.dao.AwsClientDao;
import com.hybrid.cloud.dao.FileAlreadyExistException;
import com.hybrid.cloud.dao.UserDao;
import com.hybrid.cloud.models.FileMetadata;
import com.hybrid.cloud.models.User;
import com.hybrid.cloud.util.AESEncrypterDecrypter;
import com.hybrid.cloud.util.EmailSender;

@Service
public class AwsClientServiceImpl implements AwsClientService {
	private String awsS3AudioBucket;
	private AmazonS3 amazonS3;
	private static final Logger logger = LoggerFactory.getLogger(AwsClientServiceImpl.class);
	String password = "PASSWORD";
	Charset charset = StandardCharsets.UTF_8;

	@Autowired
	AwsClientDao amazonS3ClientDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	JavaMailSender javaMailSender;
	
	EmailSender emailSender;

	@Autowired
	public AwsClientServiceImpl(Region awsRegion, AWSCredentialsProvider awsCredentialsProvider,
			String awsS3AudioBucket) {
		this.amazonS3 = AmazonS3ClientBuilder.standard().withCredentials(awsCredentialsProvider)
				.withRegion(awsRegion.getName()).build();
		this.awsS3AudioBucket = awsS3AudioBucket;

	}

	public URL uploadFileToS3Bucket(int userId, MultipartFile multipartFile) throws Exception{
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
					file.setToken(checksum);
					file.setName(fileName);
					file.setUrl(url.toString());
					file.setRole("ADMIN");
					file.setUserId(userId);
					amazonS3ClientDao.insertFileMetadata(file);
					
					User user=userDao.getUserDetails(userId);
					emailSender=new EmailSender(javaMailSender);
					emailSender.sendEmailMessage(user.getEmail(),"Token for file "+fileName, checksum);
					
				}else {
					throw new FileAlreadyExistException("File already exists ");
				}
			} catch (Exception e) {
				throw e;
			}

		} catch (AmazonServiceException ex) {
			logger.error("error [" + ex.getMessage() + "] occurred while uploading [" + fileName + "] ");
		}
		return url;
	}

	@Override
	public void deleteFileFromS3Bucket(int userId, int fileId,String fileName)throws Exception {
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(awsS3AudioBucket, fileName));
			amazonS3ClientDao.deleteFileMetadata(fileId);
			
		} catch (AmazonServiceException ex) {
			logger.error("error [" + ex.getMessage() + "] occurred while removing [" + fileName + "] ");
		}
	}

	@Override
	public ByteArrayOutputStream downloadFile(int userId, int fileId, String fileName)throws Exception {
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
			throw new Exception("Error while downloading file ");
		}
	}

}