package com.sej.escape.service.file;

import com.sej.escape.dto.file.FileReqDto;
import com.sej.escape.dto.file.FileResDto;
import com.sej.escape.entity.file.BoardFile;
import com.sej.escape.entity.file.File;
import com.sej.escape.repository.FileRepository;
import com.sej.escape.repository.file.BoardFileRepository;
import com.sej.escape.service.file.manage.FileManageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final BoardFileRepository boardFileRepository;
    private final ModelMapper modelMapper;

    public FileResDto saveFile(FileReqDto fileReqDto, FileManageService fileManageService) throws FileUploadException {
        fileManageService.uploadFile(fileReqDto);
        long fileId = 0;
        switch (fileReqDto.getType()){
            case BOARD: {
                BoardFile file = modelMapper.map(fileReqDto, BoardFile.class);
                boardFileRepository.save(file);
                fileId = file.getId();
                break;
            }
        }
        return FileResDto.builder()
                .url(fileReqDto.getUrl())
                .id(fileId)
                .randomId(fileReqDto.getId())
                .type(fileReqDto.getType())
                .build();
    }
}
