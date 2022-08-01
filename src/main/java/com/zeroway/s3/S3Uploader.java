package com.zeroway.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    // 저장된 파일 url 리스트 반환
    public List<String> uploadFiles(List<MultipartFile> multipartFiles, String dirName) throws IOException {
        List<String> fileUrls = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            fileUrls.add(uploadFile(multipartFile, dirName));
        }
        return fileUrls;
    }

    // s3에 파일을 업로드 후, 파일 url을 반환한다.
    // !!이미지 리사이징 추가 필요!!
    private String uploadFile(MultipartFile multipartFile, String dirName) throws IOException {
        String fileName = dirName + "/" + UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            PutObjectResult putObjectResult = amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata));
            System.out.println("putObjectResult = " + putObjectResult);
            return getFileUrl(fileName);
        } catch (IOException e) {
            throw new FileUploadException();
        }
    }

    private String getFileUrl(String fileName) {
        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

}
