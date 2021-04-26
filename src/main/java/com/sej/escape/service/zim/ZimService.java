package com.sej.escape.service.zim;

import com.sej.escape.constants.ZimType;
import com.sej.escape.dto.zim.ZimDto;
import com.sej.escape.dto.zim.ZimListReqDto;
import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.zim.StoreZim;
import com.sej.escape.entity.zim.ThemeZim;
import com.sej.escape.entity.zim.Zim;
import com.sej.escape.error.exception.UnDefinedConstantException;
import com.sej.escape.repository.zim.StoreZimRepository;
import com.sej.escape.repository.zim.ThemeZimRepository;
import com.sej.escape.service.member.MemberMapper;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ZimService {

    private final StoreZimService storeZimService;
    private final ThemeZimService themeZimService;
    private final AuthenticationUtil authenticationUtil;

    public long toggleZim(ZimReqDto reqDto){
        long zimId = 0;
        IZimService service = getServiceByType(reqDto.getType());
        service.toggleZim(reqDto);
        return zimId;
    }

    private IZimService getServiceByType(ZimType zimType){
        switch (zimType){
            case STORE: return storeZimService;
            case THEME: return themeZimService;
            default:
                throw new UnDefinedConstantException("요청 type에는 찜 기능이 정의되어 있지 않습니다.");
        }
    }

}
