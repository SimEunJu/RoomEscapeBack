package com.sej.escape.service.zim;

import com.sej.escape.dto.zim.ZimDto;
import com.sej.escape.dto.zim.ZimListReqDto;
import com.sej.escape.dto.zim.ZimReqDto;

import java.util.List;

public interface IZimService {

    long toggleZim(ZimReqDto reqDto);
}
