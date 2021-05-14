package com.sej.escape.service.good;

import com.sej.escape.constants.GoodType;
import com.sej.escape.dto.good.GoodReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.good.*;
import com.sej.escape.error.exception.validation.UnDefinedConstantException;
import com.sej.escape.repository.good.GoodRepository;
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
    private final GoodRepository<StoreCommentGood> storeCommentGoodRepository;
    private final GoodRepository<ThemeCommentGood> themeCommentGoodRepository;
    private final GoodRepository<NoticeBoardCommentGood> noticeBoardCommentGoodRepository;
    private final GoodRepository<ReqBoardCommentGood> reqBoardCommentGoodRepository;

    private final AuthenticationUtil authenticationUtil;

    private GoodRepository<? extends Good> getRepoByType(GoodType type){
        switch (type){
            case STORE: return storeGoodRepository;
            case THEME: return themeGoodRepository;
            case COMMENT_STORE: return storeCommentGoodRepository;
            case COMMENT_THEME: return themeCommentGoodRepository;
            case COMMENT_BOARD_NOTICE: return noticeBoardCommentGoodRepository;
            case COMMENT_BOARD_REQ: return reqBoardCommentGoodRepository;
            default: throw new UnDefinedConstantException("");
        }
    }

    private Class<? extends Good> getEntityByType(GoodType type){
        switch (type){
            case STORE: return StoreGood.class;
            case THEME: return ThemeGood.class;
            case COMMENT_STORE: return StoreCommentGood.class;
            case COMMENT_THEME: return ThemeCommentGood.class;
            case COMMENT_BOARD_NOTICE: return NoticeBoardCommentGood.class;
            case COMMENT_BOARD_REQ: return ReqBoardCommentGood.class;
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
