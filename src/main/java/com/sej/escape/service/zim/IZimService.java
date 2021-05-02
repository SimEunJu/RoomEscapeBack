package com.sej.escape.service.zim;

import com.sej.escape.dto.zim.ZimDto;
import com.sej.escape.dto.zim.ZimListReqDto;
import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.dto.zim.ZimResDto;
import com.sej.escape.entity.zim.Zim;

import java.util.List;

public interface IZimService {

    Zim toggleZim(ZimReqDto reqDto);
}
