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

    public final List<AreaSectionValue> areaSections;

    public AreaSectionComponent(){
        this.areaSections = getAreaSections();
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

    @Getter
    @AllArgsConstructor
    public class AreaSectionValue{
        private String name;
        private String title;
        private List<AreaSectionValue> children;
    }

}
