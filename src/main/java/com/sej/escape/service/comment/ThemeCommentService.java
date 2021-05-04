package com.sej.escape.service.comment;

import com.sej.escape.dto.comment.*;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.Theme;
import com.sej.escape.entity.comment.StoreComment;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.repository.comment.ThemeCommentRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ThemeCommentService {

    private final ThemeCommentRepository themeCommentRepository;
    private final AuthenticationUtil authenticationUtil;
    private final CommentMapper commentMapper;

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

    private List<ThemeCommentDto> mapStoreCommentsToDtos(List<Object[]> entitis){
        return entitis.stream().map(e -> {

            ThemeComment themeComment = (ThemeComment) e[0];
            Theme theme = (Theme) e[1];

            ThemeCommentDto dto = commentMapper.mapEntityToDto(themeComment, ThemeCommentDto.class);
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
