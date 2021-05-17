package com.sej.escape.service.file;

import com.sej.escape.constants.FileType;
import com.sej.escape.dto.file.FileReqDto;
import com.sej.escape.dto.file.FileResDto;
import com.sej.escape.entity.file.BoardFile;
import com.sej.escape.entity.file.File;
import com.sej.escape.entity.file.ThemeCommentFile;
import com.sej.escape.error.exception.validation.UnDefinedConstantException;
import com.sej.escape.repository.file.FileRepository;
import com.sej.escape.service.file.manage.FileManageService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final FileRepository<File> fileRepository;
    private final FileRepository<BoardFile> boardFileRepository;
    private final FileRepository<ThemeCommentFile> themeCommentFileRepository;
    private final ModelMapper modelMapper;

    private FileRepository<? extends File> getRepoByType(FileType type){
        switch (type){
            case BOARD: return boardFileRepository;
            case THEMECOMMENT: return themeCommentFileRepository;
            default: throw new UnDefinedConstantException("");
        }
    }

    private Class<? extends File> getEntityByType(FileType type){
        switch (type){
            case BOARD: return BoardFile.class;
            case THEMECOMMENT: return ThemeCommentFile.class;
            default: throw new UnDefinedConstantException("");
        }
    }

    public int deleteFiles(List<Long> ids){
        return fileRepository.deleteFiles(ids);
    }

    public int updateReferIds(List<Long> ids, long referId){
        return fileRepository.updateReferIds(referId, ids);
    }

    public FileResDto saveFile(FileReqDto reqDto, FileManageService fileManageService) throws FileUploadException {
        fileManageService.uploadFile(reqDto);

        FileRepository repo = getRepoByType(reqDto.getType());
        Class<? extends File> entityCls = getEntityByType(reqDto.getType());

        File file = modelMapper.map(reqDto, entityCls);
        file = (File) repo.save(file);

        return mapEntityToResDto(file, reqDto);
    }

    private FileResDto mapEntityToResDto(File file, FileReqDto reqDto){
        FileResDto resDto = modelMapper.map(file, FileResDto.class);
        resDto.setType(reqDto.getType());
        resDto.setRandomId(reqDto.getRandomId());
        resDto.setId(file.getId());
        return resDto;
    }
}
