package com.sej.escape.service.file.upload;

import com.sej.escape.dto.file.FileDto;
import com.sej.escape.dto.file.FileReqDto;
import com.sej.escape.error.exception.file.LocalFileUploadException;
import com.sej.escape.service.file.manage.FileManageService;
import lombok.NoArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@NoArgsConstructor
public class LocalFileManageService implements FileManageService {

    @Value("${spring.servlet.multipart.location}")
    private String rootPath;

    public FileDto uploadFile(FileDto fileDto){

        fileDto.setRootPath(rootPath);
        String subPath = fileDto.getSubPath();

        String uploadPath = rootPath + File.separator + subPath;
        uploadPath = uploadPath.replaceAll("/", File.separator);

        String nameWithFullPath = uploadPath + File.separator + fileDto.getName();
        Path savePath = Paths.get(nameWithFullPath);

        MultipartFile file = fileDto.getUploadFile();

        try {
            file.transferTo(savePath);

            String thumbnailNameWithFullPath = uploadPath + File.separator + "s_" + fileDto.getName();
            File thumbnail = new File(thumbnailNameWithFullPath);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbnail, 100, 100);

        } catch (IOException e) {
            throw new LocalFileUploadException(fileDto.toString(), e);
        }

        return fileDto;
    }



}
