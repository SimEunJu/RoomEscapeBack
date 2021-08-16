package com.sej.escape.service.theme;

import com.sej.escape.dto.file.FileUrlDto;
import com.sej.escape.dto.store.StoreAreaDto;
import com.sej.escape.entity.file.File;
import com.sej.escape.utils.geolocation.AreaSectionUtil;
import com.sej.escape.dto.store.StoreDto;
import com.sej.escape.dto.theme.ThemeDto;
import com.sej.escape.dto.theme.ThemeForListDto;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.Theme;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ThemeMapper {

    private final AuthenticationUtil authenticationUtil;
    private final ModelMapper modelMapper;
    private final AreaSectionUtil areaSectionUtil;

    @PostConstruct
    public void postConstruct(){

        this.modelMapper.addMappings(new PropertyMap<Theme, ThemeDto>() {
            @Override
            protected void configure() {
                skip(destination.getStore());
                map().setGenre(source.getGenreByList());
                map().setQuizType(source.getQuizTypeByList());
            }
        });
        this.modelMapper.addMappings(new PropertyMap<Theme, ThemeForListDto>() {
            @Override
            protected void configure() {
                skip(destination.getStore());
            }
        });

    }

    public <D> D mapEntityToDto(Theme theme, Class<D> dest){
        return modelMapper.map(theme, dest);
    }

    public <D> List<D> mapEntitiesToDtos(List<Theme> entities, Class<D> dest){
        return entities.stream()
                .map(entity -> mapEntityToDto(entity, dest))
                .collect(Collectors.toList());
    }

    public <D> List<D> mapEntitiesToDtos(List<Theme> entities, Class<D> dest, BiFunction<Theme, D, D> func){
        return entities.stream()
                .map(entity -> {
                    D dto = mapEntityToDto(entity, dest);
                    return func.apply(entity, dto);
                }).collect(Collectors.toList());
    }

    public <T extends ThemeForListDto> T mapThemeRowToDto(Object[] row, Class<T> dtoCls){
        Theme theme = (Theme) row[0];

        T themeDto = modelMapper.map(theme, dtoCls);

        StoreAreaDto storeAreaDto = (StoreAreaDto) row[4];
        themeDto.setStore(storeAreaDto);

        List<String> areaHierarchy = areaSectionUtil.getTitleFromAreaCode(storeAreaDto.getAreaCode());
        themeDto.setArea(areaHierarchy);

        double starAvg = row[1] != null ? ((BigInteger) row[1]).doubleValue() : 0.0;
        themeDto.setStar(starAvg);

        FileUrlDto file = (FileUrlDto) row[5];
        if(file.getRootPath() != null){
            themeDto.setImgUrl(file.getRootPath()+"/"+file.getSubPath()+"/"+file.getName());
        }

        boolean isMemberCheckZim = row[3] != null && ((BigInteger)row[3]).intValue() > 0;
        themeDto.setZimChecked(isMemberCheckZim);

        int zimCnt = row[2] != null ? ((BigInteger) row[2]).intValue() : 0;
        if(authenticationUtil.isAuthenticated()){
            zimCnt = isMemberCheckZim ? zimCnt - 1 : zimCnt;
        }
        themeDto.setZim(zimCnt);

        return themeDto;
    }

}
