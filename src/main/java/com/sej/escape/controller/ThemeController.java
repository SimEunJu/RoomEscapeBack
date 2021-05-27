package com.sej.escape.controller;

import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.dto.store.StorePageReqDto;
import com.sej.escape.dto.theme.ThemeDto;
import com.sej.escape.dto.theme.ThemeForListDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.dto.theme.ThemeNameDto;
import com.sej.escape.dto.theme.ThemePageReqDto;
import com.sej.escape.service.theme.ThemeService;
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
    public ResponseEntity<PageResDto> getThemes(@Valid @RequestBody ThemePageReqDto pageReqDto){
        PageResDto resDto =  themeService.getThemes(pageReqDto);
        return ResponseEntity.ok(resDto);
    }

    @GetMapping("/zim")
    public ResponseEntity<PageResDto> getStoresByZim(StorePageReqDto storePageReqDto){
        PageResDto resDto = themeService.getStoresByZim(storePageReqDto);
        return ResponseEntity.ok(resDto);
    }

    @GetMapping("/by/{type}")
    public ResponseEntity<Map<String, Object>> getThemesByType(@PathVariable String type, @RequestParam Map<String, String> params){
        List<ThemeForListDto> themeForListDtos = null;
        PageReqDto pageReqDto = new PageReqDto();
        switch (type){
            case "latest":
                themeForListDtos = themeService.readLatestThemes(pageReqDto);
                break;
            case "top":
                themeForListDtos = themeService.readTopThemes();
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

    @GetMapping("/names/store")
    public ResponseEntity<List<ThemeNameDto>> getThemeNamesByStore(@RequestParam long storeId){
        List<ThemeNameDto> names = themeService.getThemeNamesByStore(storeId);
        return ResponseEntity.ok(names);
    }


    @GetMapping("/names")
    public ResponseEntity<List<ThemeForListDto>> getThemeNames(@RequestParam String keyword){
        List<ThemeForListDto> themes = themeService.getThemeNamesAndStore(keyword);
        return ResponseEntity.ok(themes);
    }

}
