package com.sej.escape.controller;

import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.dto.store.StorePageReqDto;
import com.sej.escape.dto.theme.ThemeDto;
import com.sej.escape.dto.theme.ThemeForListDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.dto.theme.ThemeNameDto;
import com.sej.escape.dto.theme.ThemePageReqDto;
import com.sej.escape.service.theme.ThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/themes")
@Api("테마")
public class ThemeController {

    private final ThemeService themeService;

    @ApiOperation("특정 테마")
    @GetMapping("/{id}")
    public ResponseEntity<ThemeDto> getTheme(@ApiParam("테마 아이디") @PathVariable long id) {
        ThemeDto themeDto = themeService.getTheme(id);
        return ResponseEntity.ok(themeDto);
    }

    @ApiOperation("테마 리스트")
    @PostMapping
    public ResponseEntity<PageResDto> getThemes(@Valid @RequestBody ThemePageReqDto pageReqDto){
        PageResDto resDto =  themeService.getThemes(pageReqDto);
        return ResponseEntity.ok(resDto);
    }

    @ApiOperation("최신 테마 리스트")
    @GetMapping("/latest")
    public ResponseEntity<List<ThemeForListDto>> getLatestThemes(){
        List<ThemeForListDto> latestThemes = themeService.getLatestThemes(new PageReqDto());
        return ResponseEntity.ok(latestThemes);
    }

    @ApiOperation("인기 테마 리스트")
    @GetMapping("/top")
    public ResponseEntity<List<ThemeForListDto>> getTopThemes(){
        List<ThemeForListDto> latestThemes = themeService.getTopThemes();
        return ResponseEntity.ok(latestThemes);
    }

    @ApiOperation("사용자가 찜한 테마 리스트")
    @GetMapping("/zim")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageResDto> getThemesByZim(ThemePageReqDto pageReqDto){
        PageResDto resDto = themeService.getThemesByZim(pageReqDto);
        return ResponseEntity.ok(resDto);
    }

    @ApiOperation("가게 내의 모든 테마명 리스트")
    @GetMapping("/stores/{storeId}/names")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ThemeNameDto>> getThemeNamesByStore(@ApiParam("가게 아이디") @PathVariable long storeId){
        List<ThemeNameDto> names = themeService.getThemeNamesByStore(storeId);
        return ResponseEntity.ok(names);
    }

    @ApiOperation("키워드를 포함하는 테마명 리스트")
    @GetMapping("/names")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ThemeNameDto>> getThemeNames(@ApiParam("검색 키워드") @RequestParam String keyword){
        List<ThemeNameDto> themes = themeService.getThemesByName(keyword);
        return ResponseEntity.ok(themes);
    }

}
