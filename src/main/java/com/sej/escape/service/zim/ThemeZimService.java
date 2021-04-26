package com.sej.escape.service.zim;

import com.sej.escape.dto.zim.ZimDto;
import com.sej.escape.dto.zim.ZimListReqDto;
import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.zim.ThemeZim;
import com.sej.escape.entity.zim.ThemeZim;
import com.sej.escape.repository.zim.ThemeZimRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ThemeZimService implements IZimService{

    private final ThemeZimRepository themeZimRepository;
    private final AuthenticationUtil authenticationUtil;

    public Optional<ThemeZim> getByReferIdAndMember(long referId, Member member){
        return themeZimRepository.findByReferIdAndMember(referId, member);
    }

    @Override
    public long toggleZim(ZimReqDto reqDto){
        Member member = authenticationUtil.getAuthUserEntity();
        Optional<ThemeZim> zimOpt = this.getByReferIdAndMember(reqDto.getReferId(), member);

        ThemeZim zim = null;
        if(zimOpt.isPresent()){
            zim = zimOpt.get();
        }else{
            zim = ThemeZim.themeBuilder()
                    .referId(reqDto.getReferId())
                    .member(member)
                    .build();
        }
        zim.setZim(reqDto.getIsChecked());

        themeZimRepository.save(zim);
        return zim.getId();
    }
}
