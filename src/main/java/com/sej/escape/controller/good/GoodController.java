package com.sej.escape.controller.good;

import com.sej.escape.dto.good.GoodReqDto;
import com.sej.escape.dto.good.GoodResDto;
import com.sej.escape.service.good.GoodService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/goods")
public class GoodController {

    private final GoodService goodService;

    @PatchMapping("/{id}")
    public ResponseEntity<GoodResDto> toggleGood(@Valid @RequestBody GoodReqDto goodReqDto){
        GoodResDto resDto = goodService.toggleGood(goodReqDto);
        return ResponseEntity.ok(resDto);
    }
}
