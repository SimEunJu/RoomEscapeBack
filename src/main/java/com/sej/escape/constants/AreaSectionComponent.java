package com.sej.escape.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Getter
public class AreaSectionComponent {

    public final List<AreaSectionValue> areaSectionTree;

    public AreaSectionComponent(){
        this.areaSectionTree = getAreaSections();
    }

    private List<AreaSectionValue> getAreaSections(){
        return getChildrenAreaSections(AreaSection.NATIONWIDE);
    }

    private List<AreaSectionValue> getChildrenAreaSections(AreaSection parent){
        if(parent.getSubSections() == null) return null;
        return parent.getSubSections().stream()
                .map(item -> new AreaSectionValue( item.name(), item.getTitle(), getChildrenAreaSections(item)) )
                .collect(Collectors.toList());
    }

    public List<String> getTitleFromAreaCode(double areaCode, List<String> areas){

        for (AreaSectionValue item : areaSectionTree) {
            AreaSection areaSection = AreaSection.valueOf(item.getName());
            AreaSection.AreaCode itemAreaCode = areaSection.getAreaCodeByPostcode();
            double lower = itemAreaCode.getLower();
            double upper = itemAreaCode.getUpper();
            if(areaCode >= lower && areaCode <= upper){
                areas.add(item.getName());
                return getTitleFromAreaCode(areaCode, areas);
            }
        }
        return areas;
    }

    // TODO: title은 AreaSection.valueOf(name)으로 가져올 수 있으므로 없어도 되지 않을까
    @Getter
    @AllArgsConstructor
    public class AreaSectionValue{
        private String name;
        private String title;
        private List<AreaSectionValue> children;
    }

}
