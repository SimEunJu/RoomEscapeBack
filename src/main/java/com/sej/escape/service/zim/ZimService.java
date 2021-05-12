package com.sej.escape.service.zim;

import com.sej.escape.constants.ZimType;
import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.dto.zim.ZimResDto;
import com.sej.escape.entity.zim.StoreZim;
import com.sej.escape.entity.zim.Zim;
import com.sej.escape.error.exception.validation.UnDefinedConstantException;
import com.sej.escape.repository.zim.ZimRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ZimService {

    private final StoreZimService storeZimService;
    private final ThemeZimService themeZimService;

    public ZimResDto toggleZim(ZimReqDto reqDto){
        IZimService service = getServiceByType(reqDto.getType());
        Zim zim = service.toggleZim(reqDto);

        ZimResDto resDto = ZimResDto.builder()
                .id(zim.getId())
                .referId(zim.getReferId())
                .type(reqDto.getType().name().toLowerCase())
                .isChecked(zim.isZim())
                .build();
        return resDto;
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
