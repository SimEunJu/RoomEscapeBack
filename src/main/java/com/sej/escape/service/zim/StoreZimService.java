package com.sej.escape.service.zim;

import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.zim.StoreZim;
import com.sej.escape.entity.zim.Zim;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.zim.StoreZimRepository;
import com.sej.escape.service.member.MemberMapper;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreZimService {

    private final StoreZimRepository storeZimRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationUtil authenticationUtil;

    public Optional<StoreZim> getByReferIdAndMember(long referId, Member member){
        return storeZimRepository.findByReferIdAndMember(referId, member);
    }
    public long toggleZim(ZimReqDto reqDto){
        Member member = authenticationUtil.getAuthUserEntity();
        Optional<StoreZim> zimOpt = this.getByReferIdAndMember(reqDto.getReferId(), member);

        StoreZim zim = null;
        if(zimOpt.isPresent()){
            zim = zimOpt.get();
        }else{
            zim = StoreZim.storeBuilder()
                    .referId(reqDto.getReferId())
                    .member(member)
                    .build();
        }
        zim.setZim(reqDto.getIsChecked());

        storeZimRepository.save(zim);
        return zim.getId();
    }
}
