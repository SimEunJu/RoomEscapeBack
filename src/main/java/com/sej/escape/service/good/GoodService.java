package com.sej.escape.service.good;

import com.sej.escape.constants.dto.GoodType;
import com.sej.escape.dto.good.GoodReqDto;
import com.sej.escape.dto.good.GoodResDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.good.*;
import com.sej.escape.error.exception.validation.UnDefinedConstantException;
import com.sej.escape.repository.good.GoodRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    private GoodRepository<? extends Good> getRepoByType(GoodType type){
        switch (type){
            case STORE: return storeGoodRepository;
            case THEME: return themeGoodRepository;
            case STORE_COMMENT: return storeCommentGoodRepository;
            case THEME_COMMENT: return themeCommentGoodRepository;
            case BOARD_NOTICE_COMMENT: return noticeBoardCommentGoodRepository;
            case BOARD_REQ_COMMENT: return reqBoardCommentGoodRepository;
            default: throw new UnDefinedConstantException("");
        }
    }

    private Class<? extends Good> getEntityByType(GoodType type){
        switch (type){
            case STORE: return StoreGood.class;
            case THEME: return ThemeGood.class;
            case STORE_COMMENT: return StoreCommentGood.class;
            case THEME_COMMENT: return ThemeCommentGood.class;
            case BOARD_NOTICE_COMMENT: return NoticeBoardCommentGood.class;
            case BOARD_REQ_COMMENT: return ReqBoardCommentGood.class;
            default: throw new UnDefinedConstantException("");
        }
    }

    @SneakyThrows
    public GoodResDto toggleGood(GoodReqDto reqDto) {
        Member member = authenticationUtil.getAuthUserEntity();
        GoodRepository goodRepository = getRepoByType(reqDto.getType());
        Class<? extends Good> goodClass = getEntityByType(reqDto.getType());
        Optional<? extends Good> goodOpt = goodRepository.findByReferIdAndMember(reqDto.getReferId(), member);

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

        GoodResDto resDto = modelMapper.map(good, GoodResDto.class);
        resDto.setIsChecked(good.isGood());
        resDto.setType(reqDto.getType());

        return resDto;
    }
}
