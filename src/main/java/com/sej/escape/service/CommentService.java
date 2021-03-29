package com.sej.escape.service;

import com.sej.escape.dto.CommentDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.comment.Comment;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.repository.comment.ThemeCommentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService {

    private ThemeCommentRepository themeCommentRepository;
    private ModelMapper modelMapper;

    public List<CommentDto> readTopComments(PageReqDto pageReqDto){
        /*
        메인페이지 최대 10개
        좋아요 + 최신순
        */
        Sort sort = Sort.by(Sort.Direction.DESC, "good", "regDate");
        Pageable pageable = pageReqDto.getPageable(sort);

        LocalDateTime aWeekAgo = LocalDateTime.now().minusWeeks(1);

        Page<ThemeComment> comments = themeCommentRepository.findTopComments(aWeekAgo, pageable);

        List<CommentDto> commentDtos = mapEntityToDto(comments.getContent());

        return commentDtos;
   }

   public List<CommentDto> readLatestComments(PageReqDto pageReqDto){

        Pageable pageable = pageReqDto.getPageable(Sort.by("regDate").descending());

        Page<ThemeComment> comments = themeCommentRepository.findLatestComments(pageable);

        List<CommentDto> commentDtos = mapEntityToDto(comments.getContent());

        return commentDtos;
    }

    private <T extends Comment> List<CommentDto> mapEntityToDto(List<T> entities){
        return entities.stream()
                    .map(comment -> modelMapper.map(comment, CommentDto.class))
                    .collect(Collectors.toList());
    }

}
