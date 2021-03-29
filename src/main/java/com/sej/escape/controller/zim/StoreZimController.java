package com.sej.escape.controller.zim;

import com.sej.escape.service.ZimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/store/zim")
@RequiredArgsConstructor
public class StoreZimController {

    private final ZimService zimService;

    @PatchMapping("/{id}")
    public ResponseEntity<Boolean> toggleZim(@PathVariable long id, boolean isZimSet){
        boolean isZimSet = zimService.toggleStoreZim(id, isZimSet);
        return ResponseEntity.ok(isZimSet);
    }

}
