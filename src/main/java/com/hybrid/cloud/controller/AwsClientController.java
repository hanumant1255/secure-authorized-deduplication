package com.hybrid.cloud.controller;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hybrid.cloud.service.AwsClientService;

@RestController
@RequestMapping("/files")
public class AwsClientController {

	@Autowired
	private AwsClientService amazonS3ClientService;

	@PostMapping
	public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("userId") int userId,
			@RequestPart(value = "file") MultipartFile file) {
		URL url = amazonS3ClientService.uploadFileToS3Bucket(userId, file);
		Map<String, String> response = new HashMap<>();
		response.put("message", "file [" + file.getOriginalFilename() + "] uploading request submitted successfully.");
		response.put("url", url.toString());
		return ResponseEntity.ok().body(response);
	}

	@DeleteMapping
	public ResponseEntity<Map<String, String>> deleteFile(@RequestParam("fileId") int fileId,@RequestParam("userId") int userId,
			@RequestParam("fileName") String fileName) {
		this.amazonS3ClientService.deleteFileFromS3Bucket(userId,fileId, fileName);

		Map<String, String> response = new HashMap<>();
		response.put("message", "file [" + fileName + "] removing request submitted successfully.");
		return ResponseEntity.ok().body(response);
	}

	@GetMapping
	public ResponseEntity<byte[]> downloadFile(@RequestParam("userId") int userId, @RequestParam("fileId") int fileId,
			@RequestParam("fileName") String fileName) {
		ByteArrayOutputStream downloadInputStream = amazonS3ClientService.downloadFile(userId, fileId, fileName);

		return ResponseEntity.ok().contentType(contentType(fileName))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
				.body(downloadInputStream.toByteArray());
	}

	private MediaType contentType(String keyname) {
		String[] arr = keyname.split("\\.");
		String type = arr[arr.length - 1];
		switch (type) {
		case "txt":
			return MediaType.TEXT_PLAIN;
		case "png":
			return MediaType.IMAGE_PNG;
		case "jpg":
			return MediaType.IMAGE_JPEG;
		default:
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}
}