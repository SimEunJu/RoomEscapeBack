package com.sej.escape.service;

import com.querydsl.core.BooleanBuilder;
import com.sej.escape.constants.ListOrder;
import com.sej.escape.dto.store.StoreDto;
import com.sej.escape.dto.store.StorePageReqDto;
import com.sej.escape.entity.QStore;
import com.sej.escape.entity.Store;
import com.sej.escape.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final EntityManager em;
    private final StoreRepository storeRepository;
    private final ModelMapper modelMapper;

    @PostConstruct
    public void afterConstruct(){

    }

    public List<StoreDto> getStores(StorePageReqDto storePageReqDto){

        String queryWhere = "";
        String queryOrder = "";
        String searchKeyword = storePageReqDto.getSearchKeyword();
        if(searchKeyword.isBlank()){
            queryWhere += " AND s.storeName = '"+searchKeyword+"'";
        }

        ListOrder order = storePageReqDto.getOrder();
        if(order != null){
            switch (order){
                case LATEST:
                    queryOrder += " AND regDate DESC";
                    break;
                case GOOD:
                    queryOrder += " AND good DESC";
                    break;
                case ZIM:
                    queryOrder += " AND zim DESC";
                    break;
                case CLOSEST:
                    double latitude = storePageReqDto.getLatitude();
                    double longitude = storePageReqDto.getLongitude();
                    if(latitude != 0 && longitude != 0) {
                        queryOrder += " AND ST_DISTANCE_SPHERE(POINT(" + longitude + ", " + latitude + "), s.location) ASC";
                    }
                    break;
                default:
                    queryOrder += " AND id DESC";
                    break;
            }
        }

        String queryStr = "SELECT s.* FROM store AS s WHERE s.isDeleted = 0"+queryWhere+" ORDER BY"+queryOrder;
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
