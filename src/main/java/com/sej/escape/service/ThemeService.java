package com.sej.escape.service;

import com.google.common.base.Strings;
import com.querydsl.core.util.ArrayUtils;
import com.sej.escape.constants.AreaSection;
import com.sej.escape.constants.AreaSectionComponent;
import com.sej.escape.constants.ListOrder;

import com.sej.escape.dto.store.StoreDto;
import com.sej.escape.dto.theme.ThemeDto;
import com.sej.escape.dto.theme.ThemeForListDto;
import com.sej.escape.dto.page.PageReqDto;
import com.sej.escape.dto.theme.ThemePageReqDto;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.Theme;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.ThemeRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeRepository themeRepository;
    private final ModelMapper modelMapper;
    private final AuthenticationUtil authenticationUtil;
    private final EntityManager em;
    private final AreaSectionComponent areaSectionComponent;

    @PostConstruct
    public void postConstruct(){
        this.modelMapper.createTypeMap(Theme.class, ThemeForListDto.class)
                .addMappings(mapper -> {
                    mapper.map(Theme::getThemeName, ThemeForListDto::setName);
                });

        this.modelMapper.createTypeMap(Theme.class, ThemeDto.class)
                .addMappings(mapper -> {
                    mapper.map(Theme::getThemeName, ThemeDto::setName);
                    mapper.map(Theme::getGenreByList, ThemeDto::setGenre);
                    mapper.map(Theme::getQuizTypeByList, ThemeDto::setQuizType);
                    mapper.map(src -> areaSectionComponent.getTitleFromAreaCode(src.getStore().getAreaCode(), new ArrayList<>()),
                            (dest, v) -> dest.getStore().setArea( (List<String>) v));
                });
    }

    public ThemeDto getTheme(long id){

        String querySelectIsZimChk = ", (SELECT 0) as is_zim_chk ";
        if(authenticationUtil.isAuthenticated()){
            long memberId = authenticationUtil.getAuthUser().getId();
            querySelectIsZimChk = ", (SELECT SUM(IF(member_id = "+memberId+", 1, 0)) FROM zim WHERE ztype='T' AND refer_id = store.store_id AND is_zim = 1) as is_zim_chk ";
        }

        String queryWhere = " AND theme.theme_id = "+id;

        String queryStr = getThemeQuery(querySelectIsZimChk, queryWhere);

        Object[] result = null;
        try{
            result = (Object[]) em.createNativeQuery(queryStr, "themeResultMap").getSingleResult();
        } catch (NoResultException e){
            throw throwNoSuchResourceException(id);
        }
        ThemeDto themeDto = mapThemeRowToDto(result, ThemeDto.class);
        long storeId = themeDto.getStore().getId();
        List<ThemeForListDto> relatedThemes = getThemesUnderSameStore(storeId);
        themeDto.setRelated(relatedThemes);
        return themeDto;
    }

    public List<ThemeForListDto> getThemesUnderSameStore(long storeId){
        Store store = Store.builder().id(storeId).build();
        List<Theme> themes = themeRepository.findAllByIsDeletedFalseAndStoreEquals(store);
        return themes.stream().map(theme -> modelMapper.map(theme, ThemeForListDto.class)).collect(Collectors.toList());
    }

    private NoSuchResourceException throwNoSuchResourceException(long id){
        return new NoSuchResourceException(
                String.format("%d와 일치하는 카페가 존재하지 않습니다.", id) );
    }

    public List<ThemeForListDto> getThemes(ThemePageReqDto themePageReqDto){
        String queryWhere = "";
        String queryOrder = "";

        String searchKeyword = themePageReqDto.getSearchKeyword();
        if(!Strings.isNullOrEmpty(searchKeyword)){
            queryWhere += " AND theme.theme_name LIKE '%"+searchKeyword+"%'";
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
            querySelectIsZimChk = ", (SELECT COUNT(IF(member_id = "+memberId+", 1, 0)) FROM zim WHERE ztype='T' AND refer_id = theme.theme_id AND is_zim = 1) as is_zim_chk ";
        }

        String queryStr = getThemesQuery(querySelectIsZimChk, queryWhere, queryOrder);

        List<Object[]> results = em.createNativeQuery(queryStr, "themeResultMap")
                .setFirstResult(themePageReqDto.getPage())
                .setMaxResults(themePageReqDto.getSize())
                .getResultList();

        List<ThemeForListDto> themeForListDtos = new ArrayList<>();
        for(Object[] row : results){
            themeForListDtos.add(mapThemeRowToDto(row, ThemeForListDto.class));
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
    private <T extends ThemeForListDto> T mapThemeRowToDto(Object[] row, Class<T> dtoCls){
        Theme theme = (Theme) row[0];

        T themeDto = modelMapper.map(theme, dtoCls);

        Store store = (Store) row[1];
        StoreDto storeDto = modelMapper.map(store, StoreDto.class);
        themeDto.setStore(storeDto);

        double starAvg = row[4] != null ? (double) row[4] : 0.0;
        themeDto.setStar(starAvg);

        String fileRootPath = (String) row[2];
        String fileSubPath = (String) row[3];
        themeDto.setImgUrl(fileRootPath+"/"+fileSubPath);

        int zimCnt = row[5] != null ? ((BigInteger) row[5]).intValue() : 0;
        boolean isMemberCheckZim = row[6] != null && ((BigInteger)row[6]).intValue() > 0;
        themeDto.setZimChecked(isMemberCheckZim);
        if(authenticationUtil.isAuthenticated()){
            zimCnt = isMemberCheckZim ? zimCnt - 1 : zimCnt;
        }
        themeDto.setZim(zimCnt);

        return themeDto;
    }
    public List<ThemeForListDto> readTopThemes(PageReqDto pageReqDto){
        /*
        메인페이지 최대 10개
        좋아요 + 리뷰수 + 최신순
        */
        Sort sort = Sort.by(Sort.Direction.DESC,"reviewCnt", "regDate");
        Pageable pageable = pageReqDto.getPageable(sort);
        Page<Theme> themes = themeRepository.findTopThemes(pageable);

        List<ThemeForListDto> themeForListDtos = mapEntityToDto(themes.getContent());
        return themeForListDtos;
    }

    public List<ThemeForListDto> readLatestThemes(PageReqDto pageReqDto){

        LocalDateTime aMonthAgo = LocalDateTime.now().minusMonths(1);

        Sort sort = Sort.by(Sort.Direction.DESC, "regDate");
        Pageable pageable = pageReqDto.getPageable(sort);

        Page<Theme> themes = themeRepository.findLatestThemes(aMonthAgo, pageable);

        List<ThemeForListDto> themeForListDtos = mapEntityToDto(themes.getContent());
        return themeForListDtos;
    }

    private List<ThemeForListDto> mapEntityToDto(List<Theme> entities){
        return entities.stream()
                .map(entity -> modelMapper.map(entity, ThemeForListDto.class))
                .collect(Collectors.toList());
    }
}
