package com.sej.escape.controller.zim;

import com.sej.escape.constants.ZimType;
import com.sej.escape.dto.zim.ZimDto;
import com.sej.escape.dto.zim.ZimListReqDto;
import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.dto.zim.ZimResDto;
import com.sej.escape.error.exception.UnDefinedConstantException;
import com.sej.escape.service.zim.StoreZimService;
import com.sej.escape.service.zim.ThemeZimService;
import com.sej.escape.service.zim.ZimService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/zim")
public class ZimController {

    private final ZimService zimService;
    private final ModelMapper modelMapper;

    @PatchMapping("/toggle")
    public ResponseEntity<ZimResDto> toggleZim(@Valid @RequestBody ZimReqDto zimReqDto){
        ZimType type = zimReqDto.getType();
        long zimId = zimService.toggleZim(zimReqDto);
        ZimResDto resDto = this.getResDto(zimId, zimReqDto);
        return ResponseEntity.ok(resDto);
    }


    private ZimResDto getResDto(long id, ZimReqDto reqDto){
        ZimResDto resDto = modelMapper.map(reqDto, ZimResDto.class);
        resDto.setId(id);
        return resDto;
    }
}
