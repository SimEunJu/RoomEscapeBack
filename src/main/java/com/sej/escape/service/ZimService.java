package com.sej.escape.service;

import com.sej.escape.entity.Member;
import com.sej.escape.repository.zim.StoreZimRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ZimService {

    private final StoreZimRepository storeZimRepository;
    private final AuthenticationUtil authenticationUtil;

    public boolean toggleStoreZim(long storeId, boolean isZimSet){
        Authentication authentication = authenticationUtil.getAuthentication().getPrincipal().;
        Member member = Member.builder().email(authentication.getName()).build();
        storeZimRepository.findByReferIdAndMember(storeId, member);
    }

}
