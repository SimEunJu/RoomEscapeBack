package com.sej.escape.controller.zim;

import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.dto.zim.ZimResDto;
import com.sej.escape.service.zim.StoreZimService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/zim")
public class ZimController {

    private final StoreZimService storeZimService;
    private final ModelMapper modelMapper;

    @PatchMapping("/new")
    public ResponseEntity<ZimResDto> toggleZim(@Valid ZimReqDto zimReqDto){
        String type = zimReqDto.getType();
        Long zimId = null;
        switch (type){
            case "store": zimId = storeZimService.toggleZim(zimReqDto);
        }
        ZimResDto resDto = this.getResDto(zimId, zimReqDto);
        return ResponseEntity.ok(resDto);
    }

    private ZimResDto getResDto(long id, ZimReqDto reqDto){
        ZimResDto resDto = modelMapper.map(reqDto, ZimResDto.class);
        resDto.setId(id);
        return resDto;
    }
}
