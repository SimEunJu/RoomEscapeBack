package com.sej.escape.service.store;

import com.sej.escape.dto.file.FileUrlDto;
import com.sej.escape.entity.file.File;
import com.sej.escape.utils.geolocation.AreaSectionUtil;
import com.sej.escape.dto.store.StoreDto;
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
    private final AreaSectionUtil areaSectionUtil;

    @PostConstruct
    public void postConstruct(){
        this.modelMapper.createTypeMap(Store.class, StoreDto.class)
                .addMappings(mapper -> {
                    mapper.map(src ->
                                    areaSectionUtil.getTitleFromAreaCode(src.getAreaCode()),
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
        storeDto.setArea(areaSectionUtil.getTitleFromAreaCode(store.getAreaCode()));

        double starAvg = row[1] != null ? ((BigDecimal) row[1]).doubleValue() : 0.0;
        storeDto.setStar(starAvg);

        FileUrlDto file = (FileUrlDto) row[4];
        if(file.getRootPath() != null) {
            storeDto.setImgUrl(file.getRootPath() + "/" + file.getSubPath() + "/" + file.getName());
        }

        boolean isMemberCheckZim = row[3] != null && ((BigInteger)row[3]).intValue() > 0;
        storeDto.setZimChecked(isMemberCheckZim);

        long zimCnt = row[2] != null ? ((BigInteger) row[2]).longValue() : 0;
        if(authenticationUtil.isAuthenticated()){
            zimCnt = isMemberCheckZim ? zimCnt - 1 : zimCnt;
        }
        storeDto.setZim(zimCnt);

        return storeDto;
    }
}
