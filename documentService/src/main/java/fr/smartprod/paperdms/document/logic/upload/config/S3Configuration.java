package fr.smartprod.paperdms.document.logic.upload.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;

import java.net.URI;

/**
 * Configuration for AWS S3 client.
 * Supports both AWS S3 and S3-compatible storage (MinIO, etc.).
 */
@Configuration
public class S3Configuration {

    @Value("${paperdms.s3.region}")
    private String s3Region;

    @Value("${paperdms.s3.access-key:}")
    private String accessKey;

    @Value("${paperdms.s3.secret-key:}")
    private String secretKey;

    @Value("${paperdms.s3.endpoint:}")
    private String endpoint;

    /**
     * Create S3 client bean.
     *
     * @return Configured S3Client
     */
    @Bean
    public S3Client s3Client() {
        S3ClientBuilder builder = S3Client.builder()
            .region(Region.of(s3Region))
            .credentialsProvider(getCredentialsProvider());

        if (endpoint != null && !endpoint.isEmpty()) {
            builder.endpointOverride(URI.create(endpoint));
            builder.forcePathStyle(true);
        }

        return builder.build();
    }

    /**
     * Get credentials provider based on configuration.
     *
     * @return AWS credentials provider
     */
    private AwsCredentialsProvider getCredentialsProvider() {
        if (accessKey != null && !accessKey.isEmpty() && secretKey != null && !secretKey.isEmpty()) {
            return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            );
        }
        
        return DefaultCredentialsProvider.create();
    }
}