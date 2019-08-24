package com.hybrid.cloud;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;

@Configuration
public class AppConfig {

	@Value("${aws.access.key.id}")
	private String awsKeyId;

	@Value("${aws.access.key.secret}")
	private String awsKeySecret;

	@Value("${aws.region}")
	private String awsRegion;

	@Value("${aws.s3.audio.bucket}")
	private String awsS3AudioBucket;

	@Value("${mysql.datasource.url}")
	private String databaseUrl;

	@Value("${mysql.datasource.username}")
	private String databaseUser;

	@Value("${mysql.datasource.password}")
	private String databasePassword;

	@Value("${mysql.datasource.driver-class-name}")
	private String databaseDriver;

	@Bean(name = "awsKeyId")
	public String getAWSKeyId() {
		return awsKeyId;
	}

	@Bean(name = "awsKeySecret")
	public String getAWSKeySecret() {
		return awsKeySecret;
	}

	@Bean(name = "awsRegion")
	public Region getAWSPollyRegion() {
		return Region.getRegion(Regions.fromName(awsRegion));
	}

	@Bean(name = "awsCredentialsProvider")
	public AWSCredentialsProvider getAWSCredentials() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(this.awsKeyId, this.awsKeySecret);
		return new AWSStaticCredentialsProvider(awsCredentials);
	}

	@Bean(name = "awsS3AudioBucket")
	public String getAWSS3AudioBucket() {
		return awsS3AudioBucket;
	}

	@Primary
	@Bean
	public DataSource getDataSource() {
		DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
		dataSourceBuilder.driverClassName(databaseDriver);
		dataSourceBuilder.url(databaseUrl);
		dataSourceBuilder.username(databaseUser);
		dataSourceBuilder.password(databasePassword);
		return dataSourceBuilder.build();
	}

	@Bean
	NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(getDataSource());
	}

}