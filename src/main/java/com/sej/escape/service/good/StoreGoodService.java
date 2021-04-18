package com.sej.escape.service.good;

import com.sej.escape.dto.zim.ZimReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.good.StoreGood;
import com.sej.escape.entity.zim.StoreZim;
import com.sej.escape.repository.good.StoreGoodRepository;
import com.sej.escape.repository.zim.StoreZimRepository;
import com.sej.escape.service.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreGoodService {

    private final StoreGoodRepository storeGoodRepository;
    private final MemberMapper memberMapper;

    public Optional<StoreGood> getByReferIdAndMember(long referId, Member member){
        return storeGoodRepository.findByReferIdAndMember(referId, member);
    }

    public long toggleZim(ZimReqDto reqDto){
        Member member = memberMapper.getMemberEntityForRef();
        Optional<StoreGood> StoreGoodOpt = this.getByReferIdAndMember(reqDto.getReferId(), member);

        StoreGood good = null;
        if(StoreGoodOpt.isPresent()){
            good = StoreGoodOpt.get();
        }else{
            good = StoreGood.storeBuilder()
                    .referId(reqDto.getReferId())
                    .member(member)
                    .build();
        }
        good.setGood(reqDto.getIsChecked());

        storeGoodRepository.save(good);

        return good.getId();
    }
}
