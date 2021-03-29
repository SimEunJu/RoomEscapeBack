package com.sej.escape.controller;

import com.sej.escape.dto.CommentDto;
import com.sej.escape.dto.ThemeDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/theme")
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping
    public ResponseEntity<List<ThemeDto>> getThemes(){
        List<ThemeDto> themeDtos = new ArrayList<>();
        return ResponseEntity.ok(themeDtos);
    }

    @GetMapping("/{type}")
    public ResponseEntity<Map<String, Object>> getThemesByType(@PathVariable String type){
        List<ThemeDto> themeDtos = null;
        PageReqDto pageReqDto = new PageReqDto();
        switch (type){
            case "latest":
                themeDtos = themeService.readLatestThemes(pageReqDto);
                break;
            case "top":
                themeDtos = themeService.readTopThemes(pageReqDto);
                break;
            default:
                themeDtos = new ArrayList<>();
                break;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("list", themeDtos);
        return ResponseEntity.ok(map);
    }
}
