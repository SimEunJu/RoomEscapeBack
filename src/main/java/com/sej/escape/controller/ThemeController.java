package com.sej.escape.controller;

import com.sej.escape.dto.CommentDto;
import com.sej.escape.dto.ThemeDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/theme")
public class ThemeController {

    private ThemeService themeService;
    /*
    @GetMapping("/top")
    public ResponseEntity<List<CommentDto>> getTopReviews(PageReqDto pageReqDto){
        List<ThemeDto> comments = themeService.readTopThemes(pageReqDto);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/latest")
    public ResponseEntity<List<CommentDto>> getLatestReviews(PageReqDto pageReqDto){
        List<ThemeDto> comments = themeService.readLatestThemes(pageReqDto);
        return ResponseEntity.ok(comments);
    }

    */
}
