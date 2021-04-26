package com.sej.escape.controller;

import com.sej.escape.dto.store.StoreDto;
import com.sej.escape.dto.store.StorePageReqDto;
import com.sej.escape.service.StoreService;
import com.sun.mail.iap.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
@Slf4j
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public ResponseEntity<List<StoreDto>> getStores(@RequestBody StorePageReqDto storePageReqDto){
        List<StoreDto> stores = storeService.getStores(storePageReqDto);
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/zim")
    public ResponseEntity<List<StoreDto>> getStoresByZim(StorePageReqDto storePageReqDto){
        List<StoreDto> stores = storeService.getStoresByZim(storePageReqDto);
        return ResponseEntity.ok(stores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreDto> getStore(@PathVariable long id){
        StoreDto storeDto = storeService.getStore(id);
        return ResponseEntity.ok(storeDto);
    }

}
