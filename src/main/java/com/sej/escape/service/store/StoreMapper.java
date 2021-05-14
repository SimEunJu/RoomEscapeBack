package com.sej.escape.service.store;

import com.sej.escape.constants.AreaSectionComponent;
import com.sej.escape.dto.store.StoreDto;
import com.sej.escape.dto.store.StoreNameDto;
import com.sej.escape.entity.Store;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StoreMapper {
    
    private final ModelMapper modelMapper;
    private final AuthenticationUtil authenticationUtil;
    private final AreaSectionComponent areaSectionComponent;

    @PostConstruct
    public void postConstruct(){
        this.modelMapper.createTypeMap(Store.class, StoreDto.class)
                .addMappings(mapper -> {
                    mapper.map(src ->
                                    areaSectionComponent.getTitleFromAreaCode(src.getAreaCode(), new ArrayList<>()),
                            StoreDto::setArea);
                });
    }

    public <T> T mapStoreToDto(Store store, Class<T> dest) {
        return modelMapper.map(store, dest);
    }

    public <T> List<T> mapStoresToDtos(List<Store> stores, Class<T> dest){
        return stores.stream().map(store -> mapStoreToDto(store, dest)).collect(Collectors.toList());
    }

    public <T> List<T> mapStoresToDtos(List<Store> stores, Class<T> dest, UnaryOperator<T> func){
        return stores.stream().map(store -> {
            T dto = mapStoreToDto(store, dest);
            return func.apply(dto);
        }).collect(Collectors.toList());
    }

    public StoreDto mapStoreRowToDto(Object[] row){
        Store store = (Store) row[0];

        StoreDto storeDto = modelMapper.map(store, StoreDto.class);

        double starAvg = row[3] != null ? ((BigDecimal) row[3]).doubleValue() : 0.0;
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
