package com.sej.escape.service.zim;

import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.entity.zim.Zim;

public interface IZimService {
    Zim toggleZim(ZimReqDto reqDto);
}
