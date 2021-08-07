package com.sej.escape.service.file;

import com.querydsl.core.BooleanBuilder;
import com.sej.escape.constants.dto.FileType;
import com.sej.escape.dto.file.FileDto;
import com.sej.escape.dto.file.FileReqDto;
import com.sej.escape.dto.file.FileResDto;
import com.sej.escape.dto.file.FileUrlDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.file.BoardFile;
import com.sej.escape.entity.file.File;
import com.sej.escape.entity.file.QFile;
import com.sej.escape.entity.file.ThemeCommentFile;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.error.exception.validation.UnDefinedConstantException;
import com.sej.escape.repository.file.FileRepository;
import com.sej.escape.service.file.manage.FileManageService;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final FileRepository<File> fileRepository;
    private final FileRepository<BoardFile> boardFileRepository;
    private final FileRepository<ThemeCommentFile> themeCommentFileRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationUtil authenticationUtil;

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

    public int deleteFile(long id){
        Member member = authenticationUtil.getAuthUserEntity();
        return fileRepository.deleteFile(id, member);
    }

    public int deleteFile(FileUrlDto fileUrlDto){
        Member member = authenticationUtil.getAuthUserEntity();
        QFile qFile = QFile.file;
        BooleanBuilder builder = new BooleanBuilder(qFile.member.eq(member));
        builder.and(qFile.subPath.eq(fileUrlDto.getSubPath()));
        builder.and(qFile.name.eq(fileUrlDto.getName()));
        File file = fileRepository.findOne(builder).orElseThrow(() ->
                new NoSuchResourceException(String.format("%s 파일은 존재하지 않습니다.",
                        fileUrlDto.getSubPath()+fileUrlDto.getName())));
        return fileRepository.deleteFile(file.getId(), member);
    }

    public int updateReferIds(List<Long> ids, long referId){
        return fileRepository.updateReferIds(referId, ids);
    }

    public FileResDto saveFile(FileDto fileDto, FileManageService fileManageService) throws FileUploadException {
        fileManageService.uploadFile(fileDto);

        FileRepository repo = getRepoByType(fileDto.getType());
        Class<? extends File> entityCls = getEntityByType(fileDto.getType());

        File file = modelMapper.map(fileDto, entityCls);
        file.setMember(authenticationUtil.getAuthUserEntity());
        file = (File) repo.save(file);

        return mapEntityToResDto(file, fileDto);
    }

    private FileResDto mapEntityToResDto(File file, FileDto fileDto){
        FileResDto resDto = modelMapper.map(file, FileResDto.class);
        resDto.setType(fileDto.getType());
        resDto.setId(file.getId());
        return resDto;
    }
}
