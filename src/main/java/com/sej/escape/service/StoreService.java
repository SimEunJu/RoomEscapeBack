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
import com.sej.escape.entity.Store;
import com.sej.escape.entity.file.StoreFile;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
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
    private final AreaSectionComponent areaSectionComponent;

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

    // TODO: 원래 이렇게 나오는게 맞나...
    public List<StoreDto> getStores(StorePageReqDto storePageReqDto){

        String queryWhere = "";
        String queryOrder = "";

        String searchKeyword = storePageReqDto.getSearchKeyword();
        if(!Strings.isNullOrEmpty(searchKeyword)){
            queryWhere += " AND store.store_name = '"+searchKeyword+"'";
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
                    queryOrder += " store.zim DESC";
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

        String queryStr =
                "SELECT store.*, file.*, " +
                "(SELECT AVG(star) FROM comment WHERE ctype='S' AND refer_id = store.store_id) as star_avg " +
                "(SELECT COUNT(id) FROM zim WHERE ztype='S' AND refer_id = store.store_id AND is_zim = 1) as zim_cnt" +
                "FROM store LEFT OUTER JOIN file ON file.ftype = 'S' AND file.refer_id = store.store_id " +
                "WHERE store.is_deleted = 0"+queryWhere+" " +
                "ORDER BY"+queryOrder;

        List<Object[]> results = em.createNativeQuery(queryStr)
                .setFirstResult(storePageReqDto.getPage())
                .setMaxResults(storePageReqDto.getSize())
                .getResultList();

        List<StoreDto> storeDtos = new ArrayList<>();
        for(Object[] row : results){
            Store store = (Store) row[0];
            StoreFile storeFile = (StoreFile) row[1];
            double starAvg = (double) row[2];
            int zimCnt = (int) row[3];

            StoreDto storeDto = modelMapper.map(store, StoreDto.class);
            storeDto.setStar(starAvg);
            storeDto.setZim(zimCnt);
            storeDto.setImgUrl(storeFile.getFileUrl());
            storeDtos.add(storeDto);
        }
        return storeDtos;

    }

}
