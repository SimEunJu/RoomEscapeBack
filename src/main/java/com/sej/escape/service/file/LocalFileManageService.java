package com.sej.escape.service.file;

import com.sej.escape.dto.file.FileReqDto;
import com.sej.escape.error.exception.file.LocalFileUploadException;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class LocalFileManageService implements FileManageService {

    @Value("${spring.servlet.multipart.location}")
    private final String rootPath;

    public FileReqDto uploadFile(FileReqDto fileReqDto){

        fileReqDto.setRootPath(rootPath);
        String subPath = fileReqDto.getSubPath();

        String uploadPath = rootPath + File.separator + subPath;
        uploadPath = uploadPath.replaceAll("/", File.separator);
        makePathFolder(uploadPath);

        String nameWithFullPath = uploadPath + File.separator + fileReqDto.getName();
        Path savePath = Paths.get(nameWithFullPath);

        MultipartFile file = fileReqDto.getFile();

        try {
            file.transferTo(savePath);

            String thumbnailNameWithFullPath = uploadPath + File.separator + "s_" + fileReqDto.getName();
            File thumbnail = new File(thumbnailNameWithFullPath);
            Thumbnailator.createThumbnail(savePath.toFile(), thumbnail, 100, 100);

        } catch (IOException e) {
            throw new LocalFileUploadException(fileReqDto.toString(), e);
        }

        return fileReqDto;
    }

    private String makePathFolder(String path){

        File uploadPath = new File(path);

        if(uploadPath.exists() == false){
            uploadPath.mkdirs();
        }

        return path;
    }

}
