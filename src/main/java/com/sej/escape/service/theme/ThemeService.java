package com.sej.escape.service.theme;

import com.google.common.base.Strings;
import com.querydsl.core.util.ArrayUtils;
import com.sej.escape.constants.AreaSection;
import com.sej.escape.constants.ListOrder;

import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.dto.store.StorePageReqDto;
import com.sej.escape.dto.theme.*;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.Theme;
import com.sej.escape.entity.zim.ThemeZim;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.ThemeRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final AuthenticationUtil authenticationUtil;
    private final EntityManager em;
    private final ThemeMapper mapper;

    public List<ThemeForListDto> getThemeNamesAndStore(String keyword){
        Pageable pageable = PageRequest.of(1, 20);

        List<Theme> themes = themeRepository.findAllByIsDeletedFalseAndNameContaining(keyword);
        return mapper.mapEntitiesToDtos(themes, ThemeForListDto.class);
    }

    public ThemeDto getTheme(long id){

        String querySelectIsZimChk = ", (SELECT 0) as is_zim_chk ";
        if(authenticationUtil.isAuthenticated()){
            long memberId = authenticationUtil.getAuthUser().getId();
            querySelectIsZimChk = ", (SELECT COUNT(IF(member_id = "+memberId+", 1, NULL)) FROM zim WHERE ztype='T' AND refer_id = theme.theme_id AND is_zim = 1) as is_zim_chk ";
        }

        String queryWhere = " AND theme.theme_id = "+id;

        String queryStr = getThemeQuery(querySelectIsZimChk, queryWhere);

        Object[] result = null;
        try{
            result = (Object[]) em.createNativeQuery(queryStr, "themeResultMap").getSingleResult();
        } catch (NoResultException e){
            throw throwNoSuchResourceException(id);
        }

        ThemeDto themeDto = mapper.mapThemeRowToDto(result, ThemeDto.class);
        long storeId = themeDto.getStore().getId();
        List<Theme> themes = getThemesUnderSameStore(storeId);
        List<ThemeForListDto> relatedThemes = mapper.mapEntitiesToDtos(themes, ThemeForListDto.class);

        themeDto.setRelated(relatedThemes);
        return themeDto;
    }

    public List<ThemeNameDto> getThemeNamesByStore(long storeId){
        List<Theme> themes = getThemesUnderSameStore(storeId);
        return mapper.mapEntitiesToDtos(themes, ThemeNameDto.class, (Theme theme, ThemeNameDto dto) -> { dto.setName(theme.getName()); return dto; });
    }

    private List<Theme> getThemesUnderSameStore(long storeId){
        Store store = Store.builder().id(storeId).build();
        return themeRepository.findAllByIsDeletedFalseAndStoreEquals(store);
    }

    private NoSuchResourceException throwNoSuchResourceException(long id){
        return new NoSuchResourceException(
                String.format("%d와 일치하는 카페가 존재하지 않습니다.", id) );
    }

    public PageResDto getStoresByZim(StorePageReqDto reqDto){
        Member member = authenticationUtil.getAuthUserEntity();

        Sort sort = Sort.by(Sort.Direction.DESC, "updateDate");
        Pageable pageable = reqDto.getPageable(sort);

        Page<Object[]> themesPage = themeRepository.findallByZim(member, pageable);
        PageResDto resDto = new PageResDto(themesPage,
                (objects) -> {
                    Object[] themeZim = (Object[]) objects;
                    Theme theme = (Theme) themeZim[0];
                    ThemeZim zim = (ThemeZim) themeZim[1];
                    Store store = (Store) themeZim[2];

                    ThemeZimListResDto themeZimListResDto = ThemeZimListResDto.builder()
                            .id(theme.getId())
                            .zimId(zim.getId())
                            .name(theme.getName())
                            .isZimChecked(true)
                            .storeName(store.getName())
                            .build();
                    return themeZimListResDto;
                });
        return resDto;
    }

    public List<ThemeForListDto> getThemes(ThemePageReqDto themePageReqDto){
        String queryWhere = "";
        String queryOrder = "";

        String searchKeyword = themePageReqDto.getSearchKeyword();
        if(!Strings.isNullOrEmpty(searchKeyword)){
            queryWhere += " AND theme.name LIKE '%"+searchKeyword+"%'";
        }

        AreaSection[] areaSections = themePageReqDto.getAreaSection();
        if(!ArrayUtils.isEmpty(areaSections)){
            queryWhere += " AND";

            int len = areaSections.length;
            for (int i=0; i<len; i++) {
                AreaSection areaSection = areaSections[i];
                AreaSection.AreaCode areaCode = areaSection.getAreaCodeByPostcode();
                double lower = areaCode.getLower();
                double upper = areaCode.getUpper();
                queryWhere += " store.area_code between "+lower+" AND "+upper+"";
                if(i < len-1) queryWhere += " OR";
            }
        }

        ListOrder order = themePageReqDto.getOrder();
        if(order != null){
            switch (order){
                case LATEST:
                    queryOrder += " theme.reg_date DESC";
                    break;
                case ZIM:
                    queryOrder += " zim_cnt DESC";
                    break;
                case CLOSEST:
                    double latitude = themePageReqDto.getLatitude();
                    double longitude = themePageReqDto.getLongitude();
                    if(latitude != 0 && longitude != 0) {
                        queryOrder += " ST_DISTANCE_SPHERE(POINT(" + longitude + ", " + latitude + "), store.location) ASC";
                    }
                    break;
                default:
                    queryOrder += " theme.theme_id DESC";
                    break;
            }
        }

        String querySelectIsZimChk = ", (SELECT 0) as is_zim_chk ";
        if(authenticationUtil.isAuthenticated()){
            long memberId = authenticationUtil.getAuthUser().getId();
            querySelectIsZimChk = ", (SELECT COUNT(IF(member_id = "+memberId+", 1, NULL)) FROM zim WHERE ztype='T' AND refer_id = theme.theme_id AND is_zim = 1) as is_zim_chk ";
        }

        String queryStr = getThemesQuery(querySelectIsZimChk, queryWhere, queryOrder);

        List<Object[]> results = em.createNativeQuery(queryStr, "themeResultMap")
                .setFirstResult(themePageReqDto.getPage())
                .setMaxResults(themePageReqDto.getSize())
                .getResultList();

        List<ThemeForListDto> themeForListDtos = new ArrayList<>();
        for(Object[] row : results){
            themeForListDtos.add(mapper.mapThemeRowToDto(row, ThemeForListDto.class));
        }
        return themeForListDtos;
    }

    private String getThemesQuery(String querySelectIsZimChk, String queryWhere, String queryOrder){
        return getThemeQuery(querySelectIsZimChk, queryWhere) +
                "ORDER BY"+queryOrder;
    }

    private String getThemeQuery(String querySelectIsZimChk, String queryWhere){
        String queryStr = "SELECT theme.*, store.*, file.root_path, file.sub_path,  " +
                "(SELECT AVG(star) FROM theme_comment WHERE theme_id = theme.theme_id and theme_comment.is_deleted = 0) as star_avg, " +
                "(SELECT COUNT(*) FROM zim WHERE ztype='T' AND refer_id = theme.theme_id AND is_zim = 1) as zim_cnt " +
                querySelectIsZimChk +
                "FROM theme INNER JOIN store ON theme.store_id = store.store_id " +
                "LEFT OUTER JOIN file ON file.ftype = 'T' AND file.refer_id = theme.theme_id " +
                "WHERE theme.is_deleted = 0 "+queryWhere;
        return queryStr;
    }

    public List<ThemeForListDto> readTopThemes(PageReqDto pageReqDto){
        /*
        메인페이지 최대 10개
        좋아요 + 리뷰수 + 최신순
        */
        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        Pageable pageable = pageReqDto.getPageable(sort);
        Page<Theme> themes = themeRepository.findTopThemes(pageable);

        List<ThemeForListDto> themeForListDtos = mapper.mapEntitiesToDtos(themes.getContent(), ThemeForListDto.class);
        return themeForListDtos;
    }

    public List<ThemeForListDto> readLatestThemes(PageReqDto pageReqDto){

        LocalDateTime aMonthAgo = LocalDateTime.now().minusMonths(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        Pageable pageable = pageReqDto.getPageable(sort);

        Page<Theme> themes = themeRepository.findLatestThemes(aMonthAgo, pageable);

        List<ThemeForListDto> themeForListDtos = mapper.mapEntitiesToDtos(themes.getContent(), ThemeForListDto.class);
        return themeForListDtos;
    }


}