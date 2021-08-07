package com.sej.escape.controller;

import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.dto.store.StoreDto;
import com.sej.escape.dto.store.StoreNameDto;
import com.sej.escape.dto.store.StorePageReqDto;
import com.sej.escape.dto.theme.ThemeDto;
import com.sej.escape.dto.theme.ThemeNameDto;
import com.sej.escape.service.store.StoreService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Slf4j
@Api("가게")
public class StoreController {

    private final StoreService storeService;

    @ApiOperation(value = "가게 리스트")
    @PostMapping
    public ResponseEntity<PageResDto> getStores(@RequestBody StorePageReqDto storePageReqDto){
        PageResDto resDto = storeService.getStores(storePageReqDto);
        return ResponseEntity.ok(resDto);
    }

    @ApiOperation(value = "사용자가 찜한 가게 리스트")
    @GetMapping("/zim")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PageResDto> getStoresByZim(PageReqDto pageReqDto){
        PageResDto resDto = storeService.getStoresByZim(pageReqDto);
        return ResponseEntity.ok(resDto);
    }

    @ApiOperation(value = "특정 가게")
    @GetMapping("/{id}")
    public ResponseEntity<StoreDto> getStore(@ApiParam("가게 아이디") @PathVariable long id){
        StoreDto storeDto = storeService.getStore(id);
        return ResponseEntity.ok(storeDto);
    }

    @ApiOperation(value = "특정 테마를 가지고 있는 가게명")
    @GetMapping("/themes/{themeId}/name")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StoreNameDto> getStoreNameOfTheme(@ApiParam("테마 아이디") @PathVariable long themeId) {
        StoreNameDto nameDto = storeService.getStoreByTheme(themeId);
        return ResponseEntity.ok(nameDto);
    }

    @ApiOperation(value = "키워드를 포함하는 가게명 리스트")
    @GetMapping("/names")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<StoreNameDto>> getStoreNames(@ApiParam("검색 키워드") @RequestParam String keyword){
        List<StoreNameDto> stores = storeService.getStoresByName(keyword);
        return ResponseEntity.ok(stores);
    }
}
