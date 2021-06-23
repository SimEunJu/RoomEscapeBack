package com.sej.escape.controller.zim;

import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.dto.zim.ZimResDto;
import com.sej.escape.service.zim.ZimService;
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
public class ZimController {

    private final ZimService zimService;
    private final ModelMapper modelMapper;

    @PatchMapping("/toggle")
    public ResponseEntity<ZimResDto> toggleZim(@Valid @RequestBody ZimReqDto zimReqDto){
        ZimResDto resDto = zimService.toggleZim(zimReqDto);
        return ResponseEntity.ok(resDto);
    }
}
