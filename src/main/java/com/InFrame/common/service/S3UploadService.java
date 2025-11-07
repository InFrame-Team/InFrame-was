package com.InFrame.common.service;

import com.InFrame.common.exception.CustomException;
import com.InFrame.common.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private final S3Client s3Client;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * S3에 파일을 업로드합니다.
     * @param file 업로드할 파일
     * @return S3에 저장된 파일의 URL
     */
    public String uploadFile(MultipartFile file, String folderName) {
        if (file == null || file.isEmpty()) {
            throw new CustomException(ErrorCode.FILE_IS_EMPTY);
        }

        String originalFilename = file.getOriginalFilename();
        String uniqueFileName = createUniqueFileName(originalFilename, folderName);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(uniqueFileName)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            // 1. fromInputStream에 2개의 인자(스트림, 파일 크기) 전달
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // 업로드된 파일의 URL 반환
            return s3Client.utilities().getUrl(builder -> builder.bucket(bucket).key(uniqueFileName)).toString();

        } catch (IOException e) {
            throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    /**
     * S3에서 파일을 삭제합니다.
     * @param fileUrl 삭제할 파일의 전체 URL
     */
    public void deleteFile(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        try {
            String key = getKeyFromFileUrl(fileUrl);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (Exception e) {
            System.err.println("Failed to delete file from S3: " + fileUrl);
        }
    }

    /**
     * 파일 이름을 유니크하게 생성합니다.
     */
    private String createUniqueFileName(String originalFilename, String folderName) {
        return folderName + "/" + UUID.randomUUID().toString() + "-" + originalFilename;
    }

    /**
     * S3 URL에서 파일 키 (경로+이름) 를 추출합니다.
     */
    private String getKeyFromFileUrl(String fileUrl) {
        try {
            String decodedUrl = URLDecoder.decode(fileUrl, StandardCharsets.UTF_8);
            String bucketUrl = "https://" + bucket + ".s3.";

            String key = decodedUrl.substring(decodedUrl.indexOf(bucket) + bucket.length() + 1);

            if (key.startsWith("s3.amazonaws.com/" + bucket + "/")) {
                key = key.substring(("s3.amazonaws.com/" + bucket + "/").length());
            } else if (fileUrl.contains(bucket + ".s3.")) {
                key = decodedUrl.substring(decodedUrl.indexOf(".com/") + 5);
            } else {
                key = new java.net.URL(decodedUrl).getPath().substring(1);
                if(key.startsWith(bucket + "/")) {
                    key = key.substring(bucket.length() + 1);
                }
            }
            return key;
        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_FILE_URL);
        }
    }
}
