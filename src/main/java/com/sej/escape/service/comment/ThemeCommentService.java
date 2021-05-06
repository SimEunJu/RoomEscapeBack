package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Theme;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.comment.ThemeCommentRepository;
import com.sej.escape.service.file.FileService;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThemeCommentService {

    private final ThemeCommentRepository themeCommentRepository;
    private final ModelMapper modelMapper;
    private final FileService fileService;
    private final AuthenticationUtil authenticationUtil;
    private final CommentMapper commentMapper;

    public ThemeCommentResDto addComment(ThemeCommentDto commentDto){
        ThemeComment comment = commentMapper.mapDtoToEntity(commentDto, ThemeComment.class);
        Member member = authenticationUtil.getAuthUserEntity();
        Theme theme = Theme.builder().id(commentDto.getThemeId()).build();
        comment.setMember(member);
        comment.setTheme(theme);
        comment.setActive(commentDto.isActive());
        comment.setHorror(commentDto.isHorror());
        comment = themeCommentRepository.save(comment);

        if(commentDto.getUploadFiles().length > 0){
            List<Long> ids = Arrays.stream(commentDto.getUploadFiles()).map(file -> file.getId()).collect(Collectors.toList());
            fileService.updateReferIds(ids, comment.getId());
        }

        ThemeCommentResDto resDto = commentMapper.mapEntityToDto(comment, ThemeCommentResDto.class);
        return resDto;
    }

    public ThemeCommentResDto updateComment(long id, ThemeCommentDto modifyReqDto){
        ThemeComment comment = getCommentByIdIfExist(id);
        modelMapper.map(modifyReqDto, comment);
        ThemeComment commentUpdated = themeCommentRepository.save(comment);
        return commentMapper.mapEntityToDto(commentUpdated, ThemeCommentResDto.class);
    }

    public ThemeCommentDto getComment(long id){
        ThemeComment comment = getCommentByIdIfExist(id);
        return commentMapper.mapEntityToDto(comment, ThemeCommentDto.class);
    }

    private ThemeComment getCommentByIdIfExist(long id){
        Optional<ThemeComment> commentOpt = getCommentById(id);
        return getCommentIfExist(commentOpt, id);
    }

    private Optional<ThemeComment> getCommentById(long id){
        Optional<ThemeComment> commentOpt = themeCommentRepository.findById(id);
        return commentOpt;
    }

    private ThemeComment getCommentIfExist(Optional<ThemeComment> commentOpt, long id) {
        ThemeComment comment = commentOpt.orElseThrow( () ->
                new NoSuchResourceException(
                        String.format("%d와 일치하는 댓글이 존재하지 않습니다.", id)));
        return comment;
    }

    public CommentListResDto getCommentsByMember(CommentListReqDto reqDto){

        Sort sort = reqDto.getOrder().getSort();
        Pageable pageable = reqDto.getPageable(sort);
        Member member = authenticationUtil.getAuthUserEntity();

        Page<Object[]> commentPage = themeCommentRepository.findAllByMember(pageable, member);
        List<Object[]> themeComments = commentPage.getContent();
        return CommentListResDto.builder()
                .total(commentPage.getTotalElements())
                .comments(mapStoreCommentsToDtos(themeComments))
                .size(commentPage.getSize())
                .hasNext(commentPage.hasNext())
                .page(reqDto.getPage())
                .build();
    }

    private List<ThemeCommentForListDto> mapStoreCommentsToDtos(List<Object[]> entitis){
        return entitis.stream().map(e -> {

            ThemeComment themeComment = (ThemeComment) e[0];
            Theme theme = (Theme) e[1];

            ThemeCommentForListDto dto = commentMapper.mapEntityToDto(themeComment, ThemeCommentForListDto.class);
            dto.setName(theme.getThemeName());
            dto.setThemeId(theme.getId());

            return dto;
        }).collect(Collectors.toList());
    }

    public List<CommentDto> readTopComments(PageReqDto pageReqDto){
        /*
        메인페이지 최대 10개
        좋아요 + 최신순
        */
        Sort sort = Sort.by(Sort.Direction.DESC, "good", "regDate");
        Pageable pageable = pageReqDto.getPageable(sort);

        LocalDateTime aWeekAgo = LocalDateTime.now().minusWeeks(1);

        Page<ThemeComment> comments = themeCommentRepository.findTopComments(aWeekAgo, pageable);

        List<CommentDto> commentDtos = commentMapper.mapEntitesToDtos(comments.getContent(), CommentDto.class);

        return commentDtos;
    }

    public List<CommentDto> readLatestComments(PageReqDto pageReqDto){

        Pageable pageable = pageReqDto.getPageable(Sort.by("regDate").descending());

        Page<ThemeComment> comments = themeCommentRepository.findLatestComments(pageable);

        List<CommentDto> commentDtos = commentMapper.mapEntitesToDtos(comments.getContent(), CommentDto.class);

        return commentDtos;
    }


}
