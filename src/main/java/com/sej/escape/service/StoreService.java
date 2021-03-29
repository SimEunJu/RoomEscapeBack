package com.sej.escape.service;

import com.google.common.base.Strings;
import com.nimbusds.oauth2.sdk.AbstractOptionallyAuthenticatedRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.util.ArrayUtils;
import com.sej.escape.constants.AreaSection;
import com.sej.escape.constants.ListOrder;
import com.sej.escape.dto.store.StoreDto;
import com.sej.escape.dto.store.StorePageReqDto;
import com.sej.escape.entity.Store;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    public StoreDto getStore(long id){
        Optional<Store> storeOp = storeRepository.findById(id);
        Store store = getStoreIfExist(storeOp, id);
        return mapStoreToDto(store);
    }

    private Store getStoreIfExist(Optional<Store> storeOpt, long id){
        Store store = storeOpt.orElseThrow( () ->
                new NoSuchResourceException(
                        String.format("%d와 일치하는 카페가 존재하지 않습니다.", id)));
        return store;
    }

    private StoreDto mapStoreToDto(Store store) {
        return modelMapper.map(store, StoreDto.class);
    }

    public List<StoreDto> getStores(StorePageReqDto storePageReqDto){

        String queryWhere = "";
        String queryOrder = "";
        String searchKeyword = storePageReqDto.getSearchKeyword();
        if(!Strings.isNullOrEmpty(searchKeyword)){
            queryWhere += " AND s.store_name = '"+searchKeyword+"'";
        }

        AreaSection[] areaSections = storePageReqDto.getSelectedAreaSection();
        if(!ArrayUtils.isEmpty(areaSections)){
            queryWhere += " AND";
            int len = areaSections.length;
            for (int i=0; i<len; i++) {
                AreaSection areaSection = areaSections[i];
                AreaSection.AreaCode areaCode = areaSection.getAreaCodeByPostcode();
                double lower = areaCode.getLower();
                double upper = areaCode.getUpper();
                queryWhere += " s.area_code between "+lower+" AND "+upper+"";
                if(i < len-1) queryWhere += " OR";
            }
        }

        ListOrder order = storePageReqDto.getOrder();
        if(order != null){
            switch (order){
                case LATEST:
                    queryOrder += " reg_date DESC";
                    break;
                case GOOD:
                    queryOrder += " good DESC";
                    break;
                case ZIM:
                    queryOrder += " zim DESC";
                    break;
                case CLOSEST:
                    double latitude = storePageReqDto.getLatitude();
                    double longitude = storePageReqDto.getLongitude();
                    if(latitude != 0 && longitude != 0) {
                        queryOrder += " ST_DISTANCE_SPHERE(POINT(" + longitude + ", " + latitude + "), s.location) ASC";
                    }
                    break;
                default:
                    queryOrder += " store_id DESC";
                    break;
            }
        }

        String queryStr = "SELECT s.* FROM store AS s WHERE s.is_deleted = 0"+queryWhere+" ORDER BY"+queryOrder;
        List<Object> results = em.createNativeQuery(queryStr, Store.class)
                .setFirstResult(storePageReqDto.getPage())
                .setMaxResults(storePageReqDto.getSize())
                .getResultList();
        List<StoreDto> storeDtos = new ArrayList<>();
        for(Object row : results){
            Store store = (Store) row;
            storeDtos.add(modelMapper.map(store, StoreDto.class));
        }
        return storeDtos;
    }

}
