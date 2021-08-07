package com.sej.escape.controller.zim;

import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.dto.zim.ZimResDto;
import com.sej.escape.service.zim.ZimService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@RequestMapping("/api/zims")
@Api("찜")
public class ZimController {

    private final ZimService zimService;

    @ApiOperation("찜 토글")
    @PatchMapping("/toggle")
    public ResponseEntity<ZimResDto> toggleZim(@Valid @RequestBody ZimReqDto zimReqDto){
        ZimResDto resDto = zimService.toggleZim(zimReqDto);
        return ResponseEntity.ok(resDto);
    }
}
