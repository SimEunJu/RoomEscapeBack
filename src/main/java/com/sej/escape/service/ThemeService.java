package com.sej.escape.service;

import com.sej.escape.dto.CommentDto;
import com.sej.escape.dto.ThemeDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.Theme;
import com.sej.escape.entity.comment.ThemeComment;
import com.sej.escape.repository.ThemeRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ThemeService {

    private ThemeRepository themeRepository;
    private ModelMapper modelMapper;

    public List<ThemeDto> readTopThemes(PageReqDto pageReqDto){
        /*
        메인페이지 최대 10개
        좋아요 + 리뷰수 + 최신순
        */
        Sort sort = Sort.by(Sort.Direction.DESC, "good", "reviewCnt", "regDate");
        Pageable pageable = pageReqDto.getPageable(sort);
        Page<Theme> themes = themeRepository.findTopThemes(pageable);

        List<ThemeDto> themeDtos = mapEntityToDto(themes.getContent());
        return themeDtos;
    }

    public List<ThemeDto> readLatestThemes(PageReqDto pageReqDto){

        LocalDateTime aMonthAgo = LocalDateTime.now().minusMonths(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        Pageable pageable = pageReqDto.getPageable(sort);

        Page<Theme> themes = themeRepository.findLatestThemes(aMonthAgo, pageable);

        List<ThemeDto> themeDtos = mapEntityToDto(themes.getContent());
        return themeDtos;
    }

    private List<ThemeDto> mapEntityToDto(List<Theme> entities){
        return entities.stream()
                .map(entity -> modelMapper.map(entity, ThemeDto.class))
                .collect(Collectors.toList());
    }
}
