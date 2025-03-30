package com.itda.moamoa.domain.spec.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    public S3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 비어있습니다.");
        }
        
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String storedFileName = UUID.randomUUID().toString() + extension;
        
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        
        // PutObjectRequest를 사용하여 퍼블릭 읽기 권한 추가
        PutObjectRequest putObjectRequest = new PutObjectRequest(
            bucketName, 
            storedFileName, 
            file.getInputStream(), 
            metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead); // 공개 읽기 권한 설정
        
        amazonS3.putObject(putObjectRequest);
        
        return getFileUrl(storedFileName);
    }

    public String getFileUrl(String fileName) {
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
    }
}