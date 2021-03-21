package com.sej.escape.service.file;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import com.sej.escape.dto.FileReqDto;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class S3FileManageService implements FileManageService {

    private final TransferManager transferManager = TransferManagerBuilder.defaultTransferManager();
    @Value("${cloud.aws.s3.bucket}")
    private final String bucketName;

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
