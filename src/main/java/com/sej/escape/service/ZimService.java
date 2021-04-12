package com.sej.escape.service;

import com.sej.escape.dto.member.MemberDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.zim.StoreZim;
import com.sej.escape.repository.zim.StoreZimRepository;
import com.sej.escape.service.member.MemberMapper;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ZimService {

    private final StoreZimRepository storeZimRepository;
    private final AuthenticationUtil authenticationUtil;
    private final MemberMapper memberMapper;

    private Member getMemberEntityForRef(){
        MemberDto memberDto = authenticationUtil.getAuthUser();
        return memberMapper.mapDtoToEntityForRef(memberDto.getId());
    }

    public void toggleStoreZim(long storeId, boolean isZimSet){

        Member member = getMemberEntityForRef();
        Optional<StoreZim> storeZimOpt = storeZimRepository.findByReferIdAndMember(storeId, member);

        StoreZim zim = null;
        if(storeZimOpt.isPresent()){
            zim = storeZimOpt.get();
            zim.setZim(isZimSet);

        }else{
            zim = StoreZim.builder()
                    .isZim(isZimSet)
                    .referId(storeId)
                    .member(member)
                    .build();
        }
        storeZimRepository.save(zim);
    }

}
