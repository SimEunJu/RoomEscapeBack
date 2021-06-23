package com.sej.escape.controller.good;

import com.sej.escape.dto.good.GoodReqDto;
import com.sej.escape.dto.good.GoodResDto;
import com.sej.escape.service.good.GoodService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/goods")
public class GoodController {

    private final GoodService goodService;
    private final ModelMapper modelMapper;

    @PatchMapping("/toggle/{id}")
    public ResponseEntity<GoodResDto> toggleGood(@Valid @RequestBody GoodReqDto goodReqDto){
        Long goodId = goodService.toggleGood(goodReqDto);
        GoodResDto resDto = this.getResDto(goodId, goodReqDto);
        return ResponseEntity.ok(resDto);
    }

    private GoodResDto getResDto(long id, GoodReqDto reqDto){
        GoodResDto resDto = modelMapper.map(reqDto, GoodResDto.class);
        resDto.setId(id);
        return resDto;
    }
}
