package com.sej.escape.controller;

import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.dto.store.StoreDto;
import com.sej.escape.dto.store.StoreNameDto;
import com.sej.escape.dto.store.StorePageReqDto;
import com.sej.escape.dto.theme.ThemeDto;
import com.sej.escape.dto.theme.ThemeNameDto;
import com.sej.escape.service.store.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
@Slf4j
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<PageResDto> getStores(@RequestBody StorePageReqDto storePageReqDto){
        PageResDto resDto = storeService.getStores(storePageReqDto);
        return ResponseEntity.ok(resDto);
    }

    @GetMapping("/zim")
    public ResponseEntity<PageResDto> getStoresByZim(StorePageReqDto storePageReqDto){
        PageResDto resDto = storeService.getStoresByZim(storePageReqDto);
        return ResponseEntity.ok(resDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDto> getStore(@PathVariable long id){
        StoreDto storeDto = storeService.getStore(id);
        return ResponseEntity.ok(storeDto);
    }

    @GetMapping("/theme/{themeId}/name")
    public ResponseEntity<StoreNameDto> getStoreNameOfTheme(@PathVariable long themeId) {
        StoreNameDto nameDto = storeService.getStoreByTheme(themeId);
        return ResponseEntity.ok(nameDto);
    }

    @GetMapping("/names")
    public ResponseEntity<List<StoreNameDto>> getStoreNames(@RequestParam String keyword){
        List<StoreNameDto> stores = storeService.getStoresByName(keyword);
        return ResponseEntity.ok(stores);
    }
}
