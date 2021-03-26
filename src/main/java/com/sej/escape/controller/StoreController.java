package com.sej.escape.controller;

import com.sej.escape.dto.store.StorePageReqDto;
import com.sej.escape.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
@Slf4j
public class StoreController {

    private final StoreService storeService;

    @PostMapping
    public void getStores(@RequestBody StorePageReqDto storePageReqDto){
        storeService.getStores(storePageReqDto);
    }

    @GetMapping("/{id}")
    public void getStore(@PathVariable long id){
    }

}
