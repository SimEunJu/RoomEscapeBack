package com.sej.escape.service.zim;

import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.zim.StoreZim;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.zim.StoreZimRepository;
import com.sej.escape.service.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreZimService {

    private StoreZimRepository storeZimRepository;
    private final MemberMapper memberMapper;

    public Optional<StoreZim> getByReferIdAndMember(long referId, Member member){
        return storeZimRepository.findByReferIdAndMember(referId, member);
    }

    public long toggleZim(ZimReqDto reqDto){
        Member member = memberMapper.getMemberEntityForRef();
        Optional<StoreZim> storeZimOpt = this.getByReferIdAndMember(reqDto.getReferId(), member);

        StoreZim zim = null;
        if(storeZimOpt.isPresent()){
            zim = storeZimOpt.get();
        }else{
            zim = StoreZim.builder()
                    .referId(reqDto.getReferId())
                    .member(member)
                    .build();
        }
        zim.setZim(reqDto.getIsChecked());

        storeZimRepository.save(zim);

        return zim.getId();
    }
}
