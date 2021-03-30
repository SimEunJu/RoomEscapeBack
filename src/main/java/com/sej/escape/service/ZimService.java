package com.sej.escape.service;

import com.sej.escape.dto.MemberDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.zim.StoreZim;
import com.sej.escape.entity.zim.Zim;
import com.sej.escape.repository.zim.StoreZimRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.geolatte.geom.M;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ZimService {

    private final StoreZimRepository storeZimRepository;
    private final AuthenticationUtil authenticationUtil;

    public void toggleStoreZim(long storeId, boolean isZimSet){
        MemberDto memberDto = authenticationUtil.getAuthUser();
        Member member = Member.builder().id(memberDto.getId()).build();
        Optional<StoreZim> storeZimOpt = storeZimRepository.findByReferIdAndMember(storeId, member);
        StoreZim storeZim = null;
        if(storeZimOpt.isPresent()){
            storeZim = storeZimOpt.get();
            storeZim.setZim(isZimSet);
        }else{
            storeZim = StoreZim.builder()
                    .isZim(isZimSet)
                    .referId(storeId)
                    .build();
        }
        storeZimRepository.save(storeZim);
    }

}
