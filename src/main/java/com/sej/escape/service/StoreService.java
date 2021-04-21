package com.sej.escape.service;

import com.google.common.base.Strings;
import com.nimbusds.oauth2.sdk.AbstractOptionallyAuthenticatedRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.util.ArrayUtils;
import com.sej.escape.constants.AreaSection;
import com.sej.escape.constants.AreaSectionComponent;
import com.sej.escape.constants.ListOrder;
import com.sej.escape.dto.store.StoreDto;
import com.sej.escape.dto.store.StorePageReqDto;
import com.sej.escape.entity.QStore;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.file.StoreFile;
import com.sej.escape.error.exception.BusinessException;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.store.StoreRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final EntityManager em;
    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;
    private final AreaSectionComponent areaSectionComponent;
    private final AuthenticationUtil authenticationUtil;

    @PostConstruct
    public void postConstruct(){
        this.modelMapper.createTypeMap(Store.class, StoreDto.class)
                .addMappings(mapper -> {
                    mapper.map(Store::getStoreName, StoreDto::setName);
                    mapper.map(src ->
                                    areaSectionComponent.getTitleFromAreaCode(src.getAreaCode(), new ArrayList<>()),
                            StoreDto::setArea);
                });
    }

    public StoreDto getStore(long id){

        String querySelectIsZimChk = ", (SELECT 0) as is_zim_chk ";
        if(authenticationUtil.isAuthenticated()){
            long memberId = authenticationUtil.getAuthUser().getId();
            querySelectIsZimChk = ", (SELECT SUM(IF(member_id = "+memberId+", 1, 0)) FROM zim WHERE ztype='S' AND refer_id = store.store_id AND is_zim = 1) as is_zim_chk ";
        }

        String queryWhere = " AND store.store_id = "+id;

        String queryStr = getStoreQuery(querySelectIsZimChk, queryWhere);

        Object[] result = null;
        try{
            result = (Object[]) em.createNativeQuery(queryStr, "storeResultMap").getSingleResult();
        } catch (NoResultException e){
            throw throwNoSuchResourceException(id);
        }
        return mapStoreRowToDto(result);
    }

    private NoSuchResourceException throwNoSuchResourceException(long id){
        return new NoSuchResourceException(
                String.format("%d와 일치하는 카페가 존재하지 않습니다.", id) );
    }

    private Store getStoreIfExist(Optional<Store> storeOpt, long id){
        Store store = storeOpt.orElseThrow( () -> throwNoSuchResourceException(id) );
        return store;
    }

    private StoreDto mapStoreToDto(Store store) {
        return modelMapper.map(store, StoreDto.class);
    }

    // TODO: 서비스 계층에서 쿼리 생성하는 게 맞나... ->  repositoryImpl로 이동
    public List<StoreDto> getStores(StorePageReqDto storePageReqDto){

        String queryWhere = "";
        String queryOrder = "";

        String searchKeyword = storePageReqDto.getSearchKeyword();
        if(!Strings.isNullOrEmpty(searchKeyword)){
            queryWhere += " AND store.store_name LIKE '%"+searchKeyword+"%'";
        }

        AreaSection[] areaSections = storePageReqDto.getAreaSection();
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

        ListOrder order = storePageReqDto.getOrder();
        if(order != null){
            switch (order){
                case LATEST:
                    queryOrder += " store.reg_date DESC";
                    break;
                case ZIM:
                    queryOrder += " zim_cnt DESC";
                    break;
                case CLOSEST:
                    double latitude = storePageReqDto.getLatitude();
                    double longitude = storePageReqDto.getLongitude();
                    if(latitude != 0 && longitude != 0) {
                        queryOrder += " ST_DISTANCE_SPHERE(POINT(" + longitude + ", " + latitude + "), store.location) ASC";
                    }
                    break;
                default:
                    queryOrder += " store.store_id DESC";
                    break;
            }
        }

        String querySelectIsZimChk = ", (SELECT 0) as is_zim_chk ";
        if(authenticationUtil.isAuthenticated()){
            long memberId = authenticationUtil.getAuthUser().getId();
            querySelectIsZimChk = ", (SELECT COUNT(IF(member_id = "+memberId+", 1, 0)) FROM zim WHERE ztype='S' AND refer_id = store.store_id AND is_zim = 1 and is_deleted = 0) as is_zim_chk ";
        }

        String queryStr = getStoresQuery(querySelectIsZimChk, queryWhere, queryOrder);

        List<Object[]> results = em.createNativeQuery(queryStr, "storeResultMap")
                .setFirstResult(storePageReqDto.getPage())
                .setMaxResults(storePageReqDto.getSize())
                .getResultList();

        List<StoreDto> storeDtos = new ArrayList<>();
        for(Object[] row : results){
            storeDtos.add(mapStoreRowToDto(row));
        }
        return storeDtos;
    }

    private String getStoresQuery(String querySelectIsZimChk, String queryWhere, String queryOrder){
        return getStoreQuery(querySelectIsZimChk, queryWhere) +
                "ORDER BY"+queryOrder;
    }

    private String getStoreQuery(String querySelectIsZimChk, String queryWhere){
        String queryStr = "SELECT store.*, file.root_path, file.sub_path,  " +
                "(SELECT IFNULL(AVG(star), 0) FROM comment WHERE ctype='S' AND refer_id = store.store_id and is_deleted = 0) as star_avg, " +
                "(SELECT COUNT(*) FROM zim WHERE ztype='S' AND refer_id = store.store_id AND is_zim = 1 and is_deleted = 0) as zim_cnt " +
                querySelectIsZimChk +
                "FROM store LEFT OUTER JOIN file ON file.ftype = 'S' AND file.refer_id = store.store_id " +
                "WHERE store.is_deleted = 0 "+queryWhere;
        return queryStr;
    }

    private StoreDto mapStoreRowToDto(Object[] row){
        Store store = (Store) row[0];

        StoreDto storeDto = modelMapper.map(store, StoreDto.class);

        double starAvg = (double) row[3];
        storeDto.setStar(starAvg);

        String fileRootPath = (String) row[1];
        String fileSubPath = (String) row[2];
        storeDto.setImgUrl(fileRootPath+"/"+fileSubPath);

        long zimCnt = row[4] != null ? ((BigInteger) row[4]).longValue() : 0;
        boolean isMemberCheckZim = row[5] != null && ((BigInteger)row[5]).intValue() > 0;
        storeDto.setZimChecked(isMemberCheckZim);
        if(authenticationUtil.isAuthenticated()){
            zimCnt = isMemberCheckZim ? zimCnt - 1 : zimCnt;
        }
        storeDto.setZim(zimCnt);

        return storeDto;
    }
    
}
