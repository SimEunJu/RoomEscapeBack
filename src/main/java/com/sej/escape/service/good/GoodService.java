package com.sej.escape.service.good;

import com.sej.escape.dto.good.GoodReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.board.Board;
import com.sej.escape.entity.good.BoardGood;
import com.sej.escape.entity.good.Good;
import com.sej.escape.entity.good.StoreGood;
import com.sej.escape.entity.good.ThemeGood;
import com.sej.escape.error.exception.validation.UnDefinedConstantException;
import com.sej.escape.repository.good.GoodRepository;
import com.sej.escape.repository.good.StoreGoodRepository;
import com.sej.escape.repository.zim.StoreZimRepository;
import com.sej.escape.service.member.MemberMapper;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class GoodService {

    private final GoodRepository<StoreGood> storeGoodRepository;
    private final GoodRepository<ThemeGood> themeGoodRepository;
    private final GoodRepository<BoardGood> boardGoodRepository;
    private final AuthenticationUtil authenticationUtil;

    private GoodRepository<? extends Good> getRepoByType(String type){
        switch (type){
            case "store": return storeGoodRepository;
            case "theme": return themeGoodRepository;
            case "board": return boardGoodRepository;
            default: throw new UnDefinedConstantException("");
        }
    }

    private Class<? extends Good> getEntityByType(String type){
        switch (type){
            case "store": return StoreGood.class;
            case "theme": return ThemeGood.class;
            case "board": return BoardGood.class;
            default: throw new UnDefinedConstantException("");
        }
    }

    @SneakyThrows
    public long toggleGood(GoodReqDto reqDto) {
        Member member = authenticationUtil.getAuthUserEntity();
        GoodRepository goodRepository = getRepoByType(reqDto.getType());
        Class<? extends Good> goodClass = getEntityByType(reqDto.getType());
        Optional<Good> goodOpt = goodRepository.findByReferIdAndMember(reqDto.getReferId(), member);

        Good good = null;
        if(goodOpt.isPresent()){
            good = goodOpt.get();
        }else{
            good = goodClass.getDeclaredConstructor().newInstance();
            good.setReferId(reqDto.getReferId());
            good.setMember(member);
        }
        good.setGood(reqDto.getIsChecked());

        goodRepository.save(good);

        return good.getId();
    }
}
