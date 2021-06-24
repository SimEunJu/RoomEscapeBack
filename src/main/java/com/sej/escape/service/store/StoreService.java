package com.sej.escape.service.store;

import com.google.common.base.Strings;
import com.querydsl.core.util.ArrayUtils;
import com.sej.escape.constants.AreaSection;
import com.sej.escape.constants.dto.ListOrder;
import com.sej.escape.dto.page.PageResDto;
import com.sej.escape.dto.store.StoreDto;
import com.sej.escape.dto.store.StoreNameDto;
import com.sej.escape.dto.store.StorePageReqDto;
import com.sej.escape.dto.store.StoreZimListResDto;
import com.sej.escape.entity.Member;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.zim.StoreZim;
import com.sej.escape.error.exception.NoSuchResourceException;
import com.sej.escape.repository.store.StoreRepository;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final EntityManager em;
    private final StoreRepository storeRepository;
    private final AuthenticationUtil authenticationUtil;
    private final StoreMapper mapper;

    public List<StoreNameDto> getStoresByName(String keyword){
        Pageable pageable = PageRequest.of(1, 20);
        List<Store> stores = storeRepository.findAllByIsDeletedFalseAndNameContaining(keyword);
        return mapper.mapStoresToDtos(stores, StoreNameDto.class);
    }

    public StoreDto getStore(long id){

        String querySelectIsZimChk = ", (SELECT 0) as is_zim_chk ";
        if(authenticationUtil.isAuthenticated()){
            long memberId = authenticationUtil.getAuthUser().getId();
            querySelectIsZimChk = ", (SELECT COUNT(IF(member_id = "+memberId+", 1, NULL)) FROM zim WHERE ztype='S' AND refer_id = store.store_id AND is_zim = 1) as is_zim_chk ";
        }

        String queryWhere = " AND store.store_id = "+id;

        String queryStr = getStoreQuery(querySelectIsZimChk, queryWhere);

        Object[] result = null;
        try{
            result = (Object[]) em.createNativeQuery(queryStr, "storeResultMap").getSingleResult();
        } catch (NoResultException e){
            throw throwNoSuchResourceException(id);
        }
        return mapper.mapStoreRowToDto(result);
    }

    private NoSuchResourceException throwNoSuchResourceException(long id){
        return new NoSuchResourceException(
                String.format("%d와 일치하는 카페가 존재하지 않습니다.", id) );
    }

    private Store getStoreIfExist(Optional<Store> storeOpt, long id) {
        Store store = storeOpt.orElseThrow(() -> throwNoSuchResourceException(id));
        return store;
    }

    public StoreNameDto getStoreByTheme(long themeId){
        Optional<Store> storeOpt = storeRepository.findByTheme(themeId);
        Store store = getStoreIfExist(storeOpt, themeId);
        return mapper.mapStoreToDto(store, StoreNameDto.class);
    }

    public PageResDto getStoresByZim(StorePageReqDto reqDto){
        Member member = authenticationUtil.getAuthUserEntity();

        Sort sort = Sort.by(Sort.Direction.DESC, "updateDate");
        Pageable pageable = reqDto.getPageable(sort);

        Page<Object[]> storesPage = storeRepository.findallByZim(member, pageable);
        PageResDto resDto = new PageResDto(storesPage,
                (objects) -> {
                    Object[] storeZim = (Object[]) objects;
                    Store store = (Store) storeZim[0];
                    StoreZim zim = (StoreZim) storeZim[1];
                    StoreZimListResDto storeZimListResDto = StoreZimListResDto.builder()
                            .id(store.getId())
                            .zimId(zim.getId())
                            .name(store.getName())
                            .isZimChecked(true)
                            .build();
                    return storeZimListResDto;
        });
        return resDto;
    }

    // TODO: 서비스 계층에서 쿼리 생성하는 게 맞나... ->  repositoryImpl로 이동
    public PageResDto getStores(StorePageReqDto storePageReqDto){

        String queryWhere = "";
        String queryOrder = "";

        String searchKeyword = storePageReqDto.getSearchKeyword();
        if(!Strings.isNullOrEmpty(searchKeyword)){
            queryWhere += " AND store.name LIKE '%"+searchKeyword+"%'";
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
            querySelectIsZimChk = ", (SELECT COUNT(IF(member_id = "+memberId+", 1, NULL)) FROM zim WHERE ztype='S' AND refer_id = store.store_id AND is_zim = 1) as is_zim_chk ";
        }

        String queryStr = getStoresQuery(querySelectIsZimChk, queryWhere, queryOrder);

        List<Object[]> results = em.createNativeQuery(queryStr, "storeResultMap")
                .setFirstResult(storePageReqDto.getPage())
                .setMaxResults(storePageReqDto.getSize())
                .getResultList();

        List<StoreDto> storeDtos = new ArrayList<>();
        for(Object[] row : results){
            storeDtos.add(mapper.mapStoreRowToDto(row));
        }

        // count 쿼리
        String countQueryStr = getStoresCountQuery(queryWhere);
        BigInteger totalCnt = (BigInteger) em.createNativeQuery(countQueryStr)
                .getSingleResult();

        boolean hasNext = totalCnt.intValue() > storePageReqDto.getPage() * storePageReqDto.getSize();

        PageResDto pageResDto = new PageResDto<Store, StoreDto>();
        pageResDto.setTargetList(storeDtos);
        pageResDto.setTotal(totalCnt.intValue());
        pageResDto.setSize(storePageReqDto.getSize());
        pageResDto.setPage(storePageReqDto.getPage());
        pageResDto.setHasNext(hasNext);

        return pageResDto;
    }

    private String getStoresQuery(String querySelectIsZimChk, String queryWhere, String queryOrder){
        return getStoreQuery(querySelectIsZimChk, queryWhere) +
                "ORDER BY"+queryOrder;
    }

    private String getStoresCountQuery(String queryWhere){
        String queryStr = "SELECT count(store.store_id) " +
                "FROM store WHERE store.is_deleted = 0 " +
                queryWhere;
        return queryStr;
    }

    private String getStoreQuery(String querySelectIsZimChk, String queryWhere){
        String queryStr = "SELECT store.*, file.root_path, file.sub_path,  " +
                "(SELECT AVG(star) FROM comment WHERE ctype='S' AND refer_id = store.store_id and comment.is_deleted = 0) as star_avg, " +
                "(SELECT COUNT(*) FROM zim WHERE ztype='S' AND refer_id = store.store_id AND is_zim = 1) as zim_cnt " +
                querySelectIsZimChk +
                "FROM store LEFT OUTER JOIN file ON file.ftype = 'S' AND file.refer_id = store.store_id " +
                "WHERE store.is_deleted = 0 "+queryWhere;
        return queryStr;
    }
    
}
