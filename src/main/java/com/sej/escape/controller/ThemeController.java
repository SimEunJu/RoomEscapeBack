package com.sej.escape.controller;

import com.sej.escape.dto.theme.ThemeDto;
import com.sej.escape.dto.theme.ThemeForListDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.dto.theme.ThemePageReqDto;
import com.sej.escape.service.ThemeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/theme")
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping("{id}")
    public ResponseEntity<ThemeDto> getTheme(@PathVariable long id) {
        ThemeDto themeDto = themeService.getTheme(id);
        return ResponseEntity.ok(themeDto);
    }

    @PostMapping
    public ResponseEntity<List<ThemeForListDto>> getThemes(@Valid @RequestBody ThemePageReqDto pageReqDto){
        List<ThemeForListDto> themeDtos = themeService.getThemes(pageReqDto);
        return ResponseEntity.ok(themeDtos);
    }

    @GetMapping("/by/{type}")
    public ResponseEntity<Map<String, Object>> getThemesByType(@PathVariable String type){
        List<ThemeForListDto> themeForListDtos = null;
        PageReqDto pageReqDto = new PageReqDto();
        switch (type){
            case "latest":
                themeForListDtos = themeService.readLatestThemes(pageReqDto);
                break;
            case "top":
                themeForListDtos = themeService.readTopThemes(pageReqDto);
                break;
            default:
                themeForListDtos = new ArrayList<>();
                break;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("type", type);
        map.put("list", themeForListDtos);
        return ResponseEntity.ok(map);
    }
}
