package com.sej.escape.service.theme;

import com.sej.escape.constants.AreaSectionComponent;
import com.sej.escape.dto.store.StoreDto;
import com.sej.escape.dto.theme.ThemeDto;
import com.sej.escape.dto.theme.ThemeForListDto;
import com.sej.escape.entity.Store;
import com.sej.escape.entity.Theme;
import com.sej.escape.utils.AuthenticationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ThemeMapper {

    private final AuthenticationUtil authenticationUtil;
    private final ModelMapper modelMapper;
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

        Store store = (Store) row[1];
        StoreDto storeDto = modelMapper.map(store, StoreDto.class);
        themeDto.setStore(storeDto);

        double starAvg = row[4] != null ? ((BigDecimal) row[4]).doubleValue() : 0.0;
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

}
