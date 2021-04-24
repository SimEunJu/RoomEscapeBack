package com.sej.escape.service.file.upload;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.sej.escape.dto.file.FileReqDto;
import com.sej.escape.service.file.manage.FileManageService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class S3FileManageService implements FileManageService {

    @Value("${file.upload.temp}")
    private String tempRootPath;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.s3.region}")
    private String region;
;
    private TransferManager transferManager;

    public S3FileManageService(@Value("${cloud.aws.s3.region}") String region,
                               @Value("${cloud.aws.credentials.accessKey}") String accessKey,
                               @Value("${cloud.aws.credentials.secretKey}") String secretKey) {

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

        AmazonS3ClientBuilder s3ClientBuilder = AmazonS3Client.builder();
        s3ClientBuilder.setRegion(region);
        s3ClientBuilder.setCredentials(credentialsProvider);
        AmazonS3 s3Client = s3ClientBuilder.build();

        TransferManagerBuilder transferManager = TransferManagerBuilder.standard();
        transferManager.setS3Client(s3Client);

        this.transferManager = transferManager.build();
    }

    private String getRootUrl(){
        return "https://"+bucketName+".s3."+region+".amazonaws.com";
    }

    @Override
    public FileReqDto uploadFile(FileReqDto fileReqDto) throws FileUploadException {
        try {
            fileReqDto.setRootPath(bucketName);
            MultipartFile file = fileReqDto.getUploadFile();

            String uploadPath = tempRootPath + "/" + fileReqDto.getSubPath();
            String nameWithFullPath = uploadPath + "/" + fileReqDto.getName();
            File fileTo = File.createTempFile(nameWithFullPath, ".tmp");
            file.transferTo(fileTo);

            fileReqDto.setRootPath(getRootUrl());

            String uploadName = fileReqDto.getSubPath() + "/" + fileReqDto.getName();
            Upload upload = transferManager.upload(bucketName, uploadName, fileTo);
            upload.waitForUploadResult();

        } catch (AmazonClientException | InterruptedException | IOException e) {
            throw new FileUploadException(fileReqDto.toString(), e);
        }
        return fileReqDto;
    }

}
