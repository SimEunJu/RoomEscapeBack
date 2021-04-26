package com.sej.escape.controller.zim;

import com.sej.escape.dto.zim.ZimDto;
import com.sej.escape.dto.zim.ZimListReqDto;
import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.service.zim.StoreZimService;
import com.sej.escape.service.zim.ZimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/zim/store")
@RequiredArgsConstructor
public class StoreZimController {
}
