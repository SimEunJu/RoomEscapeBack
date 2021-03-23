package com.sej.escape.service.file;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;
import com.amazonaws.services.s3.transfer.Upload;
import com.sej.escape.dto.file.FileReqDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

@Service
public class S3FileManageService implements FileManageService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    //@Value("${cloud.aws.s3.region}")
    // private String region;
    private TransferManager transferManager;

    public S3FileManageService(@Value("${cloud.aws.s3.region}") String region){
        AmazonS3ClientBuilder s3ClientBuilder = AmazonS3Client.builder();
        s3ClientBuilder.setRegion(region);
        AmazonS3 s3Client = s3ClientBuilder.build();
        TransferManagerBuilder transferManager = TransferManagerBuilder.standard();
        transferManager.setS3Client(s3Client);
        this.transferManager = transferManager.build();
    }

    @PostConstruct
    public void afterConstruct(){

    }

    @Override
    public FileReqDto uploadFile(FileReqDto fileReqDto) throws FileUploadException {
        try {
            fileReqDto.setRootPath(bucketName);
            File file = (File) fileReqDto.getFile();
            String uploadName = fileReqDto.getSubPath() + "/" + fileReqDto.getName();
            Upload upload = transferManager.upload(bucketName, uploadName, file);
            upload.waitForUploadResult();
        } catch (AmazonClientException | InterruptedException e) {
            throw new FileUploadException(fileReqDto.toString(), e);
        }
        return fileReqDto;
    }


}
