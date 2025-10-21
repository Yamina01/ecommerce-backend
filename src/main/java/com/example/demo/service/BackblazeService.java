// package com.example.demo.service;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;
// import com.amazonaws.auth.AWSCredentials;
// import com.amazonaws.auth.AWSStaticCredentialsProvider;
// import com.amazonaws.auth.BasicAWSCredentials;
// import com.amazonaws.services.s3.AmazonS3;
// import com.amazonaws.services.s3.AmazonS3ClientBuilder;
// import com.amazonaws.services.s3.model.ObjectMetadata;
// import com.amazonaws.client.builder.AwsClientBuilder;
// import jakarta.annotation.PostConstruct;

// @Service
// public class BackblazeService {
    
//     @Value("${backblaze.keyId}")
//     private String keyId;
    
//     @Value("${backblaze.applicationKey}")
//     private String applicationKey;
    
//     @Value("${backblaze.bucketName}")
//     private String bucketName;
    
//     private AmazonS3 s3Client;
    
//     @PostConstruct
//     public void initialize() {
//         // Backblaze B2 is S3-compatible
//         AWSCredentials credentials = new BasicAWSCredentials(keyId, applicationKey);
        
//         s3Client = AmazonS3ClientBuilder.standard()
//             .withCredentials(new AWSStaticCredentialsProvider(credentials))
//             .withEndpointConfiguration(
//                 new AwsClientBuilder.EndpointConfiguration(
//                     "https://s3.us-west-002.backblazeb2.com", 
//                     "us-west-002"
//                 )
//             )
//             .build();
//     }
    
//     public String uploadProductImage(MultipartFile file, String productId) {
//         try {
//             String fileName = "products/" + productId + "/" + file.getOriginalFilename();
            
//             ObjectMetadata metadata = new ObjectMetadata();
//             metadata.setContentLength(file.getSize());
//             metadata.setContentType(file.getContentType());
            
//             s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
            
//             return s3Client.getUrl(bucketName, fileName).toString();
            
//         } catch (Exception e) {
//             throw new RuntimeException("Failed to upload image: " + e.getMessage());
//         }
//     }
// }
