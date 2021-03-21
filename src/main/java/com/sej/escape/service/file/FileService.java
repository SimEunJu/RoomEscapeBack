package com.sej.escape.service.file;

import com.sej.escape.dto.FileReqDto;
import com.sej.escape.dto.FileResDto;
import com.sej.escape.entity.File;
import com.sej.escape.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;

    public FileResDto saveFile(FileReqDto fileReqDto, FileManageService fileManageService) throws FileUploadException {
        fileManageService.uploadFile(fileReqDto);
        File file = fileRepository.save(mapDtoToEntity(fileReqDto));
        return new FileResDto(fileReqDto.getUrl(), file.getSeq());
    }

    private <T> File mapDtoToEntity(T fileDto){
        return modelMapper.map(fileDto, File.class);
    }
}
